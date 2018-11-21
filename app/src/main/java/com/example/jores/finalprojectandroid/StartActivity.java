package com.example.jores.finalprojectandroid;

import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;

import com.example.jores.finalprojectandroid.cbcnews.CBCNewsMain;
import com.example.jores.finalprojectandroid.foodandnutrition.FoodNutritionActivity;
import com.example.jores.finalprojectandroid.octranspo.OCTranspoMain;

public class StartActivity extends MenuInflationBaseActivity {

    protected static final String ACTIVITY_NAME = StartActivity.class.getSimpleName();

    Button cbcBtn;
    Button foodBtn;
    Button ocBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        cbcBtn =  findViewById(R.id.cbcNews);
        foodBtn = findViewById(R.id.foodNutrition);
        ocBtn =  findViewById(R.id.ocTranspoApp);

        cbcBtn.setOnClickListener((view) -> {
            Intent i = new Intent(StartActivity.this, CBCNewsMain.class);
            Log.i(ACTIVITY_NAME,"Starting activity " + CBCNewsMain.class.getSimpleName());
            startActivity(i);
        });

        foodBtn.setOnClickListener((view) -> {
            Intent i = new Intent(StartActivity.this, FoodNutritionActivity.class);
            Log.i(ACTIVITY_NAME,"Starting activity " + FoodNutritionActivity.class.getSimpleName());
            startActivity(i);
        });

        ocBtn.setOnClickListener((view) -> {
            Intent i = new Intent(StartActivity.this, OCTranspoMain.class);
            Log.i(ACTIVITY_NAME,"Starting activity " + OCTranspoMain.class.getSimpleName());
            startActivity(i);
        });

    }

    @Override
    public void onHelpMenuClick(MenuItem mi){
        Log.i(ACTIVITY_NAME,"Showing help menu");
    }
}
