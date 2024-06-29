package com.example.phototube_android.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.phototube_android.classes.Comment;
import com.example.phototube_android.classes.User;
import com.example.phototube_android.repository.CommentInRepository;
import com.example.phototube_android.repository.UserRepository;
import com.example.phototube_android.response.ApiResponse;
import com.example.phototube_android.response.MessageResponse;
import com.example.phototube_android.response.TokenResponse;

public class CommentInViewModel extends ViewModel {

    private CommentInRepository commentInRepository;
    private MutableLiveData<ApiResponse<Comment>> addCommentData;
    private MutableLiveData<ApiResponse<MessageResponse>> deleteCommentData;
    private MutableLiveData<ApiResponse<Comment>> updateCommentData;
    public CommentInViewModel() {
        this.commentInRepository = new CommentInRepository();
        this.addCommentData = new MutableLiveData<>();
        this.deleteCommentData = new MutableLiveData<>();
        this.updateCommentData = new MutableLiveData<>();
    }

    public MutableLiveData<ApiResponse<Comment>> getAddCommentData(){return this.addCommentData;}
    public MutableLiveData<ApiResponse<MessageResponse>> getDeleteCommentData(){return this.deleteCommentData;}
    public MutableLiveData<ApiResponse<Comment>> getUpdateCommentData(){return this.updateCommentData;}

    public void addComment(String videoId,String commentText)
    {
        commentInRepository.addComment(videoId,commentText,addCommentData);
    }
    public void deleteComment(String commentId)
    {
        commentInRepository.deleteComment(commentId,deleteCommentData);
    }

    public void updateComment(String commentId,String commentText)
    {
        commentInRepository.updateComment(commentId,commentText,updateCommentData);
    }



}
