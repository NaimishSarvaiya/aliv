package com.iotsmartaliv.model.booking;

public class CreatePaymentStripeRequestWithoutDepost {
    String customer_ID;
    int amount;
    String paymentMethod_ID;
    String room_name;
    int booking_ID;
    int paymentFor;


    public CreatePaymentStripeRequestWithoutDepost(String customer_ID, int amount, String paymentMethod_ID, String room_name, int booking_ID, int paymentFor) {
        this.customer_ID = customer_ID;
        this.amount = amount;
        this.paymentMethod_ID = paymentMethod_ID;
        this.room_name = room_name;
        this.booking_ID = booking_ID;
        this.paymentFor = paymentFor;
    }
}

