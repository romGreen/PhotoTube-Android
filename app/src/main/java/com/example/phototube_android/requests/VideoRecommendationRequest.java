package com.example.phototube_android.requests;

public class VideoRecommendationRequest {
    private String userId;
    private String videoId;

    public VideoRecommendationRequest(String userId, String videoId) {
        this.userId = userId;
        this.videoId = videoId;
    }

    // Getters and setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }
}
