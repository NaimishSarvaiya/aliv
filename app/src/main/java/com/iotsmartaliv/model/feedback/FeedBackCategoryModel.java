package com.iotsmartaliv.model.feedback;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FeedBackCategoryModel {
    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private List<FeedbackCategoryData> data;

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

    public List<FeedbackCategoryData> getData() {
        return data;
    }

    public void setData(List<FeedbackCategoryData> data) {
        this.data = data;
    }

}
