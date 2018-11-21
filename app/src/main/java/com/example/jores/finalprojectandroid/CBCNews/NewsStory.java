package com.example.jores.finalprojectandroid.CBCNews;

import android.graphics.Bitmap;

import java.util.Date;

public class NewsStory {

    private String title;
    private String description;
    private String imgSrc;
    private String author;
    private String pubDate;
    private String link;
    private String imageFileName;
    private Bitmap image;

    public NewsStory(){}

    public NewsStory(String title, String descriptionText, Bitmap image){
        this.title = title;
        this.description = descriptionText;
        this.image=image;

    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public String getPubDate() {
        return pubDate;
    }

    public String getAuthor() {
        return author;
    }
    public String getLink(){return link;}

    public String getImageFileName() {
        return imageFileName;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
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

    public void setAuthor(String author) {this.author = author; }

    public void setPubDate(String pubDate) { this.pubDate = pubDate; }

    public void setLink(String link) { this.link = link; }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    @Override
    public String toString(){
        String result = title+" "+author + " " +description;
        return result;
    }
}
