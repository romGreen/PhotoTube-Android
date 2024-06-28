package com.example.phototube_android.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.phototube_android.R;
import com.example.phototube_android.classes.User;
import com.example.phototube_android.ui.adapters.VideoAdapter;
import com.example.phototube_android.viewmodels.UserLogViewModel;
import com.example.phototube_android.classes.Video;

import java.util.ArrayList;
import java.util.List;

public class UserPageActivity extends AppCompatActivity {

    private TextView displayNameTextView;
    private RecyclerView videosRecyclerView;
    private VideoAdapter videoAdapter;
    private UserLogViewModel userLogViewModel;

    // Views from video_item.xml for the most viewed video
    private TextView videoTitleTextView;
    private ImageView videoThumbnailImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);

        displayNameTextView = findViewById(R.id.displayNameTextView);
        videosRecyclerView = findViewById(R.id.videosRecyclerView);
        videosRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        videoAdapter = new VideoAdapter(this, new ArrayList<>());
        videosRecyclerView.setAdapter(videoAdapter);

        // Initialize the views for the most viewed video
        videoTitleTextView = findViewById(R.id.videoName); // ID should be from video_item.xml
        videoThumbnailImageView = findViewById(R.id.image); // ID should be from video_item.xml

        // Initialize the UserLogViewModel
        userLogViewModel = new ViewModelProvider(this).get(UserLogViewModel.class);

        // Fetch user data
        userLogViewModel.getUser();

        // Observe user data
        userLogViewModel.getUserData().observe(this, userResponse -> {
            if (userResponse.isSuccess() && userResponse.getData() != null) {
                updateUI(userResponse.getData());
            } else {
                Toast.makeText(this, "Failed to load user data: " + userResponse.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateUI(User user) {
        displayNameTextView.setText(user.getDisplayname());
        List<Video> videos = user.getVideoList();
        Video mostViewedVideo = getMostViewedVideo(videos);

        if (mostViewedVideo != null) {
            updateMostViewedVideo(mostViewedVideo);
            videos.remove(mostViewedVideo); // Ensure the most viewed video is not duplicated in the list
        }

        videoAdapter.setVideos(videos); // Set remaining videos to the adapter
    }

    private void updateMostViewedVideo(Video video) {
        videoTitleTextView.setText(video.getTitle());
       // Glide.with(this).load(video.getImageUrl).into(videoThumbnailImageView);
    }

    private Video getMostViewedVideo(List<Video> videos) {
        if (videos == null || videos.isEmpty()) return null;

        Video mostViewed = videos.get(0); // Assume the first video is the most viewed for initialization

        for (Video video : videos) {
            if (video.getViews() > mostViewed.getViews()) {
                mostViewed = video; // Found a new most viewed video
            }
        }

        return mostViewed;
    }
}
