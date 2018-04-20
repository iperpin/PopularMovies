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
    private MovieReview movieReviews;
    private MovieTrailer movieTrailers;


    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(LOG_TAG, "OnSaved");
        outState.putParcelable("movie_reviews", movieReviews);
        outState.putParcelable("movie_trailes", movieTrailers);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);

        Intent i = getIntent();
        MovieObject movieObject = i.getParcelableExtra(getString(R.string.intent_movie_object));
        //Log.d(LOG_TAG, movieObject.toString());

        ViewPager viewPager = findViewById(R.id.pager);

        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        Bundle bundle = new Bundle();
        bundle.putParcelable(getString(R.string.intent_movie_desc), movieObject);
        descFragment = new FragmentDescription();
        descFragment.setArguments(bundle);
        adapter.addFragment(descFragment, "Description");

        revFragment = new FragmentReviews();
        adapter.addFragment(revFragment, "Reviews");

        trailersFragment = new FragmentTrailers();
        adapter.addFragment(trailersFragment, "Trailers");

        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        if (movieObject != null) {
            String title = movieObject.getTitle() != null ? movieObject.getTitle() : "";
            setTitle(title);

            request((getString(R.string.url_base) + movieObject.getId() + "/reviews"
                    + getString(R.string.api_key_tag) + getString(R.string.api_key)), "review");
            request((getString(R.string.url_base) + movieObject.getId() + "/videos"
                    + getString(R.string.api_key_tag) + getString(R.string.api_key)), "trailer");
        }
    }

    public void request(String url, final String type) {
        Log.d(LOG_TAG, "New petition to: " + url);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                DetailMovieActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                String bodyResponse = response.body().string();

                if (type.equalsIgnoreCase("review")) {
                    movieReviews = Utils.parseReviewsJSON(bodyResponse);
                    DetailMovieActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Bundle bundle = new Bundle();
                            bundle.putParcelable(getString(R.string.intent_movie_reviews), movieReviews);
                            revFragment.setArguments(bundle);
                        }
                    });
                } else if (type.equalsIgnoreCase("trailer")) {
                    movieTrailers = Utils.parseTrailersJSON(bodyResponse);
                    DetailMovieActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Bundle bundle = new Bundle();
                            bundle.putParcelable(getString(R.string.intent_movie_trailers), movieTrailers);
                            trailersFragment.setArguments(bundle);
                        }
                    });
                }
            }
        });
    }

}
