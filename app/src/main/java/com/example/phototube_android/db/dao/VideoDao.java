package com.example.phototube_android.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.phototube_android.classes.Video;


import java.util.List;

@Dao
public interface VideoDao {

    @Query("SELECT * FROM video")
    List<Video> getAll();
    @Insert
    void insert(Video... videos);
    @Query("DELETE FROM video WHERE id = :videoId")
    void deleteById(int videoId);

    @Query("DELETE FROM video WHERE _id = :videoId")
    void deleteByServerId(String videoId);

    // Fetch video by local database-generated ID (int)
    @Query("SELECT * FROM video WHERE id = :videoId LIMIT 1")
    Video getVideoById(int videoId);

    // Fetch video by server-provided ID (String)
    @Query("SELECT * FROM video WHERE _id = :serverId LIMIT 1")
    Video getVideoByServerId(String serverId);


    @Update
    void update(Video... videos);

    @Delete
    void delete(Video... videos);
    @Query("DELETE FROM video")
    void clear();

}
