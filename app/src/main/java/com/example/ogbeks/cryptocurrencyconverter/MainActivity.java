package com.example.ogbeks.cryptocurrencyconverter;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView cryptoCurrencyListView;
    RelativeLayout progressLayout;
    /** TextView that is displayed when the list is empty */
    private TextView mEmptyStateTextView;

    CryptoCurrencyAsyncTask task = new CryptoCurrencyAsyncTask();
    /** Tag for the log messages */
    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    /** URL to query the CryptoCompaare database for world currency for BTC and ETH information */
    private static final String CryptoCompare_REQUEST_URL ="https://min-api.cryptocompare.com/data/pricemulti?fsyms=BTC,ETH&tsyms=USD,EUR,HUF,JPY,AUD,RUB,NGN,CHF,GBP,HKD,NOK,CAD,CNY,MYR,SGD,INR,QAR,NZD,LRD,GHS,BSD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        cryptoCurrencyListView = (ListView) findViewById(R.id.crypto_currency_converter);
        cryptoCurrencyListView.setEmptyView(mEmptyStateTextView);
        progressLayout = (RelativeLayout) findViewById(R.id.progressbar_view);
        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Kick off an {@link AsyncTask} to perform the network request
            task.execute(CryptoCompare_REQUEST_URL);

        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.progressbar_view);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }

    }
    private void updateUri(final ArrayList<CryptoCurrency> cryptoCurrencies){
        // Create an {@link cryptoCurrencyAdapter}, whose data source is a list of
        // {@link cryptoCurrency}s. The adapter knows how to create list item views for each item
        // in the list.
        CryptoCurrencyAdapter cryptoCurrencyAdapter = new CryptoCurrencyAdapter(this, cryptoCurrencies);

        cryptoCurrencyListView.setAdapter(cryptoCurrencyAdapter);
        //Create an OnItemClickListener when a cryptoCurrencyListView child is clicked
        cryptoCurrencyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //This get the current cryptoCurrency on the listview
                CryptoCurrency cryptoCurrency = cryptoCurrencies.get(position);
                //this create and explicit intent which receive two params, the current context,
                // and the activityClass for the intent
               Intent converterIntent = new Intent(MainActivity.this, ConverterActivity.class);
                Log.v("MainActivity", cryptoCurrency.toString());

                //the putExtra method allows data to be parse together with the intent,
                //Here we pass the three @params for the next view : country name, btc rate, and eth rate
                converterIntent.putExtra("country", cryptoCurrency.getCountryName());
                converterIntent.putExtra("btcRate", cryptoCurrency.getBtcRate());
                converterIntent.putExtra("ethRate", cryptoCurrency.getEthRate());

                //The converter activity is instantiated
                startActivity(converterIntent);
            }

        });

    }
    private class CryptoCurrencyAsyncTask extends AsyncTask<String, Void, ArrayList<CryptoCurrency>>
    {
        @Override
        protected void onPreExecute() {

            progressLayout.setVisibility(View.VISIBLE);
            cryptoCurrencyListView.setVisibility(View.GONE);

            super.onPreExecute();
        }

        @Override
        protected ArrayList<CryptoCurrency> doInBackground(String... urls) {
            // Don't perform the request if there are no URLs, or the first URL is null.
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }

            ArrayList<CryptoCurrency> CryptoCurrencies = QueryUtils.fetchCryptoCurrencyData(urls[0]);

            // Return the {@link Event} object as the result for the {@link TsunamiAsyncTask}
            return CryptoCurrencies;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(ArrayList<CryptoCurrency> cryptoCurrencys) {
            if(cryptoCurrencys == null){
                mEmptyStateTextView.setText(R.string.no_crypto_currencys);
            }
            progressLayout.setVisibility(View.GONE);
            cryptoCurrencyListView.setVisibility(View.VISIBLE);
            updateUri(cryptoCurrencys);
        }
    }
}
