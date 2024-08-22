package com.example.phototube_android.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.example.phototube_android.R;

import java.util.Objects;

public class FullscreenActivity extends AppCompatActivity {

    private ExoPlayer player;
    private StyledPlayerView playerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

        playerView = findViewById(R.id.player_view);
        ImageButton btnExitFullscreen = findViewById(R.id.btn_exit_fullscreen);
        configureFullScreen();
        setupPlayer();

        btnExitFullscreen.setOnClickListener(v -> {
            if (player != null) {
                player.stop();
            }
            finish();
        });
    }

    private void setupPlayer() {
        player = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(player);

        // Retrieve the video URL from intent using the correct tag "VideoUrl"
        String videoUrl = getIntent().getStringExtra("VideoUrl");
        if (videoUrl != null) {
            Uri videoUri = Uri.parse(videoUrl);
            MediaItem mediaItem = MediaItem.fromUri(videoUri);
            player.setMediaItem(mediaItem);
            player.prepare();
            player.play();
        }
    }

    private void configureFullScreen() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        } else {
            Log.d("ActionBar", "The action bar is not available in this theme.");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.release();  // Release the player when the activity is destroyed
        }
    }
}
