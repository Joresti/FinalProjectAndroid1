package com.example.jores.finalprojectandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.jores.finalprojectandroid.cbcnews.CBCNewsMain;
import com.example.jores.finalprojectandroid.foodandnutrition.FoodNutritionActivity;
import com.example.jores.finalprojectandroid.octranspo.OCTranspoMain;

/**
 * An activity that will add the ActionBar for our project to the activity. This means all the activities
 * in our project can extends from this class and they won't all have to do the work to set up the menu,
 * since this super class handles it for them.
 */
public abstract class MenuInflationBaseActivity extends AppCompatActivity {
    /**
     *
     */
    private static final String ACTIVITY_NAME = MenuInflationBaseActivity.class.getSimpleName();

    /**
     * Calls the super onPostCreate and sets the actionBar to the toolbar for our app.
     * @param bundle The bundle required by the superclass method. Unused by this class.
     */
    @Override
    protected void onPostCreate(Bundle bundle){
        super.onPostCreate(bundle);
        setSupportActionBar(findViewById(R.id.main_toolbar));
        Log.i("HELP","ME");
    }

    /**
     * Places an options menu into out created actionBar
     * @param menu A menu to be placed in our actionbar
     * @return Whether or not we want the menu displayed, which for us is a yes
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(ACTIVITY_NAME,"Inflating menu...");
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    /**
     * The handler for when the user clicks the "CBC" menu item
     * @param mi The clicked MenuItem
     */
    public void onCBCMenuClick(MenuItem mi){
        Log.i(ACTIVITY_NAME,"Starting CBCNewsMain activity...");
        Intent i = new Intent(MenuInflationBaseActivity.this, CBCNewsMain.class);
        startActivity(i);
    }

    /**
     * The handler for when the user clicks the "Food and Nutrition" menu item
     * @param mi The clicked MenuItem
     */
    public void onFANDBMenuClick(MenuItem mi){
        Log.i(ACTIVITY_NAME,"Starting FoodNutritionActivity activity...");
        Intent i = new Intent(MenuInflationBaseActivity.this, FoodNutritionActivity.class);
        startActivity(i);
    }

    /**
     * The handler for when the user clicks the "OCTranspo" menu item
     * @param mi The clicked MenuItem
     */
    public void onOCTMenuClick(MenuItem mi){
        Log.i(ACTIVITY_NAME,"Starting OCTranspoMain activity...");
        Intent i = new Intent(MenuInflationBaseActivity.this, OCTranspoMain.class);
        startActivity(i);
    }

    /**
     * The handler for when the user clicks the "Help" menu item.
     * <p>
     * By making this abstract each activity must override it and show their own help data.
     * @param mi The clicked MenuItem
     */
    public abstract void onHelpMenuClick(MenuItem mi);
}
