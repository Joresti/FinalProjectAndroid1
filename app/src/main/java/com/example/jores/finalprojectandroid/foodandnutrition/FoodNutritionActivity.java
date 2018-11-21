package com.example.jores.finalprojectandroid.foodandnutrition;

import android.os.Bundle;

import com.example.jores.finalprojectandroid.MenuInflationBaseActivity;
import com.example.jores.finalprojectandroid.R;

public class FoodNutritionActivity extends MenuInflationBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_nutrition);

        //Adding the toolbar to the activity
        setSupportActionBar(findViewById(R.id.main_toolbar));
    }

}
