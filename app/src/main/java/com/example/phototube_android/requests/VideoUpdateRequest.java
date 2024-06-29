package com.example.phototube_android.requests;

public class VideoUpdateRequest {
    private String title;
    private String videoUrl;

    // Constructor, getters and setters
    public VideoUpdateRequest(String title, String videoUrl) {
        this.title = title;
        this.videoUrl = videoUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
