package com.example.phototube_android.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.phototube_android.db.PhotoTubeDatabase;
import com.example.phototube_android.db.dao.VideoDao;
import com.example.phototube_android.model.Video;
import com.example.phototube_android.network.ApiService;
import com.example.phototube_android.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoRepository {
    private VideoDao videoDao;
    private ApiService apiService;

    public VideoRepository(Application application) {
        PhotoTubeDatabase db = PhotoTubeDatabase.getDatabase(application);
        videoDao = db.videoDao();
        apiService = RetrofitClient.getInstance().create(ApiService.class);
    }

    // Add methods for database and network operations
    public interface VideoDataCallback {
        void onVideoDataFetched(Video video);
        void onError(Exception e);
    }

    public LiveData<List<Video>> getAllVideos() {
        LiveData<List<Video>> cachedVideos = videoDao.getAllVideos();
        apiService.getAllVideos().enqueue(new Callback<List<Video>>() {
            @Override
            public void onResponse(Call<List<Video>> call, Response<List<Video>> response) {
                if (response.isSuccessful()) {
                    // Assuming background thread operation using Room
                    AsyncTask.execute(() -> videoDao.insertAll(response.body()));
                }
            }

            @Override
            public void onFailure(Call<List<Video>> call, Throwable t) {
                // Handle failure
            }
        });
        return cachedVideos; // Returns LiveData that can be observed for changes
    }

    public void getVideoById(String videoId, VideoDataCallback callback) {
        // Assume ApiService has a method to fetch a single video by ID
        Call<Video> call = apiService.getVideoById(videoId);
        call.enqueue(new Callback<Video>() {
            @Override
            public void onResponse(Call<Video> call, Response<Video> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Video video = response.body();
                    // Optionally save to database
                    AsyncTask.execute(() -> videoDao.insertVideo(video));
                    callback.onVideoDataFetched(video);
                } else {
                    callback.onError(new Exception("Failed to fetch video data"));
                }
            }

            @Override
            public void onFailure(Call<Video> call, Throwable t) {
                callback.onError(new Exception(t));
            }
        });
    }
}
