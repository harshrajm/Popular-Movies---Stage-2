package com.example.android.popularmoviesone;

import android.content.Intent;
import android.graphics.Movie;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmoviesone.data.MoviesContract;
import com.squareup.picasso.Picasso;

public class SavedMovieDetailsActivity extends AppCompatActivity {

    ImageView imageViewPoster;
    TextView titleTV;
    TextView ratingTV;
    TextView relDateTV,reviewtv;
    TextView synTv;
    Button trailerBtn,saveMovieBtn;
    private static final String LIFECYCLE_CALLBACKS_MOVIE_NAME = "name";
    private static final String LIFECYCLE_CALLBACKS_MOVIE_RATING = "rating";
    private static final String LIFECYCLE_CALLBACKS_MOVIE_DATE = "date";
    private static final String LIFECYCLE_CALLBACKS_MOVIE_SYN = "syn";
    private static final String LIFECYCLE_CALLBACKS_MOVIE_REVIEW = "review";



    Movies movie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);



        Intent intent = getIntent();
        movie = (Movies) intent.getSerializableExtra("movie");



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
        reviewtv.setText(movie.getReviews());
        trailerBtn.setText(getString(R.string.watch_trailer));
       if (movie.getYouTubekey() == null ) {trailerBtn.setText(getString(R.string.no_trailer));trailerBtn.setEnabled(false);}
        trailerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v="+movie.getYouTubekey())));
            }
        });

        saveMovieBtn.setText(getString(R.string.unmark_favorite));
        saveMovieBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               int response = getContentResolver().delete(MoviesContract.MoviesEntry.CONTENT_URI,MoviesContract.MoviesEntry.COLUMN_MOVIE_ID+"=?",new String[]{String.valueOf(movie.getId())});
                Toast.makeText(SavedMovieDetailsActivity.this, getString(R.string.favorite_removed), Toast.LENGTH_SHORT).show();
                finish();
            }
        });

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
