package com.example.iplmarket_fe.server;

import com.google.gson.annotations.SerializedName;

public class PostData {
    @SerializedName("title")
    private String title;

    @SerializedName("content")
    private String content;

    @SerializedName("price")
    private String price;

    @SerializedName("image_name")
    private String imageName;

    @SerializedName("video_name")
    private String VideoName;

    @SerializedName("image_data")
    private String imageData;

    @SerializedName("user_id")
    private String userId;

    public PostData(String title, String content, String price, String imageName, String videoName, String imageData, String userId) {
        this.title = title;
        this.content = content;
        this.price = price;
        this.imageName = imageName;
        this.VideoName = videoName;
        this.imageData = imageData;
        this.userId = userId;
    }

}
