package com.example.phototube_android.activities;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.phototube_android.R;
import com.example.phototube_android.classes.Video;
import com.google.gson.Gson;


public class FullscreenActivity extends AppCompatActivity {

    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

        VideoView videoView = findViewById(R.id.video_view);
        ImageButton btnExitFullscreen = findViewById(R.id.btn_exit_fullscreen);
        if (videoView == null) {
            throw new RuntimeException("VideoView is not found");
        }

        // Retrieve video path
        String videoPath = getIntent().getStringExtra("videoPath");
        if (videoPath != null) {
            videoView.setVideoPath(videoPath);
            videoView.start();
        } else {
            // Handle null case
            Log.e("FullscreenActivity", "Video path is null");
            finish(); // Exit activity if no video path provided
        }

        // Example of retrieving video data
        String videoJson = getIntent().getStringExtra("video_data");
        if (videoJson != null) {
            Gson gson = new Gson();
            Video video = gson.fromJson(videoJson, Video.class);
        }

        btnExitFullscreen.setOnClickListener(v -> {
            int currentPosition = videoView.getCurrentPosition();
            Intent returnIntent = new Intent();
            returnIntent.putExtra("currentPosition", currentPosition);
            setResult(RESULT_OK, returnIntent);
            finish();
        });

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (videoView != null) {
            int currentPosition = videoView.getCurrentPosition();
            Intent returnIntent = new Intent();
            returnIntent.putExtra("currentPosition", currentPosition);
            setResult(RESULT_OK, returnIntent);
        } else {
            Log.e("FullscreenActivity", "VideoView is null on onPause");
        }
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            finish();  // Close fullscreen when user returns to portrait mode
        }
    }
}