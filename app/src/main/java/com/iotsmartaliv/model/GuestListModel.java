package com.iotsmartaliv.model;

/**
 * This model class is used for set and get the data for guest list item.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2018-10-26
 */

public class GuestListModel {
    String guestName;
    String deviceName;

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
}
