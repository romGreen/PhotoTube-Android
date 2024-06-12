package com.example.phototube_android.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentListManager {
    private static CommentListManager instance;
    private Map<Integer, List<Comment>> commentsMap;

    private CommentListManager() {
        commentsMap = new HashMap<>();
    }

    public static synchronized CommentListManager getInstance() {
        if (instance == null) {
            instance = new CommentListManager();
        }
        return instance;
    }

    public List<Comment> getCommentsForVideo(int videoId) {
        return commentsMap.getOrDefault(videoId, new ArrayList<>());
    }

}
