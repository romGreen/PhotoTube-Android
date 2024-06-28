package com.example.phototube_android.entities;

import com.example.phototube_android.classes.User;

public class UserManager {
    // Static instance of UserManager
    private static UserManager instance;
    private boolean isLoggedIn = false;

    // Instance variable to hold User object
    private User user;
    private String token;

    private String userId;

    // Private constructor to prevent instantiation
    private UserManager() {

    }

    // Method to get the single instance of UserManager
    public static synchronized UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    // Getter and Setter for User object
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public String getToken(){return this.token;}
    public void setToken(String token){
        this.token=token;
    }


    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void login() {
        isLoggedIn = true;
    }

    public void logout() {
        isLoggedIn = false;
        token="";
        user = null;
        userId ="";
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
