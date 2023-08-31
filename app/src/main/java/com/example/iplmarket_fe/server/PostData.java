package com.example.iplmarket_fe.server;

import com.google.gson.annotations.SerializedName;

public class PostData {
    @SerializedName("title")
    private String title;

    @SerializedName("content")
    private String content;

    @SerializedName("image_name")
    private String imageName;

    @SerializedName("user_id")
    private String userId;

    public PostData(String title, String content, String imageName, String userId) {
        this.title = title;
        this.content = content;
        this.imageName = imageName;
        this.userId = userId;
    }

}
