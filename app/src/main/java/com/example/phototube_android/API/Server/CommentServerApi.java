package com.example.phototube_android.API.Server;

import com.example.phototube_android.classes.Comment;
import com.example.phototube_android.requests.CommentRequest;
import com.example.phototube_android.response.MessageResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CommentServerApi {
    @POST("/api/users/{id}/videos/{pid}/comments")
    Call<Comment> addComment(
            @Path("id") String userId,
            @Path("pid") String videoId,
            @Body CommentRequest commentRequest);

    @GET("/api/users/{pid}/comments")
    Call<List<Comment>> getCommentsByVideoId(@Path("pid") String videoId);

    @DELETE("/api/users/{id}/comments")
    Call<MessageResponse> deleteComment(@Path("id") String commentId);
    @PATCH("/api/users/{id}/comments")
    Call<Comment> updateComment(@Path("id") String commentId, @Body CommentRequest commentRequest);




}
