package com.example.phototube_android.ui.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.phototube_android.model.Video;
import com.example.phototube_android.repository.VideoRepository;

import java.util.List;

public class VideoViewModel extends ViewModel {
    private final VideoRepository videoRepository;
    private LiveData<List<Video>> videos;
    private MutableLiveData<Video> currentVideo;


    public VideoViewModel(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
        videos = videoRepository.getAllVideos();
    }

    // Method to fetch videos
    public LiveData<List<Video>> getVideos() {
        if (videos == null) {
            videos = videoRepository.getAllVideos();
        }
        return videos;
    }

    public LiveData<Video> getVideoById(String videoId) {
        if (currentVideo == null) {
            currentVideo = new MutableLiveData<>();
        }
        videoRepository.getVideoById(videoId, new VideoRepository.VideoDataCallback() {
            @Override
            public void onVideoDataFetched(Video video) {
                currentVideo.postValue(video);
            }
            @Override
            public void onError(Exception e) {
                // Log the error, handle the error state, or notify the user
                Log.e("VideoViewModel", "Error fetching video", e);
                // You might want to post a null value or a specific error state to the LiveData
                currentVideo.postValue(null);
            }
        });
        return currentVideo;
    }


}
