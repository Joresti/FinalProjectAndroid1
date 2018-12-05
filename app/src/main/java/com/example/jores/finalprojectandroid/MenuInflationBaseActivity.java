package com.example.jores.finalprojectandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.jores.finalprojectandroid.cbcnews.CBCNewsMain;
import com.example.jores.finalprojectandroid.foodandnutrition.FoodNutritionActivity;
import com.example.jores.finalprojectandroid.octranspo.OCTranspoMain;

public abstract class MenuInflationBaseActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    public void onCBCMenuClick(MenuItem mi){
        Intent i = new Intent(MenuInflationBaseActivity.this, CBCNewsMain.class);
        startActivity(i);
    }

    public void onFANDBMenuClick(MenuItem mi){
        Intent i = new Intent(MenuInflationBaseActivity.this, FoodNutritionActivity.class);
        startActivity(i);
    }

    public void onOCTMenuClick(MenuItem mi){
        Intent i = new Intent(MenuInflationBaseActivity.this, OCTranspoMain.class);
        startActivity(i);
    }

}
