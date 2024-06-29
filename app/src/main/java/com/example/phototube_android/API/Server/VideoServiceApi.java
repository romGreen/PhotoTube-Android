package com.example.phototube_android.API.Server;

import com.example.phototube_android.classes.Video;
import com.example.phototube_android.requests.LikeActionRequest;
import com.example.phototube_android.requests.VideoUpdateRequest;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface VideoServiceApi {
    @GET("/api/videos")
    Call<List<Video>> getVideos();
    @GET("/api/users/{id}/videos")
    Call<List<Video>> getUserVideos(@Path("id") String userId);
    @GET("/api/users/{id}/videos/{pid}")
    Call<Video> getVideo(@Path("id") String userId, @Path("pid") String videoId);

    @Multipart
    @POST("/api/users/{id}/videos")
    Call<Video> addVideo(
            @Path("id") String userId,
            @Part("title") RequestBody title,
            @Part MultipartBody.Part videoFile
    );

    @Multipart
    @PATCH("/api/users/{id}/videos/{pid}")
    Call<Video> updateVideo(@Path("id") String userId, @Path("pid") String videoId,
                            @Part("title") RequestBody title,
                            @Part MultipartBody.Part video);

    @DELETE("/api/users/{id}/videos/{pid}")
    Call<Void> deleteVideo(@Path("id") String userId, @Path("pid") String videoId);

    @PATCH("/api/users/{id}/videos/{pid}/like")
    Call<Video> likeAction(@Path("id") String userId, @Path("pid") String videoId,
                            @Body LikeActionRequest likeRequest);

}
