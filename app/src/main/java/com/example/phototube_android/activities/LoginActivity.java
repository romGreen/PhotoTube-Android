package com.example.phototube_android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.phototube_android.R;
import com.example.phototube_android.activities.MainActivity;
import com.example.phototube_android.entities.UserManager;
import com.example.phototube_android.requests.LoginRequest;
import com.example.phototube_android.viewmodels.UserLogViewModel;
import com.example.phototube_android.viewmodels.UserViewModel;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private UserViewModel userViewModel;
    private UserLogViewModel userLogViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialize();
    }

    private void getUser() {
        userLogViewModel = new ViewModelProvider(this).get(UserLogViewModel.class);
        userLogViewModel.getUserData().observe(this, user -> {
            if (!user.isSuccess()) {
                Toast.makeText(LoginActivity.this, user.getMessage(), Toast.LENGTH_LONG).show();
                return;
            }
            UserManager.getInstance().setUser(user.getData());

        });

        userLogViewModel.getUser();
    }

    private void initialize() {
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        usernameEditText = findViewById(R.id.username_login_text);
        passwordEditText = findViewById(R.id.password_login_text);
        loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(v -> loginUser());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void loginUser() {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        LoginRequest loginRequest = new LoginRequest(username, password);
        userViewModel.loginUser(loginRequest);

        userViewModel.getTokenData().observe(this, user -> {
            if (!user.isSuccess()) {
                Toast.makeText(LoginActivity.this, user.getMessage(), Toast.LENGTH_LONG).show();
                return;
            }
            UserManager.getInstance().setToken(user.getData().getToken());
            UserManager.getInstance().setUserId(user.getData().getUserId());
            UserManager.getInstance().login();

            Toast.makeText(LoginActivity.this, user.getMessage(), Toast.LENGTH_LONG).show();
            getUser();
            finish();
        });

    }
}
