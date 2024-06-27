package com.example.phototube_android.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.phototube_android.db.Converters;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "users")
@TypeConverters(Converters.class)
public class User {
    @PrimaryKey
    @NonNull
    @SerializedName("_id")
    private String id;

    private String username;
    private String password;
    private String displayname;
    private String gender;
    private String profileImg; // Changed to String to match Mongoose schema
    private List<String> videoList; // Assuming videoList contains ObjectIds which are strings

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public List<String> getVideoList() {
        return videoList;
    }

    public void setVideoList(List<String> videoList) {
        this.videoList = videoList;
    }
}
