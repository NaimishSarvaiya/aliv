package com.iotsmartaliv.model.booking;

public class DeleteCardRequestModel {
    String customer_ID;
    String paymentMethod_ID;

    public DeleteCardRequestModel(String customer_ID, String paymentMethod_ID) {
        this.customer_ID = customer_ID;
        this.paymentMethod_ID = paymentMethod_ID;
    }
}
