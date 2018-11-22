package com.example.jores.finalprojectandroid.foodandnutrition;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

class FoodListAdapter extends ArrayAdapter<JSONObject> {

    private static final String CLASS_NAME = FoodListAdapter.class.getSimpleName();

    private static ArrayList<JSONObject> foodList = null;

    public FoodListAdapter(Context ctx) {
        super(ctx, 0);
    }

    public void setList(ArrayList<JSONObject> newList){foodList = newList;}

    @Override
    public int getCount(){
        return foodList.size();
    }

    @Override
    public JSONObject getItem(int index){
        return foodList.get(index);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        TextView hint = new TextView(this.getContext());
        try {
            hint.setText(foodList.get(position).getString("label"));
        } catch (JSONException je) {
            Log.e(CLASS_NAME, je.toString());
            hint.setText("Unknown, JSON error");
        }
        hint.setOnClickListener((l)->{
            //setFoodInfo(hintList.get(position));
            Log.i(CLASS_NAME,"This is where we show the food...");
        });
        return hint;
    }

    @Override
    public long getItemId(int index){
        return index;
    }
}
