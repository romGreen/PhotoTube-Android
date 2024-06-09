package com.example.phototube_android.entities;

public class Comment {
    private int userId;
    private String userName;
    private String commentText;
    private int videoId;
    public Comment(int userId, String userName, String commentText, int videoId) {
        this.userId = userId;
        this.userName = userName;
        this.commentText = commentText;
        this.videoId = videoId;

    }


    // Getters and setters
    public int getVideoId() {
        return videoId;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }
}
