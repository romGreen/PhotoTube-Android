package com.example.phototube_android.response;

public class isExistResponse {
    private boolean exists;

    // Constructor
    public isExistResponse(boolean exists) {
        this.exists = exists;
    }

    // Getter
    public boolean isExists() {
        return exists;
    }

    // Setter
    public void setExists(boolean exists) {
        this.exists = exists;
    }
}
