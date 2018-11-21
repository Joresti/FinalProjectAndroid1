package com.example.jores.finalprojectandroid.OCTranspo;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.jores.finalprojectandroid.R;

public class OCTranspoMain extends AppCompatActivity {

   // private Snackbar snackBar;
    private String ACTIVITY_MESSAGE = "OCTranspoMain";
    private View container;
    private Toolbar toolBar;
    private ImageView imageView;
    Button button;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_octranspo_main);

        //create SnackBar to display when OCTranspo App is clicked on
        View view = findViewById(android.R.id.content);
        Snackbar.make(view, "CST 2335 ChadSouster OC Transpo Route App", Snackbar.LENGTH_LONG)
                .show();

        //Currently crashing APP
        //Load in created layout ToolBar from activity_octranspo_main
        //toolBar = findViewById(R.id.toolBar);
        //setSupportActionBar(toolBar);

        button = findViewById(R.id.startRouteButton);
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

        imageView = findViewById(R.id.imageView_ocTranspoMain);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(OCTranspoMain.this)
                        .setTitle("Clicking the image has no effect. Click the Start Route button!" )
                        .setNegativeButton("Close", null)
                .show();
            }
        });




    }
}
