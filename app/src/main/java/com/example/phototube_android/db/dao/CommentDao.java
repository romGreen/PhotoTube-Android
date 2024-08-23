package com.example.phototube_android.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.phototube_android.classes.Comment;

import java.util.List;

@Dao
public interface CommentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Comment... comments);

    @Delete
    void delete(Comment comment);

    @Query("SELECT * FROM Comment WHERE videoId = :videoId")
    LiveData<List<Comment>> getCommentsByVideoId(String videoId);

    @Query("DELETE FROM Comment WHERE videoId = :videoId")
    void deleteCommentsByVideoId(String videoId);

    @Query("SELECT * FROM Comment")
    List<Comment> getAll();

    @Query("DELETE FROM Comment")
    void clear();
}
