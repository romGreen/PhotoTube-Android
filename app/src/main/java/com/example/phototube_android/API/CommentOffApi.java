package com.example.phototube_android.API;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.phototube_android.API.Server.CommentServerApi;
import com.example.phototube_android.classes.Comment;
import com.example.phototube_android.db.dao.CommentDao;
import com.example.phototube_android.response.ApiResponse;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CommentOffApi {

    private static final String BASE_URL = "http://10.0.2.2:1324/";
    private CommentServerApi commentServerApi;
    private CommentDao dao;
    private Retrofit retrofit;
    private MutableLiveData<ApiResponse<List<Comment>>> commentListData;

    public CommentOffApi(CommentDao dao , MutableLiveData<ApiResponse<List<Comment>>> commentListData) {
        this.dao = dao;
this.commentListData =commentListData;
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        commentServerApi = retrofit.create(CommentServerApi.class);
    }

    public void getComments(String videoId) {
        Call<List<Comment>> call = commentServerApi.getCommentsByVideoId(videoId);
        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(@NonNull Call<List<Comment>> call, @NonNull Response<List<Comment>> response) {
                if (response.isSuccessful()) {
                    new Thread(() -> {
                        Log.d("Comment DAO", "Deleting comments for videoId: " + videoId);
                        dao.deleteCommentsByVideoId(videoId); // Clear old comments for this video
                        Log.d("Comment DAO", "Deleted comments for videoId: " + videoId);

                        List<Comment> commentss = dao.getAll();  // or use `getCommentsByVideoId(videoId)`
                        for (Comment comment : commentss) {
                            Log.d("Comment DAO", "Comment ID1: " + comment.getId() + ", Video ID: " + comment.getVideoId() + ", Text: " + comment.getText());
                        }
                        dao.insert(response.body().toArray(new Comment[0])); // Insert new comments from server
                        List<Comment> comments = dao.getCommentsByVideoId(videoId).getValue(); // Fetch updated comments
                        Log.d("Comment DAO", "Fetched comments for videoId: " + videoId);
                        Log.d("Comment DAO", "Fetched " + (comments != null ? comments.size() : 0) + " comments");

                        commentListData.postValue(new ApiResponse<>(
                                comments,
                                "Comments retrieved successfully",
                                true
                        ));
                        List<Comment> commentsss = dao.getAll();  // or use `getCommentsByVideoId(videoId)`
                        for (Comment comment : commentsss) {
                            Log.d("Comment DAO", "Comment ID2: " + comment.getId() + ", Video ID: " + comment.getVideoId() + ", Text: " + comment.getText());
                        }
                    }).start();
                } else {
                    new Thread(() -> {
                        List<Comment> comments = dao.getCommentsByVideoId(videoId).getValue(); // Fetch local comments in case of error
                        commentListData.postValue(new ApiResponse<>(
                                comments,
                                "Failed to retrieve comments from server",
                                false
                        ));
                    }).start();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Comment>> call, @NonNull Throwable t) {
                new Thread(() -> {
                    List<Comment> comments = dao.getCommentsByVideoId(videoId).getValue(); // Fetch local comments in case of network error
                    commentListData.postValue(new ApiResponse<>(
                            comments,
                            "Network error: " + t.getMessage(),
                            false
                    ));
                }).start();
            }
        });
    }
}
