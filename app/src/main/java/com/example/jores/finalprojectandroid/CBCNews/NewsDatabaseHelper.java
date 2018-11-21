package com.example.jores.finalprojectandroid.CBCNews;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Primarily adapted from Android Labs - Lab5
 */
public class NewsDatabaseHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "NewsData";
    public static int VERSION_NUM = 1;
    protected final static String KEY_ID = "key_id";
    protected final static String TITLE= "title";
    protected final static String IMG_SRC = "imgSrc";
    protected final static String IMG_FILE_NAME = "imgFileName";
    protected final static String DESCRIPTION = "description";
    public String TAG = "NEWS DATABASE HELPER";

    public NewsDatabaseHelper(Context ctx){
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase){
        sqLiteDatabase.execSQL("CREATE TABLE NewsStories ( "+KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+TITLE+" TEXT, " +IMG_SRC+ " TEXT, " +IMG_FILE_NAME + " TEXT, "+ DESCRIPTION + " TEXT );" );
        Log.i(TAG, "in on CREATE");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVer, int newVer){
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS NewsStories;");
        this.onCreate(sqLiteDatabase);
        Log.i(TAG, "Calling onUpgrade");
    }
}
