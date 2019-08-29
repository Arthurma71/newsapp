package com.example.myapplication.models;

import java.util.Date;

public class News {
    private String title;
    private String content;
    private Date time;
    private int DBindex;
    private boolean isfavorite;

    News()
    {
        title = "this is a title";
        content = "this is a content";
        time = new Date(0);
        DBindex = -1;
        isfavorite = true;
    }

    public boolean isIsfavorite() {
        return isfavorite;
    }

    public Date getTime() {
        return time;
    }

    public String getContent() {
        return content;
    }

    public int getDBindex() {
        return DBindex;
    }

    public String getTitle() {
        return title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDBindex(int DBindex) {
        this.DBindex = DBindex;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public void setIsfavorite(boolean isfavorite) {
        this.isfavorite = isfavorite;
    }
}
