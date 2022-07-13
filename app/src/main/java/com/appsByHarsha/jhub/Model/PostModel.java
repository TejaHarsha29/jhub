package com.appsByHarsha.jhub.Model;

public class PostModel {
   private String post_id,post_image,description;
   private String postedBy;
   private long postedAt;
   private long postLike;

    public long getPostLike() {
        return postLike;
    }

    public void setPostLike(long postLike) {
        this.postLike = postLike;
    }

    public PostModel() {
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getPost_image() {
        return post_image;
    }

    public void setPost_image(String post_image) {
        this.post_image = post_image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public long getPostedAt() {
        return postedAt;
    }

    public void setPostedAt(long postedAt) {
        this.postedAt = postedAt;
    }

    public PostModel(String post_id, String post_image, String description, String postedBy, long postedAt) {
        this.post_id = post_id;
        this.post_image = post_image;
        this.description = description;
        this.postedBy = postedBy;
        this.postedAt = postedAt;
    }
}
