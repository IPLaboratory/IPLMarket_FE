package com.example.iplmarket_fe;

import com.google.gson.annotations.SerializedName;

public class LoginData {
    @SerializedName("id")
    private String userId;

    @SerializedName("pwd")
    private String userPwd;

    public LoginData(String userId, String userPwd) {
        this.userId = userId;
        this.userPwd = userPwd;
    }
}