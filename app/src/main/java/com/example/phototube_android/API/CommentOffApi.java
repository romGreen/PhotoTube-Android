package com.example.phototube_android.API;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.phototube_android.API.Server.CommentServerApi;
import com.example.phototube_android.classes.Comment;
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
    private Retrofit retrofit;
    private MutableLiveData<ApiResponse<List<Comment>>> commentListData;

    public CommentOffApi() {
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

    public void getComments(String videoId, MutableLiveData<ApiResponse<List<Comment>>> commentLiveData) {


        Call<List<Comment>> call = commentServerApi.getCommentsByVideoId(videoId);
        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(@NonNull Call<List<Comment>> call, @NonNull Response<List<Comment>> response) {
                if (response.isSuccessful()) {
                    // If the request is successful, post the new comment details back to the LiveData
                    commentLiveData.postValue(new ApiResponse<>(response.body(), "Comment added successfully", true));
                } else {
                    // If the server response is not successful, post an error message
                    commentLiveData.postValue(new ApiResponse<>(null, "Failed to add comment: " + response.code(), false));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Comment>> call, @NonNull Throwable t) {
                // Handle the case where the network request itself fails
                commentLiveData.postValue(new ApiResponse<>(null, "Network error: " + t.getMessage(), false));
            }
        });

    }
}
