package com.example.movies.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.example.movies.popularmovies.objects.MovieReviewResult;
import java.util.List;
import com.example.movies.popularmovies.R;

import java.util.ArrayList;



public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private static final String LOG_TAG = "ReviewAdapter";
    private ListItemClickListener mOnClickListener;
    private List<MovieReviewResult> reviews;

    public ReviewAdapter() {
        reviews = new ArrayList<>();
    }

    public void setClickListener(ListItemClickListener itemClickListener) {
        this.mOnClickListener = itemClickListener;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        Context context = viewGroup.getContext();
        int layoutIdForListItem = com.example.movies.popularmovies.R.layout.list_item_review;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        ReviewViewHolder viewHolder = new ReviewViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        String author = reviews.get(position).getAuthor();
        String comment = reviews.get(position).getContent();
        holder.commentTv.setText(comment);
        holder.authorTv.setText(author);
    }


    @Override
    public int getItemCount() {
        return reviews == null ? 0 : reviews.size();
    }


    public void update(List<MovieReviewResult> items) {
        if (items != null && items.size() > 0) {
            reviews.clear();
            reviews.addAll(items);
            notifyDataSetChanged();
        }
    }

    public void clear() {
        reviews.clear();
        notifyDataSetChanged();
    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex, MovieReviewResult movie);
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        TextView authorTv;
        TextView commentTv;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            authorTv = itemView.findViewById(R.id.author_tv);
            commentTv = itemView.findViewById(R.id.comment_tv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            if (clickedPosition != RecyclerView.NO_POSITION) {
                MovieReviewResult movie = reviews.get(clickedPosition);
                mOnClickListener.onListItemClick(clickedPosition, movie);
            }

        }
    }
}
