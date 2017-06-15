package com.example.android.popularmoviesone;

import android.content.Context;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.media.CamcorderProfile.get;

/**
 * Created by Harshraj on 12-03-2017.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesAdapterViewHolder>{

    String baseUrl ="http://image.tmdb.org/t/p/w780";

    private ArrayList<Movies> mMovies;



    final private ListItemClickListener mListItemClickListener;




    public interface ListItemClickListener{
        void onListItemClicked(int clickedItemIndex);
    }


    public MoviesAdapter(ListItemClickListener listItemClickListener){
        mListItemClickListener = listItemClickListener;
    }

    public void setData(ArrayList<Movies> movies){
        mMovies = movies;
        notifyDataSetChanged();
    }

    @Override
    public MoviesAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);


        View posterView = inflater.inflate(R.layout.recycler_item, parent, false);



        MoviesAdapterViewHolder moviesAdapterViewHolder = new MoviesAdapterViewHolder(posterView);
        return moviesAdapterViewHolder;
    }

    @Override
    public void onBindViewHolder(MoviesAdapterViewHolder holder, int position) {

        Movies movie = mMovies.get(position);

        String posterPath = movie.getPosterPath();



        String fullUrl = baseUrl+posterPath;


        Picasso.with(holder.mImageView.getContext())
                .load(fullUrl)
                .error(R.drawable.error)
                .placeholder(R.drawable.clapperboard)
                .into(holder.mImageView);

    }

    @Override
    public int getItemCount() {
        if (null == mMovies) return 0;
        return mMovies.size();
    }

    public class MoviesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView mImageView;

        public MoviesAdapterViewHolder(View itemView) {
            super(itemView);

            mImageView = (ImageView) itemView.findViewById(R.id.iv_poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mListItemClickListener.onListItemClicked(clickedPosition);
        }
    }

}
