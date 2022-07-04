package com.example.jhub.Model;

public class UserChangeModel {
    private String name,profile_photo;

    public UserChangeModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile_photo() {
        return profile_photo;
    }

    public void setProfile_photo(String profile_photo) {
        this.profile_photo = profile_photo;
    }

    public UserChangeModel(String name, String profile_photo) {
        this.name = name;
        this.profile_photo = profile_photo;
    }
}
