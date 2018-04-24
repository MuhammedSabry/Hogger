package com.sabry.muhammed.hogger.model;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {

    private String id;
    private String name;
    private String photoUrl;
    private List<String> starredPosts;

    public List<String> getStarredPosts() {
        return starredPosts;
    }

    public void setStarredPosts(List<String> starredPosts) {
        this.starredPosts = starredPosts;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
