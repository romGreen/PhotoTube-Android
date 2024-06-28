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
        //Token data
        userViewModel.getTokenData().observe(this,user -> {
            if(!user.isSuccess()){
                Toast toast = Toast.makeText(LoginActivity.this,
                        user.getMessage(), Toast.LENGTH_LONG);
                toast.show();
                return;
            }
            //put the token on a singleton
            UserManager.getInstance().setToken(user.getData().getToken());
            UserManager.getInstance().setUserId(user.getData().getUserId());
            //Gets the user details
           getUser();

            Toast toast = Toast.makeText(LoginActivity.this,
                    user.getMessage(), Toast.LENGTH_LONG);

            toast.show();
            startActivity(new Intent(this, MainActivity.class));
        });






    }

    private void getUser(){
        userLogViewModel = new ViewModelProvider(this).get(UserLogViewModel.class);
        userLogViewModel.getUser();

        userLogViewModel.getUserData().observe(this,user -> {
            if(!user.isSuccess()){
                Toast toast = Toast.makeText(LoginActivity.this,
                        user.getMessage(), Toast.LENGTH_LONG);
                toast.show();
                return;
            }

            Toast toast = Toast.makeText(LoginActivity.this,
                    user.getMessage(), Toast.LENGTH_LONG);

            toast.show();
            UserManager.getInstance().setUser(user.getData());
            UserManager.getInstance().login();

        });
    }

    private void initialize(){
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
        validation();

        LoginRequest loginRequest = new LoginRequest(username,password);
        userViewModel.loginUser(loginRequest);
        UserManager.getInstance().login();
        finish();
    }

    private void validation()
    {
        // Username validation: At least 4 characters with at least 1 letter and 1 number
       /* if (!username.matches("^(?=.*[a-zA-Z])(?=.*\\d).{4,}$")) {
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
*/
    }
}
