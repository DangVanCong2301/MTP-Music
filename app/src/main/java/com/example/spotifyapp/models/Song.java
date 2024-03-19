package com.example.spotifyapp.models;

import java.io.Serializable;

public class Song implements Serializable {
    private String id;
    private String songName;
    private String songArtist;
    private String uid;
    private long timestamp;
    private String url;
    private String categoryId;
    private String imageUrl;
    private boolean isBest;
    private int listensCount;
    private int downloadCount;
    private int index;

    public Song() {
    }

    public Song(String id, String songName, String songArtist, String uid, long timestamp, String url, String categoryId, String imageUrl, boolean isBest, int listensCount, int downloadCount, int index) {
        this.id = id;
        this.songName = songName;
        this.songArtist = songArtist;
        this.uid = uid;
        this.timestamp = timestamp;
        this.url = url;
        this.categoryId = categoryId;
        this.imageUrl = imageUrl;
        this.isBest = isBest;
        this.listensCount = listensCount;
        this.downloadCount = downloadCount;
        this.index = index;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSongArtist() {
        return songArtist;
    }

    public void setSongArtist(String songArtist) {
        this.songArtist = songArtist;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isBest() {
        return isBest;
    }

    public void setBest(boolean best) {
        isBest = best;
    }

    public int getListensCount() {
        return listensCount;
    }

    public void setListensCount(int listensCount) {
        this.listensCount = listensCount;
    }

    public int getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(int downloadCount) {
        this.downloadCount = downloadCount;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
