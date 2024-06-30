package com.example.phototube_android.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.phototube_android.API.VideoInApi;
import com.example.phototube_android.API.VideoOffApi;
import com.example.phototube_android.classes.Video;
import com.example.phototube_android.response.ApiResponse;

import java.util.List;

public class VideoOffRepository {


    private VideoOffApi videoOffApi;


    public VideoOffRepository() {
        videoOffApi = new VideoOffApi();
    }



    public void getVideos(MutableLiveData<ApiResponse<List<Video>>> VideoLiveData){
        videoOffApi.getVideos(VideoLiveData);
    }

    public void getUserVideos(String userId, MutableLiveData<ApiResponse<List<Video>>> VideoLiveData){
        videoOffApi.getUserVideos(userId, VideoLiveData); //continue that in viewmodelin
    }

    public void getVideo(String userId, String videoId,MutableLiveData<ApiResponse<Video>> VideoLiveData){
        videoOffApi.getVideo(userId, videoId, VideoLiveData);
    }
}
