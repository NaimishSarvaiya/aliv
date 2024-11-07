package com.iotsmartaliv.model.booking;

public class AttacheCardOnStripeRequest {
    String token;
    String customer_ID;

    public AttacheCardOnStripeRequest(String token, String customer_ID) {
        this.token = token;
        this.customer_ID = customer_ID;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCustomer_ID() {
        return customer_ID;
    }

    public void setCustomer_ID(String customer_ID) {
        this.customer_ID = customer_ID;
    }
}
