package com.example.phototube_android.API.Server;

import com.example.phototube_android.classes.Comment;
import com.example.phototube_android.requests.CommentRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CommentServerApi {
    @POST("/api/users/{id}/videos/{pid}/comments")
    Call<Comment> addComment(
            @Path("id") String userId,
            @Path("pid") String videoId,
            @Body CommentRequest commentRequest);



}
