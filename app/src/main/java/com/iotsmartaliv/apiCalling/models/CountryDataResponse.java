package com.iotsmartaliv.apiCalling.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * This class is used as .
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 7/8/19 :August.
 */
public class CountryDataResponse {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private CountryArrayData countryArrayData;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public CountryArrayData getCountryArrayData() {
        return countryArrayData;
    }

    public void setCountryArrayData(CountryArrayData countryArrayData) {
        this.countryArrayData = countryArrayData;
    }
}
