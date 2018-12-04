package com.example.jores.finalprojectandroid.foodandnutrition;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

class FavouriteFoodDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "foodnut_db";
    private static final int VERSION_NUM = 1;

    public static final String FOOD_NUT_TABLE = "foodnut_t";
    public static final String FOOD_ID = "foodId";
    public static final String FOOD_LABEL = "label";
    public static final String ENERC_KCAL = "ENERC_KCAL";
    public static final String PROCNT = "PROCNT";
    public static final String FAT = "FAT";
    public static final String CHOCDF = "CHOCDF";
    public static final String FIBTG = "FIBTG";
    public static final String BRAND = "brand";
    public static final String CATEGORY = "category";
    public static final String FOOD_CONTENTS_LABEL = "foodContentsLabel";

    FavouriteFoodDBHelper(Context ctx){super(ctx,DATABASE_NAME, null, VERSION_NUM);}

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + FOOD_NUT_TABLE);
        Log.i(FavouriteFoodDBHelper.class.getSimpleName(),"Creating food nutrition database database");
        db.execSQL("create table " + FOOD_NUT_TABLE + "( " +
                FOOD_ID + " text primary key, " +
                FOOD_LABEL + " text not null, " +
                ENERC_KCAL + " real, " +
                PROCNT + " real, " +
                FAT + " real, " +
                CHOCDF + " real, " +
                FIBTG + " real, " +
                BRAND + " text, " +
                CATEGORY + " text, " +
                FOOD_CONTENTS_LABEL + " text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FOOD_NUT_TABLE);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FOOD_NUT_TABLE);
        onCreate(db);
    }
}
