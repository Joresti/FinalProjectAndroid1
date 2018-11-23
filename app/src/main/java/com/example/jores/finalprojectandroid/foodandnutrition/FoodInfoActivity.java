package com.example.jores.finalprojectandroid.foodandnutrition;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.jores.finalprojectandroid.MenuInflationBaseActivity;
import com.example.jores.finalprojectandroid.R;

public class FoodInfoActivity extends MenuInflationBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_info);
    }

    @Override
    public void onHelpMenuClick(MenuItem mi){

    }
}
