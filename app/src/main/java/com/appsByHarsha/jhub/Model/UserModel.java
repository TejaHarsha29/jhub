package com.appsByHarsha.jhub.Model;

import java.util.Comparator;

public class UserModel {
    private String name,rollNumber,phoneNumber;
    private String profile_photo,user_id;
    private long follower_count,crush_count,bffCount;

    public long getFollower_count() {
        return follower_count;
    }

    public long getCrush_count() {
        return crush_count;
    }

    public void setCrush_count(long crush_count) {
        this.crush_count = crush_count;
    }

    public long getBffCount() {
        return bffCount;
    }

    public void setBffCount(long bffCount) {
        this.bffCount = bffCount;
    }

    public static Comparator<UserModel> userStar = new Comparator<UserModel>() {
        @Override
        public int compare(UserModel o1, UserModel o2) {
            return (int) (o2.getFollower_count()-o1.getFollower_count());
        }
    };
    public static Comparator<UserModel> userCrush = new Comparator<UserModel>() {
        @Override
        public int compare(UserModel o1, UserModel o2) {
            return (int) (o2.getCrush_count()-o1.getCrush_count());
        }
    };
    public static Comparator<UserModel> useBff = new Comparator<UserModel>() {
        @Override
        public int compare(UserModel o1, UserModel o2) {
            return (int) (o2.getBffCount()-o1.getBffCount());
        }
    };

    public UserModel(String name, String rollNumber, String phoneNumber, int follower_count) {
        this.name = name;
        this.rollNumber = rollNumber;
        this.phoneNumber = phoneNumber;
        this.follower_count = follower_count;
    }

    public long getFollower_count(long childrenCount) {
        return follower_count;
    }

    public void setFollower_count(long follower_count) {
        this.follower_count = follower_count;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }


    public UserModel(String name, String rollNumber, String phoneNumber) {
        this.name = name;
        this.rollNumber = rollNumber;
        this.phoneNumber = phoneNumber;
    }

    public UserModel() {
    }

    public String getProfile_photo() {
        return profile_photo;
    }

    public void setProfile_photo(String profile_photo) {
        this.profile_photo = profile_photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
