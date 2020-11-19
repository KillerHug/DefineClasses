package com.defineclasses.app.Model;

public class Course_Search_Model {
    String course_id,course,banner,duration,lectures,course_fee,description;
    int rating;

    public Course_Search_Model(String course_id, String course, String banner, String duration, String lectures, String course_fee, String description) {
        this.course_id = course_id;
        this.course = course;
        this.banner = banner;
        this.duration = duration;
        this.lectures = lectures;
        this.course_fee = course_fee;
        this.description = description;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getLectures() {
        return lectures;
    }

    public void setLectures(String lectures) {
        this.lectures = lectures;
    }

    public String getCourse_fee() {
        return course_fee;
    }

    public void setCourse_fee(String course_fee) {
        this.course_fee = course_fee;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
