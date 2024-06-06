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
        videoList.add(new Video("fuego", "Narcos",R.drawable.pic1,R.raw.narcos));
        videoList.add(new Video("play 1", "NBA",R.drawable.photo1,R.raw.play1));
        videoList.add(new Video("play 2", "NBA",R.drawable.photo2,R.raw.play2));
        videoList.add(new Video("play 3", "NBA",R.drawable.photo3,R.raw.play3));
        videoList.add(new Video("play 4", "NBA",R.drawable.photo4,R.raw.play4));
        videoList.add(new Video("play 5", "NBA",R.drawable.photo5,R.raw.play5));
        videoList.add(new Video("play 6", "NBA",R.drawable.photo6,R.raw.play6));
        videoList.add(new Video("play 7", "NBA",R.drawable.photo7,R.raw.play7));
        videoList.add(new Video("play 8", "NBA",R.drawable.photo8,R.raw.play8));
        videoList.add(new Video("play 9", "NBA",R.drawable.photo9,R.raw.play9));
        videoList.add(new Video("play 10", "NBA",R.drawable.photo10,R.raw.play10));

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
