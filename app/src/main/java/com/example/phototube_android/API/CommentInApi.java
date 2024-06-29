package com.example.phototube_android.API;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.phototube_android.API.Server.CommentServerApi;
import com.example.phototube_android.API.Server.VideoServiceApi;
import com.example.phototube_android.classes.Comment;
import com.example.phototube_android.entities.TokenInterceptor;
import com.example.phototube_android.entities.UserManager;
import com.example.phototube_android.requests.CommentRequest;
import com.example.phototube_android.response.ApiResponse;
import com.example.phototube_android.response.MessageResponse;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CommentInApi {
    private static final String BASE_URL = "http://10.0.2.2:1324/";
    private CommentServerApi commentServerApi;
    private Retrofit retrofit;
    private TokenInterceptor tokenInterceptor;

    public CommentInApi(){
        String token = UserManager.getInstance().getToken();
        tokenInterceptor = new TokenInterceptor(token);

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(tokenInterceptor)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        commentServerApi = retrofit.create(CommentServerApi.class);
    }

    public void addComment(String videoId, String commentText, MutableLiveData<ApiResponse<Comment>> commentLiveData) {
        CommentRequest commentRequest = new CommentRequest(commentText);
        String userId = UserManager.getInstance().getUserId();
        Call<Comment> call = commentServerApi.addComment(userId, videoId, commentRequest);
        call.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(@NonNull Call<Comment> call, @NonNull Response<Comment> response) {
                if (response.isSuccessful()) {
                    // If the request is successful, post the new comment details back to the LiveData
                    commentLiveData.postValue(new ApiResponse<>(response.body(), "Comment added successfully", true));
                } else {
                    // If the server response is not successful, post an error message
                    commentLiveData.postValue(new ApiResponse<>(null, "Failed to add comment: " + response.code(), false));
                }
            }

            @Override
            public void onFailure(@NonNull Call<Comment> call, @NonNull Throwable t) {
                // Handle the case where the network request itself fails
                commentLiveData.postValue(new ApiResponse<>(null, "Network error: " + t.getMessage(), false));
            }
        });
    }


    public void deleteComment(String commentId, MutableLiveData<ApiResponse<MessageResponse>> commentLiveData) {
        Call<MessageResponse> call = commentServerApi.deleteComment(commentId);
        call.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(@NonNull Call<MessageResponse> call, @NonNull Response<MessageResponse> response) {
                if (response.isSuccessful()) {
                    // If the request is successful, post the new comment details back to the LiveData
                    commentLiveData.postValue(new ApiResponse<>(response.body(), "Comment deleted successfully", true));
                } else {
                    // If the server response is not successful, post an error message
                    commentLiveData.postValue(new ApiResponse<>(null, "Failed to delete comment: " + response.code(), false));
                }
            }

            @Override
            public void onFailure(@NonNull Call<MessageResponse> call, @NonNull Throwable t) {
                // Handle the case where the network request itself fails
                commentLiveData.postValue(new ApiResponse<>(null, "Network error: " + t.getMessage(), false));
            }
        });
    }
    public void updateComment(String commentId, String commentText, MutableLiveData<ApiResponse<Comment>> commentLiveData) {
        CommentRequest commentRequest = new CommentRequest(commentText);
        Call<Comment> call = commentServerApi.updateComment(commentId, commentRequest);
        call.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(@NonNull Call<Comment> call, @NonNull Response<Comment> response) {
                if (response.isSuccessful()) {
                    // If the request is successful, post the new comment details back to the LiveData
                    commentLiveData.postValue(new ApiResponse<>(response.body(), "Comment updated successfully", true));
                } else {
                    // If the server response is not successful, post an error message
                    commentLiveData.postValue(new ApiResponse<>(null, "Failed to update comment: " + response.code(), false));
                }
            }

            @Override
            public void onFailure(@NonNull Call<Comment> call, @NonNull Throwable t) {
                // Handle the case where the network request itself fails
                commentLiveData.postValue(new ApiResponse<>(null, "Network error: " + t.getMessage(), false));
            }
        });
    }
}
