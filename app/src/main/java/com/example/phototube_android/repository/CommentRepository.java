package com.example.phototube_android.repository;

import android.app.Application;

import com.example.phototube_android.db.PhotoTubeDatabase;
import com.example.phototube_android.db.dao.CommentDao;
import com.example.phototube_android.network.ApiService;
import com.example.phototube_android.network.RetrofitClient;

public class CommentRepository {
    private CommentDao commentDao;
    private ApiService apiService;

    public CommentRepository(Application application) {
        PhotoTubeDatabase db = PhotoTubeDatabase.getDatabase(application);
        commentDao = db.commentDao();
        apiService = RetrofitClient.getInstance().create(ApiService.class);
    }

    // Add methods for database and network operations
}
