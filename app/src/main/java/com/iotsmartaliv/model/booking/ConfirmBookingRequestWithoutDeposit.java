package com.iotsmartaliv.model.booking;

public class ConfirmBookingRequestWithoutDeposit {
    String payment_ID   ;
    int booking_ID;

    public ConfirmBookingRequestWithoutDeposit(String payment_ID, int booking_ID) {
        this.payment_ID = payment_ID;
        this.booking_ID = booking_ID;
    }
}
