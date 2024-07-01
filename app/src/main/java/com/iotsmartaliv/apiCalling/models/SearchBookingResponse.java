package com.iotsmartaliv.apiCalling.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * This class is used as .
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 21/8/19 :August : 2019 on 20 : 35.
 */
public class SearchBookingResponse {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("search_criteria")
    @Expose
    private String searchCriteria;
    @SerializedName("data")
    @Expose
    private ArrayList<SearchBookingData> data = null;

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

    public String getSearchCriteria() {
        return searchCriteria;
    }

    public void setSearchCriteria(String searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    public ArrayList<SearchBookingData> getData() {
        return data;
    }

    public void setData(ArrayList<SearchBookingData> data) {
        this.data = data;
    }

}
