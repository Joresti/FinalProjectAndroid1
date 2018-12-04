package com.example.jores.finalprojectandroid.foodandnutrition;


import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.jores.finalprojectandroid.R;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DecimalFormat;

/**
 * A simple {@link Fragment} subclass.
 */
public class FoodInfoFragment extends Fragment {
    private static final String FRAGMENT_NAME = FoodInfoFragment.class.getSimpleName();

    private FavouriteFoodDBHelper favFoodDB;

    private CheckBox favouriteCheckBox;
    private TextView foodView;
    private TextView brandView;
    private TextView categoryView;
    private TextView contentsView;
    private TextView kCalView;
    private TextView procntView;
    private TextView fatView;
    private TextView chocdfView;
    private TextView fibtgView;

    public FoodInfoFragment() {
    }

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i(FRAGMENT_NAME,"Starting onCreateView");
        View view = inflater.inflate(R.layout.fragment_food_info, container, false);

        foodView = view.findViewById(R.id.food_label);
        brandView = view.findViewById(R.id.brand);
        categoryView = view.findViewById(R.id.category);
        contentsView = view.findViewById(R.id.contents);
        kCalView = view.findViewById(R.id.enerc_kcal);
        procntView = view.findViewById(R.id.procnt);
        fatView = view.findViewById(R.id.fat);
        chocdfView = view.findViewById(R.id.chocdf);
        fibtgView = view.findViewById(R.id.fibtg);
        favouriteCheckBox = view.findViewById(R.id.favourite_check);

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
