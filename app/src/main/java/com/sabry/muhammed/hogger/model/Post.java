package com.sabry.muhammed.hogger.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Post implements Serializable {

    private String id;
    private String userId;
    private String post;
    private Date date;
    private Map<String, Boolean> likedUsers;

    public Post() {
        likedUsers = new HashMap<>();
    }

    public Map<String, Boolean> getLikedUsers() {
        return likedUsers;
    }

    public void setLikedUsers(Map<String, Boolean> likedUsers) {
        this.likedUsers = likedUsers;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String question) {
        this.post = question;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
