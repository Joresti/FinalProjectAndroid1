package com.example.jores.finalprojectandroid.CBCNews;

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
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

class NewsData extends AsyncTask<String, Integer, String> {
    String TAG = "NEWS DATA";


    ProgressBar pbar;

    public NewsData(ProgressBar pbar){

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


        //pbar.setVisibility(View.INVISIBLE);

    }

    /**
     * Loosely adapted from Android Programming with Android studio - Chapter 11
     * @param urlString CBC url address
     * @return InputStream
     * @throws IOException
     */
    private InputStream OpenHttpConnection(String urlString) throws IOException {
        InputStream in = null;
        int response = -1;
        URL url = new URL(urlString);
        URLConnection conn = url.openConnection();
        if (!(conn instanceof HttpURLConnection)) {
            throw new IOException("Not an http connection");
        }
        try {
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setAllowUserInteraction(false);httpConn.setInstanceFollowRedirects(true);httpConn.setRequestMethod("GET");httpConn.connect();
            response = httpConn.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) { in = httpConn.getInputStream(); }
        }
        catch (Exception e) {
            Log.i(TAG, e.getLocalizedMessage());
        }
        return in;
    }

    /**
     * This is a function to format XML feed to a list
     * @param parser XmlPullParser object
     * @return List of XML data
     */
    public List parseNewsData(XmlPullParser parser) throws XmlPullParserException, IOException {

        List stories = new ArrayList<NewsStory>();

        while (  parser.next()!=XmlPullParser.END_DOCUMENT) {


            String name = (parser.getName() != null ? parser.getName() : "None");
            String des = (parser.getText() != null ? parser.getText() : "None");

            Log.d(TAG+" NAME", name);
            Log.d(TAG+" TEXT", des);

            if(name.equals("item")){
                ParserHelper ph =  new ParserHelper(parser);
                ph.readItem();
                stories.add(ph.getStory());
            }
        }




            //Log.d(TAG, name);
            //Log.d(TAG, des);






        return stories;

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
            in = OpenHttpConnection(url);
        }catch (IOException e){Log.d(TAG,e.getLocalizedMessage());}
        InputStreamReader isr = new InputStreamReader(in);
        String line = null;
        StringBuilder sBuilder = new StringBuilder();

        try{
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            xpp = factory.newPullParser();
            xpp.setInput(isr);
            xpp.next();
            parseNewsData(xpp);



        }catch(XmlPullParserException e ){
            e.getLocalizedMessage();
        }catch (IOException e1){
            e1.getLocalizedMessage();
        }




    }
}
