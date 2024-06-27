package com.example.phototube_android.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.phototube_android.db.PhotoTubeDatabase;
import com.example.phototube_android.db.dao.UserDao;
import com.example.phototube_android.model.User;
import com.example.phototube_android.network.ApiService;
import com.example.phototube_android.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private UserDao userDao;
    private ApiService apiService;
    private ApiService aa;

    // Define the callback interfaces
    public interface UserValidationCallback {
        void onValidationSuccess(User user);
        void onValidationFailure(Throwable throwable);
    }

    public interface UpdateUserCallback {
        void onUpdateSuccess(User user);
        void onUpdateFailure(Throwable throwable);
    }
    public interface UserAddCallback {
        void onAddSuccess(User newUser);

        void onAddFailure(Exception e);
    }

    public UserRepository(Application application) {
        PhotoTubeDatabase db = PhotoTubeDatabase.getDatabase(application);
        userDao = db.userDao();
        apiService = RetrofitClient.getInstance().create(ApiService.class);
    }

    public LiveData<List<User>> getAllUsers() {
        refreshUsers();
        return userDao.getAllUsers();
    }

    private void refreshUsers() {
        apiService.getAllUsers().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AsyncTask.execute(() -> userDao.insertAll(response.body()));
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                // Log error or handle failure to refresh users
            }
        });
    }

    public void validateUser(String username, String password, UserValidationCallback callback) {
        // This method should make a network or database call and then invoke the callback
        User user = new User(); // Assuming User class has a constructor or setters for username and password
        user.setUsername(username);
        user.setPassword(password);

        apiService.validateUser(user).enqueue(new Callback<User>() {

            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    callback.onValidationSuccess(response.body());
                } else {
                    callback.onValidationFailure(new Exception("Validation failed"));
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                callback.onValidationFailure(t);
            }
        });
    }

    public void updateUser(User user, UpdateUserCallback callback) {
        apiService.updateUser(user.getId(), user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    callback.onUpdateSuccess(response.body());
                } else {
                    callback.onUpdateFailure(new Exception("Update failed"));
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                callback.onUpdateFailure(t);
            }
        });
    }

    public void addUser(User user, UserAddCallback callback) {
        Log.d("UserRepository", "Attempting to add user: " + user.getUsername());
        apiService.addUser(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Log.d("UserRepository", "Add user successful");
                    callback.onAddSuccess(response.body());
                } else {
                    Log.d("UserRepository", "Add user failed: " + response.message());
                    callback.onAddFailure(new Exception("Add user failed: " + response.message()));
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("UserRepository", "Add user API call failed", t);
                callback.onAddFailure((Exception) t);
            }
        });
    }

}
