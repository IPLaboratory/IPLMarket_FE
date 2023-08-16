package com.example.iplmarket_fe;

import com.google.gson.annotations.SerializedName;

public class RegisterData {
    @SerializedName("userName")
    private String userName;

    @SerializedName("userPwd")
    private String userPwd;

    @SerializedName("id")
    private String id;

    public RegisterData(String userName, String userPwd, String name, String userNickname, int userNumber) {
        this.userName = userName;
        this.userPwd = userPwd;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
