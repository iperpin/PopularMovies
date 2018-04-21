package com.example.movies.popularmovies.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.movies.popularmovies.objects.MovieObject;
import com.squareup.picasso.Picasso;
import com.example.movies.popularmovies.R;

import java.util.ArrayList;
import java.util.List;


public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private static final String LOG_TAG = "MovieAdapter";
    private ListItemClickListener mOnClickListener;
    private List<MovieObject> movies;

    public MovieAdapter() {
        movies = new ArrayList<>();
    }

    public void setClickListener(ListItemClickListener itemClickListener) {
        this.mOnClickListener = itemClickListener;
    }

    @Override
    public MovieAdapter.MovieViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.list_item_movie;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        MovieViewHolder viewHolder = new MovieViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        String dataMovie = movies.get(position).getPosterPath();
        String posterPath = holder.movieIm.getContext().getString(R.string.posterImagePath);
        String completePath = posterPath + dataMovie;
        Drawable placeholder = holder.movieIm.getResources().getDrawable(R.drawable.image);
        Drawable error = holder.movieIm.getResources().getDrawable(R.drawable.cancel);
        Picasso.with(holder.movieIm.getContext())
                .load(completePath)
                .placeholder(placeholder)
                .error(error)
                .into(holder.movieIm);
    }


    @Override
    public int getItemCount() {
        return movies == null ? 0 : movies.size();
    }


    public void update(List<MovieObject> items) {
        if (items != null && items.size() > 0) {
            movies.clear();
            movies.addAll(items);
            notifyDataSetChanged();
        }
        if (items.size() == 0) {
            movies.clear();
            notifyDataSetChanged();
        }
    }

    public void clear() {
        movies.clear();
        notifyDataSetChanged();
    }

    public List<MovieObject> getMovies() {
        return movies;
    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex, MovieObject movie);
    }

    class MovieViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        ImageView movieIm;

        public MovieViewHolder(View itemView) {
            super(itemView);
            movieIm = itemView.findViewById(R.id.movie_im);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            if (clickedPosition != RecyclerView.NO_POSITION) {
                MovieObject movie = movies.get(clickedPosition);
                mOnClickListener.onListItemClick(clickedPosition, movie);
            }

        }
    }
}
