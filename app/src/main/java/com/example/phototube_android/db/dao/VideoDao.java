package com.example.phototube_android.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.phototube_android.classes.Video;

import java.util.List;

@Dao
public interface VideoDao {

    // Insert a new video
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertVideo(Video video);

    // Update an existing video
    @Update
    void updateVideo(Video video);

    // Delete a video
    @Delete
    void deleteVideo(Video video);

    // Get a single video by its MongoDB _id
    @Query("SELECT * FROM Video WHERE _id = :videoId")
    LiveData<Video> getVideoById(String videoId);

    // Get all videos
    @Query("SELECT * FROM Video")
    LiveData<List<Video>> getAllVideos();

    // Get videos by user ID
    @Query("SELECT * FROM Video WHERE userId = :userId")
    LiveData<List<Video>> getVideosByUserId(String userId);

    // Search videos by title
    @Query("SELECT * FROM Video WHERE title LIKE '%' || :searchQuery || '%'")
    LiveData<List<Video>> searchVideosByTitle(String searchQuery);

    // Get videos based on views threshold
    @Query("SELECT * FROM Video WHERE views >= :minViews")
    LiveData<List<Video>> getPopularVideos(int minViews);
}
