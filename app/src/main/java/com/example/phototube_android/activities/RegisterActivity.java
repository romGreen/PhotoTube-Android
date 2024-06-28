package com.example.phototube_android.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.phototube_android.R;
import com.example.phototube_android.entities.PhotoHandler;
import com.example.phototube_android.classes.User;
import com.example.phototube_android.viewmodels.UserViewModel;



public class RegisterActivity extends AppCompatActivity {

    private EditText etFirstName, etLastName, etUsername, etEmail, etPassword, etRePassword;
    private PhotoHandler photoHandler;
    private RadioGroup rgGender;
    private Button btnRegister;
    private Bitmap resultBit;
    private ImageButton galleryPhoto, cameraPhoto;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        EdgeToEdge.enable(this);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);



        userViewModel.getRegisterData().observe(this,user -> {
            if(!user.isSuccess()){
                Toast toast = Toast.makeText(RegisterActivity.this,
                        user.getMessage(), Toast.LENGTH_LONG);
                toast.show();
                return;
            }
            Toast toast = Toast.makeText(RegisterActivity.this,
                    user.getMessage(), Toast.LENGTH_LONG);
            toast.show();
            startActivity(new Intent(this, LoginActivity.class));
        });

        initialize();
        clickEventer();
    }
    private void initialize() {
        ImageView regPhoto = findViewById(R.id.reg_photo);
        photoHandler = new PhotoHandler(regPhoto, this);
        cameraPhoto = findViewById(R.id.add_selfie);
        etFirstName = findViewById(R.id.et_first_name);
        etLastName = findViewById(R.id.et_last_name);
        etUsername = findViewById(R.id.et_username);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etRePassword = findViewById(R.id.et_re_password);
        rgGender = findViewById(R.id.rg_gender);
        btnRegister = findViewById(R.id.btn_register);
        galleryPhoto = findViewById(R.id.btn_upload_image);
    }

    private void clickEventer() {
        btnRegister.setOnClickListener(v -> registerUser());
        galleryPhoto.setOnClickListener(v -> photoHandler.checkPermissionAndOpenGallery());
        cameraPhoto.setOnClickListener(v -> photoHandler.askCameraPermissions());
    }

    private void registerUser() {
        // Get user input
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String username = etUsername.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String rePassword = etRePassword.getText().toString().trim();

        // Get selected gender
        int selectedGenderId = rgGender.getCheckedRadioButtonId();
        RadioButton selectedGenderButton = findViewById(selectedGenderId);
        String gender = (selectedGenderButton != null) ? selectedGenderButton.getText().toString() : "";

      /*  // Basic validation
        if (firstName.isEmpty()) {
            runOnUiThread(() -> Toast.makeText(RegisterActivity.this, "Please enter your first name", Toast.LENGTH_SHORT).show());
            return;
        }

        if (lastName.isEmpty()) {
            Toast.makeText(this, "Please enter your last name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (username.isEmpty()) {
            Toast.makeText(this, "Please enter a username", Toast.LENGTH_SHORT).show();
            return;
        }

        if (email.isEmpty()) {
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.isEmpty()) {
            Toast.makeText(this, "Please enter a password", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(rePassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        if (gender.isEmpty()) {
            Toast.makeText(this, "Please select your gender", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidUsername(username)) {
            // Handle invalid username
            Toast.makeText(this, "Username must be at least 4 characters and contain at least 1 letter and 1 digit", Toast.LENGTH_SHORT).show();
            return; // or show an error message
        }

        if (!isValidPassword(password)) {
            // Handle invalid password
            Toast.makeText(this, "Password must be at least 8 characters and contain at least 1 letter and 1 digit", Toast.LENGTH_SHORT).show();
            return; // or show an error message
        }


//*/
//       userViewModel.isExist(username);

        // Prepare the User object
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setDisplayname(firstName + " " + lastName);
        user.setGender(gender);
        String profileImgUri = PhotoHandler.bitmapToUri(resultBit, this).toString();
        user.setProfileImg(profileImgUri);

        // Use ViewModel to add user
        userViewModel.addUser(user);
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

    public boolean isValidUsername(String username) {
        boolean hasLetter = false;
        boolean hasDigit = false;

        if (username.length() >= 4) {
            for (char c : username.toCharArray()) {
                if (Character.isLetter(c)) {
                    hasLetter = true;
                }
                if (Character.isDigit(c)) {
                    hasDigit = true;
                }
                if (hasLetter && hasDigit) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isValidPassword(String password) {
        boolean hasLetter = false;
        boolean hasDigit = false;

        if (password.length() >= 8) {
            for (char c : password.toCharArray()) {
                if (Character.isLetter(c)) {
                    hasLetter = true;
                }
                if (Character.isDigit(c)) {
                    hasDigit = true;
                }
                if (hasLetter && hasDigit) {
                    return true;
                }
            }
        }
        return false;
    }
}
