package com.example.jores.finalprojectandroid.foodandnutrition;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
    private Button favouriteButton;
    private Button loadMoreButton;
    private ProgressBar progressBar;

    private ArrayList<FoodData> foodList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        Log.i(FRAGMENT_NAME,"Starting onCreateView");

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_food_list, container, false);

        searchBar = rootView.findViewById(R.id.food_search_bar);
        searchButton = rootView.findViewById(R.id.search_btn);
        favouriteButton = rootView.findViewById(R.id.favourite_btn);
        progressBar = rootView.findViewById(R.id.loading_progress);

        foodListView = rootView.findViewById(R.id.food_list);
        foodListAdapter = new FoodListAdapter(getActivity());
        foodListView.setAdapter(foodListAdapter);
        foodListView.addFooterView(inflater.inflate(R.layout.footer_food_list,container, false));

        loadMoreButton = rootView.findViewById(R.id.food_list_footer_btn);
        loadMoreButton.setText(R.string.load_more_items);
        loadMoreButton.setVisibility(FoodQuerier.getFoodQuerier().hasNextPage() ? View.VISIBLE : View.INVISIBLE);

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

        favouriteButton.setOnClickListener((l)->{
            loadMoreButton.setVisibility(View.INVISIBLE);
            FavouriteFoodDBHelper favFoodDB = new FavouriteFoodDBHelper(this.getContext());
            SQLiteDatabase db = favFoodDB.getReadableDatabase();
            Cursor cursor = db.query(
                    true,
                    FavouriteFoodDBHelper.FOOD_NUT_TABLE,
                    null,
                    null,
                   null,
                    null, null, null, null
            );
            foodList.clear();
            foodListAdapter.notifyDataSetChanged();

            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                Log.i(FRAGMENT_NAME,"Reading favourite data");
                FoodQuerier.getFoodQuerier().cancel(true);
                FoodData newFoodData = new FoodData(
                    cursor.getString(cursor.getColumnIndex(FavouriteFoodDBHelper.FOOD_ID)),
                    cursor.getString(cursor.getColumnIndex(FavouriteFoodDBHelper.FOOD_LABEL)),
                    cursor.getString(cursor.getColumnIndex(FavouriteFoodDBHelper.BRAND)),
                    cursor.getString(cursor.getColumnIndex(FavouriteFoodDBHelper.CATEGORY)),
                    cursor.getString(cursor.getColumnIndex(FavouriteFoodDBHelper.FOOD_CONTENTS_LABEL)),
                    cursor.getDouble(cursor.getColumnIndex(FavouriteFoodDBHelper.ENERC_KCAL)),
                    cursor.getDouble(cursor.getColumnIndex(FavouriteFoodDBHelper.PROCNT)),
                    cursor.getDouble(cursor.getColumnIndex(FavouriteFoodDBHelper.FAT)),
                    cursor.getDouble(cursor.getColumnIndex(FavouriteFoodDBHelper.CHOCDF)),
                    cursor.getDouble(cursor.getColumnIndex(FavouriteFoodDBHelper.FIBTG))
                );
                foodList.add(newFoodData);
                foodListAdapter.notifyDataSetChanged();
                cursor.moveToNext();
            }
            cursor.close();
            db.close();
        });

        loadMoreButton.setOnClickListener((l)->{
            FoodQuerier.getFoodQuerier().queryNextPage();
        });

        progressBar.setMax(100);

        return rootView;
    }

    private void beginQuery(){
        if(searchBar.getText().toString().equals("")) {
            Toast t = Toast.makeText(this.getContext(), R.string.cant_do_blank_search, Toast.LENGTH_LONG);
            t.setGravity(Gravity.CENTER, 0, 0);
            t.show();
        }
        else {
            foodList.clear();
            foodListAdapter.notifyDataSetChanged();
            progressBar.setVisibility(View.VISIBLE);
            Log.i(FRAGMENT_NAME, "User searched for " + searchBar.getText());
            FoodQuerier.getFoodQuerier().setProgressBar(progressBar);
            FoodQuerier.getFoodQuerier().queryForString(searchBar.getText().toString(), (s) -> {
                ArrayList<FoodData> newFoodList = FoodQuerier.getFoodQuerier().getFoodList();
                if (newFoodList.size() > 0) {
                    Log.i(FRAGMENT_NAME,"Showing food list: " + newFoodList.toString());
                    foodList.addAll(newFoodList);
                    foodListAdapter.notifyDataSetChanged();
                    loadMoreButton.setVisibility(FoodQuerier.getFoodQuerier().hasNextPage() ? View.VISIBLE : View.INVISIBLE);
                }
                else
                    Toast.makeText(this.getContext(), "No Results Found", Toast.LENGTH_SHORT).show();
            });
        }
    }

    class FoodListAdapter extends ArrayAdapter<FoodData> {

        public FoodListAdapter(Context ctx) {
            super(ctx, 0);
        }

        @Override
        public int getCount(){
            return foodList.size();
        }

        @Override
        public FoodData getItem(int index){
            return foodList.get(index);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            View view = getLayoutInflater().inflate(R.layout.layout_item_list_food, parent, false);
            TextView hint = view.findViewById(R.id.label);
            hint.setText(foodList.get(position).getLabel());
            hint.setOnClickListener((l)->{
                FoodData foodItem = getItem(position);
                Log.i(FRAGMENT_NAME, "Displaying food info for " + foodItem.getLabel());
                FoodNutritionActivity.class.cast(FoodListFragment.this.getActivity()).showFoodInfo(foodItem);
            });
            return view;
        }

        @Override
        public long getItemId(int index){
            return index;
        }
    }
}
