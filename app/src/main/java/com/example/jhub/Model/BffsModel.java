package com.example.jhub.Model;

public class BffsModel {
    private String BffBy;
    private long BffAt;

    public BffsModel(String bffBy, long bffAt) {
        BffBy = bffBy;
        BffAt = bffAt;
    }

    public BffsModel() {
    }

    public String getBffBy() {
        return BffBy;
    }

    public void setBffBy(String bffBy) {
        BffBy = bffBy;
    }

    public long getBffAt() {
        return BffAt;
    }

    public void setBffAt(long bffAt) {
        BffAt = bffAt;
    }
}
