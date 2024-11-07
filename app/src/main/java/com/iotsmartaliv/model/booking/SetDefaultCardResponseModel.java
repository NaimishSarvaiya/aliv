package com.iotsmartaliv.model.booking;

import com.google.gson.annotations.SerializedName;

public class SetDefaultCardResponseModel {
    @SerializedName("statusCode")
    private int statusCode;

    @SerializedName("msg")
    private String message;
    @SerializedName("customerID")
    private String customerID;
    @SerializedName("default_paymentMethodID")
    private String default_paymentMethodID;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getDefault_paymentMethodID() {
        return default_paymentMethodID;
    }

    public void setDefault_paymentMethodID(String default_paymentMethodID) {
        this.default_paymentMethodID = default_paymentMethodID;
    }
}
