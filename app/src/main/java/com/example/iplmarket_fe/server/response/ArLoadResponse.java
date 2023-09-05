package com.example.iplmarket_fe.server.response;

import com.google.gson.annotations.SerializedName;

public class ArLoadResponse {
    @SerializedName("success")
    private boolean success;

    @SerializedName("directory")
    private String directory;

    @SerializedName("mesh.mtl")
    private String mtl;

    @SerializedName("mesh.obj")
    private String obj;

    @SerializedName("probe.hdr")
    private String hdr;

    @SerializedName("texture_kd.png")
    private String kdPng;

    @SerializedName("texture_ks.png")
    private String ksPng;

    @SerializedName("texture_n.png")
    private String nPng;

    @SerializedName("error")
    private String error;

    public boolean isSuccess() {
        return success;
    }

    public String getDirectory() {
        return directory;
    }

    public String getMtl() {
        return mtl;
    }

    public String getObj() {
        return obj;
    }

    public String getHdr() {
        return hdr;
    }

    public String getKdPng() {
        return kdPng;
    }

    public String getKsPng() {
        return ksPng;
    }

    public String getnPng() {
        return nPng;
    }

    public String getError() {
        return error;
    }
}
