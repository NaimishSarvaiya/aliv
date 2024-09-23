package com.iotsmartaliv.model.feedback;

import java.util.ArrayList;

public class MessageHistory {
    public Integer statusCode;
    public String msg;
    public String totalRecords;
    public ArrayList<MessageHistoryData> data;

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

    public String getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(String totalRecords) {
        this.totalRecords = totalRecords;
    }

    public ArrayList<MessageHistoryData> getData() {
        return data;
    }

    public void setData(ArrayList<MessageHistoryData> data) {
        this.data = data;
    }
}
