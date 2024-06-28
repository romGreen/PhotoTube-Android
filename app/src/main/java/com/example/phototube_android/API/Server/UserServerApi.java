package com.example.phototube_android.API.Server;

import com.example.phototube_android.classes.User;

import com.example.phototube_android.requests.LoginRequest;
import com.example.phototube_android.response.TokenResponse;
import com.example.phototube_android.response.isExistResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserServerApi {
    @POST("/api/users")
    Call<User> addUser(@Body User user);
    @POST("/api/users/login")
    Call<TokenResponse> login(@Body LoginRequest loginRequest);

    @GET("/api/users/{id}")
    Call<User> getUser(@Path("id") String id);

    @PATCH("/api/users/{id}")
    Call<User> updateUser(@Body User user);

    @DELETE("/api/users/{id}")
    Call<Void> deleteUser(@Body User user);
}
