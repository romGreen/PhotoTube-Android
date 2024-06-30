package com.example.phototube_android.viewmodels;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.phototube_android.classes.User;
import com.example.phototube_android.repository.UserLogRepository;
import com.example.phototube_android.response.ApiResponse;
import com.example.phototube_android.response.MessageResponse;
import com.example.phototube_android.response.UpdateUserResponse;

public class UserLogViewModel extends ViewModel {

    private UserLogRepository userLogRepository;
    private MutableLiveData<ApiResponse<User>> userData;
    private MutableLiveData<ApiResponse<User>> userInfoData;

    private MutableLiveData<ApiResponse<UpdateUserResponse>> userUpdateData;

    private MutableLiveData<ApiResponse<MessageResponse>> userDeleteData;
    public UserLogViewModel() {
        this.userLogRepository = new UserLogRepository();
        this.userUpdateData = new MutableLiveData<>();
        this.userData = new MutableLiveData<>();
        this.userDeleteData = new MutableLiveData<>();
        this.userInfoData = new MutableLiveData<>();

    }
    public MutableLiveData<ApiResponse<User>> getUserData()
    {
        return this.userData;
    }
    public MutableLiveData<ApiResponse<User>> getUserInfoData()
    {
        return this.userInfoData;
    }
    public MutableLiveData<ApiResponse<MessageResponse>> getUserDeleteData()
    {
        return this.userDeleteData;
    }
    public MutableLiveData<ApiResponse<UpdateUserResponse>> getUserUpdateData()
    {
        return this.userUpdateData;
    }
    public void getUser() {
        userLogRepository.getUser(userData);
    }
    public void getUserInfo() {
        userLogRepository.getInfoUser(userInfoData);
    }
    public void deleteUser() {
        userLogRepository.deleteUser(userDeleteData);
    }
    public void updateUser(Context context,boolean file, User user) {userLogRepository.updateUser(context, user,file,userUpdateData);}
}

