package com.example.iplmarket_fe.server.request;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {
    @SerializedName("id")
    private String userId;

    @SerializedName("pwd")
    private String userPwd;

    public LoginRequest(String userId, String userPwd) {
        this.userId = userId;
        this.userPwd = userPwd;
    }
}