package com.example.jores.finalprojectandroid.foodandnutrition;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.jores.finalprojectandroid.MenuInflationBaseActivity;
import com.example.jores.finalprojectandroid.R;

public class FoodNutritionActivity extends MenuInflationBaseActivity {

    private static final String ACTIVITY_NAME = FoodNutritionActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_nutrition);
    }

    @Override
    public void onHelpMenuClick(MenuItem mi){
        Log.i(ACTIVITY_NAME,"Showing help menu");
    }
}
