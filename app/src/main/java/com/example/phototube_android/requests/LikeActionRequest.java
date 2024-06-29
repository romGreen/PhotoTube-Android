package com.example.phototube_android.requests;

public class LikeActionRequest {
    private String action;

    public LikeActionRequest(String action) {
        this.action = action;
    }

    // Getter and setter
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
