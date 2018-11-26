package com.example.jores.finalprojectandroid.cbcnews;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;


/**
 * Learned about Parcelables from http://www.vogella.com/tutorials/AndroidParcelable/article.html
 */

public class NewsStory implements Parcelable {

    private String title;
    private String description;
    private String imgSrc;
    private String author;
    private String pubDate;
    private String link;
    private String imageFileName;
    private Bitmap image;

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public NewsStory createFromParcel(Parcel in) {
            return new NewsStory(in);
        }

        public NewsStory[] newArray(int size) {
            return new NewsStory[size];
        }
    };

    public NewsStory(){}

    public NewsStory(String title, String descriptionText, Bitmap image, String link, String imgSrc, String imageFileName){
        this.title = title;
        this.description = descriptionText;
        this.image=image;
        this.link=link;
        this.imgSrc=imgSrc;
        this.imageFileName= imageFileName;
    }

    public NewsStory(Parcel in){
        this.title= in.readString();
        this.description=in.readString();
        this.image=in.readParcelable(Bitmap.class.getClassLoader());
        this.link = in.readString();
        this.imgSrc = in.readString();
        this.imageFileName = in.readString();
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

        this.imageFileName = imageFileName.replace("/","");
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeParcelable(image,1);
        dest.writeString(this.link);
        dest.writeString(this.imgSrc);
        dest.writeString(this.imageFileName);
    }
}
