package com.example.ogbeks.cryptocurrencyconverter;

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
import java.util.Iterator;

/**
 * Created by ogbeks on 11/4/2017.
 */
public final class QueryUtils {
    /** Tag for the log messages */
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();
    /**
     * Query the CryptoCompare dataset and return an {@link ArrayList<CryptoCurrency>} object to represent all the java developer in Lagos.
     */
    public static ArrayList<CryptoCurrency> fetchCryptoCurrencyData(String requestUrl) {

        //Create a URL link from the string param
        URL url = createUrl(requestUrl);
        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Extract relevant fields from the JSON response and create an {@link ArrayList<CryptoCurrency>} object
        ArrayList<CryptoCurrency> cryptoCurrencies = extractFeatureFromJson(jsonResponse);

        // Return the {@link ArrayList<CryptoCurrency>}
        return cryptoCurrencies;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        // If the Url is null, then return early
        if(url == null)
        {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(30000 /* milliseconds */);
            urlConnection.setConnectTimeout(30000 /* milliseconds */);

            urlConnection.connect();
            //If the request was successful(response code 200);
            //then read the input stream and parse the response
            if(urlConnection.getResponseCode()==200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
                //  Log.i("jsonResponse", jsonResponse);
            }
            else{
                Log.e(LOG_TAG, "Error response code:" + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            // TODO: Handle the exception
            Log.e(LOG_TAG, "Problem retrieving the cryptoCompare JSON results",e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                inputStream.close();
            }
        }
        // Log.i("extractfromJson", jsonResponse);
        return jsonResponse;

    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        //this create the string builder which is mutable
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
    //This is not used in the app but a dynamic JSON reader. for both object and array.
    private void parseJson(JSONObject data) {

        if (data != null) {
            Iterator<String> it = data.keys();
            while (it.hasNext()) {
                String key = it.next();

                try {
                    if (data.get(key) instanceof JSONArray) {
                        JSONArray arry = data.getJSONArray(key);
                        int size = arry.length();
                        for (int i = 0; i < size; i++) {
                            parseJson(arry.getJSONObject(i));
                        }
                    } else if (data.get(key) instanceof JSONObject) {
                        parseJson(data.getJSONObject(key));
                    } else {
                        System.out.println("" + key + " : " + data.optString(key));
                    }
                } catch (Throwable e) {
                    System.out.println("" + key + " : " + data.optString(key));
                    e.printStackTrace();

                }
            }
        }
    }

    /**
     * Return an {@link ArrayList<CryptoCurrency>} ArrayList by parsing out Json information
     *
     */
    private static ArrayList<CryptoCurrency> extractFeatureFromJson(String cryptoCurrencyJson) {
        //If the JSON string is empty or null, then return early
        // Log.v("CryptoCurrenciessJson", cryptoCurrencyJson);
        if(TextUtils.isEmpty(cryptoCurrencyJson)){
            return null;
        }
        try {
            JSONObject baseJsonResponse = new JSONObject(cryptoCurrencyJson);
            JSONObject jsonBtcObject = baseJsonResponse.getJSONObject("BTC");
            JSONObject jsonEthObject = baseJsonResponse.getJSONObject("ETH");
            ArrayList<CryptoCurrency> cryptoCurrencies = new ArrayList<CryptoCurrency>();
            // If there are results in the features array
            Iterator iteratorBTC = jsonBtcObject.keys();
            Iterator iteratorETH = jsonBtcObject.keys();
            while(iteratorBTC.hasNext()&& iteratorETH.hasNext()){
                String countryName = (String)iteratorBTC.next();
                double btcRate = jsonBtcObject.optDouble(countryName);
                double ethRate = jsonEthObject.optDouble(countryName);
                //This add each cryptocurrency.
                cryptoCurrencies.add(new CryptoCurrency(countryName,btcRate,ethRate));
                }
                // Create a new {@link CryptoCurrency} object
                return cryptoCurrencies;

            }
         catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the cryptoCurrency JSON results", e);
        }
        return null;
    }

}
