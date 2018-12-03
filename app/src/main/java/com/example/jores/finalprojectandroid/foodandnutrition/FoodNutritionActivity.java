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

    public void showFoodInfo(JSONObject food){
        Log.i(ACTIVITY_NAME,food.toString());
        try {
            Bundle b = new Bundle();
            if(food.has("foodId"))
                b.putString("foodId",food.getString("foodId"));
            if(food.has("label"))
                b.putString("label",food.getString("label"));
            if(food.has("nutrients")){
                JSONObject nut = food.getJSONObject("nutrients");
                if(nut.has("ENERC_KCAL"))
                    b.putString("ENERC_KCAL", nut.getString("ENERC_KCAL"));
                if(nut.has("PROCNT"))
                    b.putString("PROCNT", nut.getString("PROCNT"));
                if(nut.has("FAT"))
                    b.putString("FAT", nut.getString("FAT"));
                if(nut.has("CHOCDF"))
                    b.putString("CHOCDF", nut.getString("CHOCDF"));
                if(nut.has("FIBTG"))
                    b.putString("FIBTG", nut.getString("FIBTG"));
            }
            if(food.has("brand"))
                b.putString("brand",food.getString("brand"));
            if(food.has("category"))
                b.putString("category",food.getString("category"));
            if(food.has("foodContentsLabel"))
                b.putString("foodContentsLabel",food.getString("foodContentsLabel"));

            Fragment foodInfoFragment = new FoodInfoFragment();
            Fragment foodSearchFragment = getSupportFragmentManager().findFragmentById(R.id.main_content);
            foodInfoFragment.setArguments(b);
            getSupportFragmentManager().beginTransaction().replace(R.id.main_content, foodInfoFragment).addToBackStack(null).commit();//, foodInfoFragment).addToBackStack(null).commit();
            foodSearchFragment.onSaveInstanceState(null);
        } catch (Exception iE){
            Log.e(ACTIVITY_NAME, iE.toString());
        }
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
