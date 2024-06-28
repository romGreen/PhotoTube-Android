package com.example.phototube_android.classes;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.phototube_android.entities.Converters;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

@Entity
@TypeConverters(Converters.class)

public class Video {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @SerializedName("_id")
    private String _id;

    @SerializedName("title")
    private String title;

    @SerializedName("views")
    private int views;

    @SerializedName("likes")
    private List<Like> likes;

    @SerializedName("date")
    private Date date;

    @SerializedName("videoUrl")
    private String videoUrl;

    @SerializedName("imageUrl")
    private String imageUrl;

    @SerializedName("createdBy")
    private String createdBy;

    @SerializedName("comments")
    private List<String> comments;


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public List<Like> getLikes() {
        return likes;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    // Getters and setters

    public static class Like {
        @SerializedName("userId")
        private String userId;

        @SerializedName("action")
        private String action;

        public Like(String userId, String action) {
            this.userId = userId;
            this.action = action;
        }
        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

    }
}
