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

    private final ArrayList<JSONObject> hintList = new ArrayList<>();

    public FoodListFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_food_list, container, false);

        ListView foodList = view.findViewById(R.id.food_list);
        foodList.setAdapter(new FoodListAdapter(getActivity()));

        return view;
    }
}
