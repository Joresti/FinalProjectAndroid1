package com.example.jores.finalprojectandroid.cbcnews;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.jores.finalprojectandroid.MenuInflationBaseActivity;
import com.example.jores.finalprojectandroid.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class CBCNewsMain extends MenuInflationBaseActivity {

    protected static final String ACTIVITY_NAME = CBCNewsMain.class.getSimpleName();

    String TAG = "CBC NEWS MAIN";
    final private  int REQUEST_INTERNET = 123;
    public String cbcUrl = "https://www.cbc.ca/cmlink/rss-topstories.xml";
    SQLiteDatabase database;
    ListView listView;
    ProgressBar pbar;
    ArrayList<NewsStory> newsArrayList;
    protected NewsAdapter newsAdapter;
    protected final static String TITLE= "title";
    protected final static String IMG_SRC = "imgSrc";
    protected final static String IMG_FILE_NAME = "imgFileName";
    protected final static String DESCRIPTION = "description";
    Button getNewsBtn;

        @Override
        public void onCreate(Bundle savedInstance) {
            super.onCreate(savedInstance);
            setContentView(R.layout.cbc_news_main);
            newsDatabaseHelper();

            //Adding the toolbar to the activity
            setSupportActionBar(findViewById(R.id.main_toolbar));

            newsArrayList = new ArrayList();
            listView = findViewById(R.id.listViewCBC);

            newsAdapter =  new NewsAdapter(this);
            listView.setAdapter(newsAdapter);

            Log.d("BEFORE", "LOADSTORIES");
            loadStoriesFromDatabase();

            pbar = findViewById(R.id.progressBarCBC);
            getNewsBtn = findViewById(R.id.getNews);
            getNewsBtn.setOnClickListener( new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(CBCNewsMain.this, new String[] {Manifest.permission.INTERNET}, REQUEST_INTERNET);
                    }else{new NewsData(pbar, newsArrayList, getApplicationContext(), newsAdapter, database).execute("https://www.cbc.ca/cmlink/rss-topstories"); }
                }

            });
        }

        public void onStart(){
            super.onStart();
            Log.i(TAG, "In the onStart() event");

        }
        public void onRestart(){

            super.onRestart();

            Log.i(TAG, "In the onRestart() event");
        }

        public void onResume(){
            super.onResume();
            Log.i(TAG, "In the onResume() event");
        }
        public void onPause(){
            super.onPause();
            Log.i(TAG, "In the onPause() event");
        }
        public void onStop(){
            super.onStop();
            Log.i(TAG, "In the onStop() event");
        }
        public void onDestroy(){
            super.onDestroy();

            Log.i(TAG, "In the onDestroy() event");
        }

    /**
     * Copying the pattern from AndroidLabs.  Function assigning database variable
     */
    public void newsDatabaseHelper(){
            NewsDatabaseHelper newsDatabaseHelper =  new NewsDatabaseHelper(this);
            database = newsDatabaseHelper.getWritableDatabase();
        }

     public void loadStoriesFromDatabase(){
         Log.d("IN", "LOADSTORIES");
        Cursor cursor =database.rawQuery("SELECT * FROM NewsStories;", null );
         Log.d("IN", "LOADSTORIES");
         while(cursor.moveToNext()){
             String title = cursor.getString(0);
             String imgSrc = cursor.getString(1);
             String imgFileName = cursor.getString(2);
             String description = cursor.getString(3);

             Log.d("DATA", title);
             Bitmap image =null;
             new GetImage(image).execute(imgFileName,imgSrc);
             //NewsStory story = new NewsStory(title,description,image);
             //newsArrayList.add(story);
         }
         newsAdapter.notifyDataSetChanged();
    }



    /*
        Structure of class taken from Android Labs project
     */
        public class NewsAdapter extends ArrayAdapter<NewsStory> {

            public NewsAdapter(Context ctx) {
                super(ctx, 0);
            }

            public int getCount() {
                return newsArrayList.size();
            }

            public NewsStory getItem(int position) {
                return newsArrayList.get(position);
            }

            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater inflater = CBCNewsMain.this.getLayoutInflater();
                NewsStory story = getItem(position);
                View result = null;
                result = inflater.inflate(R.layout.news_story, null);
                TextView titleView = result.findViewById(R.id.title);
                titleView.setText(story.getTitle());
                TextView storyView = result.findViewById(R.id.story);
                storyView.setText(story.getDescription());

                ImageView imgView = result.findViewById(R.id.image);
                //imgView.setImageBitmap(story.getImage());
                //publishProgress(new Integer[]{100});
                //imgView.setImageBitmap(image);
                return result;
            }


        }

        public class GetImage extends AsyncTask<String, Integer, String>{

            Bitmap i;

            public GetImage(Bitmap i){
                this.i = i;
            }

            @Override
            public String doInBackground(String...urls){
                i = getBitmap(urls[0],urls[1]);
                return "";
            }

            public Bitmap getBitmap(String imageStr, String imgUrl){
                Log.d("BITMAP" ,"IN BITMAP");

                Log.d("IMGURL", imgUrl);
                Bitmap image = null;
                if(fileExistence(imageStr)){
                    Log.i(TAG, "Found file " + imageStr);
                    FileInputStream fis = null;
                    try {
                        fis = openFileInput(imageStr);
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
                        FileOutputStream outputStream = openFileOutput(imageStr, Context.MODE_PRIVATE);
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
                File file = getFileStreamPath(fname);
                return file.exists();
            }

        }



    @Override
    public void onHelpMenuClick(MenuItem mi){
        Log.i(ACTIVITY_NAME,"Showing help menu");
    }
}










