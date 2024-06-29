package com.example.phototube_android.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.phototube_android.API.CommentOffApi;
import com.example.phototube_android.API.UserLogApi;
import com.example.phototube_android.API.VideoInApi;
import com.example.phototube_android.classes.Comment;
import com.example.phototube_android.response.ApiResponse;

import java.util.List;

public class CommentOffRepository {

    private CommentOffApi commentOffApi;
    public CommentOffRepository() {

        commentOffApi = new CommentOffApi();
    }

    public void getComments(String videoId, MutableLiveData<ApiResponse<List<Comment>>> commentLiveData)
    {
        this.commentOffApi.getComments(videoId,commentLiveData);
    }

}
