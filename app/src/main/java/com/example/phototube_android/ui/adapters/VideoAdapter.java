package com.example.phototube_android.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.phototube_android.R;
import com.example.phototube_android.model.Video;

import java.util.ArrayList;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {
    private final List<Video> videos;
    private final Context context;
    public List<Video> filteredVideoList;

    public VideoAdapter(Context context, List<Video> videos) {
        this.context = context;
        this.videos = videos;
        this.filteredVideoList = new ArrayList<>(videos); // Initialize with all videos
    }

    // Updated function to set videos
    public void setVideos(List<Video> newVideos) {
        filteredVideoList.clear();
        filteredVideoList.addAll(newVideos);
        notifyDataSetChanged(); // Notify the adapter of the data change
    }

    public List<Video> getFilteredVideoList() {
        return filteredVideoList;
    }

    public void addVideoToList(Video video) {
        videos.add(video);
        this.getFilteredVideoList().add(video);
        notifyDataSetChanged();
    }

    public void deleteVideo(Video video) {
        videos.remove(video);
        this.getFilteredVideoList().remove(video);
        notifyDataSetChanged();
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
        holder.viewsTextView.setText(currentVideo.getViews());
        holder.timeAgoTextView.setText(currentVideo.getDate());
        Glide.with(context)
                .load(currentVideo.getImageUrl())
                .into(holder.image);

//        holder.itemView.setOnClickListener(v -> {
//            Intent intent = new Intent(context, VideoActivity.class);
//            intent.putExtra("videoId", currentVideo.getId());
//            intent.putExtra("videoName", currentVideo.getTitle());
//            intent.putExtra("author", currentVideo.getCreatedBy());
//            intent.putExtra("videoResource", currentVideo.getVideoUrl());
//            intent.putExtra("views", currentVideo.getViews());
//            intent.putExtra("timeAgo", currentVideo.getDate());
//
//            Gson gson = new Gson();
//            String videoJson = gson.toJson(currentVideo);
//            intent.putExtra("video_data", videoJson);
//            context.startActivity(intent);
//        });
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
        final ImageView image;

        VideoViewHolder(View itemView) {
            super(itemView);
            videoNameTextView = itemView.findViewById(R.id.videoName);
            authorTextView = itemView.findViewById(R.id.author);
            image = itemView.findViewById(R.id.image);
            timeAgoTextView = itemView.findViewById(R.id.timeAgo);
            viewsTextView = itemView.findViewById(R.id.viewsCount);
        }
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Video> filteredList = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(videos);
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
