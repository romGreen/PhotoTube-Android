package com.example.phototube_android.API;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.phototube_android.API.Server.UserServerApi;
import com.example.phototube_android.API.Server.VideoServiceApi;
import com.example.phototube_android.classes.Video;
import com.example.phototube_android.db.dao.VideoDao;
import com.example.phototube_android.entities.TokenInterceptor;
import com.example.phototube_android.entities.UserManager;
import com.example.phototube_android.repository.VideoInRepository;
import com.example.phototube_android.requests.LikeActionRequest;
import com.example.phototube_android.requests.VideoRecommendationRequest;
import com.example.phototube_android.requests.VideoUpdateRequest;
import com.example.phototube_android.response.ApiResponse;
import com.example.phototube_android.response.TokenResponse;

import java.io.File;
import java.util.ArrayList;
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
    private MutableLiveData<ApiResponse<List<Video>>> videoListData;
    private VideoDao dao;

    public VideoInApi(VideoDao dao,MutableLiveData<ApiResponse<List<Video>>> videoListData){

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
        this.dao = dao;
        this.videoListData = videoListData;

    }

    public void addVideo(String userId, String title, File videoFile) {
        RequestBody titlePart = RequestBody.create(MediaType.parse("text/plain"), title);
        RequestBody requestFile = RequestBody.create(MediaType.parse("video/*"), videoFile);
        MultipartBody.Part videoPart = MultipartBody.Part.createFormData("video", videoFile.getName(), requestFile);

        Call<Video> call = videoServiceApi.addVideo(userId, titlePart, videoPart);
        call.enqueue(new Callback<Video>() {
            @Override
            public void onResponse(@NonNull Call<Video> call, @NonNull Response<Video> response) {
                if (response.isSuccessful()) {
                    new Thread(() -> {

                        dao.insert(response.body());
                        videoListData.postValue(new ApiResponse<>(
                                dao.getAll(),
                                "Video added successfully",
                                true
                        ));
                    }).start();

                } else {
                    new Thread(() -> {
                    // Handle the case where the server responds with an error
                        videoListData.postValue(new ApiResponse<>(
                                dao.getAll(),
                                "Add video failed",
                                false
                        ));
                    }).start();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Video> call, @NonNull Throwable t) {

                new Thread(() -> {
                    // Handle the case where the server responds with an error
                    videoListData.postValue(new ApiResponse<>(
                            dao.getAll(),
                            "Error: " + t.getMessage(),
                            false
                    ));
                }).start();
            }
        });
    }

    public void updateVideo(String userId, boolean file, String videoId, VideoUpdateRequest updateRequest) {
        // Create the RequestBody instance from title and video file if needed
        RequestBody titleBody = RequestBody.create(MediaType.parse("text/plain"), updateRequest.getTitle());
        MultipartBody.Part videoPart = null;

        if (file && updateRequest.getVideoUrl() != null) {
            RequestBody videoBody = RequestBody.create(MediaType.parse("video/*"), updateRequest.getVideoUrl());
            videoPart = MultipartBody.Part.createFormData("videoFile", updateRequest.getVideoUrl().getName(), videoBody);
        }

        // Make the API call to update the video on the server
        Call<Video> call = videoServiceApi.updateVideo(userId, videoId, titleBody, videoPart);
        call.enqueue(new Callback<Video>() {
            @Override
            public void onResponse(@NonNull Call<Video> call, @NonNull Response<Video> response) {
                if (response.isSuccessful()) {
                    Video updatedVideo = response.body();
                    if (updatedVideo != null) {
                        Log.d("Video Update", "Video updated on server: " + updatedVideo.getId());

                        // Perform the database update in a separate thread
                        new Thread(() -> {
                            try {
                                Video originalVideo = dao.getVideoByServerId(videoId);
                                if (originalVideo != null) {
                                    Log.d("Video Update", "Original Video ID: " + originalVideo.getId());

                                    // Set the correct local ID
                                    updatedVideo.setId(originalVideo.getId());
                                    Log.d("Video Update", "Updated Video ID before DB update: " + updatedVideo.getId());

                                    // Attempt to update the video in the database
                                    dao.update(updatedVideo);
                                    Log.d("Video Update", "Video successfully updated in local database.");

                                    // Fetch and update the LiveData
                                    List<Video> allVideos = dao.getAll();
                                    videoListData.postValue(new ApiResponse<>(
                                            allVideos,
                                            "Video updated successfully",
                                            true
                                    ));
                                } else {
                                    Log.e("Video Update", "No video found in local database with server ID: " + videoId);
                                }
                            } catch (Exception e) {
                                Log.e("Video Update", "Exception during database update: " + e.getMessage(), e);
                            }
                        }).start();
                    } else {
                        Log.e("Video Update", "Response body is null");
                    }
                } else {
                    Log.e("Video Update", "Failed to update video: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Video> call, @NonNull Throwable t) {
                Log.e("Video Update", "Network error: " + t.getMessage());
            }
        });
    }





    public void deleteVideo(String userId, String videoId) {
        Call<Void> call = videoServiceApi.deleteVideo(userId, videoId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    // If the deletion is successful, post a success message
                    new Thread(() -> {
                        dao.deleteByServerId(videoId);
                        videoListData.postValue(new ApiResponse<>(
                                dao.getAll(),
                                "Video deleted successfully",
                                true
                        ));
                    }).start();
                } else {
                    // If the server response is not successful, post an error message
                    new Thread(() -> {
                        videoListData.postValue(new ApiResponse<>(
                                dao.getAll(),
                                "Failed to delete video: " + response.code(),
                                false
                        ));
                    }).start();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                // Handle the case where the network request itself fails
                new Thread(() -> {
                    videoListData.postValue(new ApiResponse<>(
                            dao.getAll(),
                            "Network error: " + t.getMessage(),
                            false
                    ));
                }).start();
            }
        });
    }

    public void likeVideo(String videoId, LikeActionRequest likeRequest, MutableLiveData<ApiResponse<Video>> videoLiveData) {
        String userId = UserManager.getInstance().getUserId();
        Call<Video> call = videoServiceApi.likeAction(userId, videoId, likeRequest);
        call.enqueue(new Callback<Video>() {
            @Override
            public void onResponse(@NonNull Call<Video> call, @NonNull Response<Video> response) {
                if (response.isSuccessful()) {
                    // If the like or dislike action is successful, post the updated video details back to LiveData
                    new Thread(() -> {
                        dao.update(response.body());
                        videoLiveData.postValue(new ApiResponse<>(response.body(), "Like/Dislike action performed successfully", true));
                    }).start();
                } else {
                    // If the server response is not successful, post an error message
                    new Thread(() -> {
                        videoLiveData.postValue(new ApiResponse<>(null, "Failed to perform like/dislike action: " + response.code(), false));
                    }).start();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Video> call, @NonNull Throwable t) {
                // Handle the case where the network request itself fails
                new Thread(() -> {
                    videoLiveData.postValue(new ApiResponse<>(null, "Network error: " + t.getMessage(), false));
                }).start();
            }
        });
    }

    public void getRecommendations(String userId, String videoId, VideoInRepository.OnRecommendationsReceived callback) {
        // Prepare the request body
        VideoRecommendationRequest request = new VideoRecommendationRequest(userId, videoId);

        Call<List<Video>> call = videoServiceApi.getRecommendations(request);
        call.enqueue(new Callback<List<Video>>() {
            @Override
            public void onResponse(@NonNull Call<List<Video>> call, @NonNull Response<List<Video>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onReceived(response.body());
                } else {
                    callback.onReceived(null);  // Handle error case
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Video>> call, @NonNull Throwable t) {
                callback.onReceived(null);  // Handle failure
            }
        });
    }

}
