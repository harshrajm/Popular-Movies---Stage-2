package com.example.android.popularmoviesone.utility;

import android.util.Log;

import com.example.android.popularmoviesone.Movies;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Harshraj on 09-03-2017.
 */

public class QueryUtils {

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                //Log.v("response",scanner.next());
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static ArrayList<Movies> parseJson(String stringToBeParsed) throws JSONException {

        ArrayList<Movies> arrayListOfMovies = new ArrayList<Movies>();

        JSONObject forecastJson = new JSONObject(stringToBeParsed);

        JSONArray jsonArray = forecastJson.getJSONArray("results");

        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject movieObject = jsonArray.getJSONObject(i);

            int movieId = movieObject.getInt("id");
            String movieTitle = movieObject.getString("title");
            String moviePoster = movieObject.getString("poster_path");
            String movieSynopsys = movieObject.getString("overview");
            String movieReleaseDate = movieObject.getString("release_date");
            double movieRating = movieObject.getDouble("vote_average");



            arrayListOfMovies.add(new Movies(movieId,movieTitle,moviePoster,movieSynopsys,movieReleaseDate,movieRating));



        }

        return arrayListOfMovies;
    }


    public static String GetTrailer(String jsonString) throws JSONException{

        JSONObject jsonObject = new JSONObject(jsonString);

        JSONArray videos = jsonObject.getJSONArray("results");

        for (int i = 0; i < videos.length(); i++) {
            JSONObject v = videos.getJSONObject(i);
            String type = v.getString("type");
            if(type.equalsIgnoreCase("Trailer")){
                return v.getString("key");
            }
        }

        return null;
    }


    public static String GetReviews(String jsonString) throws JSONException{
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray reviews = jsonObject.getJSONArray("results");
            String response = "";
            for (int i = 0; i < reviews.length(); i++) {
                JSONObject r = reviews.getJSONObject(i);
                String review = r.getString("content");
                if (review == null || "".equals(review) ||review.equals(null)){

                }else{
                    response = response + " ---> " +review+ "\n\n";
                }
            }
            return response;
        } catch (JSONException e) {
            return  null;
        }

    }

}
