package com.example.phototube_android.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.phototube_android.model.Comment;

import java.util.List;

@Dao
public interface CommentDao {
    @Query("SELECT * FROM comments WHERE videoId = :videoId")
    List<Comment> getCommentsByVideoId(String videoId);

    @Insert
    void insertComment(Comment comment);

    @Update
    void updateComment(Comment comment);

    @Delete
    void deleteComment(Comment comment);
}

