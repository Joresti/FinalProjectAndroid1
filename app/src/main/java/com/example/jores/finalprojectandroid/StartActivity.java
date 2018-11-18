package com.example.jores.finalprojectandroid;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.jores.finalprojectandroid.CBCNews.CBCNewsMain;

public class StartActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Button cbcBtn =  findViewById(R.id.cbcNews);
        Button foodBtn = findViewById(R.id.foodNutrition);
        Button movieBtn =  findViewById(R.id.movies);

        cbcBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(StartActivity.this, CBCNewsMain.class);
                startActivity(i);


            }
        });


    }
}
