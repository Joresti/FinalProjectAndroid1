package com.example.jores.finalprojectandroid.foodandnutrition;

import android.os.AsyncTask;
import android.support.v4.util.Consumer;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * An object used to submit and parse queries to the Food database
 */
class FoodQuerier {
    private static final String CLASS_NAME = FoodQuerier.class.getSimpleName();

    private final String APP_ID = "e50b5611";
    private final String APP_KEY = "3412cfeb1ed8581c876a8a1622b9516c";
    private final String DB_URL = "https://api.edamam.com/api/food-database/parser?app_id="+APP_ID+"&app_key="+APP_KEY+"&ingr=";

    private String nextURL = null;
    private ArrayList<FoodData> foodList = new ArrayList<>();
    private AtomicBoolean completed = new AtomicBoolean(false);
    private Consumer<String> onPostExecute;
    private ProgressBar progressBar = null;

    private FoodQuery foodQuery;

    /**
     * Set the progressBar that this will use to track updates on
     * @param progressBar The progress bar to track updates on
     */
    public void setProgressBar(ProgressBar progressBar){
        this.progressBar = progressBar;
    }

    /**
     * Start a query for a given string
     * @param query The term to query the DB for
     * @param onPostExecute A computer that will be executed once the query is finished
     */
    public void queryForString(String query, Consumer<String> onPostExecute){
        this.onPostExecute = onPostExecute;

        if(foodQuery != null)
            foodQuery.cancel(true);

        foodQuery = new FoodQuery(DB_URL + query);
        foodQuery.execute();
    }

    /**
     * Starts a query for the next page provided by the last query.
     * <p>
     * Will crash if there wasn't a next page, should be used with hasNextPage
     */
    public void queryNextPage(){
        if(foodQuery != null)
            foodQuery.cancel(true);

        foodQuery = new FoodQuery(nextURL);
        foodQuery.execute();
    }

    /**
     * Checks if the last query completed had a next page of data
     * @return Whether the next page exists
     */
    public boolean hasNextPage(){
        return nextURL != null;
    }

    /**
     * Get the list of food created by the last run query
     * @return The list of food
     */
    public ArrayList<FoodData> getFoodList(){
        return foodList;
    }

    /**
     * Cancels the currently running query if there is one
     * @param interrupt Whether or not the query should be interrupted
     */
    public void cancel(Boolean interrupt){
        if(foodQuery != null)
            foodQuery.cancel(interrupt);
    }

    /**
     * Private class representing a single AsyncTask for one query
     */
    private class FoodQuery extends AsyncTask<String, Integer, String> {
        String searchURL;

        /**
         * Create a new query to search for the given searchURL
         * @param searchURL The URL to query for a response
         */
        FoodQuery(String searchURL){
            this.searchURL = searchURL;
        }

        /**
         * Overriding the superclass method, queries the DB and parses the JSON into FoodList
         * @param strings Parameters for the method
         * @return Always returns null
         */
        @Override
        protected String doInBackground(String... strings) {
            BufferedReader bf = null;
            HttpURLConnection conn = null;
            completed.set(false);
            foodList.clear();

            try {
                if (progressBar != null)
                    progressBar.setProgress(0);
                Log.i(CLASS_NAME, "Starting query for URL \"" + searchURL +"\"");
                URL url = new URL(searchURL);
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);

                if (progressBar != null)
                    progressBar.setProgress(10);

                bf = new BufferedReader(new InputStreamReader(InputStream.class.cast(conn.getContent()), "UTF-8"));

                progressBar.setProgress(20);

                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = bf.readLine()) != null) {
                    sb.append(line);
                    sb.append("\n");
                }

                progressBar.setProgress(50);

                JSONObject foodJSON = new JSONObject(sb.toString());

                JSONArray hintArray = foodJSON.getJSONArray("hints");
                for (int i = 0; i < hintArray.length(); i++) {
                    foodList.add(new FoodData(hintArray.getJSONObject(i).getJSONObject("food")));
                }

                progressBar.setProgress(90);

                nextURL = null;
                if (foodJSON.has("_links"))
                    if (foodJSON.getJSONObject("_links").has("next"))
                        if (foodJSON.getJSONObject("_links").getJSONObject("next").has("href"))
                            nextURL = foodJSON.getJSONObject("_links").getJSONObject("next").getString("href");
            } catch (JSONException | IOException e) {
                Log.e(CLASS_NAME, e.toString());
            } finally {
                try {
                    if(bf != null)
                        bf.close();
                    if(conn != null)
                        conn.disconnect();
                } catch (Exception e) {
                }
            }
            progressBar.setProgress(100);
            progressBar.setVisibility(View.INVISIBLE);
            return null;
        }

        /**
         * Overriding the superclass method, executes the previously given lambda, if it isn't null
         * @param result The results from the doInBackground method
         */
        @Override
        protected void onPostExecute(String result){
            if(onPostExecute != null && !isCancelled())
                onPostExecute.accept(result);
            foodQuery = null;
            completed.set(true);
        }
    }
}
