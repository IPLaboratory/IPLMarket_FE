package com.example.iplmarket_fe;

import com.google.gson.annotations.SerializedName;

public class UserData {
    @SerializedName("userId")
    String id;

    public UserData(String id) {
        this.id = id;
    }
}
