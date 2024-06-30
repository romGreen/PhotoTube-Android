package com.example.phototube_android.response;

import com.example.phototube_android.classes.User;

public class UpdateUserResponse {
    private String message;
    private User user;

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }
}

