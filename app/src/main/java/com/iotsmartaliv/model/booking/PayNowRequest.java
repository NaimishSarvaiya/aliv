package com.iotsmartaliv.model.booking;

public class PayNowRequest {
    String customer_ID;
    int amount;
    int deposit;
    String room_name;
    int booking_ID;
    String intentID;
    PaymentType payment_type;

    public PayNowRequest(String customer_ID, int amount, int deposit, String room_name, int booking_ID, String intentID, PaymentType payment_type) {
        this.customer_ID = customer_ID;
        this.amount = amount;
        this.deposit = deposit;
        this.room_name = room_name;
        this.booking_ID = booking_ID;
        this.intentID = intentID;
        this.payment_type = payment_type;
    }
}
