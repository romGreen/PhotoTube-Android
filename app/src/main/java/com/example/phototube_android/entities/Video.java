package com.example.phototube_android.entities;

import java.util.ArrayList;
import java.util.List;

public class Video {
    private int id;
    private String name;
    private String author;
    private String imagePath;
    private String videoPath;  // Resource ID for the video file
    private List<Comment> comments;


    // Constructor
    public Video(int id, String name, String author, String imagePath, String videoPath) {
        this.name = name;
        this.author = author;
        this.imagePath = imagePath;
        this.videoPath = videoPath;
        this.id = id;
        this.comments = CommentListManager.getInstance().getCommentsForVideo(id);

    }
    public List<Comment> getComments() {
        return comments;
    }


    public void setId (int id) {
        this.id = id;
    }
    public int getId() {
        return id;
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
