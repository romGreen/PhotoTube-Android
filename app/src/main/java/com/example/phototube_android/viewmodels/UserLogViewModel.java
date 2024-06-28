package com.example.phototube_android.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.phototube_android.classes.User;
import com.example.phototube_android.repository.UserLogRepository;
import com.example.phototube_android.repository.UserRepository;
import com.example.phototube_android.response.ApiResponse;

public class UserLogViewModel extends ViewModel {

    private UserLogRepository userLogRepository;
    private MutableLiveData<ApiResponse<User>> userData;
    public UserLogViewModel() {
        this.userData = new MutableLiveData<>();
       this.userLogRepository = new UserLogRepository();
    }
    public MutableLiveData<ApiResponse<User>> getUserData()
    {
        return this.userData;
    }
    public void getUser() {
        userLogRepository.getUser(userData);
    }
}

