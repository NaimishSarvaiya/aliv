package com.iotsmartaliv.model.feedback;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FeedbackDetails {

    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("totalRecords")
    @Expose
    private int totalRecords;
    @SerializedName("data")
    @Expose
    private FeedbackDetailsData data;

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public FeedbackDetailsData getData() {
        return data;
    }

    public void setData(FeedbackDetailsData data) {
        this.data = data;
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }
}

