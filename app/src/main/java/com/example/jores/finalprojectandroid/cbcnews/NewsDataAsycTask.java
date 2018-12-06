package com.example.jores.finalprojectandroid.cbcnews;


import android.content.ContentValues;
import android.content.Context;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;



import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * An AsyncTask class to create a seperate thread that pulls XML data from CBC rss feed, processes the result,  and stores it in a database.
 */

class NewsDataAsycTask extends AsyncTask<String, Integer, String> {
    String TAG = "NEWS DATA";
    ArrayList<NewsStoryDTO> newsStories;
    ArrayList<NewsStoryDTO> stories;
    Context context;
    CBCNewsMain cbc;
    SQLiteDatabase database;
    protected final static String TITLE= "title";
    protected final static String IMG_SRC = "imgSrc";
    protected final static String IMG_FILE_NAME = "imgFileName";
    protected final static String DESCRIPTION = "description";
    protected final static String LINK = "link";
    protected final static String DATE = "date";
    protected final static String AUTHOR = "author";
    ProgressBar pbar;

    /**
     * empty constructor
     */
    public NewsDataAsycTask(){}

    /**
     * Constructor
     * @param pbar
     * @param cbc
     * @param context
     * @param database
     */

    public NewsDataAsycTask(ProgressBar pbar, CBCNewsMain cbc, Context context, SQLiteDatabase database) {
        this.pbar = pbar;
        this.newsStories = cbc.newsArrayList;
        this.database =database;
        this.context = context;
        this.cbc =cbc;

    }

    /**
     * Override doInBackground
     * @param urls String Array
     * @return String
     */

    @Override
    protected String doInBackground(String...urls){
        Log.d(TAG, "DOING IN BACKGROUND");
        Log.d(TAG, urls[0]);
        loadNewsData(urls[0]);
        return "";
    }

    /**
     * Updates progress bar
     * @param progress Integer array
     */

    @Override
    protected void onProgressUpdate(Integer...progress){
        super.onProgressUpdate(progress);
        pbar.setProgress(progress[0]);
    }

    /**
     *  Updates CBCNewsMain ArrayList after thread completed
     * @param result
     */

    @Override
    protected void onPostExecute(String result){
        super.onPostExecute(result);
        onProgressUpdate(new Integer[]{100});

        loadStoriesFromDatabase();

        pbar.setVisibility(View.INVISIBLE);


    }
    public void loadStoriesFromDatabase(){
        Log.d("IN", "LOADSTORIES");
        Cursor cursor =database.rawQuery("SELECT * FROM NewsStories;", null );
        cbc.databaseSize = cursor.getCount();
        Log.d("IN", "LOADSTORIES");
        if (cursor ==null){
            Log.d("null", "NULL");
        }
        if (cursor !=null){
            Log.d("null", Integer.toString(cursor.getCount()));
        }

        Cursor[] c = {cursor};
        new LoadStoriesAsyncTask(context,cbc).execute(c);

        cbc.newsAdapter.notifyDataSetChanged();
    }
    /**
     * This is a function to format XML feed to a list
     * @param parser XmlPullParser object
     * @return List of XML data
     */
    public List parseNewsData(XmlPullParser parser) throws XmlPullParserException, IOException {

        stories = new ArrayList<>();

        while (  parser.next()!=XmlPullParser.END_DOCUMENT) {
            String name = (parser.getName() != null ? parser.getName() : "None");
            String des = (parser.getText() != null ? parser.getText() : "None");

            Log.d(TAG+" NAME", name);
            Log.d(TAG+" TEXT", des);

            if(name.equals("item")){
                ParserHelper ph =  new ParserHelper(parser, context);
                ph.readItem();
                addToArrayList(ph.getStory());

            }
        }
        return new ArrayList();
    }

    /**
     * This is a function to load news data from cbc API
     * @param url String
     */
    public void loadNewsData(String url) {
        Log.d(TAG, "LOADING NEWS DATA");
        int BUFFER_SIZE = 2000;
        InputStream in = null;
        XmlPullParser xpp;
        try {
            in = OpenHttpConnection.OpenHttpConnection(url);
        }catch (IOException e){Log.d(TAG,e.getLocalizedMessage());}
        Log.d(TAG, "LOADING NEWS DATA");
        InputStreamReader isr = new InputStreamReader(in);
        String line = null;
        StringBuilder sBuilder = new StringBuilder();
        Log.d(TAG, "LOADING NEWS DATA");

        try{
            Log.d(TAG, "LOADING NEWS DATA");
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            xpp = factory.newPullParser();
            xpp.setInput(isr);
            xpp.next();
            parseNewsData(xpp);
            in.close();

        }catch(XmlPullParserException e ){
            e.getLocalizedMessage();
        }catch (IOException e1){
            e1.getLocalizedMessage();
        }
    }


    /**
     * Converts NewsStoryDTO to strings that are saved in "NewsStories" database table
     * @param story NewsStoryDTO
     */
    public void setDatabase(NewsStoryDTO story){
        ContentValues cv = new ContentValues();
        cv.put(TITLE,story.getTitle());
        cv.put(IMG_SRC, story.getImgSrc());
        cv.put(IMG_FILE_NAME, story.getImageFileName());
        cv.put(DESCRIPTION,story.getDescription());
        cv.put(LINK, story.getLink());
        cv.put(DATE, story.getPubDate());
        cv.put(AUTHOR, story.getAuthor());
        database.insert("NewsStories", null, cv);

    }

    /**
     * calls setDatabase
     * @param story NewsStoryDTO
     */

    public void addToArrayList(NewsStoryDTO story){
        setDatabase(story);
    }



}
