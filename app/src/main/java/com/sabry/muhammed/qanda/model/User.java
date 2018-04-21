package com.sabry.muhammed.qanda.model;

import android.net.Uri;

import java.util.List;

public class User {
    private String id;
    private String name;
    private Uri photoUrl;
    private List<Question> starredQuestions;

    public List<Question> getStarredQuestions() {
        return starredQuestions;
    }

    public void setStarredQuestions(List<Question> starredQuestions) {
        this.starredQuestions = starredQuestions;
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

    public Uri getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(Uri photoUrl) {
        this.photoUrl = photoUrl;
    }


}
