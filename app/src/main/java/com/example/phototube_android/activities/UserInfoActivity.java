package com.example.phototube_android.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.phototube_android.R;
import com.example.phototube_android.classes.User;
import com.example.phototube_android.viewmodels.UserLogViewModel;

public class UserInfoActivity extends AppCompatActivity {

    private TextView displayNameTextView;
    private ImageView profileImageView;
    private UserLogViewModel userLogViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        // Initialize UI components
        displayNameTextView = findViewById(R.id.displayNameTextView);
        profileImageView = findViewById(R.id.profileImageView);

        // Initialize ViewModel
        userLogViewModel = new ViewModelProvider(this).get(UserLogViewModel.class);

        // Get user data
        getUserDetails();
    }

    private void getUserDetails() {
        // Fetch user data
        userLogViewModel.getUser();
        // Observe user data
        userLogViewModel.getUserData().observe(this, userResponse -> {
            if (userResponse.isSuccess() && userResponse.getData() != null) {
                updateUI(userResponse.getData());
            } else {
                Toast.makeText(this, "Failed to load user data: " + userResponse.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateUI(User user) {
        displayNameTextView.setText(user.getDisplayname());
        Glide.with(this)
                .load(user.getProfileImg())
                .placeholder(R.drawable.youtube_image) // Assuming you have a placeholder
                .into(profileImageView);
    }
}
