package com.example.jores.finalprojectandroid.foodandnutrition;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.jores.finalprojectandroid.MenuInflationBaseActivity;
import com.example.jores.finalprojectandroid.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public final class FoodNutritionActivity extends MenuInflationBaseActivity {
    private static final String ACTIVITY_NAME = FoodNutritionActivity.class.getSimpleName();

    private SharedPreferences preferences;
    private EditText searchBar;
    private Button searchButton;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        preferences = getSharedPreferences("", Context.MODE_PRIVATE);

        searchBar = findViewById(R.id.food_search_bar);
        searchButton = findViewById(R.id.search_btn);
        progressBar = findViewById(R.id.loading_progress);

        searchBar.setOnEditorActionListener((textView, actionID, event)->{
            if(actionID == EditorInfo.IME_ACTION_DONE) {
                beginQuery();
                return true;
            }
            return false;
        });

        searchButton.setOnClickListener((l)->{
            beginQuery();
        });

        progressBar.setMax(100);
    }

    @Override
    protected void onStart(){
        super.onStart();
        if(findViewById(R.id.main_toolbar) == null)
            Toast.makeText(this,"Toolbar not found",Toast.LENGTH_SHORT).show();
    }

    private void showFoodList(JSONArray hintJSONArray){
        Log.i(ACTIVITY_NAME,"Showing hints list: " + hintJSONArray);
        ArrayList<JSONObject> JSONObjectArrayList = new ArrayList<>();
        for(int i = 0; i < hintJSONArray.length(); i++){
            try {
                JSONObjectArrayList.add(hintJSONArray.getJSONObject(i).getJSONObject("food"));
            } catch(JSONException jse) {
                Log.e(ACTIVITY_NAME,jse.toString());
            }
        }
        FoodListFragment foodListFragment = FoodListFragment.class.cast(getSupportFragmentManager().findFragmentById(R.id.main_fragment));
        foodListFragment.setFoodList(JSONObjectArrayList);
    }

    private void beginQuery(){
        if(searchBar.getText().toString().equals("")) {
            Toast t = Toast.makeText(this, R.string.cant_do_blank_search, Toast.LENGTH_LONG);
            t.setGravity(Gravity.CENTER, 0, 0);
            t.show();
        }
        else {
            progressBar.setVisibility(View.VISIBLE);
            Log.i(ACTIVITY_NAME, "User searched for " + searchBar.getText());
            FoodQuerier.getFoodQuerier().setProgressBar(progressBar);
            FoodQuerier.getFoodQuerier().queryForString(searchBar.getText().toString(), (s) -> {
                JSONArray hintJSONArray = FoodQuerier.getFoodQuerier().getHintJSON();
                if (hintJSONArray.length() > 0)
                    showFoodList(hintJSONArray);
                else
                    Toast.makeText(this, "No Results Found", Toast.LENGTH_SHORT).show();
            });
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
