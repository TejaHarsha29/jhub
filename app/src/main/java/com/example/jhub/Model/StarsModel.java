package com.example.jhub.Model;

public class StarsModel {
    private String StarredBy;
    private long StarredAt;

    public StarsModel(String starredBy, long starredAt) {
        StarredBy = starredBy;
        StarredAt = starredAt;
    }

    public StarsModel() {
    }

    public String getStarredBy() {
        return StarredBy;
    }

    public void setStarredBy(String starredBy) {
        StarredBy = starredBy;
    }

    public long getStarredAt() {
        return StarredAt;
    }

    public void setStarredAt(long starredAt) {
        StarredAt = starredAt;
    }
}
