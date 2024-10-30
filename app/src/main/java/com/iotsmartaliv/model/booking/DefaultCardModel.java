package com.iotsmartaliv.model.booking;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.stripe.android.model.PaymentMethod;

public class DefaultCardModel {
    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("paymentMethod")
    @Expose
    private PaymentMethodModel paymentMethod;

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

    public PaymentMethodModel getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethodModel paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

}
