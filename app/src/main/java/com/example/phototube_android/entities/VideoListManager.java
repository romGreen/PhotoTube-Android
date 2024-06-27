package com.example.phototube_android.entities;

import com.example.phototube_android.model.Video;

import java.util.ArrayList;
import java.util.List;

public class VideoListManager {
    private static VideoListManager instance;
    private List<Video> VideoList;
    private boolean flag;

    private VideoListManager() {
        VideoList = new ArrayList<>();
        flag = false;
    }

    public static synchronized VideoListManager getInstance() {
        if (instance == null) {
            instance = new VideoListManager();
        }
        return instance;
    }
public void init(List<Video> list)
{
    if(!flag)
    {
        VideoList.addAll(list);
        flag = true;
    }

}
    public void addVideo(Video video) {
        VideoList.add(video);
    }

    public List<Video> getVideoList() {
        return VideoList;
    }
}