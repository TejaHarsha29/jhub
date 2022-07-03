package com.example.jhub.Model;

public class ConfessionModel {
    private String postedBy,confessionText,confession_id;
    long postedAt;
    int reportCount,likeCout;

    public ConfessionModel(String confessionText) {
        this.confessionText = confessionText;
    }

    public ConfessionModel(String postedBy, String confessionText, String confession_id, long postedAt, int reportCount, int likeCout) {
        this.postedBy = postedBy;
        this.confessionText = confessionText;
        this.confession_id = confession_id;
        this.postedAt = postedAt;
        this.reportCount = reportCount;
        this.likeCout = likeCout;
    }

    public ConfessionModel() {
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public String getConfessionText() {
        return confessionText;
    }

    public void setConfessionText(String confessionText) {
        this.confessionText = confessionText;
    }

    public String getConfession_id() {
        return confession_id;
    }

    public void setConfession_id(String confession_id) {
        this.confession_id = confession_id;
    }

    public long getPostedAt() {
        return postedAt;
    }

    public void setPostedAt(long postedAt) {
        this.postedAt = postedAt;
    }

    public int getReportCount() {
        return reportCount;
    }

    public void setReportCount(int reportCount) {
        this.reportCount = reportCount;
    }

    public int getLikeCout() {
        return likeCout;
    }

    public void setLikeCout(int likeCout) {
        this.likeCout = likeCout;
    }
}
