package com.sabry.muhammed.qanda.model;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {
    private String id;
    private String name;
    private String photoUrl;
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

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }


}
