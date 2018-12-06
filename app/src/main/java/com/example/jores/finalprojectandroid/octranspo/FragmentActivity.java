package com.example.jores.finalprojectandroid.octranspo;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.jores.finalprojectandroid.R;

import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class FragmentActivity extends Fragment {

    private String ACTIVITY_NAME = "FragmentActivity";
    private ListView listView;
    private ArrayList<String> routeInformation;
    private ArrayList<String> destinationArray;
    private ArrayList<String> adjustedTimeArray;
    private ArrayList<String> latitudeArray;
    private ArrayList<String> directionArray;
    private ArrayList<String> longitudeArray;
    private ArrayList<String> speedArray;
    private ArrayList<String> timeArray;
    AsyncLoader asyncLoader;

    /**
     *
     * @param bundle
     */
    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);

        Bundle newBundle = getArguments();
        if(newBundle != null) {

            String routeInfo = newBundle.getString("route");
            String directionInfo = newBundle.getString("direction");
            String numberInfo = newBundle.getString("number");

            routeInformation = new ArrayList<>();
            destinationArray = new ArrayList<>();
            adjustedTimeArray = new ArrayList<>();
            latitudeArray = new ArrayList<>();
            directionArray = new ArrayList<>();
            longitudeArray = new ArrayList<>();
            speedArray = new ArrayList<>();
            timeArray = new ArrayList<>();
            asyncLoader = new AsyncLoader();


            asyncLoader.execute(numberInfo, routeInfo, directionInfo);

        }
    }


    private class AsyncLoader extends AsyncTask<String, Integer, String> {

        String number= null;
        String route = null;
        String direction = null;
        ArrayList<String> resultArr = new ArrayList<>();
        @Override
        protected String doInBackground(String... params) {

            number = params[0];
            route = params[1];
            direction = params[2];

            try {
                resultArr = downloadRouteInfo(number, route, direction);
            }catch (IOException e){

                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
        }


        protected void onPostExecute(String result) {

            try {
                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),android.R.layout.simple_list_item_activated_1,resultArr);
                listView.setAdapter(adapter);

            }catch (NullPointerException e){

            }

        }
    }

    /**
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.activity_fragment, container, false);
        listView = view.findViewById(R.id.fragmentActivityListview);

        return view;
    }

    /**
     *
     * @param stopNumber
     * @param routeNumber
     * @param currentDirection
     * @return view
     * @throws IOException
     */
    private ArrayList<String> downloadRouteInfo (String stopNumber, String routeNumber, String currentDirection)throws IOException {
        URL url;
        ArrayList<String> resultArray = new ArrayList<>();
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        String ocTranspo = "https://api.octranspo1.com/v1.2/GetNextTripsForStop?appID=223eb5c3&&apiKey" + "=ab27db5b435b8c8819ffb8095328e775&stopNo="+stopNumber+"&routeNo="+routeNumber+"";

        try{

            url = new URL(ocTranspo);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();

            int responseCode = urlConnection.getResponseCode();

            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new IOException("Connection IOException: " + responseCode);
            }

            inputStream = urlConnection.getInputStream();
            if(inputStream != null){
                resultArray = readRouteStream(currentDirection, inputStream);
            }

        } catch (IOException e) {

            e.printStackTrace();
            Log.i(ACTIVITY_NAME, "Connection IOException caught: " + e);

        } finally {

            if (inputStream != null) {
                inputStream.close();
                urlConnection.disconnect();
            }

        }

        return resultArray;
    }

    /**
     *
     * @param busDirection
     * @param resultStream
     * @return route information from stream
     */
    private ArrayList<String> readRouteStream(String busDirection, InputStream resultStream){


        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(false);
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(resultStream, "UTF-8");

            //Loop through stream if not null and obtain bus route information
            while((parser.getEventType())!= XmlPullParser.END_DOCUMENT){
                if(parser.getEventType() == XmlPullParser.START_TAG){

                    String getName = parser.getName();

                    if(getName.equalsIgnoreCase("Direction")){

                        String directionString = parser.nextText();
                        directionArray.add(directionString)  ;

                    }else if(getName.equalsIgnoreCase("TripDestination")){

                        String destinationString = parser.nextText();
                        destinationArray.add(destinationString) ;

                    }else if(getName.equalsIgnoreCase("TripStartTime")){

                        String startTimeString = parser.nextText();
                        timeArray.add(startTimeString) ;

                    }else if(getName.equalsIgnoreCase("AdjustedScheduleTime")){

                        String adjustedTime = parser.nextText();
                        adjustedTimeArray.add(adjustedTime) ;

                    }else if(getName.equalsIgnoreCase("Latitude")) {

                        String currentLatitude = parser.nextText();
                        latitudeArray.add(currentLatitude);

                    }else if(getName.equalsIgnoreCase("Longitude")){

                        String currentLongitude = parser.nextText();
                        longitudeArray.add(currentLongitude) ;

                    }else if(getName.equalsIgnoreCase("GpsSpeed")){

                        String currentSpeed = parser.nextText();
                        speedArray.add(currentSpeed) ;

                    }
                }
                parser.next();
            }
        }catch(Exception exception){
            exception.printStackTrace();
            Log.i(ACTIVITY_NAME,"Parser Exception caught: " + exception);
        }

        double averageTime;
        double adjustedTime = 0;
        for(int i = 0; i < adjustedTimeArray.size(); i++){
            adjustedTime =+ Integer.parseInt(adjustedTimeArray.get(i));
        }

        averageTime = adjustedTime/adjustedTimeArray.size();
        routeInformation.add("AdjustedScheduleTime average: " + averageTime + "min");


        if (directionArray.get(0).equalsIgnoreCase(busDirection)){
            for(int i = 0; i < destinationArray.size()/2; i++){
                routeInformation.add("\n\nTripDestination: " + destinationArray.get(i)
                        + "\n\nLatitude: " + latitudeArray.get(i)+ "\n\nLongitude: " + longitudeArray.get(i)
                        + "\n\nSpeed: " +  speedArray.get(i) + "km/hr"
                        + "\n\nTripStartTime: " + timeArray.get(i) + "\n\nAdjustedScheduleTime: "
                        + adjustedTimeArray.get(i) + "min");

            }

        }

        return routeInformation;
    }
}
