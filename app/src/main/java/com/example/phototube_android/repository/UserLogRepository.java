package com.example.phototube_android.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.phototube_android.API.UserApi;
import com.example.phototube_android.API.UserLogApi;
import com.example.phototube_android.classes.User;
import com.example.phototube_android.response.ApiResponse;

public class UserLogRepository {
    private UserLogApi userLogApi;

    public UserLogRepository() {
        userLogApi = new UserLogApi();
    }

    public void getUser(MutableLiveData<ApiResponse<User>> userLiveData)
    {
        userLogApi.getUser(userLiveData);
    }
}
