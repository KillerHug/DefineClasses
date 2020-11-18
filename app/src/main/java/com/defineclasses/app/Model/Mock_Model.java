package com.defineclasses.app.Model;

public class Mock_Model {
    String id,name,date,banner,status;

    public Mock_Model(String id, String name, String date, String banner, String status) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.banner = banner;
        this.status = status;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
