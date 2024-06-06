package com.example.phototube_android.entities;

public class Video {
    private int imageResourceId;
    private String author;
    private String videoName;

    public Video(int imageResourceId, String author, String videoName) {
        this.imageResourceId = imageResourceId;
        this.author = author;
        this.videoName = videoName;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public String getAuthor() {
        return author;
    }

    public String getVideoName() {
        return videoName;
    }

    //public boolean getFilePath() { return videoFile;}
}
