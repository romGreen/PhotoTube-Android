package com.example.phototube_android.API;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.phototube_android.API.Server.UserServerApi;
import com.example.phototube_android.API.Server.VideoServiceApi;
import com.example.phototube_android.classes.Video;
import com.example.phototube_android.entities.TokenInterceptor;
import com.example.phototube_android.entities.UserManager;
import com.example.phototube_android.requests.LikeActionRequest;
import com.example.phototube_android.requests.VideoUpdateRequest;
import com.example.phototube_android.response.ApiResponse;
import com.example.phototube_android.response.TokenResponse;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
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
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);

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

    public void addVideo(String userId, String title, File videoFile,
                         MutableLiveData
                                 <ApiResponse<Video>> videoLiveData) {
        RequestBody titlePart = RequestBody.create(MediaType.parse("text/plain"), title);
        RequestBody requestFile = RequestBody.create(MediaType.parse("video/*"), videoFile);
        MultipartBody.Part videoPart = MultipartBody.Part.createFormData("video", videoFile.getName(), requestFile);

        Call<Video> call = videoServiceApi.addVideo(userId, titlePart, videoPart);
        call.enqueue(new Callback<Video>() {
            @Override
            public void onResponse(@NonNull Call<Video> call, @NonNull Response<Video> response) {
                if (response.isSuccessful()) {
                    // Forward the response to the original callback
                    videoLiveData.postValue(new ApiResponse<>
                            (response.body(), "Video added successfully", true));
                } else {
                    // Handle the case where the server responds with an error
                    videoLiveData.postValue(new ApiResponse<>
                            (null, "add video failed", false));
                }
            }

            @Override
            public void onFailure(@NonNull Call<Video> call, @NonNull Throwable t) {
                // Forward the failure to the original callback
                videoLiveData.postValue(new ApiResponse<>
                        (null, "Error: " + t.getMessage(), false));
            }
        });
    }

    public void updateVideo(String userId,boolean file, String videoId, VideoUpdateRequest updateRequest,
                            MutableLiveData<ApiResponse<Video>> videoLiveData) {
        MultipartBody.Part videoPart = null;
        // Create RequestBody instance from title
        RequestBody titleBody = RequestBody.create(MediaType.parse("text/plain"), updateRequest.getTitle());
        if(file)
        {
            // Create RequestBody instance from file
            RequestBody videoBody = RequestBody.create(MediaType.parse("video/*"), updateRequest.getVideoUrl());
            videoPart = MultipartBody.Part.createFormData("videoFile", updateRequest.getVideoUrl().getName(), videoBody);
        }
        Call<Video> call = videoServiceApi.updateVideo(userId, videoId, titleBody,videoPart);
        call.enqueue(new Callback<Video>() {
            @Override
            public void onResponse(@NonNull Call<Video> call, @NonNull Response<Video> response) {
                if (response.isSuccessful()) {
                    // If the update is successful, post the updated video details back to the LiveData
                    videoLiveData.postValue(new ApiResponse<>(response.body(), "Video updated successfully", true));
                } else {
                    // If the server response is not successful, post an error message
                    videoLiveData.postValue(new ApiResponse<>(null, "Failed to update video: " + response.code(), false));
                }
            }

            @Override
            public void onFailure(@NonNull Call<Video> call, @NonNull Throwable t) {
                // Handle the case where the network request itself fails
                videoLiveData.postValue(new ApiResponse<>(null, "Network error: " + t.getMessage(), false));
            }
        });
    }
    public void deleteVideo(String userId, String videoId, MutableLiveData<ApiResponse<Void>> videoLiveData) {
        Call<Void> call = videoServiceApi.deleteVideo(userId, videoId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    // If the deletion is successful, post a success message
                    videoLiveData.postValue(new ApiResponse<>(null, "Video deleted successfully", true));
                } else {
                    // If the server response is not successful, post an error message
                    videoLiveData.postValue(new ApiResponse<>(null, "Failed to delete video: " + response.code(), false));
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                // Handle the case where the network request itself fails
                videoLiveData.postValue(new ApiResponse<>(null, "Network error: " + t.getMessage(), false));
            }
        });
    }

    public void likeVideo(String videoId, LikeActionRequest likeRequest,
                          MutableLiveData<ApiResponse<Video>> videoLiveData) {
        String userId = UserManager.getInstance().getUserId();
        Call<Video> call = videoServiceApi.likeAction(userId, videoId, likeRequest);
        call.enqueue(new Callback<Video>() {
            @Override
            public void onResponse(@NonNull Call<Video> call, @NonNull Response<Video> response) {
                if (response.isSuccessful()) {
                    // If the like or dislike action is successful, post the updated video details back to LiveData
                    videoLiveData.postValue(new ApiResponse<>(response.body(), "Like/Dislike action performed successfully", true));
                } else {
                    // If the server response is not successful, post an error message
                    videoLiveData.postValue(new ApiResponse<>(null, "Failed to perform like/dislike action: " + response.code(), false));
                }
            }

            @Override
            public void onFailure(@NonNull Call<Video> call, @NonNull Throwable t) {
                // Handle the case where the network request itself fails
                videoLiveData.postValue(new ApiResponse<>(null, "Network error: " + t.getMessage(), false));
            }
        });
    }



}
