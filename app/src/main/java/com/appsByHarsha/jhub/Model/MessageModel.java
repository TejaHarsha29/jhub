package com.appsByHarsha.jhub.Model;

public class MessageModel {
    private String uid,message;
    private long postedAt;

    public MessageModel() {
    }

    public MessageModel(String uid, String message, long postedAt) {
        this.uid = uid;
        this.message = message;
        this.postedAt = postedAt;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getPostedAt() {
        return postedAt;
    }

    public void setPostedAt(long postedAt) {
        this.postedAt = postedAt;
    }
}
