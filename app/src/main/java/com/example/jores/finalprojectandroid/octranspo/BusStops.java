package com.example.jores.finalprojectandroid.octranspo;
import com.example.jores.finalprojectandroid.MenuInflationBaseActivity;
import com.example.jores.finalprojectandroid.R;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.zip.Inflater;
import android.util.Log;
import android.view.MenuItem;

public class BusStops extends MenuInflationBaseActivity {

    private static final String ACTIVITY_NAME = BusStops.class.getSimpleName();


    private final String ACTIVITY_NAME = "BusStopsActivity";
    private ArrayList<String> routeArray;
    private EditText editText;
    private ListView listView;
    private Button button;

    private SQLiteDatabase sqlDatabase;
    private RouteDatabaseHelper routeDbHelper;
    private RouteAdapter routeAdapter;
    private Cursor cursor;
    private ContentValues contentValues;


    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bus_stops);

        //Adding the toolbar to the activity
        setSupportActionBar(findViewById(R.id.main_toolbar));

        routeAdapter = new RouteAdapter(this);
        routeArray = new ArrayList<>();
        editText = findViewById(R.id.editTextBusStops);
        listView = findViewById(R.id.busStopsListView);
        button = findViewById(R.id.busStopsButton);

        routeDbHelper = new RouteDatabaseHelper(getApplicationContext());
        sqlDatabase = routeDbHelper.getWritableDatabase();
        contentValues = new ContentValues();

        String string = "SELECT " + RouteDatabaseHelper.KEY_MESSAGE + " FROM " + RouteDatabaseHelper.TABLE_NAME;
        cursor = sqlDatabase.rawQuery(string, null);

        int columnIndex = cursor.getColumnIndex(RouteDatabaseHelper.KEY_MESSAGE);

        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            String index = cursor.getString(columnIndex);
            routeArray.add(index);
            cursor.moveToNext();
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = editText.getText().toString();
                routeArray.add(content);
                contentValues.put(RouteDatabaseHelper.KEY_MESSAGE, content);
                sqlDatabase.insert(RouteDatabaseHelper.TABLE_NAME, "NullColumn", contentValues);
                listView.setAdapter(routeAdapter);
                routeAdapter.notifyDataSetChanged();
                editText.setText("");
                Toast.makeText(BusStops.this,R.string.stopAddedToast, Toast.LENGTH_SHORT)
                .show();
            }
        });


        listView.setAdapter(routeAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            /*
             * @param parent
             * @param view
             * @param position
             * @param id
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                LayoutInflater inflater = getLayoutInflater();
                View deleteView = inflater.inflate(R.layout.activity_delete_route, null);

                final Button button = deleteView.findViewById(R.id.deleteRouteButton);

                AlertDialog alert = new AlertDialog.Builder(BusStops.this)

                        .setTitle(R.string.deleteOrContinueDialog)
                        .setView(deleteView)
                        .setPositiveButton(R.string.deleteBusStopContinue, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Intent intent = new Intent(view.getContext(), RouteActivity.class);
                                Bundle routeInformationBundle = new Bundle();

                                routeInformationBundle.putString("Route Information", routeArray.get(position));
                                intent.putExtras(routeInformationBundle);
                                startActivity(intent);

                            }
                        })
                        .setNegativeButton(R.string.deleteBusStopCancel, null)
                        .show();

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        routeDbHelper.deleteRoute(routeArray.get(position));
                        Toast.makeText(BusStops.this, position + R.string.routeDeleted, Toast.LENGTH_SHORT).show();

                        String str = "SELECT " + RouteDatabaseHelper.KEY_ID + ", " + RouteDatabaseHelper.KEY_MESSAGE + " from " + RouteDatabaseHelper.TABLE_NAME;
                        cursor = sqlDatabase.rawQuery(str, null);
                        int colIndex = cursor.getColumnIndex(RouteDatabaseHelper.KEY_MESSAGE);
                        routeArray.clear();
                        cursor.moveToFirst();
                        while (!cursor.isAfterLast()) {

                            String string = cursor.getString(colIndex);
                            routeArray.add(string);
                            cursor.moveToNext();
                        }
                        routeAdapter.notifyDataSetChanged();
                        alert.dismiss();

                    }
                });

            }
        });


        View view = findViewById(android.R.id.content);
        Snackbar.make(view, R.string.savedRoutes, Snackbar.LENGTH_LONG)
                .setAction(R.string.closeToast, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .setActionTextColor(getResources().getColor(android.R.color.holo_green_light))
                .show();


    }

    //Helper class for Adapter
    private class RouteAdapter extends ArrayAdapter<String>{
        public RouteAdapter(Context context){
            super(context, 0);
        }

        public View getView(int position, View convertView, ViewGroup viewGroup){
            LayoutInflater inflater = BusStops.this.getLayoutInflater();

            View result =  inflater.inflate(R.layout.bus_stop_outgoing, null);
            TextView message = result.findViewById(R.id.routeStopTextView);
            message.setText(getItem(position));

            return result;
        }

        /**
         * @param position
         * @return routeArray position
         */
        public String getItem(int position){

            return routeArray.get(position);
        }

        /**
         *
         * @param id
         * @return id
         */
        public long getID(int id){
            return id;
        }

        /**
         *
         * @return array size
         */
        @Override
        public int getCount() {

            return routeArray.size();
        }

    }

    @Override
    public void onHelpMenuClick(MenuItem mi){
        Log.i(ACTIVITY_NAME,"Showing help menu");
    }
}
