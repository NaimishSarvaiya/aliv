package com.iotsmartaliv.interfaces;

import com.iotsmartaliv.apiAndSocket.models.IntercomRelayData;
import com.iotsmartaliv.apiAndSocket.models.VideoDeviceData;

/**
 * This class is used as Interface for video intercom device click.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 20/3/19 :March : 2019 on 11 : 51.
 */
public interface VideoIntercomItemClick {
    void onClickIntercomDevice(VideoDeviceData videoIntercomModel);

    void onRemotelyOpenDoor(String device_Sn);

    void onOptionClickIntercomeDevice(VideoDeviceData videoDeviceData);

    void onRelayClick(IntercomRelayData relayItem);
}
