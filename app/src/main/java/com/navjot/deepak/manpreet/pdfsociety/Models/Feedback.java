package com.navjot.deepak.manpreet.pdfsociety.Models;

/**
 * Created by manpreet on 25/10/17.
 */

public class Feedback {
    private float Rating;
    private String Comment;
    private String Email;
    private String Uid;

    public Feedback(){}

    public Feedback(float rating, String comment, String email, String uid) {
        Rating = rating;
        Comment = comment;
        Email = email;
        Uid = uid;
    }

    public float getRating() {
        return Rating;
    }

    public void setRating(float rating) {
        Rating = rating;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "Rating=" + Rating +
                ", Comment='" + Comment + '\'' +
                ", Email='" + Email + '\'' +
                ", Uid='" + Uid + '\'' +
                '}';
    }
}
