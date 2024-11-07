package com.iotsmartaliv.model.booking;

public class PayNowRequest {
    String customer_ID;
    int amount;
    int deposit;
    String room_name;
    int booking_ID;
    String payment_ID;
    String deposit_payment_ID;
    PaymentType payment_type;

    public PayNowRequest(String customer_ID, int amount, int deposit, String room_name, int booking_ID, String payment_ID, PaymentType payment_type,String deposit_payment_ID) {
        this.customer_ID = customer_ID;
        this.amount = amount;
        this.deposit = deposit;
        this.room_name = room_name;
        this.booking_ID = booking_ID;
        this.payment_ID = payment_ID;
        this.payment_type = payment_type;
        this.deposit_payment_ID = deposit_payment_ID;

    }
}
