package com.example.phototube_android;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phototube_android.adapter.VideoAdapter;
import com.example.phototube_android.entities.User;
import com.example.phototube_android.entities.UserManager;
import com.example.phototube_android.entities.Video;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuItem;
import java.util.ArrayList;
import java.util.List;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.content.res.Resources;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private RecyclerView recyclerView;
    private static VideoAdapter videoAdapter;
    private static List<Video> videoList;
    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //left menu
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //bottom menu
        this.bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set the listener for bottom navigation view
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                Toast.makeText(MainActivity.this, "Home", Toast.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.nav_shorts) {
                Toast.makeText(MainActivity.this, "Dashboard", Toast.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.nav_notifications) {
                Toast.makeText(MainActivity.this, "Notifications", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });

        //video list - setup recycler view
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        videoList = new ArrayList<>();
        // Example of adding videos with local drawable resources
        videoList.add(new Video("Play 1", "Narcos", resourceToString(R.drawable.pic1), resourceToString(R.raw.play1)));
        videoList.add(new Video("Play 2", "Narcos", resourceToString(R.drawable.photo2), resourceToString(R.raw.play2)));
        videoList.add(new Video("Play 3", "Narcos", resourceToString(R.drawable.photo3), resourceToString(R.raw.play3)));
        videoList.add(new Video("Play 4", "Narcos", resourceToString(R.drawable.photo4), resourceToString(R.raw.play4)));
        videoList.add(new Video("Play 5", "Narcos", resourceToString(R.drawable.photo5), resourceToString(R.raw.play5)));

        // setup adapter
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
                return true;
            }
        });
    }



    //func to add video
    public static void addVideoToList(Video video) {
        videoList.add(video);
        videoAdapter.notifyDataSetChanged(); // Notify the adapter that data has changed
    }

    // Helper method to convert a resource ID to its URI string
    private String resourceToString(int resourceId) {
        return "android.resource://" + getPackageName() + "/" + resourceId;
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateBottomNavigationUser();
    }

    private void updateBottomNavigationUser() {
        boolean isLoggedIn = UserManager.getInstance().isLoggedIn();
        Menu menu = bottomNavigationView.getMenu();
        MenuItem userItem = menu.findItem(R.id.navigation_user);

        if (isLoggedIn && userItem != null) {
            User user = UserManager.getInstance().getUser();
            if (user != null) {
                userItem.setVisible(true);
                userItem.setTitle(user.getFirstName());

                // Load the image as a Bitmap from the local file path
                Bitmap bitmap = BitmapFactory.decodeFile(user.getImageUri());
                if (bitmap != null) {
                    // Convert the bitmap to a drawable
                    Resources res = getResources();
                    BitmapDrawable iconDrawable = new BitmapDrawable(res, bitmap);
                    userItem.setIcon(iconDrawable);
                }
            }
        } else if (userItem != null) {
            userItem.setVisible(false);
        }
    }



    @Override
    protected void onStart() {
        super.onStart();
        updateMenuItems();
    }

    // update the left menu buttons based on the login or not
    private void updateMenuItems() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        Menu leftMenu = navigationView.getMenu();

        // Check if user is logged in
        boolean isLoggedIn = UserManager.getInstance().isLoggedIn();

        // Set visibility of button in left menu based on login status
        leftMenu.findItem(R.id.nav_login).setVisible(!isLoggedIn);
        leftMenu.findItem(R.id.nav_register).setVisible(!isLoggedIn);
        leftMenu.findItem(R.id.nav_add_video).setVisible(isLoggedIn);
        leftMenu.findItem(R.id.nav_logout).setVisible(isLoggedIn);
    }

    private void logoutClear() {
        // Clear the user data in UserManager
        UserManager.getInstance().setUser(null);

        // Navigate back to LoginActivity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);

        // Finish MainActivity so that the user can't go back to it
        finish();
    }


    // function to navigate in the left menu
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Get the ID of the clicked menu item
        int id = item.getItemId();

        // Handle navigation view item clicks here.
        if (id == R.id.nav_home) {
            // Handle Home click
        } else if (id == R.id.nav_profile) {
            // Handle Profile click
        } else if (id == R.id.nav_login) {
            // Handle Login click
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_register) {
            // Handle Login click
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            // Perform logout
            UserManager.getInstance().logout();
            logoutClear();
            updateMenuItems(); // Refresh menu items
        } else if (id == R.id.nav_add_video) {
            // Handle add video click
            Intent intent = new Intent(MainActivity.this, AddVideoActivity.class);
            startActivity(intent);
        }
        // Add more else-if statements as necessary


        // Close the navigation drawer
       // drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


}
