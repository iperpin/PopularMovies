package com.example.movies.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.movies.popularmovies.adapters.ViewPagerAdapter;
import com.example.movies.popularmovies.fragments.FragmentDescription;
import com.example.movies.popularmovies.fragments.FragmentReviews;
import com.example.movies.popularmovies.fragments.FragmentTrailers;
import com.example.movies.popularmovies.objects.MovieObject;
import com.example.movies.popularmovies.objects.MovieReview;
import com.example.movies.popularmovies.objects.MovieTrailer;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DetailMovieActivity extends AppCompatActivity {

    private static final String LOG_TAG = "DetailMovie";

    ViewPagerAdapter adapter;
    private FragmentReviews revFragment;
    private FragmentTrailers trailersFragment;
    private FragmentDescription descFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);

        Intent i = getIntent();
        MovieObject movieObject = i.getParcelableExtra(getString(R.string.intent_movie_object));

        ViewPager viewPager = findViewById(R.id.pager);

        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        Bundle bundle = new Bundle();
        bundle.putParcelable(getString(R.string.intent_movie_desc), movieObject);

        descFragment = new FragmentDescription();
        descFragment.setArguments(bundle);
        adapter.addFragment(descFragment, "Description");

        revFragment = new FragmentReviews();
        revFragment.setArguments(bundle);
        adapter.addFragment(revFragment, "Reviews");

        trailersFragment = new FragmentTrailers();
        trailersFragment.setArguments(bundle);
        adapter.addFragment(trailersFragment, "Trailers");

        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        if (movieObject != null) {
            String title = movieObject.getTitle() != null ? movieObject.getTitle() : "";
            setTitle(title);
        }
    }
}
