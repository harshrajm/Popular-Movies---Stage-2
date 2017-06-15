package com.example.android.popularmoviesone;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.android.popularmoviesone.data.MoviesContract.MoviesEntry;

import com.example.android.popularmoviesone.data.MoviesContract;

public class SavedMoviesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>,SavedMoviesAdapter.ListItemClickListener{


    RecyclerView recyclerView;
    SavedMoviesAdapter savedMoviesAdapter;
    private static final int ID_LOADER = 55;
    Cursor mCursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_movies);

        setTitle(getString(R.string.saved_movies));


        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_saved_movies);
        recyclerView.setLayoutManager(new GridLayoutManager(SavedMoviesActivity.this, 2));
        savedMoviesAdapter = new SavedMoviesAdapter(this,this);
        recyclerView.setAdapter(savedMoviesAdapter);

        getSupportLoaderManager().initLoader(ID_LOADER, null, this);

    }


    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
        switch (loaderId) {


            case ID_LOADER:

                Uri SavedMoviesQueryUri = MoviesContract.MoviesEntry.CONTENT_URI;

                String[] projection = {MoviesEntry.COLUMN_MOVIE_ID,MoviesEntry.COLUMN_MOVIE_NAME,
                        MoviesEntry.COLUMN_MOVIE_RATING,MoviesEntry.COLUMN_MOVIE_DATE ,
                        MoviesEntry.COLUMN_MOVIE_IMAGE_URL,MoviesEntry.COLUMN_MOVIE_YOUTUBE_KEY,
                        MoviesEntry.COLUMN_MOVIE_SYNOPSIS,MoviesEntry.COLUMN_MOVIE_REVIEWS};
                return new CursorLoader(this,
                        SavedMoviesQueryUri,
                        projection,
                        null,
                        null,
                        null);

            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursor = data;
            savedMoviesAdapter.setData(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        savedMoviesAdapter.setData(null);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {


        mCursor.moveToPosition(clickedItemIndex);

        int movieId = mCursor.getInt(0);
        String movieName = mCursor.getString(1);
        int movieRating = mCursor.getInt(2);
        String movieDate = mCursor.getString(3);
        String movieImgUrl = mCursor.getString(4);
        String movieytKey = mCursor.getString(5);
        String movieSyn = mCursor.getString(6);
        String movieReview = mCursor.getString(7);



        Movies movies = new Movies(movieId,movieName,movieImgUrl,movieSyn,movieRating,movieDate,movieytKey,movieReview,clickedItemIndex);

        Intent intent = new Intent(SavedMoviesActivity.this,SavedMovieDetailsActivity.class);
        intent.putExtra("movie",movies);
        startActivity(intent);

    }

}
