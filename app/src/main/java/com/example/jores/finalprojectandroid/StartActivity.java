package com.example.jores.finalprojectandroid;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.jores.finalprojectandroid.CBCNews.CBCNewsMain;
import com.example.jores.finalprojectandroid.OCTranspo.OCTranspoMain;

public class StartActivity extends Activity {

    protected static final String ACTIVITY_NAME = "StartActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Button cbcBtn =  findViewById(R.id.cbcNews);
        Button foodBtn = findViewById(R.id.foodNutrition);
        Button ocpBtn =  findViewById(R.id.ocTranspoApp);



        cbcBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(StartActivity.this, CBCNewsMain.class);
                startActivity(i);
                Log.i(ACTIVITY_NAME, "Switching to CBCNewsMain");

            }
        });

        ocpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StartActivity.this, OCTranspoMain.class);
                startActivity(i);
                Log.i(ACTIVITY_NAME, "Switching to OCTranspoMain");
            }
        });


    }
}
