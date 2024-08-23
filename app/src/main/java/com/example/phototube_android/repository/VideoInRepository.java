package com.example.phototube_android.repository;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.example.phototube_android.API.VideoInApi;
import com.example.phototube_android.classes.Video;
//import com.example.phototube_android.db.AppDB;
import com.example.phototube_android.db.AppDB;
import com.example.phototube_android.db.dao.VideoDao;
import com.example.phototube_android.requests.LikeActionRequest;
import com.example.phototube_android.requests.VideoUpdateRequest;
import com.example.phototube_android.response.ApiResponse;

import java.io.File;
import java.util.LinkedList;
import java.util.List;


public class VideoInRepository {

    private VideoInApi videoInApi;
    private VideoDao dao;
   VideoListData videoListData;

    public VideoInRepository(Application application) {

        AppDB appDB = AppDB.getDatabase(application);
        dao = appDB.videoDao();
        videoListData = new VideoListData();
        videoInApi = new VideoInApi(dao,videoListData);
    }
    public Video getVideoByServerId(String videoId) {
        return dao.getVideoByServerId(videoId);
    }

    public void addVideo(String userId, String title, File videoFile){
        videoInApi.addVideo(userId, title, videoFile);
    }

    public void updateVideo(String userId,boolean file, String videoId, VideoUpdateRequest updateRequest) {
        videoInApi.updateVideo(userId,file, videoId, updateRequest);
    }

    public void deleteVideo(String userId, String videoId){
        videoInApi.deleteVideo(userId, videoId);
    }

    public void likeAction(String videoId, LikeActionRequest likeRequest, MutableLiveData<ApiResponse<Video>> likeActionLiveData) {
        videoInApi.likeVideo(videoId, likeRequest,likeActionLiveData);
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
                List<Video> videos = dao.getAll();
                videoListData.postValue(new ApiResponse<>
                        (videos, "Videos fetched successfully", true));
            }).start();
        }
    }


}
