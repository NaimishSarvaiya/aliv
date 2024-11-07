package com.iotsmartaliv.model.booking;

import java.util.List;

public class BookingSlotDetailResponse {
    private int statusCode;
    private String msg;
    private List<BookingSlotDetailData> data;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<BookingSlotDetailData> getData() {
        return data;
    }

    public void setData(List<BookingSlotDetailData> data) {
        this.data = data;
    }
}
