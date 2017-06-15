package com.example.android.popularmoviesone;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.popularmoviesone.utility.QueryUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static android.media.CamcorderProfile.get;
import static com.example.android.popularmoviesone.utility.QueryUtils.getResponseFromHttpUrl;
import static com.example.android.popularmoviesone.utility.QueryUtils.parseJson;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.ListItemClickListener {

    String key;

    String httpQueryTopRated = null;
    String httpQueryMostPopular = null;
    ArrayList<Movies> listOfMovies = null;

    RecyclerView recyclerView;
    MoviesAdapter moviesAdapter;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);


        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_movies);
        recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
        moviesAdapter = new MoviesAdapter(this);
        recyclerView.setAdapter(moviesAdapter);


        if (isNetworkAvailable(this)) {
            key = getString(R.string.api_key);

            httpQueryTopRated = "https://api.themoviedb.org/3/movie/top_rated?api_key=" + key + "&language=en-US&page=1";
            httpQueryMostPopular = "https://api.themoviedb.org/3/movie/popular?api_key=" + key + "&language=en-US&page=1";
            fetchData(httpQueryTopRated);

            setTitle(getString(R.string.top_rated));
        } else {
            Toast.makeText(MainActivity.this, "No Internet Connection!! \n Checkout favorite Movies!", Toast.LENGTH_LONG).show();
        }


    }

    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public void fetchData(String httpQuery) {

        new MoviesQuery().execute(httpQuery);

    }

    @Override
    public void onListItemClicked(int clickedItemIndex) {


        Movies clickedMovie = listOfMovies.get(clickedItemIndex);

        Intent intent = new Intent(MainActivity.this, MovieDetailsActivity.class);
        intent.putExtra("movie", clickedMovie);
        startActivity(intent);

    }


    public class MoviesQuery extends AsyncTask<String, Void, ArrayList<Movies>> {

        @Override
        protected ArrayList<Movies> doInBackground(String... strings) {
            URL url = null;
            if (strings.length == 0) {
                return null;
            }

            String httpRequest = strings[0];
            try {
                url = new URL(httpRequest);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            String query = null;
            try {
                query = QueryUtils.getResponseFromHttpUrl(url);

            } catch (IOException e) {
                e.printStackTrace();
            }


            try {
                listOfMovies = QueryUtils.parseJson(query);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return listOfMovies;
        }


        @Override
        protected void onPostExecute(ArrayList<Movies> movies) {
            progressBar.setVisibility(View.INVISIBLE);

            moviesAdapter.setData(movies);


        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.most_popular) {
            moviesAdapter.setData(null);
            fetchData(httpQueryMostPopular);

            setTitle(getString(R.string.most_popular));
            return true;
        }
        if (id == R.id.top_rated) {
            moviesAdapter.setData(null);
            fetchData(httpQueryTopRated);

            setTitle(getString(R.string.top_rated));
            return true;
        }
        if (id == R.id.saved_movies) {


            Intent intent = new Intent(MainActivity.this, SavedMoviesActivity.class);
            startActivity(intent);

            return true;
        }


        return super.onOptionsItemSelected(item);
    }

}
