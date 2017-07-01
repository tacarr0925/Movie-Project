package com.example.android.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Movie;

import com.example.android.popularmovies.data.MovieContract.MovieEntry;

/**
 * Created by Travis on 6/29/2017.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "movie.db";

    private static final int DATABASE_VERSION = 1;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_MOVIE_TABLE =
                "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
                MovieEntry._ID                 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieEntry.COLUMN_MOVIE_ID     + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_TITLE        + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_RATING       + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_SYNOPSIS     + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_POSTER       + " TEXT NOT NULL, " +
                " UNIQUE (" + MovieEntry.COLUMN_MOVIE_ID + " ) ON CONFLICT REPLACE);";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
