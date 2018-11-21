package com.example.jores.finalprojectandroid.cbcnews;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.jores.finalprojectandroid.MenuInflationBaseActivity;
import com.example.jores.finalprojectandroid.R;

import java.util.ArrayList;

public class CBCNewsMain extends MenuInflationBaseActivity {

        final private  int REQUEST_INTERNET = 123;
        public String cbcUrl = "https://www.cbc.ca/cmlink/rss-topstories.xml";

        ListView listView;
        ProgressBar pbar;
        ArrayList<String> newsArrayList;

        @Override
        public void onCreate(Bundle savedInstance) {
            super.onCreate(savedInstance);
            setContentView(R.layout.cbc_news_main);

            //Adding the toolbar to the activity
            setSupportActionBar(findViewById(R.id.main_toolbar));

            newsArrayList = new ArrayList();
            listView = findViewById(R.id.listViewCBC);
            pbar = findViewById(R.id.progressBarCBC);


            if(ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.INTERNET}, REQUEST_INTERNET);
            }else{new NewsData(pbar).execute("https://www.cbc.ca/cmlink/rss-topstories"); }

        }

        /*
        Structure of class taken from Android Labs project
     */
        public class NewsAdapter extends ArrayAdapter<String> {

            public NewsAdapter(Context ctx) {
                super(ctx, 0);
            }

            public int getCount() {
                return newsArrayList.size();
            }

            public String getItem(int position) {
                return newsArrayList.get(position);
            }

            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater inflater = CBCNewsMain.this.getLayoutInflater();
                View result = null;
                result = inflater.inflate(R.layout.news_story, null);
                return result;
            }

            //public long getStoryId(int position){

            //}


        }






}
