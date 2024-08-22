package com.example.phototube_android.entities;
import androidx.room.TypeConverter;

import com.example.phototube_android.classes.Video;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

public class Converters {

    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static List<String> fromStringList(String value) {
        Type listType = new TypeToken<List<String>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromStringList(List<String> list) {
        return new Gson().toJson(list);
    }

    @TypeConverter
    public static List<Video.Like> fromLikeList(String value) {
        Type listType = new TypeToken<List<Video.Like>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromLikeList(List<Video.Like> list) {
        return new Gson().toJson(list);
    }
}
