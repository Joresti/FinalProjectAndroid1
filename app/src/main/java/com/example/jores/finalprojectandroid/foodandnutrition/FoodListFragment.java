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
import android.widget.Toast;

import com.example.jores.finalprojectandroid.R;

import java.util.ArrayList;
import android.support.annotation.NonNull;

/**
 * A fragment to let the user search for food from the Database or check their Favourites list for foods
 */
public class FoodListFragment extends Fragment {
    private static final String LOGGER_TAG = FoodListFragment.class.getSimpleName();

    ListView foodListView = null;
    FoodListAdapter foodListAdapter = null;
    private EditText searchBar;
    private ProgressBar progressBar;
    private View foodListFooter;

    private ArrayList<FoodData> favouriteList = new ArrayList<>();
    private ArrayList<FoodData> searchList = new ArrayList<>();
    private ArrayList<FoodData> displayList = new ArrayList<>();

    private FoodQuerier foodQuerier = new FoodQuerier();

    /**
     * Overriding the superclass method, inflates the layout for this fragment and initializes the values and views within it.
     * @param inflater The inflater for this fragment
     * @param container The ViewGroup for the views in this fragment
     * @param savedInstanceState The saved bundled for this fragment
     * @return The view that represents this fragment
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        Log.i(LOGGER_TAG,"Starting onCreateView");

        View rootView = inflater.inflate(R.layout.fragment_food_list, container, false);

        searchBar = rootView.findViewById(R.id.food_search_bar);
        Button searchButton = rootView.findViewById(R.id.search_btn);
        Button favouriteButton = rootView.findViewById(R.id.favourite_btn);
        progressBar = rootView.findViewById(R.id.loading_progress);

        foodListView = rootView.findViewById(R.id.food_list);
        foodListAdapter = new FoodListAdapter(getActivity());
        foodListView.setAdapter(foodListAdapter);
        foodListFooter = inflater.inflate(R.layout.footer_food_list,container, false);

        Button loadMoreButton = foodListFooter.findViewById(R.id.food_list_footer_btn);
        loadMoreButton.setText(R.string.load_more_items);

        searchBar.setOnEditorActionListener((textView, actionID, event)->{
            if(actionID == EditorInfo.IME_ACTION_DONE) {
                beginQuery();
                return true;
            }
            return false;
        });

        searchButton.setOnClickListener((l)-> beginQuery());

        favouriteButton.setOnClickListener((l)->{
            loadFavourites();
            if(displayList != favouriteList)
                displayList = favouriteList;
        });

        loadMoreButton.setOnClickListener((l)->{
            if(foodQuerier.hasNextPage()) {
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(0);
                foodQuerier.queryNextPage();
            } else {
                Toast t = Toast.makeText(this.getContext(),R.string.no_more_items,Toast.LENGTH_SHORT);
                t.setGravity(Gravity.CENTER,0,0);
                t.show();
            }
        });

        progressBar.setMax(100);

        return rootView;
    }

    /**
     * Helper method to begin the query to the database. Reads the query from the EditText and checks for invalid searches.
     */
    private void beginQuery(){
        if(searchBar.getText().toString().equals("")) {
            Toast t = Toast.makeText(this.getContext(), R.string.cant_do_blank_search, Toast.LENGTH_LONG);
            t.setGravity(Gravity.CENTER, 0, 0);
            t.show();
        }
        else {
            if(foodListView.getFooterViewsCount() == 0)
                foodListView.addFooterView(foodListFooter);
            searchList.clear();
            foodListAdapter.notifyDataSetChanged();
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(0);
            Log.i(LOGGER_TAG, "User searched for " + searchBar.getText());
            foodQuerier.setProgressBar(progressBar);
            foodQuerier.queryForString(searchBar.getText().toString(), (s) -> {
                ArrayList<FoodData> newFoodList = foodQuerier.getFoodList();
                if(displayList != searchList)
                    displayList = searchList;
                if (newFoodList.size() > 0) {
                    Log.i(LOGGER_TAG,"Showing food list: " + newFoodList.toString());
                    searchList.addAll(newFoodList);
                    foodListAdapter.notifyDataSetChanged();
                }
                else
                    Toast.makeText(this.getContext(), "No Results Found", Toast.LENGTH_SHORT).show();
            });
        }
    }

    /**
     * Updates the list of favourite foods.
     */
    private void loadFavourites(){
        if(foodListView.getFooterViewsCount() > 0)
            foodListView.removeFooterView(foodListFooter);
        FavouriteFoodDBHelper favFoodDB = new FavouriteFoodDBHelper(this.getContext());
        SQLiteDatabase db = favFoodDB.getReadableDatabase();
        Cursor cursor = db.query(
                true,
                FavouriteFoodDBHelper.FOOD_NUT_TABLE,
                null,
                null,
                null,
                null, null, FavouriteFoodDBHelper.FOOD_LABEL, null
        );
        favouriteList.clear();
        foodListAdapter.notifyDataSetChanged();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            foodQuerier.cancel(true);
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
            favouriteList.add(newFoodData);
            if(displayList == favouriteList)
                foodListAdapter.notifyDataSetChanged();

            cursor.moveToNext();
        }
        cursor.close();
        db.close();
    }

    /**
     * Places a FoodInfoFragment in this Fragment's view when the user clicks on a food to see more info on it
     * @param foodData The FoodData to be displayed by the created FoodInfoFragment
     */
    private void showFoodInfo(FoodData foodData){
        this.getActivity().getSupportFragmentManager().beginTransaction().replace(this.getView().getId(), FoodInfoFragment.newInstance(foodData)).addToBackStack(null).commit();
        this.getActivity().getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            if (displayList == favouriteList) {
                loadFavourites();
                foodListAdapter.notifyDataSetChanged();
            } else if (foodQuerier.hasNextPage() && foodListView.getFooterViewsCount() == 0)
                foodListView.addFooterView(foodListFooter);
        });
    }

    /**
     * Private class ArrayAdapter for drawing the FoodData list
     */
    class FoodListAdapter extends ArrayAdapter<FoodData> {
        FoodListAdapter(Context ctx) {
            super(ctx, 0);
        }

        @Override
        public int getCount(){
            return displayList.size();
        }

        @Override
        public FoodData getItem(int index){
            return displayList.get(index);
        }

        /**
         *
         * @param position The position in the list this view represents
         * @param convertView Used for the ViewHolder pattern
         * @param parent The ViewGroup for the cretead view
         * @return The view that represents the item in the list
         */
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent){
            ViewHolderFood viewHolder;

            if(convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.layout_item_list_food, parent, false);

                viewHolder = new ViewHolderFood();
                viewHolder.label = convertView.findViewById(R.id.label);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolderFood) convertView.getTag();
            }

            viewHolder.label.setText(displayList.get(position).getLabel());
            viewHolder.label.setOnClickListener((l) -> {
                FoodData foodItem = getItem(position);
                Log.i(LOGGER_TAG, "Displaying food info for " + foodItem.getLabel());
                showFoodInfo(foodItem);
            });

            return convertView;
        }

        @Override
        public long getItemId(int index){
            return index;
        }
    }
}
