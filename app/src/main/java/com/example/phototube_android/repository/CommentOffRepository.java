package com.example.phototube_android.repository;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.example.phototube_android.API.CommentOffApi;
import com.example.phototube_android.classes.Comment;
import com.example.phototube_android.db.AppDB;
import com.example.phototube_android.response.ApiResponse;

import java.util.LinkedList;
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
