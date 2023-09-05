package com.example.iplmarket_fe.server.response;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;

    @SerializedName("id")
    private int userId;

    private boolean success;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}