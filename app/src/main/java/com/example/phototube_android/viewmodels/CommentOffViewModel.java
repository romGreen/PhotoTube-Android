package com.example.phototube_android.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.phototube_android.classes.Comment;
import com.example.phototube_android.classes.User;
import com.example.phototube_android.repository.CommentInRepository;
import com.example.phototube_android.repository.CommentOffRepository;
import com.example.phototube_android.response.ApiResponse;
import com.example.phototube_android.response.TokenResponse;

import java.util.List;

public class CommentOffViewModel extends AndroidViewModel {

    private CommentOffRepository commentOffRepository;
    private MutableLiveData<ApiResponse<List<Comment>>> commentData;

    public CommentOffViewModel(Application application) {
        super(application);
        this.commentOffRepository = new CommentOffRepository(application);
        commentData = this.commentOffRepository.getCommentListData();

    }

    public MutableLiveData<ApiResponse<List<Comment>>> getCommentData()
    {
        return this.commentData;
    }
    public void getComments(String videoId)
    {
        commentOffRepository.getComments(videoId);
    }
}