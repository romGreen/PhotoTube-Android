package com.example.phototube_android.ui.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phototube_android.R;
import com.example.phototube_android.model.User;
import com.example.phototube_android.repository.UserRepository;
import com.example.phototube_android.repository.VideoRepository;
import com.example.phototube_android.ui.adapters.VideoAdapter;
import com.example.phototube_android.ui.viewmodels.UserViewModel;
import com.example.phototube_android.ui.viewmodels.UserViewModelFactory;
import com.example.phototube_android.ui.viewmodels.VideoViewModel;
import com.example.phototube_android.ui.viewmodels.VideoViewModelFactory;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private RecyclerView recyclerView;
    private VideoAdapter videoAdapter;
    private UserViewModel userViewModel;
    private VideoViewModel videoViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UserRepository userRepository = new UserRepository(getApplication());
        VideoRepository videoRepository = new VideoRepository(getApplication());

        UserViewModelFactory userViewModelFactory = new UserViewModelFactory(userRepository);
        VideoViewModelFactory videoViewModelFactory = new VideoViewModelFactory(videoRepository);

        userViewModel = new ViewModelProvider(this, userViewModelFactory).get(UserViewModel.class);
        videoViewModel = new ViewModelProvider(this, videoViewModelFactory).get(VideoViewModel.class);

        initialize();
        observeUser();
        if (userViewModel.isLoggedIn()) {
        Log.e("MainActivity", "log in!!!!");
}
        observeVideos();
    }

    private void initialize() {
        drawerLayout = findViewById(R.id.drawer_layout);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        videoAdapter = new VideoAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(videoAdapter);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                videoAdapter.getFilter().filter(newText);
                return true;
            }
        });
    }

    private void observeUser() {
        userViewModel.getCurrentUser().observe(this, user -> {
            boolean isLoggedIn = user != null;
            updateMenuItems(userViewModel.isLoggedIn());
            if (isLoggedIn) {
                Log.e("MainActivity", "log in!!!!");
                updateUserInfo(user);
            } else {
                clearUserInfo();
            }
        });
    }

    private void observeVideos() {
        videoViewModel.getVideos().observe(this, videos -> {
            videoAdapter.setVideos(videos);
        });
    }

    private void updateUserInfo(User user) {
        ImageView userImage = findViewById(R.id.user_image_view);
        TextView userName = findViewById(R.id.user_name_view);

        userName.setText(user.getDisplayname());

        // Check if the profile image string is not null and not empty
        if (user.getProfileImg() != null && !user.getProfileImg().isEmpty()) {
            // Decode the base64 String to a Bitmap
            Bitmap bitmap = decodeBase64ToBitmap(user.getProfileImg());
            userImage.setImageBitmap(bitmap);
            userImage.setVisibility(View.VISIBLE);
        } else {
            userImage.setVisibility(View.GONE); // Optionally hide the ImageView if no image is present
        }
    }

    private Bitmap decodeBase64ToBitmap(String encodedImage) {
        // Remove the base64 image prefix if present before decoding
        String cleanImage = encodedImage.replace("data:image/png;base64,", "").replace("data:image/jpeg;base64,","");
        byte[] decodedString = Base64.decode(cleanImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    private void clearUserInfo() {
        ImageView userImage = findViewById(R.id.user_image_view);
        TextView userName = findViewById(R.id.user_name_view);
        userImage.setVisibility(View.GONE);
        userName.setText("");
    }

    private void updateMenuItems(boolean isLoggedIn) {
        NavigationView navigationView = findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.nav_login).setVisible(!isLoggedIn);
        menu.findItem(R.id.nav_register).setVisible(!isLoggedIn);
        menu.findItem(R.id.nav_add_video).setVisible(isLoggedIn);
        menu.findItem(R.id.nav_logout).setVisible(isLoggedIn);
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
            userViewModel.logout();
            clearUserInfo();
            updateMenuItems(false);
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
        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        recreate();
    }

    private Class<?> mapActivity(int id) {
        if (id == R.id.nav_login) return LoginActivity.class;
        if (id == R.id.nav_register) return RegisterActivity.class;
        if (id == R.id.nav_add_video) return AddVideoActivity.class;
        return MainActivity.class;
    }
}
