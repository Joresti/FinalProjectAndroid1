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

public class FoodQuerier {
    private static final String CLASS_NAME = FoodQuerier.class.getSimpleName();
    private static final FoodQuerier FOOD_QUERIER = new FoodQuerier();

    private final String APP_ID = "e50b5611";
    private final String APP_KEY = "3412cfeb1ed8581c876a8a1622b9516c";
    private final String DB_URL = "https://api.edamam.com/api/food-database/parser?app_id="+APP_ID+"&app_key="+APP_KEY+"&ingr=";

    private String nextURL = null;
    private ArrayList<FoodData> foodList = new ArrayList<>();
    private JSONObject foodJSON = null;
    private Consumer<String> onPostExecute;
    private ProgressBar progressBar = null;

    private FoodQuery foodQuery = null;

    private FoodQuerier(){}

    public void setProgressBar(ProgressBar progressBar){
        this.progressBar = progressBar;
    }

    public void queryForString(String query, Consumer<String> onPostExecute){
        this.onPostExecute = onPostExecute;

        if(foodQuery != null)
            foodQuery.cancel(true);

        foodQuery = new FoodQuery(DB_URL + query);
        foodQuery.execute();
    }

    public void queryNextPage(){
        if(foodQuery != null)
            foodQuery.cancel(true);

        foodQuery = new FoodQuery(nextURL);
        foodQuery.execute();
    }

    public boolean hasNextPage(){
        return nextURL != null;
    }

    public ArrayList<FoodData> getFoodList(){return foodList;}

    public static FoodQuerier getFoodQuerier(){
        return FOOD_QUERIER;
    }

    public void cancel(Boolean interrupt){
        if(foodQuery != null)
            foodQuery.cancel(interrupt);
    }

    private class FoodQuery extends AsyncTask<String, Integer, String> {

        String searchURL;

        public FoodQuery(String searchURL){
            this.searchURL = searchURL;
        }

        @Override
        protected String doInBackground(String... strings) {
            BufferedReader bf = null;
            HttpURLConnection conn = null;
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

                foodJSON = new JSONObject(sb.toString());

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

        @Override
        protected void onPostExecute(String result){
            if(onPostExecute != null && !isCancelled())
                onPostExecute.accept(result);
            foodQuery = null;
        }
    }
}
