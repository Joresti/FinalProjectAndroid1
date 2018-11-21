package com.example.jores.finalprojectandroid.cbcnews;

import android.content.ContentValues;
import android.content.Context;

import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;



import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

class NewsData extends AsyncTask<String, Integer, String> {
    String TAG = "NEWS DATA";
    ArrayList<NewsStory> newsStories;
    ArrayList<NewsStory> stories;
    Context context;
    CBCNewsMain.NewsAdapter newsAdapter;
    SQLiteDatabase database;

    public static String DATABASE_NAME = "NewsData";
    public static int VERSION_NUM = 1;
    protected final static String KEY_ID = "key_id";
    protected final static String TITLE= "title";
    protected final static String IMG_SRC = "imgSrc";
    protected final static String IMG_FILE_NAME = "imgFileName";
    protected final static String DESCRIPTION = "description";

    public NewsData(){}

    ProgressBar pbar;

    public NewsData(ProgressBar pbar, ArrayList<NewsStory> newsStories, Context context, CBCNewsMain.NewsAdapter newsAdapter, SQLiteDatabase database) {
        this.pbar = pbar;
        this.newsStories = newsStories;
        this.context = context;
        this.newsAdapter = newsAdapter;
        this.database = database;
    }

    @Override
    protected String doInBackground(String...urls){
        Log.d(TAG, "DOING IN BACKGROUND");
        Log.d(TAG, urls[0]);
        loadNewsData(urls[0]);
        return "";
    }

    @Override
    protected void onProgressUpdate(Integer...progress){
        super.onProgressUpdate(progress);
        pbar.setProgress(progress[0]);
    }

    @Override
    protected void onPostExecute(String result){
        super.onPostExecute(result);
        onProgressUpdate(new Integer[]{100});

        newsAdapter.notifyDataSetChanged();
        pbar.setVisibility(View.INVISIBLE);

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
     * This is a function to load news data from CBC API
     * @param url
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

    public void setDatabase(NewsStory story){
        ContentValues cv = new ContentValues();
        cv.put(TITLE,story.getTitle());
        cv.put(IMG_SRC, story.getImgSrc());
        cv.put(IMG_FILE_NAME, story.getImageFileName());
        cv.put(DESCRIPTION,story.getDescription());
        database.insert("NewsStories", null, cv);
    }

    public void addToArrayList(NewsStory story){
        if (newsStories.size() >25){
            NewsStory oldStory = newsStories.get(0);
            database.delete("NewsStories", TITLE+"= ?", new String[]{oldStory.getTitle()});
            newsStories.remove(0);
        }


    }



}
