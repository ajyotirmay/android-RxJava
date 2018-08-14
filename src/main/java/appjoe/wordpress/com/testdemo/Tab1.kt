package appjoe.wordpress.com.testdemo

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import appjoe.wordpress.com.testdemo.adapter.CardAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*


/*
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Tab1.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Tab1#newInstance} factory method to
 * create an instance of this fragment.
 */
class Tab1 : Fragment(), CardAdapter.OnItemClickListener {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    private var mRecyclerView: RecyclerView? = null
    private var mCardAdapter: CardAdapter? = null
    private var mCardList: ArrayList<Card>? = null

    private var mListener: OnFragmentInteractionListener? = null

    //    @Override
    fun onCreate(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments!!.getString(ARG_PARAM1)
            mParam2 = arguments!!.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater!!.inflate(R.layout.fragment_tab1, container, false)

        mRecyclerView = v.findViewById(R.id.recycler_view)
        mRecyclerView!!.setHasFixedSize(true)
        mRecyclerView!!.layoutManager = LinearLayoutManager(activity)

        mCardList = ArrayList()

        /*
         * Retrofit code to fetch the Json data
         */
        val retrofit = Retrofit.Builder()
                .baseUrl(ApiService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val service = retrofit.create(ApiService::class.java)
        val call = service.populationData
//        val flagData = StringBuffer()

        call.enqueue(object : Callback<JsonResponse> {
            override fun onResponse(call: Call<JsonResponse>, response: Response<JsonResponse>) {
                val population = ArrayList(response.body()!!
                        .worldpopulation!!)

                for (j in population) {
                    val countryName = j.country
                    val imageUrl = j.flag
                    val rank = j.rank!!

                    mCardList!!.add(Card(imageUrl.toString(), countryName.toString(), rank))
                }

                mCardAdapter = CardAdapter(activity, mCardList!!)
                mRecyclerView!!.adapter = mCardAdapter
                mCardAdapter!!.setItemOnClickListener(this@Tab1)

            }

            override fun onFailure(call: Call<JsonResponse>, t: Throwable) {
                Log.d("JSONError", t.message)
            }
        })

        // Inflate the layout for this fragment
        return v
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

    override fun onItemClick(position: Int) {
        val imagePreview = Intent(activity, ViewImage::class.java)
        val clickedItem = mCardList!![position]

        imagePreview.putExtra(EXTRA_URL, clickedItem.imageUrl)

        startActivity(imagePreview)
    }

    /*
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"

        val EXTRA_URL = "imageUrl"

        /*
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Tab1.
     */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): Tab1 {
            val fragment = Tab1()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
