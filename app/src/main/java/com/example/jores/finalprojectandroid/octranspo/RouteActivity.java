package com.example.jores.finalprojectandroid.octranspo;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jores.finalprojectandroid.MenuInflationBaseActivity;
import com.example.jores.finalprojectandroid.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;

public class RouteActivity extends MenuInflationBaseActivity {

    protected static final String ACTIVITY_NAME = "RouteActivity";

    ArrayList<String> directionArray;
    ArrayList<String> routeArray;
    ArrayList<String> resultArray;
    Bundle routeInformation = new Bundle();
    String bundleKey;
    String busStopNumber;
    String busStopName;
    ListView listView;
    TextView stopID;
    TextView stopDescription;
    ProgressBar progressBar;
    HttpHandler httpHandler;
    String ocTranspURL;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        directionArray = new ArrayList<>();
        routeArray = new ArrayList<>();
        resultArray = new ArrayList<>();
        listView = findViewById(R.id.routeListView);
        stopDescription = findViewById(R.id.busNumberAndDirection);
        stopID = findViewById(R.id.selectedRoute);


        Log.i(ACTIVITY_NAME, "onCreate");

        View view = findViewById(android.R.id.content);
        Snackbar.make(view, R.string.routeInformationSnackBar, Snackbar.LENGTH_LONG)
                .setAction(R.string.routeActivitySnackBarClose, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .setActionTextColor(getResources().getColor(android.R.color.holo_green_light))
                .show();

        progressBar = findViewById(R.id.progressBar);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentFrameLayout, new FragmentActivity()).commit();
        routeInformation = this.getIntent().getExtras();
        bundleKey = routeInformation.getString("Route Information");

        ocTranspURL = "https://api.octranspo1.com/v1.2/GetRouteSummaryForStop?appID=223eb5c3&&apiKey=ab27db5b435b8c8819ffb8095328e775&stopNo=" + bundleKey;
        new LoadRouteActivity().execute();


        }

    //Helper AsyncTask class
    class LoadRouteActivity extends AsyncTask<URL, Integer, ArrayList<String>> {
        ArrayList<String> route;

        @Override
        protected ArrayList<String> doInBackground(URL... urls) {
            publishProgress(10);
            httpHandler = new HttpHandler();
            try {

                route = parseXML(httpHandler.connect(ocTranspURL));
                resultArray = route;

            } catch (XmlPullParserException | IOException e) {
                e.printStackTrace();
            }
            publishProgress(20);
            return route;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(ArrayList<String> s) {
            publishProgress(50);

            final ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(RouteActivity.this,
                    R.layout.route_direction_list, resultArray);

            listView.setAdapter(stringArrayAdapter);
            publishProgress(100);
            progressBar.setVisibility(View.INVISIBLE);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    CharSequence toastMessage = getString(R.string.routeSelectedToastMessage);
                    Toast.makeText(RouteActivity.this, toastMessage, Toast.LENGTH_SHORT)
                            .show();

                    FragmentActivity fragment = new FragmentActivity();
                    Bundle bundle = new Bundle();

                    bundle.putString("number", bundleKey);
                    bundle.putString("route", routeArray.get(position));
                    bundle.putString("direction",directionArray.get(position));
                    fragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentFrameLayout, fragment)
                            .commit();

                }
            });

        }
    }

    /**
     *
     * @param xmlString
     * @return XML bus information
     * @throws XmlPullParserException
     * @throws IOException
     */
    private ArrayList<String> parseXML(String xmlString) throws XmlPullParserException, IOException {
        ArrayList<String> result = new ArrayList<>();
        String routeId = null;
        String direction = null;

        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();

        xpp.setInput(new StringReader(xmlString)); // pass input whatever xml you have

        int eventType = xpp.getEventType();
        while ((eventType != XmlPullParser.END_DOCUMENT)) {
            if (eventType == XmlPullParser.START_TAG) {

                String tag = xpp.getName();
                if (tag.equalsIgnoreCase("stopdescription")) {
                    busStopName = xpp.nextText();

                } else if (tag.equalsIgnoreCase("stopno")) {
                    busStopNumber = xpp.nextText();

                } else if (tag.equalsIgnoreCase("routeno")) {
                    routeId = xpp.nextText();
                    routeArray.add(routeId);

                } else if (tag.equalsIgnoreCase("direction")) {
                    direction = xpp.nextText();
                    directionArray.add(direction);
                }

            }

            eventType = xpp.next();

        }
        if (routeArray.size() == directionArray.size()) {
            for (int i = 0; i < routeArray.size(); i++) {
                result.add(routeArray.get(i) + " -> " + directionArray.get(i));
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                        stopID.setText(busStopNumber);
                        stopDescription.setText(busStopName);

                }
            });


        }
        return result;
    }

    @Override
    public void onHelpMenuClick(MenuItem mi) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.helpTitle)
                .setMessage(R.string.helpMessage)
                .setNeutralButton(R.string.helpNeutralButton, null)
                .show();

    }
}

