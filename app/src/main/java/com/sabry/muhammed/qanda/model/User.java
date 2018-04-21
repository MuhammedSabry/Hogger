package com.sabry.muhammed.qanda.model;

import java.util.List;

public class User {
    private String id;
    private String name;
    private String photoUrl;
    private List<Question> starredQuestions;
    private List<Question> userQuestions;
    private List<Answer> userAnswers;

    public List<Question> getStarredQuestions() {
        return starredQuestions;
    }

    public void setStarredQuestions(List<Question> starredQuestions) {
        this.starredQuestions = starredQuestions;
    }

    public List<Question> getUserQuestions() {
        return userQuestions;
    }

    public void setUserQuestions(List<Question> userQuestions) {
        this.userQuestions = userQuestions;
    }

    public List<Answer> getUserAnswers() {
        return userAnswers;
    }

    public void setUserAnswers(List<Answer> userAnswers) {
        this.userAnswers = userAnswers;
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
