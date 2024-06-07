package com.example.phototube_android.entities;
public class Video {
    private String name;
    private String author;
    private String imagePath;
    private String videoPath;  // Resource ID for the video file

    // Constructor
    public Video(String name, String author, String imagePath, String videoPath) {
        this.name = name;
        this.author = author;
        this.imagePath = imagePath;
        this.videoPath = videoPath;
    }

    // Getter and setter methods
    public String getVideoName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }
}
