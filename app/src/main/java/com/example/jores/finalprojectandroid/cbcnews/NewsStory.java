package com.example.jores.finalprojectandroid.cbcnews;

public class NewsStory {

    private String title;
    private String description;
    private String imgSrc;
    private String author;
    private String pubDate;
    private String link;

    public NewsStory(){}

    public NewsStory(String title, String descriptionText, String author,
                     String pubDate ){
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

    public String getPubDate() {
        return pubDate;
    }

    public String getAuthor() {
        return author;
    }
    public String getLink(){return link;}

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public void setLink(String link) {
        this.link = link;
    }


    @Override
    public String toString(){
        String result = title+" "+author + " " +description;
        return result;
    }
}
