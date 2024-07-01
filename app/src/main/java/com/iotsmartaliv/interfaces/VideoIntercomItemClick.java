package com.iotsmartaliv.interfaces;

import com.iotsmartaliv.apiCalling.models.DeviceObject;

/**
 * This class is used as Interface for video intercom device click.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 20/3/19 :March : 2019 on 11 : 51.
 */
public interface VideoIntercomItemClick {
    void onClickIntercomDevice(DeviceObject videoIntercomModel);

    void onRemotelyOpenDoor(String device_Sn);
}
