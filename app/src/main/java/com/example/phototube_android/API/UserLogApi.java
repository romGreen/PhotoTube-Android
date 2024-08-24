package com.example.phototube_android.API;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.phototube_android.API.Server.UserServerApi;
import com.example.phototube_android.classes.User;
import com.example.phototube_android.entities.FileUtils;
import com.example.phototube_android.entities.TokenInterceptor;
import com.example.phototube_android.entities.UserManager;
import com.example.phototube_android.response.ApiResponse;
import com.example.phototube_android.response.MessageResponse;
import com.example.phototube_android.response.UpdateUserResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserLogApi {
    private static final String BASE_URL = "http://10.0.2.2:1324/";
    private UserServerApi userServerApi;
    private Retrofit retrofit;
    private TokenInterceptor tokenInterceptor;

    public UserLogApi(){
        String token = UserManager.getInstance().getToken();
        tokenInterceptor = new TokenInterceptor(token);

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(tokenInterceptor)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        userServerApi = retrofit.create(UserServerApi.class);

    }

    public void getUser(MutableLiveData<ApiResponse<User>> userLiveData) {
        String userId = UserManager.getInstance().getUserId();
        userServerApi.getUser(userId).enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    userLiveData.postValue(new ApiResponse<>(
                            response.body(),
                            "User details retrieved successfully",
                            true
                    ));
                } else {
                    Log.e("UserViewModel", "Error response code: " + response.code());
                    userLiveData.postValue(new ApiResponse<>(
                            null,
                            "Failed to retrieve user details",
                            false
                    ));
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                userLiveData.postValue(new ApiResponse<>(
                        null,
                        "Error: " + t.getMessage(),
                        false
                ));
            }
        });
    }


    public void deleteUser(MutableLiveData<ApiResponse<MessageResponse>> userLiveData) {
        String userId = UserManager.getInstance().getUserId();


        userServerApi.deleteUser(userId).enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(@NonNull Call<MessageResponse> call, @NonNull Response<MessageResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    userLiveData.postValue(new ApiResponse<>(
                            response.body(),
                            "User deleted successfully",
                            true
                    ));
                } else {
                    Log.e("UserLogApi", "Error response code: " + response.code());
                    userLiveData.postValue(new ApiResponse<>(
                            null,
                            "Failed to delete user",
                            false
                    ));
                }
            }

            @Override
            public void onFailure(@NonNull Call<MessageResponse> call, @NonNull Throwable t) {
                userLiveData.postValue(new ApiResponse<>(
                        null,
                        "Error: " + t.getMessage(),
                        false
                ));
            }
        });
    }


    public void updateUser(Context context, User user, boolean file, MutableLiveData<ApiResponse<UpdateUserResponse>> userLiveData) {
        String userId = UserManager.getInstance().getUserId();

        // Validate password if present
        if (user.getPassword() != null && !isValidPassword(user.getPassword())) {
            userLiveData.postValue(new ApiResponse<>(
                    null,
                    "Password must be at least 8 characters long and include letters",
                    false
            ));
            return; // Stop further execution if validation fails
        }

        RequestBody passwordPart = RequestBody.create(MediaType.parse("text/plain"), user.getPassword());
        RequestBody displaynamePart = RequestBody.create(MediaType.parse("text/plain"), user.getDisplayname());
        RequestBody emailPart = RequestBody.create(MediaType.parse("text/plain"), user.getEmail());
        RequestBody genderPart = RequestBody.create(MediaType.parse("text/plain"), user.getGender());

        MultipartBody.Part profileImgPart = null;
        if (file) {
            Uri profileImgUri = Uri.parse(user.getProfileImg());
            File profileImgFile;
            try {
                profileImgFile = FileUtils.getFileFromUri(context, profileImgUri);
                RequestBody profileImgRequestBody = RequestBody.create(profileImgFile, MediaType.parse("image/*"));
                profileImgPart = MultipartBody.Part.createFormData("profileImg", profileImgFile.getName(), profileImgRequestBody);
            } catch (IOException e) {
                e.printStackTrace();
                userLiveData.postValue(new ApiResponse<>(
                        null,
                        "Failed to access image file",
                        false
                ));
                return;
            }
        }

        userServerApi.updateUser(userId, profileImgPart, displaynamePart, emailPart, passwordPart, genderPart).enqueue(new Callback<UpdateUserResponse>() {
            @Override
            public void onResponse(@NonNull Call<UpdateUserResponse> call, @NonNull Response<UpdateUserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    userLiveData.postValue(new ApiResponse<>(
                            response.body(),
                            "User details updated successfully",
                            true
                    ));
                } else {
                    String errorMessage = "Update failed"; // Default error message
                    if (response.errorBody() != null) {
                        try {
                            // Parse the JSON response to get the error message
                            String errorJson = response.errorBody().string();
                            JSONObject jsonObject = new JSONObject(errorJson);
                            errorMessage = jsonObject.getString("message");
                        } catch (IOException | JSONException e) {
                            Log.e("UserLogApi", "Error parsing error message", e);
                        }
                    }
                    userLiveData.postValue(new ApiResponse<>(
                            null,
                            errorMessage,
                            false
                    ));
                }
            }

            @Override
            public void onFailure(@NonNull Call<UpdateUserResponse> call, @NonNull Throwable t) {
                userLiveData.postValue(new ApiResponse<>(
                        null,
                        "Network error: " + t.getMessage(),
                        false
                ));
            }
        });
    }

    // Utility method to validate password
    private boolean isValidPassword(String password) {
        return password.length() >= 8 && password.matches(".*[a-zA-Z]+.*");
    }


    public void getInfoUser(MutableLiveData<ApiResponse<User>> userLiveData) {
        String userId = UserManager.getInstance().getUserId();
        userServerApi.getInfoUser(userId).enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    userLiveData.postValue(new ApiResponse<>(
                            response.body(),
                            "User details retrieved successfully",
                            true
                    ));
                } else {
                    Log.e("UserViewModel", "Error response code: " + response.code());
                    userLiveData.postValue(new ApiResponse<>(
                            null,
                            "Failed to retrieve user details",
                            false
                    ));
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                userLiveData.postValue(new ApiResponse<>(
                        null,
                        "Error: " + t.getMessage(),
                        false
                ));
            }
        });
    }


}

