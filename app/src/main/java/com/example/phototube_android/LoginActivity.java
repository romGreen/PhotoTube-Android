package com.example.phototube_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.phototube_android.entities.User;
import com.example.phototube_android.entities.UserListManager;
import com.example.phototube_android.entities.UserManager;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.username_login_text);
        passwordEditText = findViewById(R.id.password_login_text);
        loginButton = findViewById(R.id.login_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void loginUser() {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        // Username validation: At least 4 characters with at least 1 letter and 1 number
        if (!username.matches("^(?=.*[a-zA-Z])(?=.*\\d).{4,}$")) {
            // Handle invalid username case here
            Toast.makeText(LoginActivity.this, "username must be at least 4 characters and contain at least 1 letter and 1 digit", Toast.LENGTH_SHORT).show();
            return;
        }

        // Password validation: At least 8 characters with at least 1 letter and 1 number
        if (!password.matches("^(?=.*[a-zA-Z])(?=.*\\d).{8,}$")) {
            // Handle invalid password case here
            Toast.makeText(LoginActivity.this, "password must be at least 8 characters and contain at least 1 letter and 1 digit", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if the username and password match any user in the list
        for (User user : UserListManager.getInstance().getUserList()) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                // Set the user in UserManager
                UserManager.getInstance().setUser(user);

                // login user
                UserManager.getInstance().login();

                // Login success
                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                // Navigate to MainActivity
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);

                // Finish LoginActivity so that the user can't go back to it
                finish();
                return;
            }
        }

        // Login failed
        Toast.makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
    }

}
