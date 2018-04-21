package com.example.movies.popularmovies.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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


    public FragmentReviews() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_detail_movie_reviews, container, false);

        recyclerView = view.findViewById(R.id.rvReviews);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 1));
        adapter = new ReviewAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setClickListener(this);

        Bundle arguments = getArguments();
        if (arguments != null && arguments.getParcelable(getString(R.string.intent_movie_desc)) != null) {
            Log.d(LOG_TAG, "onCreateView review not null");
            movieObject = getArguments().getParcelable(getString(R.string.intent_movie_desc));
        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        request((getString(R.string.url_base) + movieObject.getId() + "/reviews"
                + getString(R.string.api_key_tag) + getString(R.string.api_key)));
    }

    public void putArguments(MovieReview movieReview) {

        if (movieReview != null) {
            adapter.update(movieReview.getResults());
        }
    }

    public void request(String url) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                String bodyResponse = response.body().string();

                final MovieReview movieReview = Utils.parseReviewsJSON(bodyResponse);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
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
