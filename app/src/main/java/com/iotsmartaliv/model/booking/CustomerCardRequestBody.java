package com.iotsmartaliv.model.booking;

public class CustomerCardRequestBody {
    private String customer_ID;

    public CustomerCardRequestBody(String customer_ID) {
        this.customer_ID = customer_ID;
    }

    public String getCustomer_ID() {
        return customer_ID;
    }

    public void setCustomer_ID(String customer_ID) {
        this.customer_ID = customer_ID;
    }
}
