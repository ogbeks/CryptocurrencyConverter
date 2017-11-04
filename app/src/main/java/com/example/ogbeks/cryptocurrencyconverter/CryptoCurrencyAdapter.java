package com.example.ogbeks.cryptocurrencyconverter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by ogbeks on 11/4/2017.
 */

public class CryptoCurrencyAdapter extends ArrayAdapter<CryptoCurrency> {

    public CryptoCurrencyAdapter(Context context, ArrayList<CryptoCurrency> currencies){

        super(context,0,currencies);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.cryptocurrency_list_item, parent, false);
        }
        CryptoCurrency currency = getItem(position);
        if (currency.isActive()) {
            // Find the TextView in the users_list.xml layout with the ID country_name
            TextView countryNameTextView = (TextView) listItemView.findViewById(R.id.country_name);
            // Get the version name from the current cryptoCurrency object and
            // set this text on the name TextView
            countryNameTextView.setText(currency.getCountryName());
            TextView btcRateTextView = (TextView) listItemView.findViewById(R.id.btc_rate);
            // Format the btc rate to show 2 decimal place
            String formattedBtcRate = formatRate(currency.getBtcRate());
            btcRateTextView.setText(formattedBtcRate);
            TextView ethRateTextView = (TextView) listItemView.findViewById(R.id.eth_rate);
            // Format the eth rate to show 2 decimal place
            String formattedEthRate = formatRate(currency.getEthRate());
            ethRateTextView.setText(formattedEthRate);
        }
        return listItemView;
    }
    private String formatRate(double rate) {
        DecimalFormat rateFormat = new DecimalFormat("0.00");
        return rateFormat.format(rate);
    }

}
