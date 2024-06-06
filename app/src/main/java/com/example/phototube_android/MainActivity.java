package com.example.phototube_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phototube_android.adapter.VideoAdapter;
import com.example.phototube_android.entities.Video;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private RecyclerView recyclerView;
    private VideoAdapter videoAdapter;
    private List<Video> videoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //left menu
        //drawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //video list
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        videoList = new ArrayList<>();
        // Example of adding videos with local drawable resources
        videoList.add(new Video(R.drawable.pic1, "Author One", "Video One"));
        videoList.add(new Video(R.drawable.pic1, "Author Two", "Video Two"));
        videoList.add(new Video(R.drawable.pic1, "Author three", "Video One"));
        videoList.add(new Video(R.drawable.pic1, "Author four", "Video Two"));
        videoList.add(new Video(R.drawable.pic1, "Author five", "Video One"));
        videoList.add(new Video(R.drawable.pic1, "Author six", "Video Two"));
        videoList.add(new Video(R.drawable.pic1, "Author seven", "Video One"));
        videoList.add(new Video(R.drawable.pic1, "Author eight", "Video Two"));

        videoAdapter = new VideoAdapter(this, videoList);
        recyclerView.setAdapter(videoAdapter);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Get the ID of the clicked menu item
        int id = item.getItemId();

        // Handle navigation view item clicks here.
        if (id == R.id.nav_home) {
            // Handle Home click
        } else if (id == R.id.nav_profile) {
            // Handle Profile click
        } else if (id == R.id.login) {
            // Handle Login click
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        // Add more else-if statements as necessary


        // Close the navigation drawer
       // drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


}
