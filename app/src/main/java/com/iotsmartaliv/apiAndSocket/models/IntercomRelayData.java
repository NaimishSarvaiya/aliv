package com.iotsmartaliv.apiAndSocket.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class IntercomRelayData implements Serializable {
    @SerializedName("automation_device_ID")
    @Expose
    private String automationDeviceID;
    @SerializedName("attached_relay")
    @Expose
    private String attachedRelay;
    @SerializedName("relay_name")
    @Expose
    private String relayName;

    public String getAutomationDeviceID() {
        return automationDeviceID;
    }

    public void setAutomationDeviceID(String automationDeviceID) {
        this.automationDeviceID = automationDeviceID;
    }

    public String getAttachedRelay() {
        return attachedRelay;
    }

    public void setAttachedRelay(String attachedRelay) {
        this.attachedRelay = attachedRelay;
    }

    public String getRelayName() {
        return relayName;
    }

    public void setRelayName(String relayName) {
        this.relayName = relayName;
    }
}
