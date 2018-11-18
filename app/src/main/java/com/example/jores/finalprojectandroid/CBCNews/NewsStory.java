package com.example.jores.finalprojectandroid.CBCNews;

import java.util.Date;

public class NewsStory {

    private String title;
    private String description;
    private String imgSrc;
    private String author;
    private Date pubDate;


    public NewsStory(String title, String descriptionText, String author, Date pubDate ){
        this.title = title;
        this.description = descriptionText;
        this.author = author;
        this.pubDate = pubDate;
        //descritionParser(descriptionText);
    }

    // public void descritionParser(String des){

    //}


    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public Date getPubDate() {
        return pubDate;
    }

    public String getAuthor() {
        return author;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }
}
