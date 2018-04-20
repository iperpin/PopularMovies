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
import com.example.movies.popularmovies.adapters.ReviewAdapter;
import com.example.movies.popularmovies.objects.MovieReview;
import com.example.movies.popularmovies.objects.MovieReviewResult;

public class FragmentReviews extends Fragment implements ReviewAdapter.ListItemClickListener {

    private static final String LOG_TAG = "DetailMovie";

    private MovieReview movieReview;
    private ReviewAdapter adapter;
    private RecyclerView recyclerView;


    public FragmentReviews() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.d(LOG_TAG,"ONCREATEVIEW REVIEW");
        View view = inflater.inflate(
                R.layout.fragment_detail_movie_reviews, container, false);

        recyclerView = view.findViewById(R.id.rvReviews);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 1));
        adapter = new ReviewAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setClickListener(this);

        Bundle arguments = getArguments();
        if (arguments != null && arguments.getParcelable(getString(R.string.intent_movie_reviews))!=null){
            Log.d(LOG_TAG,"onCreateView review not null");
            movieReview = getArguments().getParcelable(getString(R.string.intent_movie_reviews));
            if (movieReview != null) {
                adapter.update(movieReview.getResults());
            }
        }

        return view;
    }

    public void putArguments(Bundle arguments){
//        if(!isAdded()){
//            return;
//        }
        if (arguments != null && arguments.getParcelable(getString(R.string.intent_movie_reviews))!=null){
            movieReview = arguments.getParcelable(getString(R.string.intent_movie_reviews));
            if (movieReview != null) {
                Log.d(LOG_TAG,"putargument review not null");
                adapter.update(movieReview.getResults());
            }
        }

    }

    @Override
    public void onListItemClick(int clickedItemIndex, MovieReviewResult movie) {

    }
}
