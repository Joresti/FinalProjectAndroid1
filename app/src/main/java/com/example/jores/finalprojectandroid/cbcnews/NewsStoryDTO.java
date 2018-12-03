package com.example.jores.finalprojectandroid.cbcnews;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;


/**
 * Data Transfer Object for a News Story
 * implements Parcelable so that Bitmap object can be transferred in a bundle.
 *
 * Learned about Parcelables from http://www.vogella.com/tutorials/AndroidParcelable/article.html
 */

public class NewsStoryDTO implements Parcelable {

    private String title;
    private String description;
    private String imgSrc;
    private String author;
    private String pubDate;
    private String link;
    private String imageFileName;
    private Bitmap image;
    private int wordcount;
    /**
     * Creates the Parcelable
     */

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public NewsStoryDTO createFromParcel(Parcel in) {
            return new NewsStoryDTO(in);
        }

        public NewsStoryDTO[] newArray(int size) {
            return new NewsStoryDTO[size];
        }
    };

    /**
     * empty constructor
     */

    public NewsStoryDTO(){}

    /**
     * constructor
     * @param title String
     * @param descriptionText String
     * @param image Bitmap
     * @param link String
     * @param imgSrc String
     * @param imageFileName String
     * @param date String
     * @param author String
     */

    public NewsStoryDTO(String title, String descriptionText, Bitmap image, String link, String imgSrc, String imageFileName, String date, String author){
        this.title = title;
        this.description = descriptionText;
        this.image=image;
        this.link=link;
        this.imgSrc=imgSrc;
        this.imageFileName= imageFileName;
        this.pubDate= date;
        this.author=author;
    }

    /**
     * Constructor for Parcelabel
     * @param in Parcel
     */
    public NewsStoryDTO(Parcel in){
        this.title= in.readString();
        this.description=in.readString();
        this.image=in.readParcelable(Bitmap.class.getClassLoader());
        this.link = in.readString();
        this.imgSrc = in.readString();
        this.imageFileName = in.readString();
        this.pubDate=in.readString();
        this.author=in.readString();
        this.wordcount = description.split(" ").length;
    }

    /**
     * Getter
     * @return String
     */
    public String getDescription() {
        return description;
    }
    /**
     * Getter
     * @return String
     */
    public String getTitle() {
        return title;
    }
    /**
     * Getter
     * @return String
     */
    public String getImgSrc() {
        return imgSrc;
    }/**
     * Getter
     * @return String
     */

    public String getPubDate() {
        return pubDate;
    }/**
     * Getter
     * @return String
     */

    public String getAuthor() {
        return author;
    }/**
     * Getter
     * @return String
     */
    public String getLink(){return link;}
    /**
     * Getter
     * @return String
     */
    public String getImageFileName() {
        return imageFileName;
    }/**
     * Getter
     * @return Bitmap
     */

    public Bitmap getImage() {
        return image;
    }
    /**
     * Getter
     * @return String
     */
    public void setImageFileName(String imageFileName) {

        this.imageFileName = imageFileName.replace("/","");
    }
    /**
     * Setter
     * @return String
     */
    public void setDescription(String description) {
        this.description = description;
    }
    /**
     * Setter
     * @return String
     */
    public void setTitle(String title) {
        this.title = title;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }
    /**
     * Setter
     * @return String
     */
    public void setAuthor(String author) {this.author = author; }
    /**
     * Setter
     * @return String
     */
    public void setPubDate(String pubDate) { this.pubDate = pubDate; }
    /**
     * Setter
     * @return String
     */
    public void setLink(String link) { this.link = link; }
    /**
     * Setter
     * @return String
     */
    public void setImage(Bitmap image) {
        this.image = image;
    }

    /**
     * toString - helpful for debugging code
     * @return
     */
    @Override
    public String toString(){
        String result = title+" "+author + " " +description;
        return result;
    }

    /**
     * Used in Parcelable
     * @return
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Writes Parcelable object NewsStoryDTO to a parcel
     * @param dest Parcel
     * @param flags int
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeParcelable(image,1);
        dest.writeString(this.link);
        dest.writeString(this.imgSrc);
        dest.writeString(this.imageFileName);
        dest.writeString(this.pubDate);
        dest.writeString(this.author);
    }
}
