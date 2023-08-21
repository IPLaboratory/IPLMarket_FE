package com.example.iplmarket_fe;

import com.google.gson.annotations.SerializedName;

public class CheckResponse {
    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

}
