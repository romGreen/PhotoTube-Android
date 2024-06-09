package com.example.phototube_android.adapter;
import static com.example.phototube_android.MainActivity.videoList;

import com.bumptech.glide.Glide;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phototube_android.R;
import com.example.phototube_android.VideoActivity;
import com.example.phototube_android.entities.Video;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {
    private final List<Video> videos;
    private final Context context;
    public List<Video> filteredVideoList;


    public VideoAdapter(Context context, List<Video> videos) {
        this.context = context;
        this.videos = videos;
        this.filteredVideoList = new ArrayList<>(); // Initially, filtered list is the same as original list

    }

    public List<Video> getVideoList() {
        return this.videos;
    }
    public List<Video> getFilteredVideoList() {
        return filteredVideoList;
    }

    //func to add video
    public void addVideoToList(Video video) {
        videoList.add(video);
        this.getFilteredVideoList().add(video);
        this.notifyDataSetChanged(); // Notify the adapter that data has changed
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
        holder.videoNameTextView.setText(currentVideo.getVideoName());
        holder.authorTextView.setText(currentVideo.getAuthor());
        Glide.with(context)
                .load(currentVideo.getImagePath()) // Here currentVideo.getImagePath() should return a valid URL or file path
                .into(holder.image);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, VideoActivity.class);
            intent.putExtra("videoId", currentVideo.getId());
            intent.putExtra("videoName", currentVideo.getVideoName());
            intent.putExtra("author", currentVideo.getAuthor());
            intent.putExtra("videoResource", currentVideo.getVideoPath());
            Gson gson = new Gson();
            String videoJson = gson.toJson(currentVideo);
            intent.putExtra("video_data", videoJson);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return filteredVideoList.size();
    }

    static class VideoViewHolder extends RecyclerView.ViewHolder {
        final TextView videoNameTextView;
        final TextView authorTextView;
        final ImageView image;

        VideoViewHolder(View itemView) {
            super(itemView);
            videoNameTextView = itemView.findViewById(R.id.videoName);
            authorTextView = itemView.findViewById(R.id.author);
            image = itemView.findViewById(R.id.image);
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
                        if (video.getVideoName().toLowerCase().contains(filterPattern)) {
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

