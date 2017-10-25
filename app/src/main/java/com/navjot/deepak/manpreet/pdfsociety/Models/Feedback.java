package com.navjot.deepak.manpreet.pdfsociety.Models;

/**
 * Created by manpreet on 25/10/17.
 */

public class Feedback {
    private float Rating;
    private String Comment;

    public Feedback(){}

    public Feedback(float rating, String comment) {
        this.Rating = rating;
        this.Comment = comment;
    }

    public float getRating() {
        return Rating;
    }

    public void setRating(float rating) {
        this.Rating = rating;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        this.Comment = comment;
    }

    @Override
    public String toString() {
        return "FeedbackActivity{" +
                "Rating=" + Rating +
                ", Comment='" + Comment + '\'' +
                '}';
    }
}
