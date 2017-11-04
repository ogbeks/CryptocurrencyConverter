package com.example.ogbeks.cryptocurrencyconverter;

/**
 * Created by ogbeks on 11/4/2017.
 */

public class CryptoCurrency {
    private String countryName;

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    private double btcRate;

    public String getCountryName() {
        return countryName;
    }

    public double getBtcRate() {
        return btcRate;
    }

    public double getEthRate() {
        return ethRate;
    }

    private double ethRate;
    private boolean isActive;

    public CryptoCurrency (String name, double btc, double eth)
    {
        countryName = name;
        btcRate = btc;
        ethRate = eth;
        isActive =true;
    }

    @Override
    public String toString() {
        return "CryptoCurrency{" +
                "countryName='" + countryName + '\'' +
                ", btcRate=" + btcRate +
                ", ethRate=" + ethRate +
                ", isActive=" + isActive +
                '}';
    }
}
