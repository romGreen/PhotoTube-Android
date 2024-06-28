package com.example.phototube_android.repository;

import androidx.lifecycle.MutableLiveData;


import com.example.phototube_android.API.VideoInApi;
import com.example.phototube_android.API.VideoOffApi;
import com.example.phototube_android.classes.Video;
import com.example.phototube_android.response.ApiResponse;

import java.util.List;


public class VideoRepository {

    private VideoInApi videoApi;

    public VideoRepository() {

        videoApi = new VideoInApi();
    }

    public void getVideos(MutableLiveData<ApiResponse<List<Video>>> VideoLiveData){
        videoApi.getVideos(VideoLiveData);
    }

}
