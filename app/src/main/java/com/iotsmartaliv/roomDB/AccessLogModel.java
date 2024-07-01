package com.iotsmartaliv.roomDB;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * This class is used as .
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 8/7/19 :July : 2019 on 17 : 23.
 */

@Entity
public class AccessLogModel {
    String number, device_SN, description, event_time;
    @PrimaryKey(autoGenerate = true)
    private int id;

    public AccessLogModel(String number, String device_SN, String description, String event_time) {
        this.number = number;
        this.device_SN = device_SN;
        this.description = description;
        this.event_time = event_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDevice_SN() {
        return device_SN;
    }

    public void setDevice_SN(String device_SN) {
        this.device_SN = device_SN;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEvent_time() {
        return event_time;
    }

    public void setEvent_time(String event_time) {
        this.event_time = event_time;
    }
}
