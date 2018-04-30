package com.example.movies.popularmovies.fragments;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.movies.popularmovies.R;
import com.example.movies.popularmovies.database.MovieContract;
import com.example.movies.popularmovies.objects.MovieObject;
import com.squareup.picasso.Picasso;

public class FragmentDescription extends Fragment {

    private static final String LOG_TAG = "FragmentDescription";


    ImageView posterImageView;
    ImageView headerImageView;
    TextView titleTextView;
    TextView releaseDateTextView;
    TextView userRatingTextView;
    TextView descriptionTextView;
    RatingBar ratingBar;
    FloatingActionButton fabButton;
    private MovieObject movieObject;
    private boolean isInFavorites = false;
    private Integer movieId;


    public FragmentDescription() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(
                R.layout.fragment_detail_movie_description, container, false);
        movieObject = getArguments().getParcelable(getString(R.string.intent_movie_desc));
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        posterImageView = view.findViewById(R.id.movie_poster_im);
        titleTextView = view.findViewById(R.id.title_tv);
        releaseDateTextView = view.findViewById(R.id.release_date_tv);
        userRatingTextView = view.findViewById(R.id.user_rating_tv);
        descriptionTextView = view.findViewById(R.id.description_tv);
        ratingBar = view.findViewById(R.id.ratingBar);
        headerImageView = view.findViewById(R.id.header_im);
        fabButton = view.findViewById(R.id.floatingActionButton);


        if (movieObject != null) {

            movieId = movieObject.getId();
            isInFavorites = checkIfInsideDatabase(movieId);

            if (isInFavorites) {
                fabButton.setImageResource(R.drawable.star_red);
            } else {
                fabButton.setImageResource(R.drawable.star);
            }


            if (movieObject.getBackdropPath() != null) {
                Drawable placeholder = view.getContext().getResources().getDrawable(R.drawable.image);
                Drawable error = view.getContext().getResources().getDrawable(R.drawable.cancel);
                Picasso.with(view.getContext())
                        .load(getString(R.string.headerImagePath) + movieObject.getBackdropPath())
                        .placeholder(placeholder)
                        .error(error)
                        .into(headerImageView);
            }

            titleTextView.setText(movieObject.getTitle());

            if (movieObject.getPosterPath() != null) {
                Drawable placeholder = view.getContext().getResources().getDrawable(R.drawable.image);
                Drawable error = view.getContext().getResources().getDrawable(R.drawable.cancel);
                Picasso.with(view.getContext())
                        .load(getString(R.string.posterImagePath) + movieObject.getPosterPath())
                        .placeholder(placeholder)
                        .error(error)
                        .into(posterImageView);
            }

            if (movieObject.getReleaseDate() != null) {
                releaseDateTextView.setText(movieObject.getReleaseDate());
            }

            if (movieObject.getVoteAverage() != null) {
                String voteAverage = String.valueOf(movieObject.getVoteAverage()) + getString(R.string.divide_ten);
                userRatingTextView.setText(voteAverage);
                float ratingStars = (float) (movieObject.getVoteAverage() / 2);
                ratingBar.setRating(ratingStars);
            }

            if (movieObject.getOverview() != null) {
                descriptionTextView.setText(movieObject.getOverview());
            }

        }

        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isInFavorites) {
                    deleteFavMovie(movieId);
                } else {
                    addFavoriteMovie(movieObject);
                }
            }
        });
    }


    private void addFavoriteMovie(MovieObject movie) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieContract.MovieEntry.MOVIE_ID, movie.getId());
        contentValues.put(MovieContract.MovieEntry.MOVIE_VOTEAVG, movie.getVoteAverage());
        contentValues.put(MovieContract.MovieEntry.MOVIE_TITLE, movie.getTitle());
        contentValues.put(MovieContract.MovieEntry.MOVIE_POSTER, movie.getPosterPath());
        contentValues.put(MovieContract.MovieEntry.MOVIE_BACKDROP, movie.getBackdropPath());
        contentValues.put(MovieContract.MovieEntry.MOVIE_OVERVIEW, movie.getOverview());
        contentValues.put(MovieContract.MovieEntry.MOVIE_RELEASEDATE, movie.getReleaseDate());
        contentValues.put(MovieContract.MovieEntry.MOVIE_VOTECOUNT, movie.getVoteCount());
        contentValues.put(MovieContract.MovieEntry.MOVIE_VIDEO, movie.getVideo());
        contentValues.put(MovieContract.MovieEntry.MOVIE_POPULARITY, movie.getPopularity());
        contentValues.put(MovieContract.MovieEntry.MOVIE_ORIGINAL_LANGUAGE, movie.getOriginalLanguage());
        contentValues.put(MovieContract.MovieEntry.MOVIE_ORIGINAL_TITLE, movie.getOriginalTitle());
        contentValues.put(MovieContract.MovieEntry.MOVIE_ADULT, movie.getAdult());

        // Insert the content values via a ContentResolver
        Uri uri = getContext().getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);

        isInFavorites = true;
        fabButton.setImageResource(R.drawable.star_red);
    }

    private void deleteFavMovie(int id) {
        final String SELECTION = MovieContract.MovieEntry.MOVIE_ID + " = " + id;
        fabButton.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.white));
        getContext().getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI, SELECTION, null);
        isInFavorites = false;
        fabButton.setImageResource(R.drawable.star);
    }

    public Boolean checkIfInsideDatabase(int id) {

        String[] projection = {
                MovieContract.MovieEntry.COLUMN_ID,
                MovieContract.MovieEntry.MOVIE_ID,
                MovieContract.MovieEntry.MOVIE_VOTEAVG,
                MovieContract.MovieEntry.MOVIE_TITLE,
                MovieContract.MovieEntry.MOVIE_POSTER,
                MovieContract.MovieEntry.MOVIE_BACKDROP,
                MovieContract.MovieEntry.MOVIE_OVERVIEW,
                MovieContract.MovieEntry.MOVIE_RELEASEDATE,
                MovieContract.MovieEntry.MOVIE_VOTECOUNT,
                MovieContract.MovieEntry.MOVIE_VIDEO,
                MovieContract.MovieEntry.MOVIE_POPULARITY,
                MovieContract.MovieEntry.MOVIE_ORIGINAL_LANGUAGE,
                MovieContract.MovieEntry.MOVIE_ORIGINAL_TITLE,
                MovieContract.MovieEntry.MOVIE_ADULT
        };

        Cursor mCursor = getContext().getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI, projection,
                MovieContract.MovieEntry.MOVIE_ID + "=" + id,
                null, null);

        if (mCursor.getCount() > 0) {
            return true;
        }

        mCursor.close();

        return false;
    }
}
