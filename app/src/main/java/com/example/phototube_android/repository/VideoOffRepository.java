package com.example.phototube_android.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.phototube_android.API.VideoInApi;
import com.example.phototube_android.API.VideoOffApi;
import com.example.phototube_android.classes.Video;
import com.example.phototube_android.db.AppDB;
import com.example.phototube_android.db.dao.VideoDao;
import com.example.phototube_android.response.ApiResponse;

import java.util.LinkedList;
import java.util.List;

public class VideoOffRepository {


    private VideoOffApi videoOffApi;
    private VideoDao dao;
    VideoListData videoListData;
    public VideoOffRepository(Application application) {

        AppDB appDB = AppDB.getDatabase(application);
        dao = appDB.videoDao();
        videoOffApi = new VideoOffApi(dao);
        videoListData = new VideoListData();
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



    public MutableLiveData<ApiResponse<List<Video>>> getVideoListData() {
        return videoListData;
    }

    public class VideoListData extends MutableLiveData<ApiResponse<List<Video>>> {

        public VideoListData() {
            super();
            setValue(new ApiResponse<>(new LinkedList<>(), "", true));
        }

        @Override
        protected void onActive() {
            refreshVideos();
        }

        public MutableLiveData<ApiResponse<List<Video>>> getAllVideos() {
            return videoListData;
        }

        public void refreshVideos() {
            new Thread(() -> {
                List<Video> posts = dao.getAll();
                videoListData.postValue(new ApiResponse<>
                        (posts, "Posts fetched successfully", true));
            }).start();
        }
    }

}
