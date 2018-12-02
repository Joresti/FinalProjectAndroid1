package com.example.jores.finalprojectandroid.cbcnews;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.jores.finalprojectandroid.MenuInflationBaseActivity;
import com.example.jores.finalprojectandroid.R;
import java.util.ArrayList;


public class CBCNewsMain extends MenuInflationBaseActivity {

    protected static final String ACTIVITY_NAME = CBCNewsMain.class.getSimpleName();

    CBCNewsMain cbc = this;
    String TAG = "cbc NEWS MAIN";
    int databaseSize;
    final private  int REQUEST_INTERNET = 123;
    public String cbcUrl = "https://www.cbc.ca/cmlink/rss-topstories.xml";
    SQLiteDatabase database;
    ListView listView;
    ProgressBar pbar;
    Boolean breakingNews =true;
    ArrayList<NewsStoryDTO> newsArrayList;
    ArrayList<NewsStoryDTO> stagingArrayList;
    protected NewsAdapter newsAdapter;
    protected final static String TITLE= "title";
    protected final static String IMG_SRC = "imgSrc";
    protected final static String IMG_FILE_NAME = "imgFileName";
    protected final static String DESCRIPTION = "description";
    Button getNewsBtn;
    Button getSavedBtn;
    android.support.v7.widget.Toolbar toolbar;
    EditText searchBar;

    @Override
    public void onCreate(Bundle savedInstance) {
            super.onCreate(savedInstance);
            setContentView(R.layout.cbc_news_main);
            NewsDatabaseHelper.getInstance(this);
            database= NewsDatabaseHelper.getDatabase();
            toolbar=findViewById(R.id.main_toolbar);
            searchBar= findViewById(R.id.editTextCBC);


            newsArrayList = new ArrayList();
            listView = findViewById(R.id.listViewCBC);

            newsAdapter =  new NewsAdapter(this);
            listView.setAdapter(newsAdapter);
            listView.setOnItemClickListener( new ListView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView adapterView, View view, int i, long l){
                  if(true){

                        Intent intent = new Intent(CBCNewsMain.this, ArticleDetails.class);

                        NewsStoryDTO ns = (NewsStoryDTO) newsAdapter.getItem(i);


                        Log.d("LIST VIEW imgsrc", ns.getImgSrc());
                      Log.d("LIST VIEW imgsrc", ns.getPubDate());
                        intent.putExtra("Story",ns);
                        intent.putExtra("table", false);
                        startActivity(intent);
                    }
                }
            });

            Log.d("BEFORE", "LOADSTORIES");
            loadStoriesFromDatabase();

            pbar = findViewById(R.id.progressBarCBC);
            getNewsBtn = findViewById(R.id.getNews);
            getNewsBtn.setOnClickListener( new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    searchBar.setText("");
                    breakingNews=true;
                    Log.d("DATABASE SIZE", Integer.toString(cbc.databaseSize));
                    database.execSQL("DELETE FROM NewsStories;");
                    Snackbar.make(getNewsBtn,"Searching for breaking news!", Snackbar.LENGTH_SHORT).show();
                    if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(CBCNewsMain.this, new String[] {Manifest.permission.INTERNET}, REQUEST_INTERNET);
                    }else{new NewsDataAsycTask(pbar, cbc, getApplicationContext(), database).execute("https://www.cbc.ca/cmlink/rss-topstories"); }
                }
            });
            getSavedBtn = findViewById(R.id.getSavedStories);

            getSavedBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Snackbar.make(getSavedBtn,"Fetching saved stories",Snackbar.LENGTH_SHORT).show();
                    searchBar.setText("");
                    breakingNews=false;
                    loadSavedStoriesFromDatabase();
                }
            });
            searchBar.addTextChangedListener(searchHandler);


            Log.d(TAG, "in oncreate");
        }
        private final TextWatcher searchHandler =  new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                new SearchBarAsyncTask(getApplicationContext(), CBCNewsMain.this).execute();

            }
        };



        public void onHelpMenuClick(MenuItem mi){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.menu_btn_help);
            builder.setMessage(R.string.cbc_about_msg);

            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        };

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


        public void loadStoriesFromDatabase(){




            Log.d("IN", "LOADSTORIES");
            Cursor cursor =database.rawQuery("SELECT * FROM NewsStories;", null );
            databaseSize = cursor.getCount();
            cursor.close();


            cursor =database.rawQuery("SELECT * FROM NewsStories;", null );
            databaseSize = cursor.getCount();

            Log.d("IN", "LOADSTORIES");
            if (cursor ==null){
                Log.d("null", "NULL");
            }
            if (cursor !=null){
                Log.d("LOAD COUNT", Integer.toString(cursor.getCount()));
            }
            Cursor[] c = {cursor};
            newsArrayList = new ArrayList<NewsStoryDTO>();

            new LoadStories( getApplicationContext(),this).execute(c);

        }

        public void loadSavedStoriesFromDatabase(){
            Log.d("SAVED", "LOADSAVED");
            Cursor cursor =database.rawQuery("SELECT * FROM SavedStories;", null );


            if (cursor ==null){
                Log.d("null", "NULL");
            }
            if (cursor !=null){
                Log.d("SAVED", Integer.toString(cursor.getCount()));
            }
            Cursor[] c = {cursor};
            newsArrayList = new ArrayList<NewsStoryDTO>();

            new LoadStories(getApplicationContext(),this).execute(c);

        }

        /*
            Structure of class taken from Android Labs project
         */
        public class NewsAdapter extends ArrayAdapter<NewsStoryDTO> {

            public NewsAdapter(Context ctx) {
                super(ctx, 0);
            }

            public int getCount() {
                return newsArrayList.size();
            }

            public NewsStoryDTO getItem(int position) {
                return newsArrayList.get(position);
            }

            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater inflater = CBCNewsMain.this.getLayoutInflater();
                NewsStoryDTO story = getItem(position);
                Log.d("STORY", story.toString());
                View result = null;
                result = inflater.inflate(R.layout.news_story, null);
                TextView titleView = result.findViewById(R.id.title);
                titleView.setText(story.getTitle());
                TextView dateView = result.findViewById(R.id.date);
                dateView.setText(story.getPubDate());

                ImageView imgView = result.findViewById(R.id.image);
                imgView.setImageBitmap(story.getImage());

                return result;
            }


        }




    }










