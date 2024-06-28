package com.example.phototube_android.response;

// Api response: ability to hold data, message and success status
public class ApiResponse<T> {
    private T data;
    private String message;
    private boolean success;

    public ApiResponse(T data, String message, boolean success) {
        this.data = data;
        this.message = message;
        this.success = success;
    }

    // Getters and setters
    public T getData() { return data; }
    public String getMessage() { return message; }
    public boolean isSuccess() { return success; }

    public void setData(T data) { this.data = data; }
    public void setMessage(String message) { this.message = message; }
    public void setSuccess(boolean success) { this.success = success; }
}
