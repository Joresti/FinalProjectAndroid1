package com.example.jores.finalprojectandroid.cbcnews;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Primarily adapted from Android Labs - Lab5
 * Also: https://www.androiddesignpatterns.com/2012/05/correctly-managing-your-sqlite-database.html
 */
public class NewsDatabaseHelper extends SQLiteOpenHelper {

    private static SQLiteDatabase database;
    private static NewsDatabaseHelper sInstance;

    public static String DATABASE_NAME = "NewsDataAsycTask";
    public static int VERSION_NUM = 1;
    protected final static String KEY_ID = "key_id";
    protected final static String TITLE= "title";
    protected final static String IMG_SRC = "imgSrc";
    protected final static String IMG_FILE_NAME = "imgFileName";
    protected final static String DESCRIPTION = "description";
    protected final static String LINK = "link";
    protected final static String DATE = "date";
    protected final static String AUTHOR = "author";
    public String TAG = "NEWS DATABASE HELPER";



    public static synchronized NewsDatabaseHelper getInstance(Context context){
        if(sInstance ==null){
            sInstance = new NewsDatabaseHelper(context.getApplicationContext());
        }
        database = sInstance.getWritableDatabase();

        return sInstance;
    }

    private NewsDatabaseHelper(Context ctx){
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }



    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase){

        sqLiteDatabase.execSQL("CREATE TABLE NewsStories ( "+TITLE+" TEXT, " +IMG_SRC+ " TEXT, " +IMG_FILE_NAME + " TEXT, "+ DESCRIPTION + " TEXT, "+LINK+" TEXT, "+DATE+" TEXT, "+AUTHOR+" TEXT );" );
        sqLiteDatabase.execSQL("CREATE TABLE SavedStories ( "+TITLE+" TEXT, " +IMG_SRC+ " TEXT, " +IMG_FILE_NAME + " TEXT, "+ DESCRIPTION + " TEXT, "+LINK+" TEXT, "+DATE+" TEXT, "+AUTHOR+" TEXT );" );
        Log.i(TAG, "in on CREATE");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVer, int newVer){
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS NewsStories;");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS SavedStories;");
        this.onCreate(sqLiteDatabase);
        Log.i(TAG, "Calling onUpgrade");
    }
    @Override
    public void onDowngrade(SQLiteDatabase sqLiteDatabase, int oldVer, int newVer){
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS NewsStories;");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS SavedStories;");
        this.onCreate(sqLiteDatabase);
        Log.i(TAG, "Calling onUpgrade");
    }


    public static SQLiteDatabase getDatabase(){
        return database;
    }



}
