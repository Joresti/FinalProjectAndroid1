package com.example.jores.finalprojectandroid.cbcnews;

import android.database.Cursor;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.view.View;


/**
 * class to gather statistics on the saved news stories
 */

public class GetSavedStatsAsyncTask extends AsyncTask<String, Integer, String>  {

    CBCNewsMain cbc;

    /**
     * constructor
     * @param cbc CBCNewsMain
     */
    public GetSavedStatsAsyncTask(CBCNewsMain cbc){
        this.cbc=cbc;
    }

    /**
     *  Creates a cursor object from the saved stories in the database.  Loops through cursor and gets the word count of the description part of the article,
     *  generates statistics and returns a String.
     * @param params String
     * @return String
     */
    @Override
    public String doInBackground(String...params) {

        Cursor cursor = NewsDatabaseHelper.getDatabase().rawQuery("SELECT * FROM SavedStories;", null );
        int numberOfStories = cursor.getCount();

        int totalWordCount=0;
        int minWordCount=10000;
        int maxWordCount=0;

        while(cursor.moveToNext()) {
            String description = cursor.getString(3);
            int wordCount= description.split(" ").length;
            totalWordCount+=wordCount;

            if(minWordCount>wordCount){minWordCount=wordCount;}
            if(maxWordCount<wordCount){maxWordCount=wordCount;}
        }
        cursor.close();
        double avgWordCount = (numberOfStories == 0 ? 0 : totalWordCount/numberOfStories);
        if (minWordCount==10000){minWordCount=0;}

        String returnString = Integer.toString(numberOfStories)+" stories are saved. Average word count = "+avgWordCount+". \n"
                + "Min word count = "+minWordCount+". Max word count = "+maxWordCount+".";

        return returnString;
    }

    /**
     *  Sends a Snack bar with the results back to the CBCNewsMain Activity
     * @param returnString String
     */
    @Override
    public void onPostExecute(String returnString){

        Snackbar snackbar = Snackbar.make(cbc.searchBar,returnString,Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Ok", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();


    }

}
