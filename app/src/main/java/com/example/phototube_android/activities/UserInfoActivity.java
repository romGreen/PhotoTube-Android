package com.example.phototube_android.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.phototube_android.R;
import com.example.phototube_android.classes.User;
import com.example.phototube_android.entities.PhotoHandler;
import com.example.phototube_android.entities.UserManager;
import com.example.phototube_android.viewmodels.UserLogViewModel;

public class UserInfoActivity extends AppCompatActivity {

    private EditText  displaynameEditText,emailEditText, passwordEditText, rePasswordEditText;
    private TextView usernameTextView;
    private ImageView image;
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
        clickEventer();
    }

    private void clickEventer() {
        btnUpload.setOnClickListener(v -> updateUser());
        deleteBtn.setOnClickListener(v -> deleteUser());
        galleryPhoto.setOnClickListener(v -> photoHandler.checkPermissionAndOpenGallery());
        cameraPhoto.setOnClickListener(v -> photoHandler.askCameraPermissions());
    }

    private void initialize()
    {
        // Initialize UI components
        displaynameEditText = findViewById(R.id.display_name);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        rePasswordEditText = findViewById(R.id.re_password);
        infoGender = findViewById(R.id.gender);
        galleryPhoto = findViewById(R.id.btn_upload_image_info);
        cameraPhoto = findViewById(R.id.add_selfie_info);
        btnUpload = findViewById(R.id.btn_update_info);
        deleteBtn = findViewById(R.id.btn_delete_info);

        image = findViewById(R.id.reg_photo_info);
        // Initialize ViewModel
        userLogViewModel = new ViewModelProvider(this).get(UserLogViewModel.class);
        photoHandler = new PhotoHandler(image, this);
        // Get user data
        getUserDetails();
    }

   private void getUserDetails() {
        // Fetch user data
        userLogViewModel.getUserInfo();
        // Observe user data
        userLogViewModel.getUserInfoData().observe(this, userResponse -> {
            if (userResponse.isSuccess() && userResponse.getData() != null) {
                updateUI(userResponse.getData());
            } else {
                Log.e("UserProfileActivity", "Failed to load user data: " + userResponse.getMessage());
                Toast.makeText(this, "Failed to load user data: " + userResponse.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateUI(User user) {

        displaynameEditText.setText(user.getDisplayname());
        emailEditText.setText(user.getEmail());
        passwordEditText.setText(user.getPassword());
        RadioButton maleRadioButton = findViewById(R.id.maleInfo);
        RadioButton femaleRadioButton = findViewById(R.id.femaleInfo);

        if (user.getGender().equalsIgnoreCase("Male")) {
            maleRadioButton.setChecked(true);
        } else if (user.getGender().equalsIgnoreCase("Female")) {
            femaleRadioButton.setChecked(true);
        }
        String imageUrl = "http://10.0.2.2:" + user.getProfileImg();

        Glide.with(this)
                .load(imageUrl)
                .into(image);
    }

    private void deleteUser(){
        userLogViewModel.deleteUser();

        userLogViewModel.getUserDeleteData().observe(this, userResponse -> {
            Log.d("UserInfoActivity", "Observer triggered");
            if (userResponse.isSuccess() && userResponse.getData() != null) {
                Log.d("UserInfoActivity", "User data loaded: " + userResponse.getData());
                Toast.makeText(this,  userResponse.getMessage(), Toast.LENGTH_LONG).show();
                UserManager.getInstance().logout();
                Intent intent = new Intent(UserInfoActivity.this , MainActivity.class);
                startActivity(intent);
            } else {
                Log.e("UserInfoActivity", "Failed to load user data: " + userResponse.getMessage());
                Toast.makeText(this, "Failed to load user data: " + userResponse.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    private void updateUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String rePassword = rePasswordEditText.getText().toString().trim();  // Assume you have a corresponding EditText for this
        String displayname = displaynameEditText.getText().toString().trim();

        // Get selected gender
        int selectedGenderId = infoGender.getCheckedRadioButtonId();
        RadioButton selectedGenderButton = findViewById(selectedGenderId);
        String gender = (selectedGenderButton != null) ? selectedGenderButton.getText().toString() : "";

        if (!password.equals(rePassword)) {
            Toast.makeText(this, "Password and Re-Password do not match", Toast.LENGTH_SHORT).show();
            return; // Stop further processing
        }

        User user = new User();
        user.setPassword(password);
        user.setEmail(email);
        user.setDisplayname(displayname);
        user.setGender(gender);
        Boolean file;
        if (resultBit != null) {
            String profileImgUri = PhotoHandler.bitmapToUri(resultBit, this).toString();
            user.setProfileImg(profileImgUri);
            file = true;
        } else {
            String existingProfileImgUri = UserManager.getInstance().getUser().getProfileImg();
            user.setProfileImg(existingProfileImgUri);
            file = false;
        }

        userLogViewModel.updateUser(this, file, user);
        userLogViewModel.getUserUpdateData().observe(this, userResponse -> {
            if (userResponse.isSuccess() && userResponse.getData() != null) {
                UserManager.getInstance().getUser().setEmail(email);
                UserManager.getInstance().getUser().setDisplayname(displayname);
                UserManager.getInstance().getUser().setGender(gender);
                UserManager.getInstance().getUser().setPassword(password);
                UserManager.getInstance().getUser().setProfileImg(userResponse.getData().getUser().getProfileImg());
                Toast.makeText(this, userResponse.getData().getMessage(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(UserInfoActivity.this, MainActivity.class);
                startActivity(intent);
            } else {
                Log.e("UserInfoActivity", userResponse.getMessage());
                Toast.makeText(this, userResponse.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        photoHandler.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    // Handling the result of the photo
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.resultBit = photoHandler.onActivityResult(requestCode, resultCode, data);
    }
}
