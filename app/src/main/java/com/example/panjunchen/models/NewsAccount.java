package com.example.panjunchen.models;

public class NewsAccount {
    private String username;
    private String password;
    private String imageURL;

    public NewsAccount(){

    }

    public NewsAccount(String username, String password, String imageURL)
    {
        this.username = username;
        this.password = password;
        this.imageURL = imageURL;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
