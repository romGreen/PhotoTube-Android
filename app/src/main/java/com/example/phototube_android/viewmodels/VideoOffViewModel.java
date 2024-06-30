package com.example.phototube_android.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.phototube_android.classes.Video;
import com.example.phototube_android.repository.VideoInRepository;
import com.example.phototube_android.repository.VideoOffRepository;
import com.example.phototube_android.response.ApiResponse;

import java.util.List;

public class VideoOffViewModel extends ViewModel {
    private VideoOffRepository videoOffRepository;
    private MutableLiveData<ApiResponse<List<Video>>> VideoData;
    private MutableLiveData<ApiResponse<Video>> singleVideoData;
    private MutableLiveData<ApiResponse<List<Video>>> userVideosData;
    public VideoOffViewModel() {
        this.videoOffRepository = new VideoOffRepository();
        this.singleVideoData = new MutableLiveData<>();
        this.userVideosData = new MutableLiveData<>();
        this.VideoData = new MutableLiveData<>();
    }
    public MutableLiveData<ApiResponse<Video>> getSingleVideoData() {
        return singleVideoData;
    }

    public MutableLiveData<ApiResponse<List<Video>>> getUserVideosData() {
        return userVideosData;
    }

    public MutableLiveData<ApiResponse<List<Video>>> getVideoData()
    {
        return this.VideoData;
    }

    public void getVideos()
    {
        videoOffRepository.getVideos(VideoData);
    }

    public void getVideo(String userId, String videoId)
    {
        videoOffRepository.getVideo(userId, videoId, singleVideoData);
    }

    public void getUserVideos(String userId) {
        videoOffRepository.getUserVideos(userId, userVideosData);
    }

}
