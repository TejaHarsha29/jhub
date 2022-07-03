package com.example.jhub.Model;

public class CrushModel {

    private String crushBy;
    private long crushAt;

    public CrushModel(String crushBy, long crushAt) {
        this.crushBy = crushBy;
        this.crushAt = crushAt;
    }

    public CrushModel() {
    }

    public String getCrushBy() {
        return crushBy;
    }

    public void setCrushBy(String crushBy) {
        this.crushBy = crushBy;
    }

    public long getCrushAt() {
        return crushAt;
    }

    public void setCrushAt(long crushAt) {
        this.crushAt = crushAt;
    }
}
