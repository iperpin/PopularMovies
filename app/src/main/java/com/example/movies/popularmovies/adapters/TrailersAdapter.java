package com.example.movies.popularmovies.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.movies.popularmovies.R;
import com.example.movies.popularmovies.objects.Youtube;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailersViewHolder> {

    private static final String LOG_TAG = "ReviewAdapter";
    private static final String TRAILERS_THUMBNAIL_BASE_URL = "https://img.youtube.com/vi/";
    private ListItemClickListener mOnClickListener;
    private List<Youtube> trailers;

    public TrailersAdapter() {
        trailers = new ArrayList<>();
    }

    public void setClickListener(ListItemClickListener itemClickListener) {
        this.mOnClickListener = itemClickListener;
    }

    @Override
    public TrailersViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.list_item_trailer;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        TrailersViewHolder viewHolder = new TrailersViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TrailersViewHolder holder, int position) {
        String name = trailers.get(position).getName();
        String comment = trailers.get(position).getName();
        holder.videoName.setText(name);
        Drawable placeholder = holder.videoName.getContext().getResources().getDrawable(R.drawable.image);
        Drawable error = holder.videoName.getContext().getResources().getDrawable(R.drawable.cancel);
        Picasso.with(holder.videoIm.getContext())
                .load(TRAILERS_THUMBNAIL_BASE_URL + trailers.get(position).getSource() + "/0.jpg")
                .placeholder(placeholder)
                .error(error)
                .into(holder.videoIm);
    }


    @Override
    public int getItemCount() {
        return trailers == null ? 0 : trailers.size();
    }


    public void update(List<Youtube> items) {
        if (items != null && items.size() > 0) {
            trailers.clear();
            trailers.addAll(items);
            notifyDataSetChanged();
        }
    }

    public void clear() {
        trailers.clear();
        notifyDataSetChanged();
    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex, Youtube movie);
    }

    class TrailersViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        TextView videoName;
        ImageView videoIm;

        public TrailersViewHolder(View itemView) {
            super(itemView);
            videoName = itemView.findViewById(R.id.video_name_tv);
            videoIm = itemView.findViewById(R.id.video_im);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            if (clickedPosition != RecyclerView.NO_POSITION) {
                Youtube movie = trailers.get(clickedPosition);
                mOnClickListener.onListItemClick(clickedPosition, movie);
            }

        }
    }
}
