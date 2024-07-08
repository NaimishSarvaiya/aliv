package com.iotsmartaliv.model;

public class RelayItemModel {
    private String automationDeviceId;
    private int relayNumber;

    public RelayItemModel(String deviceName, int relayNumber) {
        this.automationDeviceId = deviceName;
        this.relayNumber = relayNumber;
    }

    public String getAutomationDeviceId() {
        return automationDeviceId;
    }

    public int getRelayNumber() {
        return relayNumber;
    }
}
