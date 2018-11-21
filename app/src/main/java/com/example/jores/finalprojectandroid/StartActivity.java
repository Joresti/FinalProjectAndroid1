package com.example.jores.finalprojectandroid;

import android.content.ClipData;
import android.content.Intent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.jores.finalprojectandroid.CBCNews.CBCNewsMain;
import com.example.jores.finalprojectandroid.foodandnutrition.FoodNutritionActivity;

public class StartActivity extends AppCompatActivity {

    Button cbcBtn;
    Button foodBtn;
    MenuItem foodMI;
    Button movieBtn;
    Toolbar mainToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        cbcBtn =  findViewById(R.id.cbcNews);
        foodBtn = findViewById(R.id.foodNutrition);
        foodMI = findViewById(R.id.menu_btn_fand);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    public void onFoodAndNutritionMenuClick(MenuItem mi){
        Intent i = new Intent(StartActivity.this, FoodNutritionActivity.class);
        startActivity(i);
    }
}
