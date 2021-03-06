package com.example.movies.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;

import com.example.movies.popularmovies.objects.MovieObject;
import com.example.movies.popularmovies.objects.MoviePageObject;
import com.example.movies.popularmovies.objects.MovieReview;
import com.example.movies.popularmovies.objects.MovieTrailer;
import com.google.gson.Gson;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

public class Utils {

    private static final String LOG_TAG = "Utils";

    public static List<MovieObject> parseMoviesJSON(String json) {
        Gson gson = new Gson();
        MoviePageObject moviePageObject = gson.fromJson(json, MoviePageObject.class);
        return moviePageObject.getResults();
    }

    public static MovieTrailer parseTrailersJSON(String json) {
        Gson gson = new Gson();
        MovieTrailer movieTrailer = gson.fromJson(json, MovieTrailer.class);
        return movieTrailer;
    }

    public static MovieReview parseReviewsJSON(String json) {
        Gson gson = new Gson();
        MovieReview movieReview = gson.fromJson(json, MovieReview.class);
        return movieReview;
    }

    public static void savePrefsInt(Context context, String key, int value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static int LoadPreferencesInt(Context context, String key, int value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        int data = sharedPreferences.getInt(key, value);
        return data;
    }

    public static String getUrlSpinnerChoice(Context context) {
        String[] urlChoiceArray = context.getResources().getStringArray(R.array.urls_choice);
        int lastSpinnerPosition = Utils.LoadPreferencesInt(context, context.getString(R.string.spinner_position), 0);
        String urlChoice = urlChoiceArray[lastSpinnerPosition];
        return urlChoice;
    }

    public static String getUrlChoice(Context context, int index) {
        String[] urlChoiceArray = context.getResources().getStringArray(R.array.urls_choice);
        String urlChoice = urlChoiceArray[index];
        return urlChoice;
    }


    public static boolean isNetworkAvailable(Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public static int numberOfColumns(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // You can change this divider to adjust the size of the poster
        int widthDivider = 400;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 2) return 2; //to keep the grid aspect
        return nColumns;
    }
}
