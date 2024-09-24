package com.iotsmartaliv.model.booking;

public class TransactionModel {
    String bookingName;
    String bookingDate;
    String rate;
    int rateIncrese;


    public TransactionModel(String bookingName, String bookingDate, String rate, int rateIncrese) {
        this.bookingName = bookingName;
        this.bookingDate = bookingDate;
        this.rate = rate;
        this.rateIncrese = rateIncrese;
    }

    public String getBookingName() {
        return bookingName;
    }

    public void setBookingName(String bookingName) {
        this.bookingName = bookingName;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public int getRateIncrese() {
        return rateIncrese;
    }

    public void setRateIncrese(int rateIncrese) {
        this.rateIncrese = rateIncrese;
    }
}
