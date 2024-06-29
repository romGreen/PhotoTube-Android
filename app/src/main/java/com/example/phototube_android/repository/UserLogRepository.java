package com.example.phototube_android.repository;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.example.phototube_android.API.UserLogApi;
import com.example.phototube_android.classes.User;
import com.example.phototube_android.response.ApiResponse;
import com.example.phototube_android.response.MessageResponse;

public class UserLogRepository {
    private UserLogApi userLogApi;

    public UserLogRepository() {
        userLogApi = new UserLogApi();
    }

    public void getUser(MutableLiveData<ApiResponse<User>> userLiveData)
    {
        userLogApi.getUser(userLiveData);
    }

    public void getInfoUser(MutableLiveData<ApiResponse<User>> userLiveData)
    {
        userLogApi.getInfoUser(userLiveData);
    }

    public void updateUser(Context context, User user,boolean file,MutableLiveData<ApiResponse<User>> userLiveData)
    {
        userLogApi.updateUser( context, user, file,userLiveData);
    }
    public void deleteUser(MutableLiveData<ApiResponse<MessageResponse>> userLiveData)
    {
        userLogApi.deleteUser(userLiveData);
    }
}
