package com.example.iplmarket_fe.home;

public class PageProduct {
    private boolean liked;
    private int imageResource;
    private String name;
    private String price;

    public PageProduct(int imageResource, String name, String price, boolean liked) {
        this.imageResource = imageResource;
        this.name = name;
        this.price = price;
        this.liked = liked;
    }

    public boolean isLiked() {
        return liked;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }
}