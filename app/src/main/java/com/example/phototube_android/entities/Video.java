package com.example.phototube_android.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Video {
    private  int likes;
    private String views;
    private String timeAgo;
    private int id;
    private String name;
    private String author;
    private String imagePath;
    private String videoPath;  // Resource ID for the video file
    private List<Comment> comments;
    private Map<String, Integer> usersReactions; // (userName, 1/0/-1)

    // Constructor
    public Video(int id, String name, String author, String views, String timeAgo, String imagePath, String videoPath) {
        this.name = name;
        this.author = author;
        this.views = views;
        this.timeAgo = timeAgo;
        this.imagePath = imagePath;
        this.videoPath = videoPath;
        this.id = id;
        this.comments = CommentListManager.getInstance().getCommentsForVideo(id);
        this.usersReactions = new HashMap<>();
        this.likes = 0;
    }
    public void addLike(String userName) {
        usersReactions.put(userName, 1);
        likes += 1;
    }

    public boolean isLikedBy(String userName) {
        return usersReactions.getOrDefault(userName, 0) == 1;
    }

    public void removeLike(String userName) {
        if (isLikedBy(userName)) {
            usersReactions.put(userName, 0);
            likes -= 1;
        }
    }

    public void addDislike(String userName) {
        usersReactions.put(userName, -1);
        likes -= 1;

    }

    public boolean isDislikedBy(String userName) {
        return usersReactions.getOrDefault(userName, 0) == -1;
    }

    public void removeDislike(String userName) {
        if (isDislikedBy(userName)) {
            usersReactions.put(userName, 0);
            likes += 1;
        }
    }

    public int getLikes() {
        return likes;
    }




    public void setUsersReaction(String userName, int reaction) {
        usersReactions.put(userName, reaction);
    }


    public Map<String, Integer> getUsersReactions() {
        return usersReactions;
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

    public void setVideoName(String name) {
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

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String getTimeAgo() {
        return timeAgo;
    }

    public void setTimeAgo(String timeAgo) {
        this.timeAgo = timeAgo;
    }
}
