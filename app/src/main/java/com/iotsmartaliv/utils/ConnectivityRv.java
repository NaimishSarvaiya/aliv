package com.iotsmartaliv.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ConnectivityRv extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, final Intent intent) {
        Log.v("ConnectivityReceiver", "onReceive().." + intent.getAction());
    }
}