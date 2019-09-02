package com.example.panjunchen.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class News {
    private String title;
    private String content;
    private Date publishtime;
    private int isfavorite;
    private int DBindex;
    private Date readtime;
    private String publisher;
    private String hashcode;
    private List<String> tag;
    private String category;
    private List<String> imageURL;
    private String videoURL;

    News()
    {
        title = "this is a title";
        content = "this is a content";
        publishtime = new Date(0);
        isfavorite = 0;
        DBindex = -1;
        readtime = new Date(0);
        publisher = "MWC";
        tag = new ArrayList<String>();
        imageURL = new ArrayList<String>();
        videoURL = "MWC";
        tag.add("baigei");
    }

    public void setImageURL(List<String> imageURL) {
        this.imageURL = imageURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public void setTag(List<String> tag) {
        this.tag = tag;
    }

    public List<String> getTag() {
        return tag;
    }

    public List<String> getImageURL() {
        return imageURL;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public boolean isIsfavorite() {
        return isfavorite == 1;
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


    public void setIsfavorite(int isfavorite) {
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

    public int getNewsType() {
        if(!videoURL.equals(""))return 3;
        else if(imageURL.size() == 1)return 0;
        else if(imageURL.size() > 1)return 1;
        else return 2;
    }
}
