package com.example.jores.finalprojectandroid.cbcnews;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.example.jores.finalprojectandroid.R;

/**
 * Class to hold a fragment, fragment will display news story
 */

public class ArticleDetails extends FragmentActivity {


    public ArticleDetails(){}

    /**
     * Class taking extras from intent and passing it to a fragment
     * @param savedInstanceBundle arguments passed to this activity - custom function does not use
     */
    @Override
    public void onCreate(Bundle savedInstanceBundle){

        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.article_details);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft =  fm.beginTransaction();

        Intent i = getIntent();
        Bundle bundle = i.getExtras();



        ArticleFragment af = new ArticleFragment();
        af.setArguments(bundle);
        ft.replace(R.id.frame,af);
        ft.commit();

    }

}
