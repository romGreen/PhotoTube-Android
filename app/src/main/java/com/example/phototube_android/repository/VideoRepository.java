package com.example.phototube_android.repository;

import androidx.lifecycle.MutableLiveData;


import com.example.phototube_android.API.VideoInApi;
import com.example.phototube_android.classes.Video;
import com.example.phototube_android.requests.LikeActionRequest;
import com.example.phototube_android.requests.VideoUpdateRequest;
import com.example.phototube_android.response.ApiResponse;

import java.io.File;
import java.util.List;


public class VideoRepository {

    private VideoInApi videoInApi;


    public VideoRepository() {
        videoInApi = new VideoInApi();
    }

    public void getVideos(MutableLiveData<ApiResponse<List<Video>>> VideoLiveData){
        videoInApi.getVideos(VideoLiveData);
    }

    public void getUserVideos(String userId, MutableLiveData<ApiResponse<List<Video>>> VideoLiveData){
        videoInApi.getUserVideos(userId, VideoLiveData); //continue that in viewmodelin
    }

    public void getVideo(String userId, String videoId,MutableLiveData<ApiResponse<Video>> VideoLiveData){
        videoInApi.getVideo(userId, videoId, VideoLiveData);
    }

    public void addVideo(String userId, String title, File videoFile, MutableLiveData<ApiResponse<Video>> VideoLiveData){
        videoInApi.addVideo(userId, title, videoFile, VideoLiveData);
    }

    public void updateVideo(String userId, String videoId, VideoUpdateRequest updateRequest,
                            MutableLiveData<ApiResponse<Video>> videoLiveData) {
        videoInApi.updateVideo(userId, videoId, updateRequest, videoLiveData);
    }

    public void deleteVideo(String userId, String videoId, MutableLiveData<ApiResponse<Void>> deleteVideoLiveData){
        videoInApi.deleteVideo(userId, videoId, deleteVideoLiveData);
    }

    public void likeAction(String videoId, LikeActionRequest likeRequest, MutableLiveData<ApiResponse<Video>> likeActionLiveData) {
        videoInApi.likeVideo(videoId, likeRequest, likeActionLiveData);
    }


}
