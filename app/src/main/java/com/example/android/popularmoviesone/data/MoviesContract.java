package com.example.android.popularmoviesone.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Harshraj on 05-06-2017.
 */

public class MoviesContract {

    private MoviesContract(){}


    public static final String CONTENT_AUTHORITY = "com.example.android.popularmoviesone";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIES = "movies";


    public static final class MoviesEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_MOVIES);

        public static final String TABLE_NAME = "movies";

        public final static String _ID = BaseColumns._ID;
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_MOVIE_NAME = "movie_name";
        public static final String COLUMN_MOVIE_RATING = "movie_rating";
        public static final String COLUMN_MOVIE_DATE = "movie_date";
        public static final String COLUMN_MOVIE_IMAGE_URL = "movie_img_url";
        public static final String COLUMN_MOVIE_YOUTUBE_KEY = "movie_yt_key";
        public static final String COLUMN_MOVIE_SYNOPSIS = "movie_synopsis";
        public static final String COLUMN_MOVIE_REVIEWS = "movie_reviews";
    }

}
