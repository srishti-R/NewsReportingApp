package com.android.example.newsreportingapp;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class QueryUtils {

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e("QueryUtils", "Problem building the URL ", e);
        }
        return url;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e("QueryUtils", "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e("QueryUtils", "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static List<News> extractNews(String jsonResponse) {
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }
        // Create an empty ArrayList that we can start adding earthquakes to
        List<News> gridNews = new ArrayList<News>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {


            String data="";
            JSONObject jsonObject=new JSONObject(jsonResponse);
            JSONObject obj= jsonObject.getJSONObject("response");
            JSONArray results=obj.getJSONArray("results");
            for(int i=0; i<results.length(); i++) {


                JSONObject h = results.getJSONObject(i);
                String time1=h.getString("webPublicationDate");

                String headline = h.getString("webTitle");
                String url = h.getString("webUrl");

                JSONObject fields=h.getJSONObject("fields");
                String image=fields.getString("thumbnail");
               Drawable imageId=LoadImageFromWebURL(image);


                    News recent = new News( headline, time1,imageId, url );
                    gridNews.add(recent);
                }





        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the JSON results", e);
        }

        // Return the list of earthquakes
        return gridNews;
    }
    public static List<News> fetchNewsData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);
        Log.e("this", "fetching...");
        // Perform HTTP request to the URL and receive a JSON response back



        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e("QueryUtils", "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Earthquake}s
        List<News> news = extractNews(jsonResponse);

        // Return the list of {@link Earthquake}s
        return news;
    }
    public static Drawable LoadImageFromWebURL(String url) {
        try {
            InputStream iStream = (InputStream) new URL(url).getContent();
            Drawable drawable = Drawable.createFromStream(iStream, url);
            return drawable;
        } catch (Exception e) {
            return null;
        }}

}

