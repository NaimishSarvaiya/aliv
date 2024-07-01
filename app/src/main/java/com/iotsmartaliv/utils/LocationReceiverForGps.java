package com.iotsmartaliv.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * This class is used for tracking location .
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2018-07-23
 */

public class LocationReceiverForGps extends BroadcastReceiver {
    private static GpsChangeListener mGpsChangeListener;

    public static void setGpsChangeListener(GpsChangeListener listener) {
        mGpsChangeListener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().matches("android.location.PROVIDERS_CHANGED")) {
            if (mGpsChangeListener != null) {
                mGpsChangeListener.onGPSChange();
            }
        }
    }

    public interface GpsChangeListener {
        void onGPSChange();
    }
}
