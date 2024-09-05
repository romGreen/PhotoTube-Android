package com.example.phototube_android.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.phototube_android.API.CommentInApi;
import com.example.phototube_android.API.CommentOffApi;
import com.example.phototube_android.classes.Comment;
import com.example.phototube_android.response.ApiResponse;
import com.example.phototube_android.response.MessageResponse;

public class CommentInRepository {
    private CommentInApi commentInApi;
    public CommentInRepository() {
        commentInApi = new CommentInApi();
    }
    public void addComment(String videoId, String commentText, MutableLiveData<ApiResponse<Comment>> addCommentData)
    {
        commentInApi.addComment(videoId,commentText,addCommentData);
    }
    public void deleteComment(String commentId,MutableLiveData<ApiResponse<MessageResponse>> deleteCommentData)
    {
        commentInApi.deleteComment(commentId,deleteCommentData);
    }
    public void updateComment(String commentId, String commentText, MutableLiveData<ApiResponse<Comment>> updateCommentData)
    {
        commentInApi.updateComment(commentId,commentText,updateCommentData);
    }
}

