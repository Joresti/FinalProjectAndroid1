package com.example.jores.finalprojectandroid;

import android.content.Intent;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.jores.finalprojectandroid.cbcnews.CBCNewsMain;
import com.example.jores.finalprojectandroid.foodandnutrition.FoodNutritionActivity;
import com.example.jores.finalprojectandroid.CBCNews.CBCNewsMain;
import com.example.jores.finalprojectandroid.OCTranspo.OCTranspoMain;

public class StartActivity extends MenuInflationBaseActivity {

    Button cbcBtn;
    Button foodBtn;
    Button movieBtn;
    Toolbar mainToolBar;

    protected static final String ACTIVITY_NAME = "StartActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        cbcBtn =  findViewById(R.id.cbcNews);
        foodBtn = findViewById(R.id.foodNutrition);
        movieBtn =  findViewById(R.id.movies);
        mainToolBar = findViewById(R.id.main_toolbar);

        setSupportActionBar(mainToolBar);

        cbcBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent i = new Intent(StartActivity.this, CBCNewsMain.class);
            startActivity(i);
            }
        });

        foodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent i = new Intent(StartActivity.this, FoodNutritionActivity.class);
            startActivity(i);
            }
        });

    }
}
