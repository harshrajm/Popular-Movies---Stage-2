package com.example.android.popularmoviesone;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmoviesone.data.MoviesContract;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.media.CamcorderProfile.get;

/**
 * Created by Harshraj on 05-06-2017.
 */

public class SavedMoviesAdapter extends RecyclerView.Adapter<SavedMoviesAdapter.SavedMoviesAdapterViewHolder> {

    private final Context mContext;
    Cursor mCursor;
    String baseUrl ="http://image.tmdb.org/t/p/w780";
    final private ListItemClickListener mListItemClickListener;

    public interface ListItemClickListener{
        void onListItemClick(int clickedItemIndex);
    }

    public SavedMoviesAdapter(Context context,ListItemClickListener mListItemClickListener1) {
        mListItemClickListener = mListItemClickListener1;
        mContext = context;
    }

    @Override
    public SavedMoviesAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater
                .from(mContext)
                .inflate(R.layout.recycler_item, viewGroup, false);

        view.setFocusable(true);

        return new SavedMoviesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SavedMoviesAdapterViewHolder holder, int position) {

        mCursor.moveToPosition(position);

        String url = mCursor.getString(4);





        String fullUrl = baseUrl+url;


        Picasso.with(holder.mImageView.getContext())
                .load(fullUrl)
                .error(R.drawable.error)
                .placeholder(R.drawable.clapperboard)                     // optional*/
                .into(holder.mImageView);

    }

    public void setData(Cursor cursor){
        mCursor = cursor;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
    }

    class SavedMoviesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView mImageView;

        public SavedMoviesAdapterViewHolder(View itemView) {

            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.iv_poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mListItemClickListener.onListItemClick(clickedPosition);
        }
    }

}
