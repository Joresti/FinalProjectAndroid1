package com.example.jores.finalprojectandroid.foodandnutrition;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jores.finalprojectandroid.R;

import org.json.JSONArray;
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
    private EditText searchBar;
    private Button searchButton;
    private ProgressBar progressBar;

    private ArrayList<JSONObject> foodList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_food_list, container, false);

        searchBar = rootView.findViewById(R.id.food_search_bar);
        searchButton = rootView.findViewById(R.id.search_btn);
        progressBar = rootView.findViewById(R.id.loading_progress);

        foodListView = rootView.findViewById(R.id.food_list);
        foodListAdapter = new FoodListAdapter(getActivity());
        foodListView.setAdapter(foodListAdapter);

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

        return rootView;
    }

    private void showFoodList(JSONArray hintJSONArray){
        Log.i(FRAGMENT_NAME,"Showing hints list: " + hintJSONArray);
        //ArrayList<JSONObject> foodJSONList = new ArrayList<>();
        foodList.clear();
        for(int i = 0; i < hintJSONArray.length(); i++){
            try {
                foodList.add(hintJSONArray.getJSONObject(i).getJSONObject("food"));
            } catch(JSONException jse) {
                Log.e(FRAGMENT_NAME,jse.toString());
            }
        }
        foodListAdapter.notifyDataSetChanged();
        //foodListAdapter.setList(foodJSONList);
    }

    private void beginQuery(){
        if(searchBar.getText().toString().equals("")) {
            Toast t = Toast.makeText(this.getContext(), R.string.cant_do_blank_search, Toast.LENGTH_LONG);
            t.setGravity(Gravity.CENTER, 0, 0);
            t.show();
        }
        else {
            progressBar.setVisibility(View.VISIBLE);
            Log.i(FRAGMENT_NAME, "User searched for " + searchBar.getText());
            FoodQuerier.getFoodQuerier().setProgressBar(progressBar);
            FoodQuerier.getFoodQuerier().queryForString(searchBar.getText().toString(), (s) -> {
                JSONArray hintJSONArray = FoodQuerier.getFoodQuerier().getHintJSON();
                if (hintJSONArray.length() > 0)
                    showFoodList(hintJSONArray);
                else
                    Toast.makeText(this.getContext(), "No Results Found", Toast.LENGTH_SHORT).show();
            });
        }
    }

    @Override
    public void onAttach(Context ctx){
        super.onAttach(ctx);
        /*try {
            foodListAdapter.notifyDataSetChanged();
        } catch(Exception e) {
            Log.e(FRAGMENT_NAME,e.toString());
        }*/
    }

    class FoodListAdapter extends ArrayAdapter<JSONObject> {

        public FoodListAdapter(Context ctx) {
            super(ctx, 0);
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
            TextView hint = view.findViewById(R.id.label);
            try {
                hint.setText(foodList.get(position).getString("label"));
            } catch (JSONException je) {
                Log.e(FRAGMENT_NAME, je.toString());
                hint.setText("Unknown, JSON error");
            }
            hint.setOnClickListener((l)->{
                try {
                    JSONObject foodItem = getItem(position);
                    Log.i(FRAGMENT_NAME, "Displaying food info for " + foodItem.getString("label"));
                    FoodNutritionActivity.class.cast(FoodListFragment.this.getActivity()).showFoodInfo(foodItem);
                } catch (JSONException jE){
                    Log.e(FRAGMENT_NAME, jE.toString());
                }
            });
            return hint;
        }

        @Override
        public long getItemId(int index){
            return index;
        }
    }
}
