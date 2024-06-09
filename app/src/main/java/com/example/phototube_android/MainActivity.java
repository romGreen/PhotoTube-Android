package com.example.phototube_android;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.phototube_android.adapter.VideoAdapter;
import com.example.phototube_android.entities.User;
import com.example.phototube_android.entities.UserManager;
import com.example.phototube_android.entities.Video;
import com.example.phototube_android.entities.VideoListManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private RecyclerView recyclerView;
    @SuppressLint("StaticFieldLeak")
    public static VideoAdapter videoAdapter;
    public static List<Video> videoList;
    private BottomNavigationView bottomNavigationView;
    private LinearLayout loginSection,addVideoSection,registerSection;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();

        // Update user information if logged in
        if (UserManager.getInstance().isLoggedIn()) {
            updateUserInfo();
        }

    }

    private void initialize(){
        drawerLayout = findViewById(R.id.drawer_layout);
        addVideoSection = findViewById(R.id.add_video_section);
        registerSection = findViewById(R.id.register_section);
        loginSection = findViewById(R.id.login_section);

        // Left menu
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Video list - setup recycler view
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Video> videoListInit = new ArrayList<>();
        videoListInit.add(new Video(1, "Play 1", "Narcos", resourceToString(R.drawable.pic1), resourceToString(R.raw.play1)));
        videoListInit.add(new Video(2, "Play 2", "Narcos", resourceToString(R.drawable.photo2), resourceToString(R.raw.play2)));
        videoListInit.add(new Video(3, "Play 3", "Narcos", resourceToString(R.drawable.photo3), resourceToString(R.raw.play3)));
        videoListInit.add(new Video(4, "Play 4", "Narcos", resourceToString(R.drawable.photo4), resourceToString(R.raw.play4)));
        videoListInit.add(new Video(5, "Play 5", "Narcos", resourceToString(R.drawable.photo5), resourceToString(R.raw.play5)));
        VideoListManager.getInstance().init(videoListInit);
        videoAdapter = new VideoAdapter(this,    VideoListManager.getInstance().getVideoList());
        videoAdapter.getFilteredVideoList().addAll( VideoListManager.getInstance().getVideoList());
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
        //bottom navigate
        setClickListeners();
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
    protected void onResume() {
        super.onResume();
    }


    // Helper method to convert a resource ID to its URI string
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

    private void logoutClear() {
        UserManager.getInstance().setUser(null);
        // Update UI components to reflect the logout state
        updateUIForLogout();

        // Optionally, you can restart the activity to refresh the UI
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);
    }
    private void updateUIForLogout() {


        // Update the bottom navigation or any other UI elements
        LinearLayout addVideoSection = findViewById(R.id.add_video_section);
        LinearLayout registerSection = findViewById(R.id.register_section);

        if (addVideoSection != null && registerSection != null) {
            addVideoSection.setVisibility(View.GONE);
            registerSection.setVisibility(View.VISIBLE);
        }

        // Optionally clear any user data displayed in the UI
        TextView usernameTextView = findViewById(R.id.usernameTextView);
        if (usernameTextView != null) {
            usernameTextView.setText("");
        }
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
