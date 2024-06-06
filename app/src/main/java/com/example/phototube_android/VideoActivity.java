package com.example.phototube_android;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

        videoView = findViewById(R.id.video_view);
        videoNameTextView = findViewById(R.id.videoNameTextView);
        authorTextView = findViewById(R.id.authorTextView);

        // Retrieve the video details from intent
        Intent intent = getIntent();
        String videoName = intent.getStringExtra("videoName");
        String author = intent.getStringExtra("author");
        int videoResource = intent.getIntExtra("videoResource", 0);

        videoNameTextView.setText(videoName);
        authorTextView.setText(author);

        // Prepare and play the video
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + videoResource);
        videoView.setVideoURI(videoUri);
        videoView.start();

        // Optional: Add listeners for completion and errors
        videoView.setOnCompletionListener(mp -> {
            // Handle completion
        });

        videoView.setOnErrorListener((mp, what, extra) -> {
            // Handle errors
            return true; // True indicates we handled the error
        });
    }
}