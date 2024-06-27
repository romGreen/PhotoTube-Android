package com.example.phototube_android.entities;

import com.example.phototube_android.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserListManager {
    private static UserListManager instance;
    private List<User> userList;

    private UserListManager() {
        userList = new ArrayList<>();
    }

    public static synchronized UserListManager getInstance() {
        if (instance == null) {
            instance = new UserListManager();
        }
        return instance;
    }

    public void addUser(User user) {
        userList.add(user);
    }

    public List<User> getUserList() {
        return userList;
    }
}
