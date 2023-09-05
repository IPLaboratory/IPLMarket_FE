package com.example.iplmarket_fe.server.response;

import com.google.gson.annotations.SerializedName;

public class WriteContentResponse {

    @SerializedName("success")
    private boolean success;
    @SerializedName("message")
    private String message;

    public boolean isSuccess() {
        return success;
    }
}
