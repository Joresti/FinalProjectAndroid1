package com.example.jores.finalprojectandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.jores.finalprojectandroid.cbcnews.CBCNewsMain;
import com.example.jores.finalprojectandroid.foodandnutrition.FoodNutritionActivity;
import com.example.jores.finalprojectandroid.octranspo.OCTranspoMain;

public abstract class MenuInflationBaseActivity extends AppCompatActivity {

    private static final String ACTIVITY_NAME = MenuInflationBaseActivity.class.getSimpleName();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        Log.i(ACTIVITY_NAME,"Inflating menu...");
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);

        return true;
    }

    public void onCBCMenuClick(MenuItem mi){
        Log.i(ACTIVITY_NAME,"Starting CBCNewsMain activity...");
        Intent i = new Intent(MenuInflationBaseActivity.this, CBCNewsMain.class);
        startActivity(i);
    }

    public void onFANDBMenuClick(MenuItem mi){
        Log.i(ACTIVITY_NAME,"Starting FoodNutritionActivity activity...");
        Intent i = new Intent(MenuInflationBaseActivity.this, FoodNutritionActivity.class);
        startActivity(i);
    }

    public void onOCTMenuClick(MenuItem mi){
        Log.i(ACTIVITY_NAME,"Starting OCTranspoMain activity...");
        Intent i = new Intent(MenuInflationBaseActivity.this, OCTranspoMain.class);
        startActivity(i);
    }

    public abstract void onHelpMenuClick(MenuItem mi);
}
