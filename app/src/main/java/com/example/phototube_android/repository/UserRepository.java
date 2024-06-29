package com.example.phototube_android.repository;


import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import com.example.phototube_android.API.UserApi;
import com.example.phototube_android.API.UserLogApi;
import com.example.phototube_android.classes.User;
import com.example.phototube_android.classes.Video;
import com.example.phototube_android.requests.LoginRequest;
import com.example.phototube_android.response.ApiResponse;
import com.example.phototube_android.response.TokenResponse;
import com.example.phototube_android.response.isExistResponse;

import java.util.List;

import retrofit2.Callback;


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
