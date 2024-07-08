package com.iotsmartaliv.model;

public class OpenVideoDeviceRelayRequest {
    private String automation_device_ID;
    private String relay_number;

    public OpenVideoDeviceRelayRequest(String automation_device_ID, String relay_number) {
        this.automation_device_ID = automation_device_ID;
        this.relay_number = relay_number;
    }
}
