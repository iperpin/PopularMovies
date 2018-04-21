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

import com.example.movies.popularmovies.BuildConfig;
import com.example.movies.popularmovies.R;
import com.example.movies.popularmovies.Utils;
import com.example.movies.popularmovies.adapters.TrailersAdapter;
import com.example.movies.popularmovies.objects.MovieObject;
import com.example.movies.popularmovies.objects.MovieTrailer;
import com.example.movies.popularmovies.objects.Youtube;

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
    private MovieTrailer movieTrailer;


    public FragmentTrailers() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        if (movieTrailer != null) {
            outState.putParcelable(getString(R.string.saved_trailers), movieTrailer);
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
                requestItems();
            }
        });

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            Log.d(LOG_TAG, "Load saved trailers");
            movieTrailer = savedInstanceState.getParcelable(getString(R.string.saved_trailers));
            updateTrailers(movieTrailer);
        } else {
            Log.d(LOG_TAG, "Go to internet to fetch trailers");
            requestItems();
        }
    }

    private void requestItems() {
        if (Utils.isNetworkAvailable(getContext())) {
            request((getString(R.string.url_base) + movieObject.getId() + "/" + getString(R.string.trailers_tag)
                    + getString(R.string.api_key_tag) + BuildConfig.MY_MOVIE_API_KEY));
        } else {
            setNoInternetTextView();
        }
        swipeLayout.setRefreshing(false);
    }

    public void putArguments(MovieTrailer movieTrailer) {
        if (movieTrailer != null) {
            if (movieTrailer.getYoutube() == null || movieTrailer.getYoutube().size() == 0) {
                noInternetTextView.setText(getString(R.string.no_trailers));
                noInternetTextView.setVisibility(View.VISIBLE);
            } else {
                updateTrailers(movieTrailer);
            }
        }
    }

    private void updateTrailers(MovieTrailer trailers) {
        if (trailers == null) {
            return;
        }
        if (trailers.getYoutube() == null) {
            return;
        }
        adapter.update(trailers.getYoutube());

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
                        setNoInternetTextView();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                String bodyResponse = response.body().string();

                movieTrailer = Utils.parseTrailersJSON(bodyResponse);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        noInternetTextView.setVisibility(View.INVISIBLE);
                        Log.d(LOG_TAG, movieTrailer.toString());
                        putArguments(movieTrailer);
                    }
                });
            }

        });
    }

    private void setNoInternetTextView() {
        adapter.clear();
        noInternetTextView.setText(getString(R.string.no_internet));
        noInternetTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onListItemClick(int clickedItemIndex, Youtube movie) {
        goToYoutube(movie.getSource());
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
