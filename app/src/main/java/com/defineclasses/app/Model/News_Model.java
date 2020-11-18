package com.defineclasses.app.Model;

public class News_Model {
    String news_id, news_date,news_title,news_content,news_attachment;
    private boolean expandable;

    public News_Model(String news_id, String news_date, String news_title, String news_content, String news_attachment) {
        this.news_id = news_id;
        this.news_date = news_date;
        this.news_title = news_title;
        this.news_content = news_content;
        this.news_attachment = news_attachment;
        this.expandable=false;
    }

    public boolean isExpandable() {
        return expandable;
    }

    public void setExpandable(boolean expandable) {
        this.expandable = expandable;
    }

    public String getNews_id() {
        return news_id;
    }

    public void setNews_id(String news_id) {
        this.news_id = news_id;
    }

    public String getNews_date() {
        return news_date;
    }

    public void setNews_date(String news_date) {
        this.news_date = news_date;
    }

    public String getNews_title() {
        return news_title;
    }

    public void setNews_title(String news_title) {
        this.news_title = news_title;
    }

    public String getNews_content() {
        return news_content;
    }

    public void setNews_content(String news_content) {
        this.news_content = news_content;
    }

    public String getNews_attachment() {
        return news_attachment;
    }

    public void setNews_attachment(String news_attachment) {
        this.news_attachment = news_attachment;
    }
}
