package com.example.jores.finalprojectandroid.cbcnews;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class ParserHelper {

    String TAG = "PARSER HELPER";
    NewsStory newsStory = new NewsStory();
    XmlPullParser parser;

    public ParserHelper(XmlPullParser parser){
        this.parser = parser;

    }


    public void readItem() throws XmlPullParserException, IOException {


        while (parser.next() != XmlPullParser.END_DOCUMENT) {


            String name = (parser.getName() != null ? parser.getName() : "None");
            String des = (parser.getText() != null ? parser.getText() : "None");

            if (name.equals("title")) {
                readTitle();
            }
            else if (name.equals("link")){readLink();}

            else if (name.equals("pubDate")){readPubDate();}
            else if (name.equals("author")){readAuthor();}

            else if (name.equals("description")) {
                readDescription();
                break;


            }
        }Log.d(TAG, newsStory.toString());




    }
    public void logNameAndText()throws XmlPullParserException,IOException{
        String name = parser.getName();
       // Log.d("PARSER HELPER", name);
        parser.next();
        String text = parser.getText();
        //Log.d("PARSER HELPER", text);
    }


    public void readTitle() throws XmlPullParserException, IOException{
        logNameAndText();
        newsStory.setTitle(parser.getText());
        parser.next();
    }

    public void readLink() throws XmlPullParserException, IOException{
        logNameAndText();
        newsStory.setLink(parser.getText());
        parser.next();
    }

    public void skipTag() throws XmlPullParserException, IOException{
        parser.next();parser.next();parser.next();parser.next();
    }

    public void readPubDate() throws XmlPullParserException, IOException{
        logNameAndText();
        newsStory.setPubDate(parser.getText());
        parser.next();
    }
    public void readAuthor() throws XmlPullParserException,IOException{
        logNameAndText();
        newsStory.setAuthor(parser.getText());
        parser.next();
    }
    public void readDescription()throws XmlPullParserException,IOException {
        parser.next();
        String des =  parser.getText();
        int srcStart = des.indexOf("src")+5;
        int srcEnd = des.indexOf("\'",srcStart);
        String imgSrc = des.substring(srcStart,srcEnd);
        newsStory.setImgSrc(imgSrc);


        int titleInfoStart =  des.indexOf("title")+7;
        int titleInfoEnd = des.indexOf("\'", titleInfoStart+2);
        String titleInfo = des.substring(titleInfoStart, titleInfoEnd);
        int start = des.indexOf("<p>")+3;
        int end = des.indexOf("</p>");
        String description = des.substring(start,end);

        newsStory.setDescription(titleInfo+"\n"+description);
        parser.next();
        parser.next();
        parser.next();

    }

    public NewsStory getStory(){
        return newsStory;
    }





}
