package com.example.iplmarket_fe.server.request;

import com.google.gson.annotations.SerializedName;

public class PostListRequest {
    @SerializedName("userId")
    String id;

    public PostListRequest(String id) {
        this.id = id;
    }
}
