package com.chiragawale.booklistingapp;

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


public class QueryUtil {
    private static final String LOG_TAG = "QueryUtl";


    private QueryUtil() {
    }

    /*
    Returns the Refined data collected from the server
     */
    public static List<Book> getBookData(String request_url) {
        Log.i("Fetech Earthquake data", "LOG ");

        URL url = createUrl(request_url);
        String result = "";
        try {
             result = makeHttpRequest(url);
        }catch (IOException i){
            Log.e(LOG_TAG,"IO Exception");
        }

        List<Book> bookList = getBookList(result);
        return bookList;
    }

    /*
    Converts the String url to URL object
     */
    public static URL createUrl(String requestUrl) {
        URL url = null;

        try {
            url = new URL(requestUrl);
        } catch (MalformedURLException m) {
            Log.e(LOG_TAG, "Malformed URL exception");
        }
        return url;

    }

    /*
    Connects to the server and gets the input stream sent by the server
     */
    public static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }


        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }


        } catch (IOException e) {
            Log.e(LOG_TAG, "IOException");
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

    /*
    Convert the input stream from server into string that contains whole json response from server
     */
    public static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();

        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
        BufferedReader reader = new BufferedReader(inputStreamReader);
        String line = reader.readLine();
        while (line != null) {
            stringBuilder.append(line);
            line = reader.readLine();
        }

        return stringBuilder.toString();

    }
    //Returns the list of books after extracting data from the JSON response
    public static List<Book> getBookList(String jsonResponse){
        List<Book> bookList = new ArrayList<>();
        double price = 0;
        String currency = "";
        double averageRating = 0;
        try {
            JSONObject root = new JSONObject(jsonResponse);
            JSONArray itemsArray = root.getJSONArray("items");
            for(int i = 0; i < itemsArray.length();i++) {
                JSONObject currentItem = itemsArray.getJSONObject(i);
                JSONObject volumeInfo = currentItem.getJSONObject("volumeInfo");
                String bookName = volumeInfo.getString("title");
                Log.v(LOG_TAG,bookName);

                JSONArray authorsArray =volumeInfo.getJSONArray("authors");
                String author = authorsArray.getString(0);
                Log.v(LOG_TAG,author);

                if(volumeInfo.has("averageRating")) {
                     averageRating = volumeInfo.getDouble("averageRating");
                    Log.v(LOG_TAG, "AVERAGE RAING " + averageRating);
                }
                JSONObject saleInfo = currentItem.getJSONObject("saleInfo");
                String saleability = saleInfo.getString("saleability");
                Log.v(LOG_TAG,saleability);

                if (saleability.equalsIgnoreCase("FOR_SALE")){
                    JSONObject listPrice = saleInfo.getJSONObject("listPrice");
                    price = listPrice.getDouble("amount");
                    currency = listPrice.getString("currencyCode");
                    Log.v(LOG_TAG,"PRice " + price);
                    Log.v(LOG_TAG,currency);

                }
                String infoLink = volumeInfo.getString("infoLink");

                Book book = new Book(bookName,author,infoLink,currency,averageRating,price);
                bookList.add(book);
            }



        } catch (JSONException e) {

            Log.e(LOG_TAG,"JSON EXCEPTION");
        }
        return bookList;
    }

}
