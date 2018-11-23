package com.example.jores.finalprojectandroid.foodandnutrition;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jores.finalprojectandroid.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FoodListFragment extends Fragment {
    private static final String FRAGMENT_NAME = FoodListFragment.class.getSimpleName();

    ListView foodListView = null;
    FoodListAdapter foodListAdapter = null;

    public void setFoodList(ArrayList<JSONObject> foodList){
        foodListAdapter.setList(foodList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_food_list, container, false);

        foodListView = rootView.findViewById(R.id.food_list);
        foodListAdapter = new FoodListAdapter(getActivity());
        foodListView.setAdapter(foodListAdapter);

        return rootView;
    }

    class FoodListAdapter extends ArrayAdapter<JSONObject> {
        private ArrayList<JSONObject> foodList = new ArrayList<>();

        public FoodListAdapter(Context ctx) {
            super(ctx, 0);
        }

        public void setList(ArrayList<JSONObject> foodList){
            this.foodList.clear();
            this.foodList.addAll(foodList);
            notifyDataSetChanged();
        }

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
            View view = getLayoutInflater().inflate(R.layout.layout_item_list_food, null);
            TextView hint = view.findViewById(R.id.label);//new TextView(this.getContext());
            try {
                hint.setText(foodList.get(position).getString("label"));
            } catch (JSONException je) {
                Log.e(FRAGMENT_NAME, je.toString());
                hint.setText("Unknown, JSON error");
            }
            hint.setOnClickListener((l)->{
                Log.i(FRAGMENT_NAME,"This is where we show the food...");
            });
            return hint;
        }

        @Override
        public long getItemId(int index){
            return index;
        }
    }
}
