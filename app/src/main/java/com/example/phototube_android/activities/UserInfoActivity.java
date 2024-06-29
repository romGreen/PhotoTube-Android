package com.example.phototube_android.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.phototube_android.R;
import com.example.phototube_android.classes.User;
import com.example.phototube_android.entities.PhotoHandler;
import com.example.phototube_android.viewmodels.UserLogViewModel;

public class UserInfoActivity extends AppCompatActivity {

    private EditText  displaynameEditText,emailEditText, passwordEditText, rePasswordEditText;
    private TextView usernameTextView;
    private PhotoHandler photoHandler;
    private RadioGroup infoGender;
    private Button btnUpload;
    private Bitmap resultBit;
    private ImageButton galleryPhoto, cameraPhoto,deleteBtn;
    private UserLogViewModel userLogViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        initialize();

    }

    private void initialize()
    {
        // Initialize UI components
        usernameTextView = findViewById(R.id.username);
        displaynameEditText = findViewById(R.id.display_name);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        rePasswordEditText = findViewById(R.id.re_password);
        infoGender = findViewById(R.id.gender);
        galleryPhoto = findViewById(R.id.btn_upload_image_info);
        cameraPhoto = findViewById(R.id.add_selfie_info);

        // Initialize ViewModel
        userLogViewModel = new ViewModelProvider(this).get(UserLogViewModel.class);

        // Get user data
        //getUserDetails();



    }

   /* private void getUserDetails() {
        // Fetch user data
        userLogViewModel.getUserInfo();
        // Observe user data
        userLogViewModel.getUserInfoData().observe(this, userResponse -> {
            Log.d("UserProfileActivity", "Observer triggered");
            if (userResponse.isSuccess() && userResponse.getData() != null) {
                Log.d("UserProfileActivity", "User data loaded: " + userResponse.getData());
                updateUI(userResponse.getData());
            } else {
                Log.e("UserProfileActivity", "Failed to load user data: " + userResponse.getMessage());
                Toast.makeText(this, "Failed to load user data: " + userResponse.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
*/
    private void updateUI(User user) {

        displaynameEditText.setText(user.getDisplayname());
        emailEditText.setText(user.getEmail());
//        emailEditText.setText(user.getEmail());
//        passwordEditText.setText(user.getPassword());
//
//        Glide.with(this)
//                .load(user.getProfileImg())
//                .placeholder(R.drawable.youtube_image) // Assuming you have a placeholder
//                .into(profileImageView);
    }
}
