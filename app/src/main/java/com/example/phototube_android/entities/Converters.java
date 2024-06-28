package com.example.phototube_android.entities;

// Converters.java
import androidx.room.TypeConverter;

import com.example.phototube_android.classes.Video;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;

public class Converters {
    @TypeConverter
    public static String fromLikeList(List<Video.Like> likes) {
        if (likes == null) {
            return null;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Video.Like>>() {}.getType();
        return gson.toJson(likes, type);
    }

    @TypeConverter
    public static List<Video.Like> toLikeList(String likesString) {
        if (likesString == null) {
            return null;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Video.Like>>() {}.getType();
        return gson.fromJson(likesString, type);
    }
}