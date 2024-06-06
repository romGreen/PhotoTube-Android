package com.example.phototube_android.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phototube_android.R;
import com.example.phototube_android.VideoActivity;
import com.example.phototube_android.entities.Video;

import java.util.List;


public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {
    private final List<Video> videos;
    private final Context context;

    public VideoAdapter(Context context, List<Video> videos) {
        this.context = context;
        this.videos = videos;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.video_item, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        Video currentVideo = videos.get(position);
        holder.videoNameTextView.setText(currentVideo.getVideoName());
        holder.authorTextView.setText(currentVideo.getAuthor());
        holder.image.setImageResource(currentVideo.getImageResourceId());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, VideoActivity.class);
            intent.putExtra("videoName", currentVideo.getVideoName());
            intent.putExtra("author", currentVideo.getAuthor());
            intent.putExtra("videoResource", currentVideo.getVideoResourceId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return videos.size();
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
}
