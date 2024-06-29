package com.example.phototube_android.classes;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

@Entity(tableName = "comments")
public class Comment {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @SerializedName("_id")
    private String _id;

    @SerializedName("text")
    private String text;

    @SerializedName("createdBy")
    private String createdBy; // User id
    @SerializedName("videoId")
    private String videoId;
    @SerializedName("date")
    private Date date;

    @SerializedName("userProfileImg")
    private String userProfileImg;

    @SerializedName("username")
    private String username;

    // Getters and Setters

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String get_id() {
        return _id;
    }

    public void set_id(@NonNull String _id) {
        this._id = _id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getUserProfileImg() {
        return userProfileImg;
    }

    public void setUserProfileImg(String userProfileImg) {
        this.userProfileImg = userProfileImg;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
