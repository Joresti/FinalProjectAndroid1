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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FoodQuerier {
    private static final String CLASS_NAME = FoodQuerier.class.getSimpleName();
    private static final FoodQuerier FOOD_QUERIER = new FoodQuerier();

    private final String APP_ID = "e50b5611";
    private final String APP_KEY = "3412cfeb1ed8581c876a8a1622b9516c";
    private final String DB_URL = "https://api.edamam.com/api/food-database/parser?app_id="+APP_ID+"&app_key="+APP_KEY+"&ingr=";

    private boolean queryComplete = false;
    private String query = "";
    private JSONObject foodJSON = null;
    private Consumer<String> onPostExecute;
    private ProgressBar progressBar = null;

    private FoodQuery foodQuery = null;

    private FoodQuerier(){}

    public boolean isQueryComplete(){
        return queryComplete;
    }

    public void setProgressBar(ProgressBar progressBar){
        this.progressBar = progressBar;
    }

    public void queryForString(String query){
        queryForString(query,onPostExecute);
    }

    public void queryForString(String query, Consumer<String> onPostExecute){
        queryComplete = false;
        this.query = query;
        this.onPostExecute = onPostExecute;
        if(foodQuery != null)
            foodQuery.cancel(true);

        foodQuery = new FoodQuery();
        foodQuery.execute();
    }

    public JSONObject getFoodJSON(){
        return foodJSON;
    }

    public JSONObject getParsedFoodJSON(){
        try {
            return foodJSON.getJSONArray("parsed").getJSONObject(0).getJSONObject("food");
        } catch (JSONException jse){
            Log.e(CLASS_NAME, jse.toString());
        }
        return null;
    }

    public JSONArray getHintJSON(){
        try {
            return foodJSON.getJSONArray("hints");
        } catch (JSONException jse){
            Log.e(CLASS_NAME, jse.toString());
        }
        return null;
    }

    public static FoodQuerier getFoodQuerier(){
        return FOOD_QUERIER;
    }

    private class FoodQuery extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            BufferedReader bf = null;
            try {
                if(progressBar != null)
                    progressBar.setProgress(0);
                Log.i(CLASS_NAME,"Starting query");
                URL url = new URL(DB_URL+query);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);

                if(progressBar != null)
                    progressBar.setProgress(10);

                bf = new BufferedReader(new InputStreamReader(InputStream.class.cast(conn.getContent()),"UTF-8"));

                StringBuilder sb = new StringBuilder();
                String line = null;
                while((line = bf.readLine()) != null){
                    progressBar.setProgress(progressBar.getProgress()+10);
                    sb.append(line);
                    sb.append("\n");
                }

                foodJSON = new JSONObject(sb.toString());
                Log.i(CLASS_NAME,"Created JSON Object: " + foodJSON);
                progressBar.setProgress(100);
                progressBar.setVisibility(View.INVISIBLE);
            } catch (Exception e) {
            } finally {
                try {
                    if (bf != null)
                        bf.close();
                } catch (Exception e) {}
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result){
            queryComplete = true;
            if(onPostExecute != null)
                onPostExecute.accept(result);
            foodQuery = null;
        }
    }
}
