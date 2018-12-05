package com.example.jores.finalprojectandroid.foodandnutrition;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * A helper class to assist in managing the DB of favourite foods
 */
class FavouriteFoodDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "foodnut_db";
    private static final int VERSION_NUM = 1;

    static final String FOOD_NUT_TABLE = "foodnut_t";
    static final String FOOD_ID = "foodId";
    static final String FOOD_LABEL = "label";
    static final String ENERC_KCAL = "ENERC_KCAL";
    static final String PROCNT = "PROCNT";
    static final String FAT = "FAT";
    static final String CHOCDF = "CHOCDF";
    static final String FIBTG = "FIBTG";
    static final String BRAND = "brand";
    static final String CATEGORY = "category";
    static final String FOOD_CONTENTS_LABEL = "foodContentsLabel";

    FavouriteFoodDBHelper(Context ctx){super(ctx,DATABASE_NAME, null, VERSION_NUM);}

    /**
     * Overriding the superclass method, initializes the Database and creates the table
     * @param db The Database to create the tables in
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
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

    /**
     * Overriding the superclass method, destroys the table
     * @param db The Database to destroy the old table in
     * @param oldVersion The old version number
     * @param newVersion The new version number
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FOOD_NUT_TABLE);
        onCreate(db);
    }

    /**
     * Overriding the superclass method, destroys the table
     * @param db The Database to destroy the old table in
     * @param oldVersion The old version number
     * @param newVersion The new version number
     */
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FOOD_NUT_TABLE);
        onCreate(db);
    }
}
