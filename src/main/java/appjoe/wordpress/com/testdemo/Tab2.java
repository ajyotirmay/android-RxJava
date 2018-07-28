package appjoe.wordpress.com.testdemo;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.provider.ContactsContract;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Tab2.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Tab2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Tab2 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 123;

    Button mbutton;

    Cursor mCursor, fCursor;
    String contactData;
    int count = 0;
    File location = new File(Environment.getExternalStorageDirectory(), "Android/data/com.wordpress.appjoe/csv/");
    File fileLocation;
    FileOutputStream dest;
    String path;
//    FileHelper fileHelper;

    public Tab2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Tab2.
     */
    // TODO: Rename and change types and number of parameters
    public static Tab2 newInstance(String param1, String param2) {
        Tab2 fragment = new Tab2();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted. do the contacts
                    // related task you need to do.
                    Log.d("Custom permission", "Permission granted");
                } else {
                    // permission denied. Disable the functionality that
                    // depends on this permission.
                    Log.d("Custom permission", "Something is wrong");
                }
                return;
            }

            default: {
                Log.d("Custom permission: ", "Permission denied");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_tab2, container, false);
        mbutton = v.findViewById(R.id.extractContact);

        mbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Here, thisActivity is the current activity
                if (ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.READ_CONTACTS)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Permission is not granted
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                            Manifest.permission.READ_CONTACTS)) {
                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                    } else {
                        // No explanation needed; request the permission
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.READ_CONTACTS},
                                MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                } else {
                    // Permission has already been granted
                    Observer observer = new Observer() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            mCursor = getCursor();
                            fCursor = getCursor();
                            location.mkdirs();
                            path = location.getAbsolutePath();
                            try {
                                dest = new FileOutputStream(path);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onNext(Object o) {
                            contactData = o.toString();
                            fCursor.moveToPosition(count);
                            fileLocation = new File(path, getName(fCursor)+".csv");
                            try {
                                FileOutputStream fileOut = new FileOutputStream(fileLocation);
                                fileOut.write(contactData.getBytes());
                                fileOut.flush();
                                fileOut.close();

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {
                            mCursor.close();
                            fCursor.close();

                            // Creating archive once the CSV files are generated
                            if (zip(path, location.getParent())) {
                                // ToDo implement snackbar here
                                Snackbar.make(v.findViewById(R.id.tab2_layout), "Archive created", Snackbar.LENGTH_LONG)
                                        .setAction("Dismiss",
                                                new View.OnClickListener(){
                                                    @Override
                                                    public void onClick (View v) {}
                                                }).show();
                                Log.d("zip", "Zip successful");
                            }
                        }
                    };

                    io.reactivex.Observable.create(new ObservableOnSubscribe<String>() {
                        @Override
                        public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                            try {
                                for (count = 0; count < mCursor.getCount(); count++) {
                                    emitter.onNext(loadContacts(count));
                                }
                                emitter.onComplete();
                            } catch (Exception e) {
                                emitter.onError(e);
                            }
                        }
                    }).subscribeOn(Schedulers.io())
                            .distinct()
                            .subscribeWith(observer);

                }
            }
        });

        // Inflate the layout for this fragment
        return v;
    }

    public String loadContacts(int i) {
        StringBuilder mBuilder = new StringBuilder();
        ContentResolver mContentResolver = getActivity().getContentResolver();

        mCursor.moveToPosition(i);
        if (mCursor.getCount() > 0 ) {
            String id = getID(mCursor);
            String name = getName(mCursor);
            int hasPhoneNumber = hasNumber(mCursor);

            if (hasPhoneNumber > 0) {
                mBuilder.append("\"").append(name).append("\"");
                Cursor cursor = mContentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "= ?",
                        new String[]{id}, null);

                assert cursor != null;
                while (cursor.moveToNext()) {
                    String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                            .replaceAll("\\s", "");

                    // if number is not existing in the list, then add number to the string
                    if (!(mBuilder.toString().contains(phoneNumber))) {
                        mBuilder.append(", ").append(phoneNumber);
                    }
                }

                cursor.close();
            }
        }
        return mBuilder.toString();
    }

    private Cursor getCursor() {
        ContentResolver mContentResolver = getActivity().getContentResolver();
        return mContentResolver.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, ContactsContract.Contacts.DISPLAY_NAME + " ASC");
    }

    private String getID(Cursor cursor) {
        String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
        return id;
    }

    private String getName(Cursor cursor) {
        String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        return name;
    }

    private int hasNumber(Cursor cursor) {
        return Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    // Methods to help with zipping of generated CSVs
    private static final int BUFFER_SIZE = 2048;
    private String parentPath = "";
    private String destinationFileName = "Contacts_CSV.zip";

    private boolean zip (String sourcePath, String destinationPath) {
        new File(destinationPath).mkdirs();
        FileOutputStream fileOutputStream;
        ZipOutputStream zipOutputStream = null;

        try {
            if (!destinationPath.endsWith("/")) {
                destinationPath = destinationPath + "/";
            }
            String destination = destinationPath + destinationFileName;
            File file = new File(destination);
            if (!file.exists()) {
                file.createNewFile();
            }

            fileOutputStream = new FileOutputStream(file);
            zipOutputStream = new ZipOutputStream(new BufferedOutputStream(fileOutputStream));


            parentPath = new File(sourcePath).getParent() + "/";

            zipFile(zipOutputStream, sourcePath);

        } catch (IOException e) {
            e.printStackTrace();
            Log.d("zip",e.getMessage());
            return false;
        } finally {
            if (zipOutputStream!=null)
                try {
                    zipOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

        return true;
    }

    private void zipFile (ZipOutputStream zipOutputStream, String sourcePath) throws IOException {
        java.io.File files = new java.io.File(sourcePath);
        java.io.File[] fileList = files.listFiles();

        String entryPath="";
        BufferedInputStream input;
        for (java.io.File file : fileList) {
            if (!file.isDirectory()) {
                byte data[] = new byte[BUFFER_SIZE];
                FileInputStream fileInputStream = new FileInputStream(file.getPath());
                input = new BufferedInputStream(fileInputStream, BUFFER_SIZE);
                entryPath = file.getAbsolutePath().replace(parentPath, "");

                ZipEntry entry = new ZipEntry(entryPath);
                zipOutputStream.putNextEntry(entry);

                int count;
                while ((count = input.read(data, 0, BUFFER_SIZE)) != -1) {
                    zipOutputStream.write(data, 0, count);
                }
                input.close();
            }
        }
    }
}
