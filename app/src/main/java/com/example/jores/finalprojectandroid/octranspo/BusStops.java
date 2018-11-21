package com.example.jores.finalprojectandroid.octranspo;
import com.example.jores.finalprojectandroid.MenuInflationBaseActivity;
import com.example.jores.finalprojectandroid.R;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

public class BusStops extends MenuInflationBaseActivity {

    private static final String ACTIVITY_NAME = BusStops.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_stops);
    }

    @Override
    public void onHelpMenuClick(MenuItem mi){
        Log.i(ACTIVITY_NAME,"Showing help menu");
    }
}
