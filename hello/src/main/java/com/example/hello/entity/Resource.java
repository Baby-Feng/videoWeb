package com.example.hello.entity;

/*
* 该实体类为课程实体，用于返回对关键词的搜索结果
*
*
* */

public class Resource {
    //编号、标题、作者、来源、url、标签
    private String tid;
    private String type;
    private String title;
    private String author;
    private String platform;
    private String url;
    private String label;
    private String comment;
    private int collection;
    private int play;
    private double score;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getCollection() {
        return collection;
    }

    public void setCollection(int collection) {
        this.collection = collection;
    }

    public int getPlay() {
        return play;
    }

    public void setPlay(int play) {
        this.play = play;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "Resource{" +
                "tid='" + tid + '\'' +
                ", type='" + type + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", platform='" + platform + '\'' +
                ", url='" + url + '\'' +
                ", label='" + label + '\'' +
                ", comment='" + comment + '\'' +
                ", collection=" + collection +
                ", play=" + play +
                ", score=" + score +
                '}';
    }
}
