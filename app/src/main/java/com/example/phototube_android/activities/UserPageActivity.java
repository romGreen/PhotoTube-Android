package com.example.phototube_android.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.phototube_android.R;
import com.example.phototube_android.classes.User;
import com.example.phototube_android.entities.UserManager;
import com.example.phototube_android.ui.adapters.VideoAdapter;
import com.example.phototube_android.viewmodels.UserLogViewModel;
import com.example.phototube_android.classes.Video;
import com.example.phototube_android.viewmodels.VideoInViewModel;
import com.example.phototube_android.viewmodels.VideoOffViewModel;
import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class UserPageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private TextView displayNameTextView;
    private RecyclerView videosRecyclerView;
    @SuppressLint("StaticFieldLeak")
    private VideoAdapter videoAdapter;
    private UserLogViewModel userLogViewModel;

    // Views from video_item.xml for the most viewed video
    private TextView videoTitleTextView,titleTextView, authorTextView, timeAgoTextView, viewsCountTextView;
    private ImageView videoThumbnailImageView, creatorImgView;
    private VideoOffViewModel videoOffViewModel;
    private VideoInViewModel videoInViewModel;
    private String creatorId;
    private String createdBy;
    private Video mostViewedVideo;
    private LinearLayout loginSection, addVideoSection, registerSection;
    private DrawerLayout drawerLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);
        initialize();
        videoOffViewModel.getUserVideos(creatorId);
        observeUserVideos();
        getVideos();

    }



    private void initialize()
    {
        Intent intent = getIntent();
        // Retrieve the extras
        creatorId = intent.getStringExtra("creatorId");
        createdBy = intent.getStringExtra("createdBy");
        videoOffViewModel = new ViewModelProvider(this).get(VideoOffViewModel.class);

        displayNameTextView = findViewById(R.id.displayNameTextView);
        videosRecyclerView = findViewById(R.id.videosRecyclerView);
        videosRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        addVideoSection = findViewById(R.id.add_video_section);
        registerSection = findViewById(R.id.register_section);
        loginSection = findViewById(R.id.login_section);
        drawerLayout = findViewById(R.id.drawer_layout);


        // Initialize the views for the most viewed video
        videoTitleTextView = findViewById(R.id.mostViewedVideoTitle);
        titleTextView = findViewById(R.id.videoName);
        authorTextView = findViewById(R.id.author);
        timeAgoTextView = findViewById(R.id.timeAgo);
        viewsCountTextView = findViewById(R.id.viewsCount);
        videoThumbnailImageView = findViewById(R.id.thumbnail);
        creatorImgView = findViewById(R.id.creatorImage);

        setClickListeners();
        setupNavigationView();

    }

    // Observe user data
    private void observeUserVideos() {
        videoOffViewModel.getUserVideosData().observe(this, apiResponse -> {
            if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                List<Video> videos = new ArrayList<>(apiResponse.getData());
                Log.d("UserPageActivity", "Number of videos loaded: " + videos.size());
                if (!videos.isEmpty()) {
                    mostViewedVideo = findAndRemoveMostViewedVideo(videos);
                    displayMostViewedVideo(mostViewedVideo);
                }
                displayNameTextView.setText(createdBy); // Set user's display name
            } else {
                Toast.makeText(this, "Failed to load videos: " + apiResponse.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getVideos() {
        videoOffViewModel = new ViewModelProvider(this).get(VideoOffViewModel.class);
        videoOffViewModel.getUserVideos(creatorId);
        videoOffViewModel.getUserVideosData().observe(this, videoList -> {
            if (videoList.isSuccess()) {
                videoList.getData().remove(mostViewedVideo);
                videoAdapter = new VideoAdapter(this, videoList.getData());
                videosRecyclerView.setAdapter(videoAdapter);
            }
        });
    }

    private Video findAndRemoveMostViewedVideo(List<Video> videos) {
        if (videos == null || videos.isEmpty()) return null;

        Video mostViewedVideo = Collections.max(videos, Comparator.comparingInt(Video::getViews));
        videos.remove(mostViewedVideo); // Remove the most viewed video from the list
        return mostViewedVideo;
    }

    private void displayMostViewedVideo(Video video) {
        if (video == null) {
            Log.d("UserPageActivity", "No most viewed video to display.");
            return;
        }

        titleTextView.setText(video.getTitle());
        authorTextView.setText(video.getCreatedBy());
        String views = String.valueOf(video.getViews())+ " views";
        viewsCountTextView.setText(views);
        // Format date
        String formattedDate = formatDate(video.getDate());
        timeAgoTextView.setText(formattedDate);


        // Load a frame from the video using Glide
        String videoUrl = video.getVideoUrl();
        Glide.with(videoThumbnailImageView.getContext())
                .asBitmap()
                .load("http://10.0.2.2:" +videoUrl)
                .frame(1000000) // Load frame at 1 second (1000000 microseconds)
                .into(videoThumbnailImageView);

        // Load creator image
        String creatorImageUrl = video.getCreatorImg();
        Glide.with(creatorImgView.getContext())
                .load("http://10.0.2.2:" + creatorImageUrl)
                .into(creatorImgView);

    }

    private void setupNavigationView() {
        NavigationView navigationView = findViewById(R.id.left_view);
        navigationView.setNavigationItemSelectedListener(this);
        updateMenuItems(UserManager.getInstance().isLoggedIn());
    }

    private void updateMenuItems(boolean isLoggedIn) {
        NavigationView navigationView = findViewById(R.id.left_view);
        Menu menu = navigationView.getMenu();

        addVideoSection.setVisibility(isLoggedIn ? View.VISIBLE : View.GONE);
        registerSection.setVisibility(isLoggedIn ? View.GONE : View.VISIBLE);

        menu.findItem(R.id.nav_login).setVisible(!isLoggedIn);
        menu.findItem(R.id.nav_register).setVisible(!isLoggedIn);
        menu.findItem(R.id.nav_add_video).setVisible(isLoggedIn);
        menu.findItem(R.id.nav_logout).setVisible(isLoggedIn);

        if (isLoggedIn) {
            updateUserInfo();
        }
    }

    private void setClickListeners() {
        registerSection.setOnClickListener(v -> startActivity(new Intent(UserPageActivity.this, RegisterActivity.class)));
        loginSection.setOnClickListener(v -> {
            Class<?> targetActivity = UserManager.getInstance().isLoggedIn() ? UserInfoActivity.class : LoginActivity.class;
            startActivity(new Intent(UserPageActivity.this, targetActivity));
        });
        addVideoSection.setOnClickListener(v -> startActivity(new Intent(UserPageActivity.this, AddVideoActivity.class)));
    }

    private void updateUserInfo() {
        User user = UserManager.getInstance().getUser();
        if (user != null) {
            ImageView userImage = findViewById(R.id.user_image_view);
            TextView userName = findViewById(R.id.user_name_view);
            userImage.setVisibility(View.VISIBLE);
            userName.setText(user.getDisplayname());
            Glide.with(this).load("http://10.0.2.2:" + user.getProfileImg()).into(userImage);
        }
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle Home click
        } else if (id == R.id.nav_login) {
            startActivity(new Intent(this, LoginActivity.class));
        } else if (id == R.id.nav_register) {
            startActivity(new Intent(this, RegisterActivity.class));
        } else if (id == R.id.nav_logout) {
            logoutClear();
        } else if (id == R.id.nav_add_video) {
            startActivity(new Intent(this, AddVideoActivity.class));
        } else if (id == R.id.nav_dark_mode) {
            toggleNightMode();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    private void logoutClear() {
        UserManager.getInstance().logout();
        clearUserInfo();
        updateMenuItems(false);
        updateUIForLogout();
        Toast.makeText(UserPageActivity.this, "User Logout", Toast.LENGTH_LONG).show();
        restartActivity();
    }

    private void toggleNightMode() {
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        AppCompatDelegate.setDefaultNightMode(currentNightMode == Configuration.UI_MODE_NIGHT_YES ?
                AppCompatDelegate.MODE_NIGHT_NO : AppCompatDelegate.MODE_NIGHT_YES);
        recreate();
    }

    private void clearUserInfo() {
        ImageView userImage = findViewById(R.id.user_image_view);
        TextView userName = findViewById(R.id.user_name_view);
        userImage.setVisibility(View.GONE);
        userName.setText("");
    }

    private void updateUIForLogout() {
        addVideoSection.setVisibility(View.GONE);
        registerSection.setVisibility(View.VISIBLE);
        findViewById(R.id.user_image_view).setVisibility(View.GONE);
        TextView usernameTextView = findViewById(R.id.usernameTextView);
        if (usernameTextView != null) {
            usernameTextView.setText("");
        }
    }

    private void restartActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
