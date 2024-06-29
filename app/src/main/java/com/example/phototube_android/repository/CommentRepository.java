//package com.example.phototube_android.repository;
//
//import androidx.lifecycle.MutableLiveData;
//
//
//import com.example.phototube_android.API.CommentInApi;
//import com.example.phototube_android.classes.Comment;
//import com.example.phototube_android.classes.Video;
//import com.example.phototube_android.requests.LikeActionRequest;
//import com.example.phototube_android.requests.VideoUpdateRequest;
//import com.example.phototube_android.response.ApiResponse;
//
//import java.io.File;
//import java.util.List;
//
//
//public class CommentRepository {
//
//    private CommentInApi commentInApi;
//
//    public CommentRepository() {
//        commentInApi = new CommentInApi();
//    }
//
//    public void getComments(MutableLiveData<ApiResponse<List<Comment>>> CommentLiveData){
//        commentInApi.getComments(CommentLiveData);
//    }
//
//    public void getUserComments(String userId, MutableLiveData<ApiResponse<List<Comment>>> CommentLiveData){
//        commentInApi.getUserVideos(userId, CommentLiveData); //continue that in viewmodelin
//    }
//
//    public void getComment(String userId, String videoId,MutableLiveData<ApiResponse<Comment>> CommentLiveData){
//        commentInApi.getComment(userId, videoId, CommentLiveData);
//    }
//
//    public void addComment(String userId, String title, File videoFile, MutableLiveData<ApiResponse<Video>> CommentLiveData){
//        commentInApi.addComment(userId, title, videoFile, CommentLiveData);
//    }
//
//    public void updateComment(String userId, String videoId, VideoUpdateRequest updateRequest,
//                            MutableLiveData<ApiResponse<Video>> CommentLiveData) {
//        commentInApi.updateComment(userId, videoId, updateRequest, CommentLiveData);
//    }
//
//    public void deleteComment(String userId, String videoId, MutableLiveData<ApiResponse<Void>> deleteCommentLiveData){
//        commentInApi.deleteComment(userId, videoId, deleteCommentLiveData);
//    }
//
//    public void likeAction(String userId, String videoId, LikeActionRequest likeRequest,
//                           MutableLiveData<ApiResponse<Video>> likeActionLiveData) {
//        commentInApi.likeVideo(userId, videoId, likeRequest, likeActionLiveData);
//    }
//
//
//}
