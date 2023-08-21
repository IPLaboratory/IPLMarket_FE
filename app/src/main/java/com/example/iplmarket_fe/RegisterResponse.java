package com.example.iplmarket_fe;

import com.google.gson.annotations.SerializedName;

public class RegisterResponse {
    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}