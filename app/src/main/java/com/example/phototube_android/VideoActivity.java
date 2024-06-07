package com.example.phototube_android;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class VideoActivity extends AppCompatActivity {
    private VideoView videoView;
    private TextView videoNameTextView, authorTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        // Initialize views
        videoView = findViewById(R.id.video_view);
        videoNameTextView = findViewById(R.id.videoNameTextView);
        authorTextView = findViewById(R.id.authorTextView);

        // Get data from intent
        String videoPath = getIntent().getStringExtra("videoResource");
        String videoName = getIntent().getStringExtra("videoName");
        String author = getIntent().getStringExtra("author");

        // Set video information
        videoNameTextView.setText(videoName);
        authorTextView.setText(author);

        // Set up the VideoView to play the video
        Uri videoUri = Uri.parse(videoPath);
        videoView.setVideoURI(videoUri);
        videoView.start(); // Start playing automatically

        // Media controller
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
    }
}
