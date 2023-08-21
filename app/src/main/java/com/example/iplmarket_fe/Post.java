package com.example.iplmarket_fe;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Post {
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
    public int getNum(){
        return num;
    }

    public Post(String postTitle, String postContent, String postThumbnail, Date postRegistDate, String userId, int num) {
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.postThumbnail = postThumbnail;
        this.postRegistDate = postRegistDate;
        this.userId = userId;
        this.num  = num;
    }
}
