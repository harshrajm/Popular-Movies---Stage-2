package com.example.android.popularmoviesone.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.android.popularmoviesone.data.MoviesContract.MoviesEntry;


import static android.R.attr.version;

/**
 * Created by Harshraj on 05-06-2017.
 */

public class MoviesDbHelper extends SQLiteOpenHelper {

    //DB name
    private static final String DATABASE_NAME = "movie.db";
    //DB version
    private static final int DATABASE_VERSION = 1;

    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_MOVIES_TABLE = "CREATE TABLE " + MoviesEntry.TABLE_NAME + " ("
                                            + MoviesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                                            + MoviesEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, "
                                            + MoviesEntry.COLUMN_MOVIE_NAME + " TEXT NOT NULL, "
                                            + MoviesEntry.COLUMN_MOVIE_RATING + " INT NOT NULL, "
                                            + MoviesEntry.COLUMN_MOVIE_DATE + " TEXT NOT NULL, "
                                            + MoviesEntry.COLUMN_MOVIE_IMAGE_URL + " TEXT NOT NULL, "
                                            + MoviesEntry.COLUMN_MOVIE_YOUTUBE_KEY + " TEXT, "
                                            + MoviesEntry.COLUMN_MOVIE_SYNOPSIS + " TEXT, "
                                            + MoviesEntry.COLUMN_MOVIE_REVIEWS + " TEXT );";

        db.execSQL(SQL_MOVIES_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
