package com.example.hello.entity;

import java.util.Date;

public class record {
    private String username;
    private String tid;
    private String title;
    private String author;
    private String platform;
    private String url;
    private String label;
    private Date date;

    @Override
    public String toString() {
        return "record{" +
                "username='" + username + '\'' +
                ", tid='" + tid + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", platform='" + platform + '\'' +
                ", url='" + url + '\'' +
                ", label='" + label + '\'' +
                ", date=" + date +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
