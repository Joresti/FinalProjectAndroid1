package com.example.jores.finalprojectandroid.octranspo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class RouteDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "StoredRouteNumbers.db";
    private static final int VERSION_NUMBER = 1;
    public static final String TABLE_NAME = "RouteTable";
    public static final String KEY_MESSAGE = "RouteColumn";
    public static final String KEY_ID = "ID";


    /**
     *
     * @param context
     */
    public RouteDatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION_NUMBER);
    }

    /**
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("RouteDatabaseHelper", "Calling onCreate");
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_MESSAGE + " TEXT);";
        db.execSQL(CREATE_TABLE);
    }

    /**
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("RouteDatabaseHelper", "Calling onUpgrade, old Version = "
                + oldVersion + " newVersion = " + newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    /**
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("RouteDatabaseHelper", "Calling onDowngrade, old Version = "
                + oldVersion + " newVersion = " + newVersion);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    /**
     *
     * @param id
     */
    public void deleteRoute(String id){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
          sqLiteDatabase.delete(TABLE_NAME, KEY_MESSAGE + " = '" + id + "'", null);
    }

}
