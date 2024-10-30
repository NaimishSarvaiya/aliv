package com.iotsmartaliv.model.booking;

public class PayNowWithoutDepositRequest {
    String customer_ID;
    int amount;
    String room_name;
    int booking_ID;
    public PayNowWithoutDepositRequest(String customer_ID, int amount, String room_name, int booking_ID) {
        this.customer_ID = customer_ID;
        this.amount = amount;
        this.room_name = room_name;
        this.booking_ID = booking_ID;
    }

}
