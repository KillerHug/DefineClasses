package com.defineclasses.app.Model;

import java.util.List;

public class Subject_Model {
    String subject,subject_id;
    List<Topic_Model> topic_modelList;
    private boolean expandable;

    public Subject_Model(String subject, List<Topic_Model> topic_modelList) {
        this.subject = subject;
        this.topic_modelList = topic_modelList;
        this.expandable=false;
    }

    public boolean isExpandable() {
        return expandable;
    }

    public void setExpandable(boolean expandable) {
        this.expandable = expandable;
    }

    public Subject_Model(String subject, String subject_id) {
        this.subject = subject;
        this.subject_id=subject_id;
    }

    public List<Topic_Model> getTopic_modelList() {
        return topic_modelList;
    }

    public void setTopic_modelList(List<Topic_Model> topic_modelList) {
        this.topic_modelList = topic_modelList;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(String subject_id) {
        this.subject_id = subject_id;
    }
}
