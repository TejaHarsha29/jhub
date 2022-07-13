package com.appsByHarsha.jhub.Model;

public class NotificationModel {

    private String notificationBy;
    private long notificationAt;
    private String type;
    private String postId;

    public String getNotId() {
        return NotId;
    }

    public void setNotId(String notId) {
        NotId = notId;
    }

    private String NotId;

    private String postedBy;
    private boolean checkOpen;

    public String getNotificationBy() {
        return notificationBy;
    }

    public void setNotificationBy(String notificationBy) {
        this.notificationBy = notificationBy;
    }

    public long getNotificationAt() {
        return notificationAt;
    }

    public void setNotificationAt(long notificationAt) {
        this.notificationAt = notificationAt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public boolean isCheckOpen() {
        return checkOpen;
    }

    public void setCheckOpen(boolean checkOpen) {
        this.checkOpen = checkOpen;
    }

    public NotificationModel() {
    }
}
