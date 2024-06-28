package com.example.phototube_android.API;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.phototube_android.API.Server.UserServerApi;
import com.example.phototube_android.API.Server.VideoServiceApi;
import com.example.phototube_android.classes.Video;
import com.example.phototube_android.entities.TokenInterceptor;
import com.example.phototube_android.entities.UserManager;
import com.example.phototube_android.response.ApiResponse;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VideoInApi {

    private static final String BASE_URL = "http://10.0.2.2:1324/";
    private VideoServiceApi videoServiceApi;
    private Retrofit retrofit;
    private TokenInterceptor tokenInterceptor;

    public VideoInApi(){
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

        videoServiceApi = retrofit.create(VideoServiceApi.class);

    }

    public void getVideos(MutableLiveData<ApiResponse<List<Video>>> VideoLiveData){
        videoServiceApi.getVideos().enqueue(new Callback<List<Video>>() {
            @Override
            public void onResponse(@NonNull Call<List<Video>> call, @NonNull Response<List<Video>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    VideoLiveData.postValue(new ApiResponse<>(
                            response.body(),
                            "User details retrieved successfully",
                            true
                    ));
                } else {
                    Log.e("UserViewModel", "Error response code: " + response.code());
                    VideoLiveData.postValue(new ApiResponse<>(
                            null,
                            "Failed to retrieve user details",
                            false
                    ));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Video>> call, @NonNull Throwable t) {
                VideoLiveData.postValue(new ApiResponse<>(
                        null,
                        "Error: " + t.getMessage(),
                        false
                ));
            }
        });
    }

}
