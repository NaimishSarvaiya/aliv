package com.iotsmartaliv.apiAndSocket.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * This class is used as .
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 7/8/19 :August.
 */
public class CountryArrayData {

    @SerializedName("country")
    @Expose
    private List<Country> country = null;
    @SerializedName("default_country")
    @Expose
    private List<DefaultCountryData> defaultCountry = null;

    public List<Country> getCountry() {
        return country;
    }

    public void setCountry(List<Country> country) {
        this.country = country;
    }

    public List<DefaultCountryData> getDefaultCountry() {
        return defaultCountry;
    }

    public void setDefaultCountry(List<DefaultCountryData> defaultCountry) {
        this.defaultCountry = defaultCountry;
    }
}
