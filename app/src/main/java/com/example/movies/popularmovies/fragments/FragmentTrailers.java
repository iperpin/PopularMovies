package com.example.movies.popularmovies.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
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
import com.example.movies.popularmovies.adapters.TrailersAdapter;
import com.example.movies.popularmovies.objects.MovieTrailer;
import com.example.movies.popularmovies.objects.MovieTrailerResult;

public class FragmentTrailers extends Fragment implements TrailersAdapter.ListItemClickListener {

    private static final String LOG_TAG = "DetailMovie";

    private MovieTrailer movieTrailer;
    private TrailersAdapter adapter;
    private RecyclerView recyclerView;


    public FragmentTrailers() {
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_detail_movie_trailers, container, false);

        recyclerView = view.findViewById(R.id.rvTrailers);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 1));
        adapter = new TrailersAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setClickListener(this);

        Bundle arguments = getArguments();
        if (arguments != null && arguments.getParcelable(getString(R.string.intent_movie_trailers))!=null) {
            Log.d(LOG_TAG,"oncreateview trailer not null");
            movieTrailer = getArguments().getParcelable(getString(R.string.intent_movie_trailers));
            if (movieTrailer != null) {
                adapter.update(movieTrailer.getResults());
            }
        }

        return view;
    }

    public void putArguments(Bundle arguments){
//        if(isAdded()){
//            return;
//        }
        if (arguments != null && arguments.getParcelable(getString(R.string.intent_movie_trailers))!=null) {
            movieTrailer = arguments.getParcelable(getString(R.string.intent_movie_trailers));
            if (movieTrailer != null) {
                Log.d(LOG_TAG,"put argument trailer not null");
                adapter.update(movieTrailer.getResults());
            }
        }
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
            intent.setData( Uri.parse("http://www.youtube.com/watch?v=" + id));
            startActivity(intent);
        }
    }
}
