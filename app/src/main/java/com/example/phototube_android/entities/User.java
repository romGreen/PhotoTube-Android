package com.example.phototube_android.entities;

import android.net.Uri;

public class User {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;
    private String gender;
    private Uri imageUri;

    // Constructor

    public User(String firstName, String lastName, String username, String email, String password, String gender, Uri imageUri) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.imageUri = imageUri;
    }


    // Getters and Setters


    public String getFirstName() {
        return firstName;
    }


    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Uri getImageUri() {
        return imageUri;
    }

}
