package com.example.phototube_android.requests;

import java.io.File;

public class VideoUpdateRequest {
    private String title;
    private File video;

    // Constructor, getters and setters
    public VideoUpdateRequest(String title, File videoUrl) {
        this.title = title;
        this.video = videoUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public File getVideoUrl() {
        return video;
    }

    public void setVideoUrl(File videoUrl) {
        this.video = videoUrl;
    }
}
