package com.example.phototube_android.classes;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.phototube_android.entities.Converters;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity
@TypeConverters(Converters.class)
public class User {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @SerializedName("_id")
    private String _id;
    @SerializedName("username")
    private String username;
    @SerializedName("password")
    private String password;
    @SerializedName("displayname")
    private String displayname;

    @SerializedName("email")
    private String email;
    @SerializedName("gender")
    private String gender;
    @SerializedName("profileImg")
    private String profileImg; // Changed to String to match Mongoose schema

    @SerializedName("videoList")
    private List<Video> videoList; // Assuming videoList contains ObjectIds which are strings

    // Getters and Setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String userId) {
        this._id = userId;
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
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public List<Video> getVideoList() {
        return videoList;
    }

    public void setVideoList(List<Video> videoList) {
        this.videoList = videoList;
    }
}
