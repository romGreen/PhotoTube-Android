package com.example.phototube_android.db;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import com.example.phototube_android.classes.Comment;
import com.example.phototube_android.classes.User;
import com.example.phototube_android.classes.Video;
import com.example.phototube_android.db.dao.CommentDao;
import com.example.phototube_android.db.dao.VideoDao;
import com.example.phototube_android.entities.Converters;

@Database(entities = {User.class, Video.class, Comment.class}, version = 3, exportSchema = false)
@TypeConverters(Converters.class)
public abstract class AppDB extends RoomDatabase {
    public abstract VideoDao videoDao();
    public abstract CommentDao commentDao();
    private static volatile AppDB INSTANCE;

    public static AppDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDB.class, "appDB")  // Updated database name here
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
