package com.iotsmartaliv.model.booking;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DeleteCardModel {
    @SerializedName("statusCode")
    private int statusCode;

    @SerializedName("msg")
    private String message;
    @SerializedName("paymentMethodId")
    private String paymentMethodId;

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

    public String getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(String paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }
}
