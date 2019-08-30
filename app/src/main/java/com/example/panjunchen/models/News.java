package com.example.panjunchen.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class News {
    private String title;
    private String content;
    private Date publishtime;
    private boolean isfavorite;
    private int DBindex;
    private Date readtime;
    private String publisher;
    private String hashcode;
    private List<String> tag;

    News()
    {
        title = "this is a title";
        content = "this is a content";
        publishtime = new Date(0);
        isfavorite = true;
        readtime = new Date(0);
        publisher = "MWC";
        tag = new ArrayList<String>();
        tag.add("baigei");
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

    public String getTitle() {
        return title;
    }

    public int getDBindex() {
        return DBindex;
    }

    public void setContent(String content) {
        this.content = content;
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

    public void setDBindex(int DBindex) {
        this.DBindex = DBindex;
    }
}
