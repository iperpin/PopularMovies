package com.example.movies.popularmovies.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
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
import com.example.movies.popularmovies.adapters.TrailersAdapter;
import com.example.movies.popularmovies.objects.MovieObject;
import com.example.movies.popularmovies.objects.MovieTrailer;
import com.example.movies.popularmovies.objects.MovieTrailerResult;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FragmentTrailers extends Fragment implements TrailersAdapter.ListItemClickListener {

    private static final String LOG_TAG = "DetailMovie";

    private MovieObject movieObject;
    private TrailersAdapter adapter;
    private RecyclerView recyclerView;
    private TextView noInternetTextView;
    private SwipeRefreshLayout swipeLayout;


    public FragmentTrailers() {
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_detail_movie_trailers, container, false);

        recyclerView = view.findViewById(R.id.rvTrailers);
        swipeLayout = view.findViewById(R.id.swipe_layout);
        noInternetTextView = view.findViewById(R.id.tv_no_internet);
        noInternetTextView.setVisibility(View.INVISIBLE);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 1));
        adapter = new TrailersAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setClickListener(this);

        Bundle arguments = getArguments();
        if (arguments != null && arguments.getParcelable(getString(R.string.intent_movie_desc)) != null) {
            movieObject = getArguments().getParcelable(getString(R.string.intent_movie_desc));
        }

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItems();
            }
        });

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        refreshItems();
    }

    private void refreshItems() {
        request((getString(R.string.url_base) + movieObject.getId() + "/videos"
                + getString(R.string.api_key_tag) + getString(R.string.api_key)));
        swipeLayout.setRefreshing(false);
    }

    public void putArguments(MovieTrailer movieTrailer) {
        if (movieTrailer != null) {
            if (movieTrailer.getResults().size()==0){
                noInternetTextView.setText(getString(R.string.no_trailers));
                noInternetTextView.setVisibility(View.VISIBLE);
            }
            adapter.update(movieTrailer.getResults());
        }
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

                final MovieTrailer movieTrailer = Utils.parseTrailersJSON(bodyResponse);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        noInternetTextView.setVisibility(View.INVISIBLE);
                        putArguments(movieTrailer);
                    }
                });
            }

        });
    }

    @Override
    public void onListItemClick(int clickedItemIndex, MovieTrailerResult movie) {
        goToYoutube(movie.getKey());
    }

    private void goToYoutube(String id) {
        Intent intent = null;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.google.android.youtube");
            intent.setData(Uri.parse("http://www.youtube.com/watch?v=" + id));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://www.youtube.com/watch?v=" + id));
            startActivity(intent);
        }
    }
}
