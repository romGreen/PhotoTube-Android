package com.example.phototube_android.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.phototube_android.model.Video;

import java.util.List;

@Dao
public interface VideoDao {
    @Query("SELECT * FROM videos")
    LiveData<List<Video>> getAllVideos();

    @Insert
    void insertVideo(Video video);

    @Insert
    void insertAll(List<Video> videos);

    @Update
    void updateVideo(Video video);

    @Delete
    void deleteVideo(Video video);
}
