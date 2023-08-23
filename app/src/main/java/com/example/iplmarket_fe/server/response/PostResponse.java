package com.example.iplmarket_fe.server.response;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class PostResponse {
    @SerializedName("title")
    private String postTitle;
    @SerializedName("content")
    private String postContent;
    @SerializedName("thumbnail_image")
    private String postThumbnail;
    @SerializedName("regist_date")
    private Date postRegistDate;
    @SerializedName("user_id")
    private String userId;
    @SerializedName("num")
    private int num;
    @SerializedName("price")
    private String price;
    public String getPostTitle() {
        return postTitle;
    }
    public String getPostContent(){
        return postContent;
    }
    public String getPostThumbnail(){
        return postThumbnail;
    }
    public Date getPostRegistDate(){
        return postRegistDate;
    }
    public String getUserId(){return userId;}
    public String getPrice(){return price;}
    public int getNum(){
        return num;
    }

    public PostResponse(String postTitle, String postContent, String postThumbnail, Date postRegistDate, String userId, int num, String price) {
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.postThumbnail = postThumbnail;
        this.postRegistDate = postRegistDate;
        this.userId = userId;
        this.num  = num;
        this.price = price;
    }
}
