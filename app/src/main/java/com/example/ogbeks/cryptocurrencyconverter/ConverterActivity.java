package com.example.ogbeks.cryptocurrencyconverter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ConverterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private String countryName;
    private double btcRate;
    private double ethRate;
    private Spinner spinnerFrom;
    private Spinner spinnerTo;
    private String currencyFrom;
    private String currencyTo;
    private EditText inputAmountText;
    private TextView convertedAmountLabel;
    private TextView convertedAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_converter);
        //Get the data parse from the MainActivity

        countryName = getIntent().getStringExtra("country");
        btcRate = getIntent().getDoubleExtra("btcRate",0.00);
        ethRate = getIntent().getDoubleExtra("ethRate",0.00);
        CustomAddValues();
    }

    // add items into spinner dynamically
    public void CustomAddValues() {

        spinnerFrom = (Spinner) findViewById(R.id.spinner_from);
        spinnerTo =(Spinner) findViewById(R.id.spinner_to);
        List<String> list = new ArrayList<String>();
        list.add("BTC");
        list.add("ETH");
        list.add(countryName);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFrom.setAdapter(dataAdapter);
        spinnerTo.setAdapter(dataAdapter);
    }
    // get the selected dropdown list value
    public void ConvertCurrency(View v) {

        spinnerFrom = (Spinner) findViewById(R.id.spinner_from);
        spinnerTo = (Spinner) findViewById(R.id.spinner_to);
        convertedAmountLabel =(TextView) findViewById(R.id.converted_textview_label);
        convertedAmount =(TextView) findViewById(R.id.converted_textview);

        currencyFrom= String.valueOf(spinnerFrom.getSelectedItem());
        currencyTo= String.valueOf(spinnerTo.getSelectedItem());

        inputAmountText =(EditText) findViewById(R.id.inputAmount);
        String editInputAmount =inputAmountText.getText().toString().trim();
        double inputAmount = Double.parseDouble(editInputAmount);
        double amountConverter;

        if (editInputAmount== null || editInputAmount.length()==0||editInputAmount.equals("")||editInputAmount.isEmpty()){
            inputAmountText.setError("Pls,Enter the amount");
            inputAmountText.requestFocus();
        }
        else {
            if(currencyFrom.matches(currencyTo))
            {
                convertedAmountLabel.setText(currencyTo);
                convertedAmount.setText(inputAmountText.getText().toString());
            }
            else if(currencyFrom.matches("BTC")&& currencyTo.matches("ETH"))
            {
                convertedAmountLabel.setText(currencyTo);
                amountConverter = ethRate/btcRate * inputAmount;
                convertedAmount.setText(formatRate(amountConverter));
            }
            else if(currencyFrom.matches("ETH")&& currencyTo.matches("BTC"))
            {
                convertedAmountLabel.setText(currencyTo);
                amountConverter = btcRate/ethRate * inputAmount;
                convertedAmount.setText(formatRate(amountConverter));

            }
            else if(currencyFrom.matches("ETH")&& currencyTo.matches(countryName))
            {
                convertedAmountLabel.setText(currencyTo);
                amountConverter = ethRate * inputAmount;
                convertedAmount.setText(formatRate(amountConverter));

            }
            else if(currencyFrom.matches("BTC")&& currencyTo.matches(countryName))
            {
                convertedAmountLabel.setText(currencyTo);
                amountConverter = btcRate * inputAmount;
                convertedAmount.setText(formatRate(amountConverter));
            }
            else if(currencyFrom.matches(countryName)&& currencyTo.matches("ETH"))
            {
                convertedAmountLabel.setText(currencyTo);
                amountConverter = inputAmount/ethRate;
                convertedAmount.setText(formatRate(amountConverter));

            }
            else if(currencyFrom.matches(countryName)&& currencyTo.matches("BTC"))
            {
                convertedAmountLabel.setText(currencyTo);
                amountConverter = inputAmount/btcRate;
                convertedAmount.setText(formatRate(amountConverter));

            }
        }


    }
    private String formatRate(double rate) {
        DecimalFormat rateFormat = new DecimalFormat("0.0000");
        return rateFormat.format(rate);
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Doing nothing
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
