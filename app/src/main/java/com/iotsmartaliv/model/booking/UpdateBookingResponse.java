package com.iotsmartaliv.model.booking;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateBookingResponse {
    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private Data data;

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
    public class Data {

        @SerializedName("pending_amount")
        @Expose
        private Integer pendingAmount;
        @SerializedName("booking_ID")
        @Expose
        private Integer bookingID;

        public Integer getPendingAmount() {
            return pendingAmount;
        }

        public void setPendingAmount(Integer pendingAmount) {
            this.pendingAmount = pendingAmount;
        }

        public Integer getBookingID() {
            return bookingID;
        }

        public void setBookingID(Integer bookingID) {
            this.bookingID = bookingID;
        }

    }
}
