package com.example.phototube_android.API;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.phototube_android.API.Server.UserServerApi;
import com.example.phototube_android.classes.User;
import com.example.phototube_android.entities.UserManager;
import com.example.phototube_android.requests.LoginRequest;
import com.example.phototube_android.response.ApiResponse;
import com.example.phototube_android.response.TokenResponse;
import com.example.phototube_android.response.isExistResponse;


import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.Response;
import retrofit2.Callback;
import retrofit2.converter.gson.GsonConverterFactory;


public class UserApi {
    private static final String BASE_URL = "http://10.0.2.2:1324/";
    private UserServerApi userServerApi;

    private Retrofit retrofit;

    public UserApi()
    {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        userServerApi = retrofit.create(UserServerApi.class);

    }

    public void addUser(User user,MutableLiveData<ApiResponse<User>> registerLiveData){
        userServerApi.addUser(user).enqueue(new Callback<User>(){
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
            if (response.isSuccessful() && response.body() != null) {
                registerLiveData.postValue(new ApiResponse<>
                        (response.body(), "User register successfully", true));
            } else {
                registerLiveData.postValue(new ApiResponse<>
                        (null, "Username exists", false));
            }
        }
        @Override
        public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
            registerLiveData.postValue(new ApiResponse<>
                    (null, "Error: " + t.getMessage(), false));
            }
        });
    }



    public void loginUser(LoginRequest loginRequest, MutableLiveData<ApiResponse<TokenResponse>> tokenLiveData)
    {
        userServerApi.login(loginRequest).enqueue(new Callback<TokenResponse>(){
            @Override
            public void onResponse(@NonNull Call<TokenResponse> call, @NonNull Response<TokenResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    tokenLiveData.postValue(new ApiResponse<>
                            (response.body(), "User login successfully", true));
                } else {
                    tokenLiveData.postValue(new ApiResponse<>
                            (null, "Password or username not good", false));
                }
            }
            @Override
            public void onFailure(@NonNull Call<TokenResponse> call, @NonNull Throwable t) {
                tokenLiveData.postValue(new ApiResponse<>
                        (null, "Error: " + t.getMessage(), false));
            }
        });
    }

//    public void getUser(MutableLiveData<ApiResponse<User>> userLiveData){
//        String token = UserManager.getInstance().getToken();
//        String userId = UserManager.getInstance().getUserId();
//        userServerApi.getUser(userId,"Bearer " + token).enqueue(new Callback<User>(){
//            @Override
//            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    userLiveData.postValue(new ApiResponse<>
//                            (response.body(), "User Info gets", true));
//                } else {
//                    userLiveData.postValue(new ApiResponse<>
//                            (null, "Error in getting userInfo", false));
//                }
//            }
//            @Override
//            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
//                userLiveData.postValue(new ApiResponse<>
//                        (null, "Error: " + t.getMessage(), false));
//            }
//        });
//    }


//
//    public void updateUser()
//    {}
//    public void deleteUser()
//    {}

}
