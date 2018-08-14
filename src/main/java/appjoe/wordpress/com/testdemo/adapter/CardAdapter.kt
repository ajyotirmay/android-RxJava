package appjoe.wordpress.com.testdemo.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView


import com.squareup.picasso.Picasso

import java.util.ArrayList

import appjoe.wordpress.com.testdemo.Card
import appjoe.wordpress.com.testdemo.R

class CardAdapter(private val mContext: Context, private val mCardList: ArrayList<Card>) : RecyclerView.Adapter<CardAdapter.CardViewHolder>() {
    private var mListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setItemOnClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val v = LayoutInflater.from(mContext).inflate(R.layout.card, parent, false)
        return CardViewHolder(v)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val currentItem = mCardList[position]

        val imageUrl = currentItem.imageUrl
        val countryName = currentItem.countryName
        val rank = currentItem.rank

        holder.mTextViewCountry.text = countryName
        holder.mTextViewRank.text = "Rank: $rank"

        Picasso.get().load(imageUrl).fit().centerInside().into(holder.mImageView)
    }

    override fun getItemCount(): Int {
        return mCardList.size
    }

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mImageView: ImageView
        var mTextViewCountry: TextView
        var mTextViewRank: TextView

        init {
            mImageView = itemView.findViewById(R.id.image_view)
            mTextViewCountry = itemView.findViewById(R.id.text_view_country_name)
            mTextViewRank = itemView.findViewById(R.id.text_view_rank)

            itemView.setOnClickListener {
                if (mListener != null) {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        mListener!!.onItemClick(position)
                    }
                }
            }
        }
    }
}
