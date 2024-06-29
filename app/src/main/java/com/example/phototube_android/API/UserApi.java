package com.example.phototube_android.API;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.phototube_android.API.Server.UserServerApi;
import com.example.phototube_android.classes.User;
import com.example.phototube_android.classes.Video;
import com.example.phototube_android.entities.FileUtils;
import com.example.phototube_android.entities.UserManager;
import com.example.phototube_android.requests.LoginRequest;
import com.example.phototube_android.response.ApiResponse;
import com.example.phototube_android.response.TokenResponse;
import com.example.phototube_android.response.isExistResponse;


import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.Response;
import retrofit2.Callback;
import retrofit2.converter.gson.GsonConverterFactory;


public class UserApi {
    private static final String BASE_URL = "http://10.0.2.2:1324/";
    private UserServerApi userServerApi;

    private Retrofit retrofit;

    public UserApi()
    {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        userServerApi = retrofit.create(UserServerApi.class);

    }

    public void addUser(Context context, User user, MutableLiveData<ApiResponse<User>> registerLiveData){

        RequestBody usernamePart = RequestBody.create(MediaType.parse("text/plain"), user.getUsername());
        RequestBody passwordPart = RequestBody.create(MediaType.parse("text/plain"), user.getPassword());
        RequestBody displaynamePart = RequestBody.create(MediaType.parse("text/plain"), user.getDisplayname());
        RequestBody emailPart = RequestBody.create(MediaType.parse("text/plain"), user.getEmail());
        RequestBody genderPart = RequestBody.create(MediaType.parse("text/plain"), user.getGender());


        String profileImgString = user.getProfileImg();
        Uri profileImgUri = Uri.parse(profileImgString);
        File imageFile;
        try {
            imageFile = FileUtils.getFileFromUri(context, profileImgUri);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle error
            return;
        }


        RequestBody filePart = RequestBody.create(MediaType.parse("multipart/form-data"),imageFile);
        MultipartBody.Part profileImg = MultipartBody.Part.createFormData("profileImg", imageFile.getName(), filePart);

        Call<User> call = userServerApi.addUser(usernamePart, passwordPart, displaynamePart, emailPart, genderPart, profileImg);

        call.enqueue(new Callback<User>(){
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    registerLiveData.postValue(new ApiResponse<>
                            (response.body(), "User register successfully", true));
                } else {
                    registerLiveData.postValue(new ApiResponse<>
                            (null, "Username exists", false));
                }
            }
            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                registerLiveData.postValue(new ApiResponse<>
                        (null, "Error: " + t.getMessage(), false));
            }
        });
    }

    // Function to fetch user videos
    public void getUserVideos(String userId, Callback<List<Video>> callback) {
        Call<List<Video>> call = userServerApi.getUserVideos(userId);
        call.enqueue(new Callback<List<Video>>() {
            @Override
            public void onResponse(Call<List<Video>> call, Response<List<Video>> response) {
                if (response.isSuccessful()) {
                    callback.onResponse(call, response);
                } else {
                    // Error handling - could include logging or modifying the response
                    callback.onFailure(call, new RuntimeException("Response unsuccessful"));
                }
            }

            @Override
            public void onFailure(Call<List<Video>> call, Throwable t) {
                callback.onFailure(call, t);
            }
        });
    }



    public void loginUser(LoginRequest loginRequest, MutableLiveData<ApiResponse<TokenResponse>> tokenLiveData)
    {
        userServerApi.login(loginRequest).enqueue(new Callback<TokenResponse>(){
            @Override
            public void onResponse(@NonNull Call<TokenResponse> call, @NonNull Response<TokenResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    tokenLiveData.postValue(new ApiResponse<>
                            (response.body(), "User login successfully", true));
                } else {
                    tokenLiveData.postValue(new ApiResponse<>
                            (null, "Password or username not good", false));
                }
            }
            @Override
            public void onFailure(@NonNull Call<TokenResponse> call, @NonNull Throwable t) {
                tokenLiveData.postValue(new ApiResponse<>
                        (null, "Error: " + t.getMessage(), false));
            }
        });
    }


}
