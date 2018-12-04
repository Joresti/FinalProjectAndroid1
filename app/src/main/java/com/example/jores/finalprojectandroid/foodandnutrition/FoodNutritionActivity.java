package com.example.jores.finalprojectandroid.foodandnutrition;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.jores.finalprojectandroid.MenuInflationBaseActivity;
import com.example.jores.finalprojectandroid.R;

import org.json.JSONObject;

public final class FoodNutritionActivity extends MenuInflationBaseActivity {
    private static final String ACTIVITY_NAME = FoodNutritionActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_nut);

        try {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_content, FoodListFragment.class.newInstance()).commit();
        } catch (Exception e) {
            Log.e(ACTIVITY_NAME,e.toString());
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        if(findViewById(R.id.main_toolbar) == null)
            Toast.makeText(this,"Toolbar not found",Toast.LENGTH_SHORT).show();
    }

    public void showFoodInfo(FoodData food){
        Fragment foodInfoFragment = FoodInfoFragment.newInstance(food);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, foodInfoFragment).addToBackStack(null).commit();
    }

    @Override
    public void onHelpMenuClick(MenuItem mi){
        Log.i(ACTIVITY_NAME,"Showing help menu");
        new AlertDialog.Builder(this)
                .setTitle(R.string.food_search_help_title)
                .setMessage(R.string.food_search_help_message)
                .setNeutralButton(R.string.okay_long,null)
                .show();
    }
}
