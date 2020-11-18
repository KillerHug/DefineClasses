package com.defineclasses.app.Model;

public class Review_Model
{
    String review_id,full_name,email,message,student_photo,registered_date;
    int ratingBar;

    public Review_Model(String review_id,String full_name,String email,String message,String student_photo,String registered_date,int ratingBar) {
        this.review_id = review_id;
        this.full_name = full_name;
        this.email = email;
        this.ratingBar = ratingBar;
        this.message = message;
        this.registered_date = registered_date;
        this.student_photo = student_photo;
    }

    public String getReview_id() {
        return review_id;
    }

    public void setReview_id(String review_id) {
        this.review_id = review_id;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getRatingBar() {
        return ratingBar;
    }

    public void setRatingBar(int ratingBar) {
        this.ratingBar = ratingBar;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRegistered_date() {
        return registered_date;
    }

    public void setRegistered_date(String registered_date) {
        this.registered_date = registered_date;
    }

    public String getStudent_photo() {
        return student_photo;
    }

    public void setStudent_photo(String student_photo) {
        this.student_photo = student_photo;
    }
}
