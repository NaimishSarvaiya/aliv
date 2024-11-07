package com.iotsmartaliv.model.booking;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CustomerCardsResponse {
    @SerializedName("statusCode")
    private int statusCode;

    @SerializedName("msg")
    private String message;

    @SerializedName("cards")
    private List<PaymentMethodModel> cards;

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

    public List<PaymentMethodModel> getCards() {
        return cards;
    }

    public void setCards(List<PaymentMethodModel> cards) {
        this.cards = cards;
    }
}
