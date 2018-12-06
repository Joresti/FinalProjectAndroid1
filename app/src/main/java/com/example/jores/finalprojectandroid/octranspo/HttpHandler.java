package com.example.jores.finalprojectandroid.octranspo;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class HttpHandler {

    /**
     * Default constructor
     */
    public HttpHandler(){

    }

    /**
     *
     * @param ocUrl
     * @return response from connection
     */
    public String connect(String ocUrl) {
        String response = null;
        try {

            URL url = new URL(ocUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");

            InputStream input = new BufferedInputStream(connection.getInputStream());
            response = streamToString(input);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     *
     * @param input
     * @return StringBuilder of stream
     * @throws IOException
     */
    public String streamToString(InputStream input) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null){
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            input.close();
        }

        return sb.toString();
    }
}
