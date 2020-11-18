package com.defineclasses.app.Model;

public class Topic_Model {
    String topic_id, topic_name, topic_type, topic_subject_id, topic_course_id, topic_video;
    String course_id, course, banner, duration, lectures, course_fee, description;

    public Topic_Model(String course_id, String course, String banner, String duration, String lectures, String course_fee, String description, String topic_id, String topic_name, String topic_type, String topic_video) {
        this.topic_id = topic_id;
        this.topic_name = topic_name;
        this.topic_type = topic_type;
        this.topic_video = topic_video;
        this.course_id = course_id;
        this.course = course;
        this.banner = banner;
        this.duration = duration;
        this.lectures = lectures;
        this.course_fee = course_fee;
        this.description = description;
    }
    public Topic_Model(String topic_id, String topic_name, String topic_type, String topic_video) {
        this.topic_id = topic_id;
        this.topic_type=topic_type;
        this.topic_name = topic_name;
        this.topic_video = topic_video;
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


    public String getTopic_id() {
        return topic_id;
    }

    public void setTopic_id(String topic_id) {
        this.topic_id = topic_id;
    }

    public String getTopic_name() {
        return topic_name;
    }

    public void setTopic_name(String topic_name) {
        this.topic_name = topic_name;
    }

    public String getTopic_type() {
        return topic_type;
    }

    public void setTopic_type(String topic_type) {
        this.topic_type = topic_type;
    }

    public String getTopic_subject_id() {
        return topic_subject_id;
    }

    public void setTopic_subject_id(String topic_subject_id) {
        this.topic_subject_id = topic_subject_id;
    }

    public String getTopic_course_id() {
        return topic_course_id;
    }

    public void setTopic_course_id(String topic_course_id) {
        this.topic_course_id = topic_course_id;
    }

    public String getTopic_video() {
        return topic_video;
    }

    public void setTopic_video(String topic_video) {
        this.topic_video = topic_video;
    }
}
