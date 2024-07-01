package com.iotsmartaliv.model;

/**
 * This model class is used for video intercom .
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2018-10-24
 */
public class VideoIntercomModel {
    String iotName;
    String intercomName;

    public String getIotName() {
        return iotName;
    }

    public void setIotName(String iotName) {
        this.iotName = iotName;
    }

    public String getIntercomName() {
        return intercomName;
    }

    public void setIntercomName(String intercomName) {
        this.intercomName = intercomName;
    }
}
