package com.iotsmartaliv.model.booking;

public class UpdateBookingDatesRequest {
    int booking_ID;
    String start_date;
    String end_date;
    String new_selected_slot;

    public UpdateBookingDatesRequest(int booking_ID, String start_date, String end_date, String new_selected_slot) {
        this.booking_ID = booking_ID;
        this.start_date = start_date;
        this.end_date = end_date;
        this.new_selected_slot = new_selected_slot;
    }

    public UpdateBookingDatesRequest(int booking_ID, String start_date, String end_date) {
        this.booking_ID = booking_ID;
        this.start_date = start_date;
        this.end_date = end_date;
    }
}
