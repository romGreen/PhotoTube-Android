package com.example.phototube_android.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.phototube_android.classes.Comment;
import com.example.phototube_android.classes.User;
import com.example.phototube_android.repository.CommentInRepository;
import com.example.phototube_android.repository.CommentOffRepository;
import com.example.phototube_android.response.ApiResponse;
import com.example.phototube_android.response.TokenResponse;

import java.util.List;

public class CommentOffViewModel extends ViewModel {

    private CommentOffRepository commentOffRepository;
    private MutableLiveData<ApiResponse<List<Comment>>> commentData;
    private MutableLiveData<ApiResponse<TokenResponse>> tokenData;
    public CommentOffViewModel() {
        this.commentOffRepository = new CommentOffRepository();
        commentData = new MutableLiveData<>();

    }

    public MutableLiveData<ApiResponse<List<Comment>>> getCommentData()
    {
        return this.commentData;
    }
    public void getComments(String videoId)
    {
        commentOffRepository.getComments(videoId,commentData);
    }
}