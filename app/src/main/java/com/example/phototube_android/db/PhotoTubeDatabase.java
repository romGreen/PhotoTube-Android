//package com.example.phototube_android.db;
//
//
//import android.content.Context;
//
//import androidx.room.Database;
//import androidx.room.Room;
//import androidx.room.RoomDatabase;
//import androidx.room.TypeConverters;
//
//import com.example.phototube_android.classes.Comment;
//import com.example.phototube_android.classes.User;
//import com.example.phototube_android.classes.Video;
//
//
//@Database(entities = {User.class, Video.class, Comment.class}, version = 2, exportSchema = false)
//@TypeConverters(Converters.class)
//public abstract class PhotoTubeDatabase extends RoomDatabase {
//    private static volatile PhotoTubeDatabase INSTANCE;
//
//
//
//    public static PhotoTubeDatabase getDatabase(final Context context) {
//        if (INSTANCE == null) {
//            synchronized (PhotoTubeDatabase.class) {
//                if (INSTANCE == null) {
//                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
//                                    PhotoTubeDatabase.class, "photo_tube_database")
//                            .fallbackToDestructiveMigration()
//                            .build();
//                }
//            }
//        }
//        return INSTANCE;
//    }
//}