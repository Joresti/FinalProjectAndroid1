package com.example.jores.finalprojectandroid.foodandnutrition;


import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.jores.finalprojectandroid.R;

import java.text.DecimalFormat;

/**
 * A fragment to display the info of a supplied FoodData
 */
public class FoodInfoFragment extends Fragment {
    private static final String FRAGMENT_NAME = FoodInfoFragment.class.getSimpleName();

    private FavouriteFoodDBHelper favFoodDB;

    /**
     * Create a new instance of this fragment based off the FoodData provided
     * @param foodData The data to abse this fragment off of
     * @return A new instance of this fragment the arguments appropriately set
     */
    public static FoodInfoFragment newInstance(FoodData foodData){
        FoodInfoFragment fif = new FoodInfoFragment();

        Bundle b = new Bundle();
        b.putString("foodId",foodData.getFoodID());
        b.putString("label",foodData.getLabel());
        b.putDouble("ENERC_KCAL", foodData.getkCal());
        b.putDouble("PROCNT", foodData.getProcnt());
        b.putDouble("FAT", foodData.getFat());
        b.putDouble("CHOCDF", foodData.getChocdf());
        b.putDouble("FIBTG", foodData.getFibtg());
        b.putString("brand", foodData.getBrand());
        b.putString("category", foodData.getCategory());
        b.putString("foodContentsLabel", foodData.getContents());

        fif.setArguments(b);

        return fif;
    }

    /**
     * Overridng the super class method, creates and populates the view with the data that must be supplied in the arguments
     * @param inflater A layout inflater to inflate the view
     * @param container The ViewGroup for the created view
     * @param savedInstanceState Saved properties
     * @return The view created for this fragment
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i(FRAGMENT_NAME,"Starting onCreateView");
        View view = inflater.inflate(R.layout.fragment_food_info, container, false);

        TextView foodView = view.findViewById(R.id.food_label);
        TextView brandView = view.findViewById(R.id.brand);
        TextView categoryView = view.findViewById(R.id.category);
        TextView contentsView = view.findViewById(R.id.contents);
        TextView kCalView = view.findViewById(R.id.enerc_kcal);
        TextView procntView = view.findViewById(R.id.procnt);
        TextView fatView = view.findViewById(R.id.fat);
        TextView chocdfView = view.findViewById(R.id.chocdf);
        TextView fibtgView = view.findViewById(R.id.fibtg);
        CheckBox favouriteCheckBox = view.findViewById(R.id.favourite_check);

        String label = getArguments().getString("label");
        String brand = getArguments().getString("brand");
        String category = getArguments().getString("category");
        String contents = getArguments().getString("contents");
        Double kCal = getArguments().getDouble("ENERC_KCAL");
        Double procnt = getArguments().getDouble("PROCNT");
        Double fat = getArguments().getDouble("FAT");
        Double chocdf = getArguments().getDouble("CHOCDF");
        Double fibtg = getArguments().getDouble("FIBTG");

        DecimalFormat df = new DecimalFormat("0.00");

        foodView.setText((label == null ? "Why am I null" : label));
        brandView.setText((brand == null ? "N/A" : brand));
        categoryView.setText((category == null ? "N/A" : category));
        contentsView.setText((contents == null ? "N/A" : contents));
        kCalView.setText(df.format(kCal));
        procntView.setText(df.format(procnt));
        fatView.setText(df.format(fat));
        chocdfView.setText(df.format(chocdf));
        fibtgView.setText(df.format(fibtg));

        favFoodDB = new FavouriteFoodDBHelper(this.getContext());
        Cursor cursor = favFoodDB.getReadableDatabase().query(
                true,
                FavouriteFoodDBHelper.FOOD_NUT_TABLE,
                new String[]{FavouriteFoodDBHelper.FOOD_ID},
                FavouriteFoodDBHelper.FOOD_ID + " = ?",
                new String[]{getArguments().getString("foodId")},
                null, null, null, null
        );

        favouriteCheckBox.setChecked(cursor.getCount() > 0);
        favouriteCheckBox.setOnCheckedChangeListener((listener,checked)->{
            if(checked) {
                ContentValues cv = new ContentValues();
                cv.put(FavouriteFoodDBHelper.FOOD_ID, getArguments().getString("foodId"));
                cv.put(FavouriteFoodDBHelper.FOOD_LABEL, getArguments().getString("label"));
                cv.put(FavouriteFoodDBHelper.BRAND, getArguments().getString("brand"));
                cv.put(FavouriteFoodDBHelper.CATEGORY, getArguments().getString("category"));
                cv.put(FavouriteFoodDBHelper.FOOD_CONTENTS_LABEL, getArguments().getString("contents"));
                cv.put(FavouriteFoodDBHelper.ENERC_KCAL, getArguments().getDouble("ENERC_KCAL"));
                cv.put(FavouriteFoodDBHelper.PROCNT, getArguments().getDouble("PROCNT"));
                cv.put(FavouriteFoodDBHelper.FAT, getArguments().getDouble("FAT"));
                cv.put(FavouriteFoodDBHelper.CHOCDF, getArguments().getDouble("CHOCDF"));
                cv.put(FavouriteFoodDBHelper.FIBTG, getArguments().getDouble("FIBTG"));

                favFoodDB.getWritableDatabase().insert(FavouriteFoodDBHelper.FOOD_NUT_TABLE, null, cv);

                Snackbar.make(this.getView(),R.string.added_to_fav, Snackbar.LENGTH_SHORT).show();
            } else {
                favFoodDB.getWritableDatabase().delete(
                        FavouriteFoodDBHelper.FOOD_NUT_TABLE,
                        FavouriteFoodDBHelper.FOOD_ID + " = ?",
                        new String[]{getArguments().getString("foodId")});
                Snackbar.make(this.getView(),R.string.removed_from_fav, Snackbar.LENGTH_SHORT).show();
            }
        });

        cursor.close();
        return view;
    }
}
