package com.example.iplmarket_fe.server.request;

import com.google.gson.annotations.SerializedName;

public class PostDetailRequest {
    @SerializedName("postNum")
    int postnum;

    @SerializedName("id")
    String id;

    public PostDetailRequest(int postnum, String id){
        this.postnum = postnum;
        this.id = id;
    }
}
