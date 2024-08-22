package com.example.phototube_android.repository;


import android.app.Application;
import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import com.example.phototube_android.API.UserApi;
import com.example.phototube_android.classes.User;
//import com.example.phototube_android.db.AppDB;
import com.example.phototube_android.requests.LoginRequest;
import com.example.phototube_android.response.ApiResponse;
import com.example.phototube_android.response.TokenResponse;


public class UserRepository {

    private UserApi userApi;


    public UserRepository() {
        userApi = new UserApi();
    }
    public void addUser(Context context, User user, MutableLiveData<ApiResponse<User>> RegisterLiveData) {
        userApi.addUser(context, user,RegisterLiveData);
    }


    public void loginUser(LoginRequest loginRequest, MutableLiveData<ApiResponse<TokenResponse>> tokenLiveData)
    {
        userApi.loginUser(loginRequest,tokenLiveData);
    }


}
