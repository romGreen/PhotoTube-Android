package com.example.phototube_android.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.phototube_android.R;
import com.example.phototube_android.classes.User;
import com.example.phototube_android.classes.Video;
import com.example.phototube_android.entities.UserManager;
import com.example.phototube_android.ui.adapters.VideoAdapter;
import com.example.phototube_android.viewmodels.UserLogViewModel;
import com.example.phototube_android.viewmodels.VideoOffViewModel;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private RecyclerView recyclerView;
    private VideoOffViewModel videoOffViewModel;
    private LinearLayout loginSection, addVideoSection, registerSection;

    @SuppressLint("StaticFieldLeak")
    public static VideoAdapter videoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
    }

    private void initialize() {
        drawerLayout = findViewById(R.id.drawer_layout);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        addVideoSection = findViewById(R.id.add_video_section);
        registerSection = findViewById(R.id.register_section);
        loginSection = findViewById(R.id.login_section);
        getVideos();
        setClickListeners();
        setupSearchView();
        setupNavigationView();

        if (UserManager.getInstance().isLoggedIn()) {
            updateUserInfo();
        }
    }

    private void setupSearchView() {
        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText == null || newText.isEmpty()) {
                    videoAdapter.restoreOriginalList(); // Restore original list when search is cleared
                }
                else if (videoAdapter != null) {
                    videoAdapter.getFilter().filter(newText);
                }
                return true;
            }
        });
    }

    private void setupNavigationView() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        updateMenuItems(UserManager.getInstance().isLoggedIn());
    }

    private void setClickListeners() {
        registerSection.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, RegisterActivity.class)));
        loginSection.setOnClickListener(v -> {
            Class<?> targetActivity = UserManager.getInstance().isLoggedIn() ? UserInfoActivity.class : LoginActivity.class;
            startActivity(new Intent(MainActivity.this, targetActivity));
        });
        addVideoSection.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, AddVideoActivity.class)));
    }

    private void getVideos() {
        videoOffViewModel = new ViewModelProvider(this).get(VideoOffViewModel.class);
        videoOffViewModel.getVideos();
        videoOffViewModel.getVideoData().observe(this, videoList -> {
            if (videoList.isSuccess()) {
                videoAdapter = new VideoAdapter(this, videoList.getData());
                videoAdapter.restoreOriginalList(); // Initially restore the list to ensure proper initialization
                recyclerView.setAdapter(videoAdapter);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateMenuItems(UserManager.getInstance().isLoggedIn());
    }

    private void updateMenuItems(boolean isLoggedIn) {
        NavigationView navigationView = findViewById(R.id.nav_view);
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

    private void logoutClear() {
        UserManager.getInstance().logout();
        clearUserInfo();
        updateMenuItems(false);
        updateUIForLogout();
        Toast.makeText(MainActivity.this, "User Logout", Toast.LENGTH_LONG).show();
        restartActivity();
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


    private void toggleNightMode() {
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        AppCompatDelegate.setDefaultNightMode(currentNightMode == Configuration.UI_MODE_NIGHT_YES ?
                AppCompatDelegate.MODE_NIGHT_NO : AppCompatDelegate.MODE_NIGHT_YES);
        recreate();
    }
}
