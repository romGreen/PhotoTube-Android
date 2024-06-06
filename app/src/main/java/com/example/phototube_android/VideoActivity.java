package com.example.phototube_android;

import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class VideoActivity extends AppCompatActivity {
    private VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_page);

        String videoName = getIntent().getStringExtra("videoName");
        String author = getIntent().getStringExtra("author");

        // Set these values to your views
        TextView videoNameTextView = findViewById(R.id.videoNameTextView);
        videoNameTextView.setText(videoName);

        TextView authorTextView = findViewById(R.id.authorTextView);
        authorTextView.setText(author);

        // Load and play your video based on the passed information
        videoView = findViewById(R.id.video_view);
        // 'video_file' is the name of your video file in the /res/raw folder without the extension
        int videoResId = getResources().getIdentifier("video_file", "raw", getPackageName());
        String videoPath = "android.resource://" + getPackageName() + "/" + videoResId;
        videoView.setVideoURI(Uri.parse(videoPath));
        videoView.start();

    }
}
