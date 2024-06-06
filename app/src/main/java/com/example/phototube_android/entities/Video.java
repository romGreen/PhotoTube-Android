package com.example.phototube_android.entities;
public class Video {
    private String name;
    private String author;
    private int imageResourceId;
    private int videoResourceId;  // Resource ID for the video file

    // Constructor
    public Video(String name, String author, int imageResourceId, int videoResourceId) {
        this.name = name;
        this.author = author;
        this.imageResourceId = imageResourceId;
        this.videoResourceId = videoResourceId;
    }

    // Getter and setter methods
    public String getVideoName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public int getVideoResourceId() {
        return videoResourceId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setImageResourceId(int imageResourceId) {
        this.imageResourceId = imageResourceId;
    }

    public void setVideoResourceId(int videoResourceId) {
        this.videoResourceId = videoResourceId;
    }
}
