package com.example.phototube_android.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
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

import com.example.phototube_android.classes.Video;
import com.example.phototube_android.ui.adapters.VideoAdapter;
import com.example.phototube_android.R;
import com.example.phototube_android.classes.User;
import com.example.phototube_android.entities.UserManager;

import com.example.phototube_android.viewmodels.UserLogViewModel;
import com.example.phototube_android.viewmodels.VideoOffViewModel;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private RecyclerView recyclerView;
    private VideoOffViewModel videoOffViewModel;

    @SuppressLint("StaticFieldLeak")
    public static VideoAdapter videoAdapter;
    private LinearLayout loginSection,addVideoSection,registerSection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();

        if (UserManager.getInstance().isLoggedIn()) {
            updateUserInfo();
        }
    }

    private void initialize() {
        drawerLayout = findViewById(R.id.drawer_layout);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        getVideos();

        recyclerView.setAdapter(videoAdapter);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //bottom navigate
        addVideoSection = findViewById(R.id.add_video_section);
        registerSection = findViewById(R.id.register_section);
        loginSection = findViewById(R.id.login_section);
        setClickListeners();


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

    private void getVideos(){

        videoOffViewModel = new ViewModelProvider(this).get(VideoOffViewModel.class);
        videoOffViewModel.getVideos();

        videoOffViewModel.getVideoData().observe(this,videoList -> {
            if(!videoList.isSuccess()){
                Toast toast = Toast.makeText(MainActivity.this,
                        videoList.getMessage(), Toast.LENGTH_LONG);
                toast.show();
                return;
            }

            Toast toast = Toast.makeText(MainActivity.this,
                    videoList.getMessage(), Toast.LENGTH_LONG);

            toast.show();


            videoAdapter = new VideoAdapter(this,  videoList.getData());
            videoAdapter.getFilteredVideoList().addAll(videoList.getData());
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
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

        addVideoSection.setVisibility(isLoggedIn ? View.VISIBLE : View.GONE);
        registerSection.setVisibility(isLoggedIn ? View.GONE : View.VISIBLE);
        leftMenu.findItem(R.id.nav_login).setVisible(!isLoggedIn);
        leftMenu.findItem(R.id.nav_register).setVisible(!isLoggedIn);
        leftMenu.findItem(R.id.nav_add_video).setVisible(isLoggedIn);
        leftMenu.findItem(R.id.nav_logout).setVisible(isLoggedIn);

        if (isLoggedIn) {
            updateUserInfo();
        }
    }

    private void updateUserInfo() {
        if (UserManager.getInstance().isLoggedIn()) {
            User user = UserManager.getInstance().getUser();
            if (user != null) {
                ImageView userImage = findViewById(R.id.user_image_view);
                TextView userName = findViewById(R.id.user_name_view);
                userImage.setVisibility(View.VISIBLE);
                userName.setText(user.getDisplayname());
                userImage.setImageURI(Uri.parse(user.getProfileImg()));
            }
        }
    }

    private void logoutClear() {
        UserManager.getInstance().setUser(null);
        // Update UI components to reflect the logout state
        updateUIForLogout();

        // Optionally, you can restart the activity to refresh the UI
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);
    }

    private void clearUserInfo() {
        ImageView userImage = findViewById(R.id.user_image_view);
        TextView userName = findViewById(R.id.user_name_view);
        userImage.setVisibility(View.GONE);
        userName.setText("");
    }

    private void updateUIForLogout() {
        if (addVideoSection != null && registerSection != null) {
            addVideoSection.setVisibility(View.GONE);
            registerSection.setVisibility(View.VISIBLE);
        }
        ImageView userImage = findViewById(R.id.user_image_view);
        userImage.setVisibility((View.GONE));
        // Optionally clear any user data displayed in the UI
        TextView usernameTextView = findViewById(R.id.usernameTextView);
        if (usernameTextView != null) {
            usernameTextView.setText("");
        }
    }




    private Bitmap decodeBase64ToBitmap(String encodedImage) {
        // Remove the base64 image prefix if present before decoding
        String cleanImage = encodedImage.replace("data:image/png;base64,", "").replace("data:image/jpeg;base64,","");
        byte[] decodedString = Base64.decode(cleanImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }


    private void updateMenuItems(boolean isLoggedIn) {
        NavigationView navigationView = findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.nav_login).setVisible(!isLoggedIn);
        menu.findItem(R.id.nav_register).setVisible(!isLoggedIn);
        menu.findItem(R.id.nav_add_video).setVisible(isLoggedIn);
        menu.findItem(R.id.nav_logout).setVisible(isLoggedIn);
    }

    private void setClickListeners() {
        registerSection.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
        if( !UserManager.getInstance().isLoggedIn())
        {
            loginSection.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            });
        }

        addVideoSection.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddVideoActivity.class);
            startActivity(intent);
        });
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
            UserManager.getInstance().logout();
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
