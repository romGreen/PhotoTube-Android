package com.example.phototube_android.repository;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.example.phototube_android.API.CommentOffApi;
import com.example.phototube_android.API.UserLogApi;
import com.example.phototube_android.API.VideoInApi;
import com.example.phototube_android.classes.Comment;
import com.example.phototube_android.classes.Video;
import com.example.phototube_android.db.AppDB;
import com.example.phototube_android.db.dao.CommentDao;
import com.example.phototube_android.db.dao.VideoDao;
import com.example.phototube_android.response.ApiResponse;

import java.util.LinkedList;
import java.util.List;

public class CommentOffRepository {

    private CommentOffApi commentOffApi;
    CommentListData commentListData;
    private CommentDao dao;
    public CommentOffRepository(Application application) {
        AppDB appDB = AppDB.getDatabase(application);
        dao = appDB.commentDao();
        this.commentListData = new CommentListData();
        commentOffApi = new CommentOffApi(dao,commentListData);

    }

    public void getComments(String videoId)
    {
        this.commentOffApi.getComments(videoId);
    }
    public MutableLiveData<ApiResponse<List<Comment>>> getCommentListData()
    {
        return this.commentListData;
    }

    public class CommentListData extends MutableLiveData<ApiResponse<List<Comment>>> {

        public CommentListData() {
            super();
            setValue(new ApiResponse<>(new LinkedList<>(), "", true));
        }

        @Override
        protected void onActive() {
            refreshComments();
        }

        public MutableLiveData<ApiResponse<List<Comment>>> getAllVideos() {
            return commentListData;
        }

        public void refreshComments() {
            new Thread(() -> {
                List<Comment> comments = dao.getAll();
                commentListData.postValue(new ApiResponse<>
                        (comments, "Comments fetched successfully", true));
            }).start();
        }
    }

}
