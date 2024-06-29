package com.example.phototube_android.ui.adapters;

import android.content.Context;
import android.content.Intent;
import com.example.phototube_android.activities.EditVideoActivity;
import android.media.Image;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.phototube_android.R;


import com.example.phototube_android.classes.Video;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {
    private List<Video> videos;
    private final Context context;
    public List<Video> filteredVideoList;


    public VideoAdapter(Context context, List<Video> videos) {
        this.context = context;
        this.videos = videos;
        this.filteredVideoList = videos;

    }


    public List<Video> getFilteredVideoList() {
        return filteredVideoList;
    }

    //func to add video
    public void addVideoToList(Video video) {
        videos.add(video);
        //this.getFilteredVideoList().add(video);
        this.notifyDataSetChanged(); // Notify the adapter that data has changed
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
        notifyDataSetChanged();  // Notify any registered observers that the data set has changed.
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.video_item, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        Video currentVideo = filteredVideoList.get(position);

        holder.videoNameTextView.setText(currentVideo.getTitle());
        holder.authorTextView.setText(currentVideo.getCreatedBy());
        holder.viewsTextView.setText(String.valueOf(currentVideo.getViews()));


        // Load a frame from the video using Glide
        String videoUrl = currentVideo.getVideoUrl();
        Glide.with(holder.thumbnail.getContext())
                .asBitmap()
                .load("http://10.0.2.2:1324" +videoUrl)
                .frame(1000000) // Load frame at 1 second (1000000 microseconds)
                .into(holder.thumbnail);

        // Load creator image
        String creatorImageUrl = currentVideo.getCreatorImg();
        Glide.with(holder.creatorImage.getContext())
                .load("http://10.0.2.2:1324" + creatorImageUrl)
                .into(holder.creatorImage);

        // Format date
        String formattedDate = formatDate(currentVideo.getDate());
        holder.timeAgoTextView.setText(formattedDate);

// Set click listener for the video thumbnail
        holder.thumbnail.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), EditVideoActivity.class);
            intent.putExtra("videoId", currentVideo.get_id());
            intent.putExtra("Title", currentVideo.getTitle());
            intent.putExtra("VideoUrl", currentVideo.getVideoUrl());
            holder.itemView.getContext().startActivity(intent);
        });
//
//        // Set click listener for the creator image
//        holder.creatorImage.setOnClickListener(v -> {
//            Intent intent = new Intent(holder.itemView.getContext(), CreatorProfileActivity.class);
//            intent.putExtra("creatorId", .getCreatorId());
//            holder.itemView.getContext().startActivity(intent);
//        });
    }

    private String formatDate(Date date) {
        try {
            SimpleDateFormat desiredFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
            return desiredFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public int getItemCount() {
        return filteredVideoList.size();
    }

    static class VideoViewHolder extends RecyclerView.ViewHolder {
        final TextView videoNameTextView;
        final TextView authorTextView;
        final TextView timeAgoTextView;
        final TextView viewsTextView;
        final ImageView thumbnail;
        final ImageView creatorImage;

        VideoViewHolder(View itemView) {
            super(itemView);
            videoNameTextView = itemView.findViewById(R.id.videoName);
            authorTextView = itemView.findViewById(R.id.author);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            creatorImage = itemView.findViewById(R.id.creatorImage);
            timeAgoTextView = itemView.findViewById(R.id.timeAgo);
            viewsTextView = itemView.findViewById(R.id.viewsCount);
        }
    }


    // Filter logic
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Video> filteredList = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(videos); // no filter applied
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (Video video : videos) {
                        if (video.getTitle().toLowerCase().contains(filterPattern)) {
                            filteredList.add(video);
                        }
                    }
                }
                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredVideoList.clear();
                filteredVideoList.addAll((List) results.values);
                notifyDataSetChanged();
            }
        };
    }
}