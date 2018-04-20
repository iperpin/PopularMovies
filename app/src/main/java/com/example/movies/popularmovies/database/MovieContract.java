package com.example.movies.popularmovies.database;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.example.movies.popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private MovieContract() {
    }

    public static class MovieEntry implements BaseColumns {

        public static final String COLUMN_ID = "_id";
        public static final String TABLE_NAME = "movies";
        public static final String MOVIE_ID = "id";
        public static final String MOVIE_VOTEAVG = "vote_avg";
        public static final String MOVIE_TITLE = "title";
        public static final String MOVIE_POSTER = "poster";
        public static final String MOVIE_BACKDROP = "backdrop";
        public static final String MOVIE_OVERVIEW = "overview";
        public static final String MOVIE_RELEASEDATE = "release_date";
        public static final String MOVIE_VOTECOUNT = "vote_count";
        public static final String MOVIE_VIDEO = "video";
        public static final String MOVIE_POPULARITY = "popularity";
        public static final String MOVIE_ORIGINAL_LANGUAGE = "original_language";
        public static final String MOVIE_ORIGINAL_TITLE = "original_title";
        public static final String MOVIE_ADULT = "adult";


        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(TABLE_NAME).build();

        public static Uri buildMoviesUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
