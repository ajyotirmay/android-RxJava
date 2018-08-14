package appjoe.wordpress.com.testdemo

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.ContactsContract
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [Tab2.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [Tab2.newInstance] factory method to
 * create an instance of this fragment.
 */
//    FileHelper fileHelper;

class Tab2 : Fragment() {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    private var mListener: OnFragmentInteractionListener? = null
    private val requestReadContactsPermission = 123

    private lateinit var mButton: Button

    internal var mCursor: Cursor? = null
    internal var fCursor: Cursor? = null
    internal lateinit var contactData: String
    internal var count = 0
    internal var location = File(Environment.getExternalStorageDirectory(), "Android/data/com.wordpress.appjoe/csv/")
    internal lateinit var fileLocation: File
    internal lateinit var dest: FileOutputStream
    internal lateinit var path: String

    private val cursor: Cursor?
        get() {
            val mContentResolver = activity.contentResolver
            return mContentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.Contacts.DISPLAY_NAME + " ASC")
        }
    private var parentPath = ""
    private val destinationFileName = "Contacts_CSV.zip"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments.getString(ARG_PARAM1)
            mParam2 = arguments.getString(ARG_PARAM2)
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            requestReadContactsPermission -> {
                // If request is cancelled, the result arrays are empty
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted. do the contacts
                    // related task you need to do.
                    Log.d("Custom permission", "Permission granted")
                } else {
                    // permission denied. Disable the functionality that
                    // depends on this permission.
                    Log.d("Custom permission", "Something is wrong")
                }
                return
            }

            else -> {
                Log.d("Custom permission: ", "Permission denied")
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater!!.inflate(R.layout.fragment_tab2, container, false)
        mButton = v.findViewById(R.id.extractContact)

        mButton.setOnClickListener {
            // Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(context,
                            Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

                // Permission is not granted
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                                Manifest.permission.READ_CONTACTS)) {
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                } else {
                    // No explanation needed; request the permission
                    ActivityCompat.requestPermissions(activity,
                            arrayOf(Manifest.permission.READ_CONTACTS),
                            requestReadContactsPermission)

                    // requestReadContactsPermission is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            } else {
                // Permission has already been granted
                val observer = object : Observer<Any> {
                    override fun onSubscribe(d: Disposable) {
                        mCursor = cursor
                        fCursor = cursor
                        location.mkdirs()
                        path = location.absolutePath
                        try {
                            dest = FileOutputStream(path)
                        } catch (e: FileNotFoundException) {
                            e.printStackTrace()
                        }

                    }

                    override fun onNext(o: Any) {
                        contactData = o.toString()
                        fCursor!!.moveToPosition(count)
                        fileLocation = File(path, getName(fCursor!!) + ".csv")
                        try {
                            val fileOut = FileOutputStream(fileLocation)
                            fileOut.write(contactData.toByteArray())
                            fileOut.flush()
                            fileOut.close()

                        } catch (e: IOException) {
                            e.printStackTrace()
                        }

                    }

                    override fun onError(e: Throwable) {

                    }

                    override fun onComplete() {
                        mCursor!!.close()
                        fCursor!!.close()

                        // Creating archive once the CSV files are generated
                        if (zip(path, location.parent)) {
                            // ToDo implement snackbar here
                            Snackbar.make(v.findViewById(R.id.tab2_layout), "Archive created", Snackbar.LENGTH_LONG)
                                    .setAction("Dismiss"
                                    ) { }.show()
                            Log.d("zip", "Zip successful")
                        }
                    }
                }

                io.reactivex.Observable.create(ObservableOnSubscribe<String> { emitter ->
                    try {
                        count = 0
                        while (count < mCursor!!.count) {
                            emitter.onNext(loadContacts(count))
                            count++
                        }
                        emitter.onComplete()
                    } catch (e: Exception) {
                        emitter.onError(e)
                    }
                }).subscribeOn(Schedulers.io())
                        .distinct()
                        .subscribeWith<Observer<Any>>(observer)

            }
        }

        // Inflate the layout for this fragment
        return v
    }

    fun loadContacts(i: Int): String {
        val mBuilder = StringBuilder()
        val mContentResolver = activity.contentResolver

        mCursor!!.moveToPosition(i)
        if (mCursor!!.count > 0) {
            val id = getID(mCursor!!)
            val name = getName(mCursor!!)
            val hasPhoneNumber = hasNumber(mCursor!!)

            if (hasPhoneNumber > 0) {
                mBuilder.append("\"").append(name).append("\"")
                val cursor = mContentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "= ?",
                        arrayOf(id), null)!!

                while (cursor.moveToNext()) {
                    val phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                            .replace("\\s".toRegex(), "")

                    // if number is not existing in the list, then add number to the string
                    if (!mBuilder.toString().contains(phoneNumber)) {
                        mBuilder.append(", ").append(phoneNumber)
                    }
                }

                cursor.close()
            }
        }
        return mBuilder.toString()
    }

    private fun getID(cursor: Cursor): String {
        return cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
    }

    private fun getName(cursor: Cursor): String {
        return cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
    }

    private fun hasNumber(cursor: Cursor): Int {
        return Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)))
    }


    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        if (mListener != null) {
            mListener!!.onFragmentInteraction(uri)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    private fun zip(sourcePath: String, destinationPath: String): Boolean {
        var destinationPath = destinationPath
        File(destinationPath).mkdirs()
        val fileOutputStream: FileOutputStream
        var zipOutputStream: ZipOutputStream? = null

        try {
            if (!destinationPath.endsWith("/")) {
                destinationPath = "$destinationPath/"
            }
            val destination = destinationPath + destinationFileName
            val file = File(destination)
            if (!file.exists()) {
                file.createNewFile()
            }

            fileOutputStream = FileOutputStream(file)
            zipOutputStream = ZipOutputStream(BufferedOutputStream(fileOutputStream))


            parentPath = File(sourcePath).parent + "/"

            zipFile(zipOutputStream, sourcePath)

        } catch (e: IOException) {
            e.printStackTrace()
            Log.d("zip", e.message)
            return false
        } finally {
            if (zipOutputStream != null)
                try {
                    zipOutputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

        }

        return true
    }

    @Throws(IOException::class)
    private fun zipFile(zipOutputStream: ZipOutputStream, sourcePath: String) {
        val files = java.io.File(sourcePath)
        val fileList = files.listFiles()

        var entryPath = ""
        var input: BufferedInputStream
        for (file in fileList) {
            if (!file.isDirectory) {
                val data = ByteArray(BUFFER_SIZE)
                val fileInputStream = FileInputStream(file.path)
                input = BufferedInputStream(fileInputStream, BUFFER_SIZE)
                entryPath = file.absolutePath.replace(parentPath, "")

                val entry = ZipEntry(entryPath)
                zipOutputStream.putNextEntry(entry)

                var count: Int

                do {
                    count = input.read(data, 0, BUFFER_SIZE)
                    zipOutputStream.write(data, 0, count)

                } while (count != -1)

                input.close()
            }
        }
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Tab2.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): Tab2 {
            val fragment = Tab2()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }

        // Methods to help with zipping of generated CSVs
        private val BUFFER_SIZE = 2048
    }
}
