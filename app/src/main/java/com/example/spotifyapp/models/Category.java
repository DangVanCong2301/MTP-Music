package com.example.spotifyapp.models;

public class Category {
    private String id;
    private String categoryName;
    private String imageUrl;

    private String uid;
    private long timestamp;

    public Category() {
    }
    public Category(String id, String categoryName, String imageUrl, String uid, long timestamp) {
        this.id = id;
        this.categoryName = categoryName;
        this.imageUrl = imageUrl;
        this.uid = uid;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
