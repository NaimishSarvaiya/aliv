package com.iotsmartaliv.model;

public class CountryPhoneFormat {
    private String countryCode;
    private int minLength;
    private int maxLength;

    public CountryPhoneFormat(String countryCode, int minLength, int maxLength) {
        this.countryCode = countryCode;
        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public int getMinLength() {
        return minLength;
    }

    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }
}
