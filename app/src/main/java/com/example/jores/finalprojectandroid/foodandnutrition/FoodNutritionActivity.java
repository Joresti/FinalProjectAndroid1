package com.example.jores.finalprojectandroid.foodandnutrition;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jores.finalprojectandroid.MenuInflationBaseActivity;
import com.example.jores.finalprojectandroid.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public final class FoodNutritionActivity extends MenuInflationBaseActivity {

    private static final String ACTIVITY_NAME = FoodNutritionActivity.class.getSimpleName();
    private FoodQuery fq = null;

    private SharedPreferences preferences;
    private ListView hintListView;
    private RelativeLayout foodInfo;
    private EditText searchBar;
    private TextView foodLabel;
    private final AtomicBoolean userPressedEnter = new AtomicBoolean(false);
    private final ArrayList<JSONObject> hintList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_nutrition);

        preferences = getSharedPreferences("", Context.MODE_PRIVATE);

        hintListView = findViewById(R.id.hint_list);
        foodInfo = findViewById(R.id.food_info);
        searchBar = findViewById(R.id.food_search_bar);
        foodLabel = findViewById(R.id.food_label);

        hintListView.setAdapter(new HintAdapter(this));

        searchBar.setOnEditorActionListener((textView, actionID, event)->{
            if(actionID == EditorInfo.IME_ACTION_DONE) {
                Log.i(ACTIVITY_NAME, "User searched for " + searchBar.getText());
                if(fq != null)
                    fq.cancel(true);
                fq = new FoodQuery();
                fq.execute();
                return true;
            }
            return false;
        });
    }

    @Override
    public void onHelpMenuClick(MenuItem mi){
        Log.i(ACTIVITY_NAME,"Showing help menu");
    }

    private void setFoodInfo(JSONObject foodJSON){
        try {
            String label = foodJSON.getString("label");
            SpannableString labelString = new SpannableString(label);
            labelString.setSpan(new UnderlineSpan(), 0, label.length(), 0);
            foodLabel.setText(labelString);
            hintListView.setVisibility(View.INVISIBLE);
            foodInfo.setVisibility(View.VISIBLE);
            TextView.class.cast(findViewById(R.id.raw_json)).setText(foodJSON.toString());
        } catch (JSONException je) {
            Log.e(ACTIVITY_NAME,je.toString());
        }
    }

    class FoodQuery extends AsyncTask<String, Integer, String> {
        private JSONObject finalObj = null;

        private static final String APP_ID = "e50b5611";
        private static final String APP_KEY = "3412cfeb1ed8581c876a8a1622b9516c";
        private static final String DB_URL = "https://api.edamam.com/api/food-database/parser?app_id="+APP_ID+"&app_key="+APP_KEY+"&ingr=";

        @Override
        protected String doInBackground(String... strings) {
            BufferedReader bf = null;
            try {
                URL url = new URL(DB_URL+searchBar.getText());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);

                bf = new BufferedReader(new InputStreamReader(InputStream.class.cast(conn.getContent()),"UTF-8"));

                StringBuilder sb = new StringBuilder();
                String line = null;
                while((line = bf.readLine()) != null){
                    sb.append(line);
                    sb.append("\n");
                }

                finalObj = new JSONObject(sb.toString());

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
            System.out.println("User searched for a \""+searchBar.getText()+"\"");
            System.out.println("Db returned: " + finalObj);
            try {
                JSONArray parsed = finalObj.getJSONArray("parsed");
                if(parsed.length() > 0) {
                    setFoodInfo(parsed.getJSONObject(0).getJSONObject("food"));
                } else {
                    JSONArray hints = finalObj.getJSONArray("hints");
                    if(hints.length() > 0) {
                        hintList.clear();
                        for(int i=0; i < hints.length();i++){
                            hintList.add(hints.getJSONObject(i).getJSONObject("food"));
                        }
                        hintListView.setVisibility(View.VISIBLE);
                        foodInfo.setVisibility(View.INVISIBLE);
                    } else {
                        Toast toast = Toast.makeText(FoodNutritionActivity.this,"No results found",Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();
                    }
                }
            } catch (JSONException je){
                Log.e(ACTIVITY_NAME, je.toString());
            }
        }
    }

    private class HintAdapter extends ArrayAdapter<JSONObject> {
        public HintAdapter(Context ctx) {
            super(ctx, 0);
        }

        @Override
        public int getCount(){
            return hintList.size();
        }

        @Override
        public JSONObject getItem(int index){
            return hintList.get(index);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            TextView hint = new TextView(this.getContext());
            try {
                hint.setText(hintList.get(position).getString("label"));
            } catch (JSONException je) {
                Log.e(ACTIVITY_NAME, je.toString());
                hint.setText("Unknown, JSON error");
            }
            hint.setOnClickListener((l)->{
                setFoodInfo(hintList.get(position));
            });
            return hint;
        }

        @Override
        public long getItemId(int index){
            return index;
        }
    }
}
