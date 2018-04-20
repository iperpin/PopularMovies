package com.example.movies.popularmovies.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MovieDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "movie.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE IF NOT EXISTS " + MovieContract.MovieEntry.TABLE_NAME + " ( " +
                MovieContract.MovieEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieContract.MovieEntry.MOVIE_ID + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.MOVIE_VOTEAVG + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.MOVIE_TITLE + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.MOVIE_POSTER + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.MOVIE_BACKDROP + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.MOVIE_OVERVIEW + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.MOVIE_RELEASEDATE + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.MOVIE_VOTECOUNT + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.MOVIE_VIDEO + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.MOVIE_POPULARITY + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.MOVIE_ORIGINAL_LANGUAGE + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.MOVIE_ORIGINAL_TITLE + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.MOVIE_ADULT + " TEXT NOT NULL " +
                ");";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);

    }
}
