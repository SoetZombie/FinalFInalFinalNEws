package com.example.luke.finalfinalfinalnews;

import android.text.TextUtils;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import org.json.JSONArray;
import org.json.JSONException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;



public class Query {
    private static final int URL_CONNECTION_READ_TIMEOUT = 10000;
    private static final int URL_CONNECTION_CONNECT_TIMEOUT = 15000;

    private Query() {
    }

    public static List<News> fetchNewsData(String requestUrl) {
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
           e.printStackTrace();
        }
        List<News> newsList = extractNews(jsonResponse);
        return newsList;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(URL_CONNECTION_READ_TIMEOUT);
            urlConnection.setConnectTimeout(URL_CONNECTION_CONNECT_TIMEOUT);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            int httpResponse = urlConnection.getResponseCode();
            if (httpResponse == 301) {
                String newUrl = urlConnection.getHeaderField("Location");
                urlConnection = (HttpURLConnection) new URL(newUrl).openConnection();
            }
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    @NonNull
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

    @Nullable
    private static List<News> extractNews(String newsJSON) {
        List<News> newses = new ArrayList<>();
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }
        try {

            JSONObject core = new JSONObject(newsJSON);
            JSONObject response = core.getJSONObject("response");
            if (response.has("results")) {
                JSONArray results = response.getJSONArray("results");

                for (int i = 0; i < results.length(); i++) {
                    JSONObject newsObject = results.getJSONObject(i);
                    String title = newsObject.getString("webTitle");
                    String section = newsObject.getString("sectionName");
                    JSONObject finalObject = newsObject.getJSONObject("fields");
                    String author;
                    if (finalObject.has("byline")) {
                        author = finalObject.getString("byline");
                    } else {
                        author = "";
                    }
                    String date;
                    if (newsObject.has("webPublicationDate")) {
                        date = newsObject.getString("webPublicationDate");
                    } else {
                        date = "";
                    }
                    String url = newsObject.getString("webUrl");
                    newses.add(new News(title, section, author, date, url));
                }
            }

        } catch (JSONException e) {
           e.printStackTrace();
        }
        return newses;
    }
}
