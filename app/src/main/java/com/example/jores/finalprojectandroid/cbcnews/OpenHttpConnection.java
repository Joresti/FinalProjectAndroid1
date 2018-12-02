package com.example.jores.finalprojectandroid.cbcnews;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 *
 */

public class OpenHttpConnection {

    static String TAG = "OPEN HTTP CONNECTION";

    /**
     * Loosely adapted from Android Programming with Android studio - Chapter 11
     * @param urlString cbc url address
     * @return InputStream
     * @throws IOException
     */

       protected static InputStream OpenHttpConnection(String urlString) throws IOException {
        Log.d(TAG, "LOADING NEWS DATA");
        InputStream in = null;
        int response = -1;
        URL url = new URL(urlString);
        URLConnection conn = url.openConnection();
        if (!(conn instanceof HttpURLConnection)) {
            Log.d(TAG, "BAD");
            throw new IOException("Not an http connection");

        }
        try {
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setAllowUserInteraction(false);httpConn.setInstanceFollowRedirects(true);httpConn.setRequestMethod("GET");httpConn.connect();
            response = httpConn.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) { in = httpConn.getInputStream(); }
        }
        catch (Exception e) {
            Log.i(TAG, e.getLocalizedMessage());
        }
        return in;
    }
}
