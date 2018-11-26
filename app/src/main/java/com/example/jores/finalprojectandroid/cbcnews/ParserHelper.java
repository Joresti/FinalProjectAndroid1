package com.example.jores.finalprojectandroid.cbcnews;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ParserHelper {
    Context context;
    String TAG = "PARSER HELPER";
    NewsStory newsStory = new NewsStory();
    XmlPullParser parser;

    public ParserHelper(XmlPullParser parser, Context context) {
        this.parser = parser;
        this.context = context;

    }

    public void readItem() throws XmlPullParserException, IOException {
        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            String name = (parser.getName() != null ? parser.getName() : "None");
            String des = (parser.getText() != null ? parser.getText() : "None");
            if (name.equals("title")) {
                readTitle();
            } else if (name.equals("link")) {
                readLink();
            } else if (name.equals("pubDate")) {
                readPubDate();
            } else if (name.equals("author")) {
                readAuthor();
            } else if (name.equals("description")) {
                readDescription();
                break;
            }
        }
        Log.d(TAG, newsStory.toString());
    }

    public void logNameAndText() throws XmlPullParserException, IOException {
        String name = parser.getName();
        // Log.d("PARSER HELPER", name);
        parser.next();
        String text = parser.getText();
        //Log.d("PARSER HELPER", text);
    }

    public void readTitle() throws XmlPullParserException, IOException {
        logNameAndText();
        newsStory.setTitle(parser.getText());
        parser.next();
    }

    public void readLink() throws XmlPullParserException, IOException {
        logNameAndText();
        newsStory.setLink(parser.getText());
        parser.next();
    }

    public void skipTag() throws XmlPullParserException, IOException {
        parser.next();
        parser.next();
        parser.next();
        parser.next();
    }

    public void readPubDate() throws XmlPullParserException, IOException {
        logNameAndText();
        newsStory.setPubDate(parser.getText());
        parser.next();
    }

    public void readAuthor() throws XmlPullParserException, IOException {
        logNameAndText();
        newsStory.setAuthor(parser.getText());
        parser.next();
    }

    public void readDescription() throws XmlPullParserException, IOException {
        parser.next();
        String des = parser.getText();
        int srcStart = des.indexOf("src") + 5;
        int srcEnd = des.indexOf("\'", srcStart);
        String imgSrc = des.substring(srcStart, srcEnd);

        int startFileName = srcEnd - 10;

        String fileName = des.substring(startFileName, srcEnd);
        Log.d("File Name", fileName);
        newsStory.setImageFileName(fileName);

        newsStory.setImgSrc(imgSrc);
        int titleInfoStart = des.indexOf("title") + 7;
        int titleInfoEnd = des.indexOf("\'", titleInfoStart + 2);
        String titleInfo = des.substring(titleInfoStart, titleInfoEnd);

        Log.d("DES", titleInfo);
        int start = des.indexOf("<p>") + 3;
        int end = des.indexOf("</p>");
        String description = des.substring(start, end);
        newsStory.setImage(getBitmap());
        newsStory.setDescription(titleInfo + "\n" + description);
        parser.next();
        parser.next();
        parser.next();
    }

    public Bitmap getBitmap(){
        Log.d("BITMAP" ,"IN BITMAP");

        String imageStr = newsStory.getImageFileName();
        String imgUrl = newsStory.getImgSrc();

        Log.d("IMGURL", imgUrl);
        Bitmap image = null;
        if(fileExistence(imageStr)){
            Log.i(TAG, "Found file " + imageStr);
            FileInputStream fis = null;
            try {
                fis = context.openFileInput(imageStr);
            } catch (FileNotFoundException e) {
                Log.d("FILE NOT FOUND", "PROBLEM");
                e.printStackTrace();
            }
            image = BitmapFactory.decodeStream(fis);
            try {
                fis.close();
            }
            catch (IOException e){e.getLocalizedMessage();}
        }else {
            Log.i(TAG, "Did not find file.  Downloading " + imageStr);
            try {
                image = HttpUtils.getImage(imgUrl);
                FileOutputStream outputStream = context.openFileOutput(imageStr, Context.MODE_PRIVATE);
                image.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);
                outputStream.flush();
                outputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                Log.d("IOEXCEP", "PROBLEM");
                e.getLocalizedMessage();
            }
        }
        Log.d("BITMAP" ,"OUT BITMAP");
        return image;
    }



    public boolean fileExistence(String fname){
        File file = context.getFileStreamPath(fname);
        return file.exists();
    }


    public NewsStory getStory() {
        return newsStory;
    }



}