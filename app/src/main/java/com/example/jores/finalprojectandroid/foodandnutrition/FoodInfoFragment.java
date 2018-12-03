package com.example.jores.finalprojectandroid.foodandnutrition;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jores.finalprojectandroid.R;

import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class FoodInfoFragment extends Fragment {
    private static final String FRAGMENT_NAME = FoodInfoFragment.class.getSimpleName();

    public FoodInfoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i(FRAGMENT_NAME,"Starting onCreateView");
        View view = inflater.inflate(R.layout.fragment_food_info, container, false);

        TextView foodLabel = view.findViewById(R.id.food_label);
        String label = getArguments().getString("label");
        foodLabel.setText((label == null ? "Why am I null" : label));

        return view;
    }
}
