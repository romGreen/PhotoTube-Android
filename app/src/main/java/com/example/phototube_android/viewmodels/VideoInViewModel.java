package com.example.phototube_android.viewmodels;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.phototube_android.classes.Video;
import com.example.phototube_android.repository.VideoInRepository;
import com.example.phototube_android.requests.LikeActionRequest;
import com.example.phototube_android.requests.VideoUpdateRequest;
import com.example.phototube_android.response.ApiResponse;

import java.io.File;

public class VideoInViewModel extends ViewModel {
    private MutableLiveData<ApiResponse<Video>> addVideoData;
    private VideoInRepository videoRepository;
    private MutableLiveData<ApiResponse<Video>> updateVideoData;
    private MutableLiveData<ApiResponse<Video>> likeActionLiveData;
    private MutableLiveData<ApiResponse<Void>> deleteVideoData;


    public VideoInViewModel() {
        this.videoRepository = new VideoInRepository();
        this.addVideoData = new MutableLiveData<>();
        this.updateVideoData = new MutableLiveData<>();
        this.likeActionLiveData = new MutableLiveData<>();
        this.deleteVideoData = new MutableLiveData<>();
    }



    public MutableLiveData<ApiResponse<Video>> getLikeActionLiveData() {
        return likeActionLiveData;
    }

    public MutableLiveData<ApiResponse<Video>> getAddVideoData() {
        return addVideoData;
    }



    public MutableLiveData<ApiResponse<Video>> getUpdateVideoData() {
        return updateVideoData;
    }

    public MutableLiveData<ApiResponse<Void>> getDeleteVideoData() {
        return deleteVideoData;
    }




    public void addVideo(String userId, String title, File videoFile) {
        videoRepository.addVideo(userId, title, videoFile, addVideoData);
    }

    public void updateVideo(String userId,boolean file, String videoId, VideoUpdateRequest updateRequest) {
        videoRepository.updateVideo(userId,file, videoId, updateRequest, updateVideoData);
    }

    public void deleteVideo(String userId, String videoId) {
        videoRepository.deleteVideo(userId, videoId, deleteVideoData);
    }
    public void likeAction(String videoId, LikeActionRequest likeActionRequest) {
        videoRepository.likeAction(videoId, likeActionRequest, likeActionLiveData);
    }

}