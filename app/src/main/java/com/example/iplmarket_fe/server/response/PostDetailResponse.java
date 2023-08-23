package com.example.iplmarket_fe.server.response;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class PostDetailResponse {
    @SerializedName("num")
    private int num;
    @SerializedName("title")
    private String title;
    @SerializedName("content")
    private String content;
    @SerializedName("price")
    private String price;
    @SerializedName("original_image")
    private String originalImage;
    @SerializedName("regist_date")
    private Date registDate;
    @SerializedName("user_id")
    private String userId;
    @SerializedName("isClickedLike")
    private boolean isClickedLike;
    @SerializedName("likeCount")
    private int likeCount;

    public int getNum() {
        return num;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getPrice() {
        return price;
    }

    public String getOriginalImage() {
        return originalImage;
    }

    public Date getRegistDate() {
        return registDate;
    }

    public String getUserId() {
        return userId;
    }

    public boolean isClickedLike() {
        return isClickedLike;
    }

    public int getLikeCount() {
        return likeCount;
    }
}
