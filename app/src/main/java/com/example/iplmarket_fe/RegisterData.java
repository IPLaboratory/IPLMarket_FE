package com.example.iplmarket_fe;

import com.google.gson.annotations.SerializedName;

public class RegisterData {
    @SerializedName("id")
    private String userId;

    @SerializedName("pwd")
    private String userPwd;

    @SerializedName("name")
    private String userName;

    @SerializedName("nickname")
    private String userNickname;

    @SerializedName("phone_number")
    private String userNumber;

    public RegisterData(String userId, String userPwd, String userName, String userNickname, String userNumber) {
        this.userId = userId;
        this.userPwd = userPwd;
        this.userName = userName;
        this.userNickname = userNickname;
        this.userNumber = userNumber;
    }

}
