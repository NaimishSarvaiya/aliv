package com.iotsmartaliv.model.feedback;

import java.util.ArrayList;

public class FeedbackModel {
    public Integer statusCode;
    public String msg;
    public String totalRecords;
    public ArrayList<FeedbackData> data;

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

    public ArrayList<FeedbackData> getData() {
        return data;
    }

    public void setData(ArrayList<FeedbackData> data) {
        this.data = data;
    }

    public String getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(String totalRecords) {
        this.totalRecords = totalRecords;
    }
}
