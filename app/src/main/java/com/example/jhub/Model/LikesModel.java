package com.example.jhub.Model;

public class LikesModel {
    private  String Likedby;
    private long LikedAt;

    public LikesModel() {
    }

    public LikesModel(String likedby, long likedAt) {
        Likedby = likedby;
        LikedAt = likedAt;
    }

    public String getLikedby() {
        return Likedby;
    }

    public void setLikedby(String likedby) {
        Likedby = likedby;
    }

    public long getLikedAt() {
        return LikedAt;
    }

    public void setLikedAt(long likedAt) {
        LikedAt = likedAt;
    }
}
