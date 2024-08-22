package com.example.phototube_android.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.phototube_android.classes.Video;
import com.google.android.exoplayer2.C;

import java.util.List;

@Dao
public interface VideoDao {



    @Query("SELECT * FROM video")
    List<Video> getAll();
    @Insert
    void insert(Video... videos);


    @Update
    void update(Video... videos);

    @Delete
    void delete(Video... videos);
    @Query("DELETE FROM video")
    void clear();

}
