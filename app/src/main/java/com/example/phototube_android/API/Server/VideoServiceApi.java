package com.example.phototube_android.API.Server;

import com.example.phototube_android.classes.User;
import com.example.phototube_android.classes.Video;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;

public interface VideoServiceApi {
    @GET("/api/videos")
    Call<List<Video>> getVideos();
    @GET("/api/users/{id}/videos")
    Call<List<Video>> getUserVideos();
    @GET("/api/users/{id}/videos/{pid}")
    Call<Video> getVideo(@Body Video video);

    @PATCH("/api/users/{id}/videos/{pid}")
    Call<Video> updateVideo(@Body Video video);

    @DELETE("/api/users/{id}/videos/{pid}")
    Call<Void> deleteVideo(@Body Video video);


}
