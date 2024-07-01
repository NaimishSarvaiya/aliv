package com.iotsmartaliv.model;

public class CheckBookingRequest {
    private String appuser_ID;
    private String device_SN;

    public CheckBookingRequest(String appuser_ID, String device_SN) {
        this.appuser_ID = appuser_ID;
        this.device_SN = device_SN;
    }
}
