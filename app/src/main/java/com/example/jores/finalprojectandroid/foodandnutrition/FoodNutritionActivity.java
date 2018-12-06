package com.example.jores.finalprojectandroid.foodandnutrition;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.jores.finalprojectandroid.MenuInflationBaseActivity;
import com.example.jores.finalprojectandroid.R;

/**
 * The main activity for the Food and Nutrition Database portion of our project
 */
public final class FoodNutritionActivity extends MenuInflationBaseActivity {
    private static final String LOGGER_TAG = FoodNutritionActivity.class.getSimpleName();

    /**
     * Overrides the superclass method, sets the view for this activity and inserts the FoodListFragment
     * @param savedInstanceState Only used to be pass to the superclass method
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_nut);

        try {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_content, FoodListFragment.class.newInstance()).commit();
        } catch (Exception e) {
            Log.e(LOGGER_TAG,e.toString());
        }
    }

    /**
     * Overriding the MenuInflationBaseActivity method to show the help for this activity
     * @param mi The clicked MenuItem
     */
    @Override
    public void onHelpMenuClick(MenuItem mi){
        Log.i(LOGGER_TAG,"Showing help menu");
        new AlertDialog.Builder(this)
                .setTitle(R.string.food_search_help_title)
                .setMessage(R.string.food_search_help_message)
                .setNeutralButton(R.string.okay_long,null)
                .show();
    }
}
