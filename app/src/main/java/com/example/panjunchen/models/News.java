package com.example.panjunchen.models;

import java.util.Date;
import java.util.List;

public class News {
    private String title;
    private String content;
    private Date publishtime;
    private int DBindex;
    private boolean isfavorite;
    private Date readtime;
    private String publisher;
    private String hashcode;
    private List<String> tag;

    News()
    {
        title = "this is a title";
        content = "this is a content";
        publishtime = new Date(0);
        DBindex = -1;
        isfavorite = true;
        readtime = new Date(0);
        publisher = "MWC";
    }

    public boolean isIsfavorite() {
        return isfavorite;
    }

    public Date getPublishtime() {
        return publishtime;
    }

    public Date getReadtime() {
        return readtime;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getHashcode() {
        return hashcode;
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


    public void setIsfavorite(boolean isfavorite) {
        this.isfavorite = isfavorite;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setPublishtime(Date publishtime) {
        this.publishtime = publishtime;
    }

    public void setReadtime(Date readtime) {
        this.readtime = readtime;
    }

    public void setHashcode(String hashcode) {
        this.hashcode = hashcode;
    }
}
