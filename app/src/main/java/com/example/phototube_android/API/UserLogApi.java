package com.example.phototube_android.API;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.phototube_android.API.Server.UserServerApi;
import com.example.phototube_android.classes.User;
import com.example.phototube_android.entities.TokenInterceptor;
import com.example.phototube_android.entities.UserManager;
import com.example.phototube_android.response.ApiResponse;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserLogApi {
    private static final String BASE_URL = "http://10.0.2.2:1324/";
    private UserServerApi userServerApi;
    private Retrofit retrofit;
    private TokenInterceptor tokenInterceptor;

    public UserLogApi(){
        String token = UserManager.getInstance().getToken();
        tokenInterceptor = new TokenInterceptor(token);

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(tokenInterceptor)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        userServerApi = retrofit.create(UserServerApi.class);

    }

    public void getUser(MutableLiveData<ApiResponse<User>> userLiveData) {
        String userId = UserManager.getInstance().getUserId();
        userServerApi.getUser(userId).enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    userLiveData.postValue(new ApiResponse<>(
                            response.body(),
                            "User details retrieved successfully",
                            true
                    ));
                } else {
                    Log.e("UserViewModel", "Error response code: " + response.code());
                    userLiveData.postValue(new ApiResponse<>(
                            null,
                            "Failed to retrieve user details",
                            false
                    ));
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                userLiveData.postValue(new ApiResponse<>(
                        null,
                        "Error: " + t.getMessage(),
                        false
                ));
            }
        });
    }


}

