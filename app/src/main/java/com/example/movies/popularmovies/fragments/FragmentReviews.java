package com.example.movies.popularmovies.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.movies.popularmovies.R;
import com.example.movies.popularmovies.Utils;
import com.example.movies.popularmovies.adapters.ReviewAdapter;
import com.example.movies.popularmovies.objects.MovieObject;
import com.example.movies.popularmovies.objects.MovieReview;
import com.example.movies.popularmovies.objects.MovieReviewResult;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FragmentReviews extends Fragment implements ReviewAdapter.ListItemClickListener {

    private static final String LOG_TAG = "DetailMovie";

    private MovieObject movieObject;
    private ReviewAdapter adapter;
    private RecyclerView recyclerView;
    private TextView noInternetTextView;
    private SwipeRefreshLayout swipeLayout;
    private MovieReview movieReview;


    public FragmentReviews() {
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {

        if (movieReview != null) {
            outState.putParcelable(getString(R.string.saved_review), movieReview);
        }
        super.onSaveInstanceState(outState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_detail_movie_recyclerview, container, false);

        recyclerView = view.findViewById(R.id.rv);
        swipeLayout = view.findViewById(R.id.swipe_layout);
        noInternetTextView = view.findViewById(R.id.tv_no_internet);
        noInternetTextView.setVisibility(View.INVISIBLE);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 1));
        adapter = new ReviewAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setClickListener(this);

        Bundle arguments = getArguments();
        if (arguments != null && arguments.getParcelable(getString(R.string.intent_movie_desc)) != null) {
            movieObject = getArguments().getParcelable(getString(R.string.intent_movie_desc));
        }

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestItems();
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            Log.d(LOG_TAG,"Load saved reviews");
            movieReview = savedInstanceState.getParcelable(getString(R.string.saved_review));
            updateReviews(movieReview);
        } else {
            Log.d(LOG_TAG,"Go to internet to fetch reviews");
            requestItems();
        }
    }


    private void requestItems() {
        request((getString(R.string.url_base) + movieObject.getId() + "/"+getString(R.string.reviews_tag)
                + getString(R.string.api_key_tag) + getString(R.string.api_key)));
        swipeLayout.setRefreshing(false);
    }

    public void putArguments(MovieReview movieReview) {

        if (movieReview != null) {
            if (movieReview.getResults()==null || movieReview.getResults().size() == 0) {
                noInternetTextView.setText(getString(R.string.no_reviews));
                noInternetTextView.setVisibility(View.VISIBLE);
            }
            updateReviews(movieReview);
        }
    }


    private void updateReviews(MovieReview reviews) {
        adapter.update(reviews.getResults());
    }

    public void request(String url) {
        Log.d(LOG_TAG, "New petition to: " + url);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        noInternetTextView.setText(getString(R.string.no_internet));
                        noInternetTextView.setVisibility(View.VISIBLE);
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                String bodyResponse = response.body().string();

                movieReview = Utils.parseReviewsJSON(bodyResponse);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        noInternetTextView.setVisibility(View.INVISIBLE);
                        putArguments(movieReview);
                    }
                });
            }

        });
    }

    @Override
    public void onListItemClick(int clickedItemIndex, MovieReviewResult movie) {

    }
}
