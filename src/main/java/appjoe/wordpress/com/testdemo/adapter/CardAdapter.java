package appjoe.wordpress.com.testdemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import appjoe.wordpress.com.testdemo.Card;
import appjoe.wordpress.com.testdemo.R;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {
    private Context mContext;
    private ArrayList<Card> mCardList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setItemOnClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public CardAdapter(Context context, ArrayList<Card> cardList) {
        mContext = context;
        mCardList = cardList;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.card, parent, false);
        return new CardViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        Card currentItem = mCardList.get(position);

        String imageUrl = currentItem.getImageUrl();
        String countryName = currentItem.getCountryName();
        int rank = currentItem.getRank();

        holder.mTextViewCountry.setText(countryName);
        holder.mTextViewRank.setText("Rank: " + rank);

        Picasso.get().load(imageUrl).fit().centerInside().into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return mCardList.size();
    }

    public class CardViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTextViewCountry;
        public TextView mTextViewRank;

        public CardViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.image_view);
            mTextViewCountry = itemView.findViewById(R.id.text_view_country_name);
            mTextViewRank = itemView.findViewById(R.id.text_view_rank);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
