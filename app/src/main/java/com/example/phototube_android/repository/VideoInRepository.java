package com.example.phototube_android.repository;

import androidx.lifecycle.MutableLiveData;


import com.example.phototube_android.API.VideoInApi;
import com.example.phototube_android.classes.Video;
import com.example.phototube_android.requests.LikeActionRequest;
import com.example.phototube_android.requests.VideoUpdateRequest;
import com.example.phototube_android.response.ApiResponse;

import java.io.File;
import java.util.List;


public class VideoInRepository {

    private VideoInApi videoInApi;


    public VideoInRepository() {
        videoInApi = new VideoInApi();
    }


    public void addVideo(String userId, String title, File videoFile, MutableLiveData<ApiResponse<Video>> VideoLiveData){
        videoInApi.addVideo(userId, title, videoFile, VideoLiveData);
    }

    public void updateVideo(String userId,boolean file, String videoId, VideoUpdateRequest updateRequest,
                            MutableLiveData<ApiResponse<Video>> videoLiveData) {
        videoInApi.updateVideo(userId,file, videoId, updateRequest, videoLiveData);
    }

    public void deleteVideo(String userId, String videoId, MutableLiveData<ApiResponse<Void>> deleteVideoLiveData){
        videoInApi.deleteVideo(userId, videoId, deleteVideoLiveData);
    }

    public void likeAction(String videoId, LikeActionRequest likeRequest, MutableLiveData<ApiResponse<Video>> likeActionLiveData) {
        videoInApi.likeVideo(videoId, likeRequest, likeActionLiveData);
    }


}
