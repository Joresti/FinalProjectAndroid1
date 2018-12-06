package com.example.jores.finalprojectandroid.octranspo;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.jores.finalprojectandroid.MenuInflationBaseActivity;
import com.example.jores.finalprojectandroid.R;

public class OCTranspoMain extends MenuInflationBaseActivity {

    private final String ACTIVITY_MESSAGE = "OCTranspoMainActivity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_octranspo_main);

        //Adding the toolbar to the activity
        setSupportActionBar(findViewById(R.id.main_toolbar));

        //create SnackBar to display when OCTranspo App is clicked on
        View view = findViewById(android.R.id.content);
        Snackbar.make(view, R.string.ocTranspoSnackBar, Snackbar.LENGTH_LONG)
                .setAction(R.string.closeToast, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                })
                .setActionTextColor(getResources().getColor(android.R.color.holo_green_light))
                .show();



        Button button = findViewById(R.id.startRouteButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(ACTIVITY_MESSAGE, "User clicked Start Route button");

                CharSequence toastMessage = getString(R.string.startRouteButtonToastMessage);
                Intent intent = new Intent(OCTranspoMain.this, BusStops.class );
                Toast.makeText(OCTranspoMain.this, toastMessage, Toast.LENGTH_LONG).show();
                startActivity(intent);
            }
        });


        ImageView imageView = findViewById(R.id.imageView_ocTranspoMain);
        imageView.setOnClickListener((v) -> new AlertDialog.Builder(OCTranspoMain.this)
                .setTitle(R.string.clickStartRouteToBegin )
                .setNegativeButton("Close", null)
                .show());

    }
}
