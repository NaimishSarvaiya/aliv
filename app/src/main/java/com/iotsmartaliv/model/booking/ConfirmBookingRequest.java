package com.iotsmartaliv.model.booking;

public class ConfirmBookingRequest {
    String payment_ID   ;
    int booking_ID;
    String deposit_payment_ID;
    PaymentType payment_type;
    public ConfirmBookingRequest(String payment_ID, int booking_ID, String deposit_payment_ID,PaymentType payment_type) {
        this.payment_ID = payment_ID;
        this.booking_ID = booking_ID;
        this.deposit_payment_ID = deposit_payment_ID;
        this.payment_type = payment_type;
    }
    public ConfirmBookingRequest(String payment_ID, int booking_ID) {
        this.payment_ID = payment_ID;
        this.booking_ID = booking_ID;
    }
}
