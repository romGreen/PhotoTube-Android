package com.example.phototube_android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.phototube_android.adapter.VideoAdapter;
import com.example.phototube_android.entities.User;
import com.example.phototube_android.entities.UserManager;
import com.example.phototube_android.entities.Video;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private RecyclerView recyclerView;
    private static VideoAdapter videoAdapter;
    private static List<Video> videoList;
    private BottomNavigationView bottomNavigationView;
    private ImageView userImageView;
    private TextView userNameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);

        // Left menu
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);





        // Update user information if logged in
        if (UserManager.getInstance().isLoggedIn()) {
            updateUserInfo();
        }

        // Video list - setup recycler view
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        videoList = new ArrayList<>();
        videoList.add(new Video(1, "Play 1", "Narcos", resourceToString(R.drawable.pic1), resourceToString(R.raw.play1)));
        videoList.add(new Video(2, "Play 2", "Narcos", resourceToString(R.drawable.photo2), resourceToString(R.raw.play2)));
        videoList.add(new Video(3, "Play 3", "Narcos", resourceToString(R.drawable.photo3), resourceToString(R.raw.play3)));
        videoList.add(new Video(4, "Play 4", "Narcos", resourceToString(R.drawable.photo4), resourceToString(R.raw.play4)));
        videoList.add(new Video(5, "Play 5", "Narcos", resourceToString(R.drawable.photo5), resourceToString(R.raw.play5)));

        // Setup adapter
        videoAdapter = new VideoAdapter(this, videoList);
        recyclerView.setAdapter(videoAdapter);

        // Setup SearchView
        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                videoAdapter.getFilter().filter(newText);
                videoAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    private void resetComments() {
        SharedPreferences prefs = getSharedPreferences("VideoComments", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
    }

    public static void addVideoToList(Video video) {
        videoList.add(video);
        videoAdapter.getFilteredVideoList().add(video);
        videoAdapter.notifyDataSetChanged();
    }

    private String resourceToString(int resourceId) {
        return "android.resource://" + getPackageName() + "/" + resourceId;
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateMenuItems();
    }

    private void updateMenuItems() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        Menu leftMenu = navigationView.getMenu();
        boolean isLoggedIn = UserManager.getInstance().isLoggedIn();

        leftMenu.findItem(R.id.nav_login).setVisible(!isLoggedIn);
        leftMenu.findItem(R.id.nav_register).setVisible(!isLoggedIn);
        leftMenu.findItem(R.id.nav_add_video).setVisible(isLoggedIn);
        leftMenu.findItem(R.id.nav_logout).setVisible(isLoggedIn);

        if (isLoggedIn) {
            updateUserInfo();
        }
    }

    private void logoutClear() {
        UserManager.getInstance().setUser(null);
        finish();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle Home click
        } else if (id == R.id.nav_profile) {
            // Handle Profile click
        } else if (id == R.id.nav_login) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_register) {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            UserManager.getInstance().logout();
            logoutClear();
            updateMenuItems();
        } else if (id == R.id.nav_add_video) {
            Intent intent = new Intent(MainActivity.this, AddVideoActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_dark_mode) {
            int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
            if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
            recreate();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void updateUserInfo() {
        if (UserManager.getInstance().isLoggedIn()) {
            User user = UserManager.getInstance().getUser();
            if (user != null) {


                ImageView userImage = findViewById(R.id.user_image_view);
                TextView userName = findViewById(R.id.user_name_view);

                userImage.setVisibility(View.VISIBLE);
                userName.setText(user.getFirstName());

                userImage.setImageURI(user.getImageUri());
            }
        }
    }
    // Launch ImageTestActivity when the button is clicked

}
