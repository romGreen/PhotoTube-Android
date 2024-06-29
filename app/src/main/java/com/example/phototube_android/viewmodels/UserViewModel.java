package com.example.phototube_android.viewmodels;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.phototube_android.classes.User;
import com.example.phototube_android.classes.Video;
import com.example.phototube_android.repository.UserLogRepository;
import com.example.phototube_android.repository.UserRepository;
import com.example.phototube_android.requests.LoginRequest;
import com.example.phototube_android.response.ApiResponse;
import com.example.phototube_android.response.TokenResponse;
import com.example.phototube_android.response.isExistResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserViewModel extends ViewModel {
        private UserRepository userRepository;
        private MutableLiveData<ApiResponse<User>> registerData;
        private MutableLiveData<ApiResponse<TokenResponse>> tokenData;
        public UserViewModel() {
            this.userRepository = new UserRepository();
            this.registerData = new MutableLiveData<>();
            this.tokenData = new MutableLiveData<>();
        }


        public MutableLiveData<ApiResponse<User>> getRegisterData()
        {
            return this.registerData;
        }
        public MutableLiveData<ApiResponse<TokenResponse>> getTokenData()
        {
            return this.tokenData;
        }

        public void addUser(Context context, User user) {
            userRepository.addUser(context, user,registerData);
        }


        public void loginUser(LoginRequest loginRequest)
        {
            userRepository.loginUser(loginRequest,tokenData);
        }

    }
