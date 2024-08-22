package com.example.phototube_android.API;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.phototube_android.API.Server.UserServerApi;
import com.example.phototube_android.API.Server.VideoServiceApi;
import com.example.phototube_android.classes.User;
import com.example.phototube_android.classes.Video;
import com.example.phototube_android.db.dao.VideoDao;
import com.example.phototube_android.response.ApiResponse;
import com.example.phototube_android.viewmodels.VideoOffViewModel;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VideoOffApi {


    private static final String BASE_URL = "http://10.0.2.2:1324/";
    private VideoServiceApi videoServiceApi;
    private VideoDao dao;
    private Retrofit retrofit;
    private MutableLiveData<ApiResponse<List<Video>>> videoListData;

    public VideoOffApi(VideoDao dao, MutableLiveData<ApiResponse<List<Video>>> videoListData) {
        this.dao = dao;
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        videoServiceApi = retrofit.create(VideoServiceApi.class);
        this.videoListData = videoListData;
    }

    public void getVideos(MutableLiveData<ApiResponse<List<Video>>> VideoLiveData){
        videoServiceApi.getVideos().enqueue(new Callback<List<Video>>() {
            @Override
            public void onResponse(@NonNull Call<List<Video>> call, @NonNull Response<List<Video>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    new Thread(() -> {
                        dao.clear();
                        dao.insert(response.body().toArray(new Video[0]));
                        VideoLiveData.postValue(new ApiResponse<>(
                                dao.getAll(),
                            "User details retrieved successfully",
                            true
                    ));
                    }).start();
                } else {
                    Log.e("UserViewModel", "Error response code: " + response.code());
                    new Thread(() -> {
                    VideoLiveData.postValue(new ApiResponse<>(
                            dao.getAll(),
                            "Failed to retrieve user details",
                            false));
                    }).start();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Video>> call, @NonNull Throwable t) {
                new Thread(() -> {
                    VideoLiveData.postValue(new ApiResponse<>(
                        dao.getAll(),
                        "Error: " + t.getMessage(),
                        false));
                }).start();
            }
        });
    }

    public void getUserVideos(String userId, MutableLiveData<ApiResponse<List<Video>>> videoLiveData) {
        videoServiceApi.getUserVideos(userId).enqueue(new Callback<List<Video>>() {
            @Override
            public void onResponse(@NonNull Call<List<Video>> call, @NonNull Response<List<Video>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    videoLiveData.postValue(new ApiResponse<>(
                            response.body(),
                            "Videos retrieved successfully",
                            true
                    ));
                } else {
                    Log.e("VideoInApi", "Failed to retrieve videos: Response code " + response.code());
                    videoLiveData.postValue(new ApiResponse<>(
                            null,
                            "Failed to retrieve videos",
                            false
                    ));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Video>> call, @NonNull Throwable t) {
                Log.e("VideoInApi", "Error retrieving videos: " + t.getMessage(), t);
                videoLiveData.postValue(new ApiResponse<>(
                        null,
                        "Error retrieving videos: " + t.getMessage(),
                        false
                ));
            }
        });
    }

    public void getVideo(String userId, String videoId, MutableLiveData<ApiResponse<Video>> videoLiveData) {
        videoServiceApi.getVideo(userId, videoId).enqueue(new Callback<Video>() {
            @Override
            public void onResponse(@NonNull Call<Video> call, @NonNull Response<Video> response) {
                if (response.isSuccessful() && response.body() != null) {
                    videoLiveData.postValue(new ApiResponse<>(
                            response.body(),
                            "Video details retrieved successfully",
                            true
                    ));
                } else {
                    Log.e("VideoInApi", "Error fetching video: " + response.code());
                    videoLiveData.postValue(new ApiResponse<>(
                            null,
                            "Failed to retrieve video details",
                            false
                    ));
                }
            }

            @Override
            public void onFailure(@NonNull Call<Video> call, @NonNull Throwable t) {
                videoLiveData.postValue(new ApiResponse<>(
                        null,
                        "Network error: " + t.getMessage(),
                        false
                ));
            }
        });
    }


}