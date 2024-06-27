package com.example.phototube_android.ui.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.phototube_android.model.User;
import com.example.phototube_android.repository.UserRepository;

import java.util.List;


    public class UserViewModel extends ViewModel {
        private UserRepository userRepository;
        private LiveData<List<User>> allUsers;
        private MutableLiveData<User> currentUser;

        public UserViewModel(UserRepository userRepository) {
            this.userRepository = userRepository;
            allUsers = userRepository.getAllUsers();
            currentUser = new MutableLiveData<>();
        }

        public LiveData<List<User>> getAllUsers() {
            return allUsers;
        }

        public LiveData<User> getCurrentUser() {
            Log.d("UserViewModel", "Returning currentUser LiveData");

            return currentUser;
        }

        public void login(String username, String password) {
            userRepository.validateUser(username, password, new UserRepository.UserValidationCallback() {
                @Override
                public void onValidationSuccess(User user) {
                    currentUser.postValue(user); // Correctly post value asynchronously
                }

                @Override
                public void onValidationFailure(Throwable t) {
                    // Handle login failure, such as posting a null or a specific error message
                    currentUser.postValue(null);
                }
            });
        }

        public void logout() {
            currentUser.setValue(null); // Clear current user on logout
        }

        public void updateUser(User user) {
            userRepository.updateUser(user, new UserRepository.UpdateUserCallback() {
                @Override
                public void onUpdateSuccess(User updatedUser) {
                    currentUser.postValue(updatedUser); // Update LiveData if user data changes
                }

                @Override
                public void onUpdateFailure(Throwable t) {
                    // Handle update failure
                }
            });
        }

        public void addUser(User user) {
            userRepository.addUser(user, new UserRepository.UserAddCallback() {
                @Override
                public void onAddSuccess(User newUser) {
                    Log.d("UserViewModel", "Add user successful, updating currentUser");
                    currentUser.postValue(newUser);
                }

                @Override
                public void onAddFailure(Exception e) {
                    Log.d("UserViewModel", "Add user failed");

                    // Handle the error, maybe post a null or specific error value to currentUser
                }
            });
        }

        public boolean isLoggedIn() {
            return currentUser.getValue() != null;
        }
    }
