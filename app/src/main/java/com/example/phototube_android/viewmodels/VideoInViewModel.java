package com.example.phototube_android.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.phototube_android.classes.Video;
import com.example.phototube_android.repository.VideoRepository;
import com.example.phototube_android.requests.LikeActionRequest;
import com.example.phototube_android.requests.VideoUpdateRequest;
import com.example.phototube_android.response.ApiResponse;
import com.example.phototube_android.response.TokenResponse;

import java.io.File;
import java.util.List;

public class VideoInViewModel extends ViewModel {
    private MutableLiveData<ApiResponse<Video>> addVideoData;
    private VideoRepository videoRepository;
    private MutableLiveData<ApiResponse<List<Video>>> VideoData;
    private MutableLiveData<ApiResponse<List<Video>>> userVideosData;
    private MutableLiveData<ApiResponse<Video>> updateVideoData;
    private MutableLiveData<ApiResponse<Video>> likeActionLiveData;
    private MutableLiveData<ApiResponse<Void>> deleteVideoData;
    private MutableLiveData<ApiResponse<TokenResponse>> tokenData;

    public VideoInViewModel() {
        this.videoRepository = new VideoRepository();
        this.addVideoData = new MutableLiveData<>();
        this.updateVideoData = new MutableLiveData<>();
        this.likeActionLiveData = new MutableLiveData<>();
        this.deleteVideoData = new MutableLiveData<>();
        this.VideoData = new MutableLiveData<>();
        this.userVideosData = new MutableLiveData<>();
        this.tokenData = new MutableLiveData<>();
    }

    public MutableLiveData<ApiResponse<List<Video>>> getVideoData() {
        return this.VideoData;
    }

    public MutableLiveData<ApiResponse<Video>> getLikeActionLiveData() {
        return likeActionLiveData;
    }

    public MutableLiveData<ApiResponse<Video>> getAddVideoData() {
        return addVideoData;
    }

    public VideoRepository getVideoRepository() {
        return videoRepository;
    }

    public MutableLiveData<ApiResponse<List<Video>>> getUserVideosData() {
        return userVideosData;
    }

    public MutableLiveData<ApiResponse<Video>> getUpdateVideoData() {
        return updateVideoData;
    }

    public MutableLiveData<ApiResponse<Void>> getDeleteVideoData() {
        return deleteVideoData;
    }

    public MutableLiveData<ApiResponse<TokenResponse>> getTokenData() {
        return tokenData;
    }

    public void getUserVideos(String userId) {
        videoRepository.getUserVideos(userId, userVideosData);
    }

    public void addVideo(String userId, String title, File videoFile) {
        videoRepository.addVideo(userId, title, videoFile, addVideoData);
    }

    public void updateVideo(String userId, String videoId, VideoUpdateRequest updateRequest) {
        videoRepository.updateVideo(userId, videoId, updateRequest, updateVideoData);
    }

    public void deleteVideo(String userId, String videoId) {
        videoRepository.deleteVideo(userId, videoId, deleteVideoData);
    }
    public void likeAction(String userId, String videoId, LikeActionRequest likeActionRequest) {
        videoRepository.likeAction(userId, videoId, likeActionRequest, likeActionLiveData);
    }

}