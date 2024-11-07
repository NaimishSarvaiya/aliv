package com.iotsmartaliv.model.booking;

public class AddBookingSlotRequest {
    int room_ID;
    int appuser_ID;
    String start_date;
    String end_date;
    String slot_ID;
    int fees;
    int deposit;


    public AddBookingSlotRequest(int room_ID, int appuser_ID, String start_date, String end_date, String slot_ID, int fees, int deposit) {
        this.room_ID = room_ID;
        this.appuser_ID = appuser_ID;
        this.start_date = start_date;
        this.end_date = end_date;
        this.slot_ID = slot_ID;
        this.fees = fees;
        this.deposit = deposit;
    }
    public AddBookingSlotRequest(int room_ID, int appuser_ID, String start_date, String end_date, String slot_ID, int fees) {
        this.room_ID = room_ID;
        this.appuser_ID = appuser_ID;
        this.start_date = start_date;
        this.end_date = end_date;
        this.slot_ID = slot_ID;
        this.fees = fees;
    }

    public AddBookingSlotRequest(int room_ID, int appuser_ID, String start_date, String end_date, int fees, int deposit) {
        this.room_ID = room_ID;
        this.appuser_ID = appuser_ID;
        this.start_date = start_date;
        this.end_date = end_date;
        this.fees = fees;
        this.deposit = deposit;
    }
    public AddBookingSlotRequest(int room_ID, int appuser_ID, String start_date, String end_date, int fees) {
        this.room_ID = room_ID;
        this.appuser_ID = appuser_ID;
        this.start_date = start_date;
        this.end_date = end_date;
        this.fees = fees;
    }


}
