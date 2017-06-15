package com.example.android.popularmoviesone;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmoviesone.data.MoviesContract;
import com.example.android.popularmoviesone.utility.QueryUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


import com.example.android.popularmoviesone.data.MoviesContract.MoviesEntry;

public class MovieDetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String[]> {

    ImageView imageViewPoster;
    TextView titleTV;
    TextView ratingTV;
    TextView relDateTV,reviewtv;
    TextView synTv;
    Button trailerBtn,saveMovieBtn;
    private static final int ID_DETAILS_LOADER = 11;
    Movies movie;
    String trailerkey;
    String allReviews;
    boolean alreadyFavorite;
    private  String key = null;

    private static final String LIFECYCLE_CALLBACKS_MOVIE_NAME = "name";
    private static final String LIFECYCLE_CALLBACKS_MOVIE_RATING = "rating";
    private static final String LIFECYCLE_CALLBACKS_MOVIE_DATE = "date";
    private static final String LIFECYCLE_CALLBACKS_MOVIE_SYN = "syn";
    private static final String LIFECYCLE_CALLBACKS_MOVIE_REVIEW = "review";
    private static final String LIFECYCLE_CALLBACKS_MOVIE_IMG_URL = "imgUrl";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        Intent intent = getIntent();
         movie = (Movies) intent.getSerializableExtra("movie");

        key = getString(R.string.api_key);

        imageViewPoster = (ImageView) findViewById(R.id.iv_poster_mini);
        titleTV = (TextView) findViewById(R.id.tv_movie_title);
        ratingTV = (TextView) findViewById(R.id.tv_rating);
        relDateTV = (TextView) findViewById(R.id.tv_release_date);
        synTv = (TextView) findViewById(R.id.tv_synopsis);
        trailerBtn = (Button) findViewById(R.id.trailer_btn);
        saveMovieBtn = (Button) findViewById(R.id.save_mv_btn);
        reviewtv = (TextView) findViewById(R.id.tv_reviews);

        if(savedInstanceState != null){
            if(savedInstanceState.containsKey(LIFECYCLE_CALLBACKS_MOVIE_NAME)){
                String title = savedInstanceState.getString(LIFECYCLE_CALLBACKS_MOVIE_NAME);
                String rating = savedInstanceState.getString(LIFECYCLE_CALLBACKS_MOVIE_RATING);
                String date = savedInstanceState.getString(LIFECYCLE_CALLBACKS_MOVIE_DATE);
                String syn = savedInstanceState.getString(LIFECYCLE_CALLBACKS_MOVIE_SYN);
                String review = savedInstanceState.getString(LIFECYCLE_CALLBACKS_MOVIE_REVIEW);

                titleTV.setText(title);
                ratingTV.setText(rating);
                relDateTV.setText(date);
                synTv.setText(syn);
                reviewtv.setText(review);

            }
        }

        String baseUrl ="http://image.tmdb.org/t/p/w780";

        String fullUrl = baseUrl+movie.getPosterPath();

        Picasso.with(imageViewPoster.getContext())
                .load(fullUrl)
                .error(R.drawable.error)
                .placeholder(R.drawable.clapperboard)
                .into(imageViewPoster);

        titleTV.setText(movie.getTitle());
        int rating = (int) movie.getUserRating();
        ratingTV.setText(String.valueOf(rating)+"/10");
        relDateTV.setText(movie.getReleaseDate());
        synTv.setText(movie.getSynopsis());

        trailerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v="+trailerkey)));
            }
        });
        trailerBtn.setEnabled(false);

        saveMovieBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!alreadyFavorite) {
                    insertMovie();
                }else{
                    getContentResolver().delete(MoviesContract.MoviesEntry.CONTENT_URI,MoviesContract.MoviesEntry.COLUMN_MOVIE_ID+"=?",new String[]{String.valueOf(movie.getId())});
                    Toast.makeText(MovieDetailsActivity.this, getString(R.string.favorite_removed), Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        });
        saveMovieBtn.setEnabled(false);


        getSupportLoaderManager().initLoader(ID_DETAILS_LOADER,null,this);



        Bundle bundle = new Bundle();
        bundle.putString("movie_id",String.valueOf(movie.getId()));

        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.initLoader(ID_DETAILS_LOADER,null,this);

        checkIfMovieAlreadysaved();
    }

    private void checkIfMovieAlreadysaved() {
        String[] projection = {MoviesEntry.COLUMN_MOVIE_ID,MoviesEntry.COLUMN_MOVIE_NAME,
                MoviesEntry.COLUMN_MOVIE_RATING,MoviesEntry.COLUMN_MOVIE_DATE ,
                MoviesEntry.COLUMN_MOVIE_IMAGE_URL,MoviesEntry.COLUMN_MOVIE_YOUTUBE_KEY,
                MoviesEntry.COLUMN_MOVIE_SYNOPSIS,MoviesEntry.COLUMN_MOVIE_REVIEWS};
        Cursor cursor = getContentResolver().query(MoviesContract.MoviesEntry.CONTENT_URI,projection, MoviesEntry.COLUMN_MOVIE_ID+"=?",new String[]{String.valueOf(movie.getId())},null);

        if(cursor.getCount() == 0){

            alreadyFavorite = false;

        }else{
            alreadyFavorite = true;

            saveMovieBtn.setText(getString(R.string.unmark_favorite));
        }
    }


    @Override
    public Loader<String[]> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<String[]>(this) {
            @Override
            protected void onStartLoading() {

                forceLoad();

            }

            @Override
            public String[] loadInBackground() {
                String[] response = new String[2];
                URL url1 = null;
                URL url2 = null;
                String trailerKey = null;
                String urlString1 = "https://api.themoviedb.org/3/movie/"+movie.getId()+"/videos?api_key="+key+"&language=en-US&page=1";
                String urlString2 = "https://api.themoviedb.org/3/movie/"+movie.getId()+"/reviews?api_key="+key+"&language=en-US&page=1";
                try {
                    url1 = new URL(urlString1);
                    url2 = new URL(urlString2);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                String query1 = null;
                String query2 = null;
                try {
                    query1 = QueryUtils.getResponseFromHttpUrl(url1);
                    query2 = QueryUtils.getResponseFromHttpUrl(url2);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    trailerKey = QueryUtils.GetTrailer(query1);
                    String reviews =  QueryUtils.GetReviews(query2);
                response[0] = trailerKey;
                response[1] = reviews;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return response;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String[]> loader, String data[]) {

        trailerkey = data[0];
        String reviews = data[1];

        saveMovieBtn.setEnabled(true);

        if(trailerkey == null || trailerkey.equals("") || trailerkey.equals(null)){
                trailerBtn.setText(getString(R.string.no_trailer));
        }else{
            trailerBtn.setEnabled(true);
            trailerBtn.setText(getString(R.string.watch_trailer));
        }
        if(reviews == null || reviews.equals("") || reviews.equals(null)){
            reviewtv.setText(getString(R.string.no_reviews));
            allReviews = getString(R.string.no_reviews);
        }else{
            reviewtv.setText(reviews);
            allReviews = reviews;
        }

    }

    @Override
    public void onLoaderReset(Loader<String[]> loader) {

    }


    private void insertMovie() {

        ContentValues contentValues = new ContentValues();
        contentValues.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_ID,movie.getId());
        contentValues.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_NAME,movie.getTitle());
        contentValues.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_RATING,movie.getUserRating());
        contentValues.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_DATE,movie.getReleaseDate());
        contentValues.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_IMAGE_URL,movie.getPosterPath());
        contentValues.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_YOUTUBE_KEY,trailerkey);
        contentValues.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_SYNOPSIS,movie.getSynopsis());
        contentValues.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_REVIEWS,allReviews);

        Uri uri = getContentResolver().insert(MoviesContract.MoviesEntry.CONTENT_URI,contentValues);

        Toast.makeText(getApplicationContext(),getString(R.string.favorite_added), Toast.LENGTH_LONG).show();

        saveMovieBtn.setText(getString(R.string.unmark_favorite));
        alreadyFavorite = true;


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(LIFECYCLE_CALLBACKS_MOVIE_NAME,movie.getTitle());
        outState.putString(LIFECYCLE_CALLBACKS_MOVIE_RATING,String.valueOf(movie.getUserRating()));
        outState.putString(LIFECYCLE_CALLBACKS_MOVIE_DATE,movie.getReleaseDate());
        outState.putString(LIFECYCLE_CALLBACKS_MOVIE_SYN,movie.getSynopsis());
        outState.putString(LIFECYCLE_CALLBACKS_MOVIE_REVIEW,movie.getReviews());


    }

}
