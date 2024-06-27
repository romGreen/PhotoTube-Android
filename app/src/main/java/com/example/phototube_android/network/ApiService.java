package com.example.phototube_android.network;

import com.example.phototube_android.model.Comment;
import com.example.phototube_android.model.User;
import com.example.phototube_android.model.Video;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    @GET("users")
    Call<List<User>> getAllUsers();

    @GET("videos")
    Call<List<Video>> getAllVideos();

    @GET("comments")
    Call<List<Comment>> getAllComments();

    @GET("videos/{id}")
    Call<Video> getVideoById(@Path("id") String videoId);

    // Login validation. Assumes the server can accept POST requests to validate users.
    @POST("/api/users/login/")
    Call<User> validateUser(@Body User user);

    // Update user details. Assumes PUT request is used for updates on the server.
    @PUT("users/{id}")
    Call<User> updateUser(@Path("id") String id, @Body User user);

    @POST("/api/users/")
    Call<User> addUser(@Body User user);

    // Additional methods can be added to manage videos and comments similarly.
}
