package com.example.jores.finalprojectandroid.foodandnutrition;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

class FoodNutritionDBHelper extends SQLiteOpenHelper {
    FoodNutritionDBHelper first = new FoodNutritionDBHelper(null);
    FoodNutritionDBHelper second = new FoodNutritionDBHelper(null);

    private static final String DATABASE_NAME = "foodnut_db";
    private static final int VERSION_NUM = 1;

    private static final String FOOD_NUT_TABLE = "foodnut_t";
    private static final String FOOD_ID = "foodId";
    private static final String FOOD_LABEL = "label";
    private static final String ENERC_KCAL = "ENERC_KCAL";
    private static final String PROCNT = "PROCNT";
    private static final String FAT = "FAT";
    private static final String CHOCDF = "CHOCDF";
    private static final String FIBTG = "FIBTG";
    private static final String BRAND = "brand";
    private static final String CATEGORY = "category";
    private static final String FOOD_CONTENTS_LABEL = "foodContentsLabel";

    FoodNutritionDBHelper(Context ctx){super(ctx,DATABASE_NAME, null, VERSION_NUM);}

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(FoodNutritionDBHelper.class.getSimpleName(),"Creating food nutrition database database");
        db.execSQL("create table " + FOOD_NUT_TABLE + "( " +
                FOOD_ID + " text primary key, " +
                FOOD_LABEL + " text not null, " +
                ENERC_KCAL + " text not null, " +
                PROCNT + " text not null, " +
                FAT + " text not null, " +
                CHOCDF + " text not null, " +
                FIBTG + " text not null, " +
                BRAND + " text not null, " +
                CATEGORY + " text not null, " +
                FOOD_CONTENTS_LABEL + " text not null);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
