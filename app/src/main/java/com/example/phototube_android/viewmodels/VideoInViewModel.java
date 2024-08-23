package com.example.phototube_android.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.phototube_android.classes.Video;
import com.example.phototube_android.repository.VideoInRepository;
import com.example.phototube_android.requests.LikeActionRequest;
import com.example.phototube_android.requests.VideoUpdateRequest;
import com.example.phototube_android.response.ApiResponse;

import java.io.File;
import java.util.List;

public class VideoInViewModel extends AndroidViewModel {
    private MutableLiveData<ApiResponse<List<Video>>> VideoData;
    private VideoInRepository videoRepository;
    private MutableLiveData<ApiResponse<Video>> likeActionLiveData;



    public VideoInViewModel(Application application) {
        super(application);
        this.videoRepository = new VideoInRepository(application);
        this.VideoData = this.videoRepository.getVideoListData();
        this.likeActionLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<ApiResponse<List<Video>>> getVideoListData() {
        return this.VideoData;
    }

    public MutableLiveData<ApiResponse<Video>> getLikeActionLiveData() {
        return likeActionLiveData;
    }


    public void addVideo(String userId, String title, File videoFile) {
        videoRepository.addVideo(userId, title, videoFile);
    }
    public Video getVideoByServerId(String videoId) {
        return videoRepository.getVideoByServerId(videoId);
    }

    public void updateVideo(String userId,boolean file, String videoId, VideoUpdateRequest updateRequest) {
        videoRepository.updateVideo(userId,file, videoId, updateRequest);
    }

    public void deleteVideo(String userId, String videoId) {
        videoRepository.deleteVideo(userId, videoId);
    }
    public void likeAction(String videoId, LikeActionRequest likeActionRequest) {
        videoRepository.likeAction(videoId, likeActionRequest,likeActionLiveData);
    }

}