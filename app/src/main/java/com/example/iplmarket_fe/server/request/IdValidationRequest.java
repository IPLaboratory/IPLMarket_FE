package com.example.iplmarket_fe.server.request;

import com.google.gson.annotations.SerializedName;

public class IdValidationRequest {
    @SerializedName("id")
    String id;

    public IdValidationRequest(String id) {
        this.id = id;
    }
}
