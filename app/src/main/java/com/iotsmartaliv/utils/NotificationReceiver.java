package com.iotsmartaliv.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static com.iotsmartaliv.utils.NotificationUtil.CHANNEL_ID;

public class NotificationReceiver extends BroadcastReceiver{
    private static String LOCK_SCREEN_KEY = "lockScreenKey";
    private static String CALLER_NAME = "CALLER_NAME";
    @Override
    public void onReceive(Context context, Intent intent) {

        String callerName = intent.getStringExtra(CALLER_NAME);

        if(intent.getBooleanExtra(LOCK_SCREEN_KEY, true)) {
            NotificationUtil.getInstance(context).showNotificationWithFullScreenIntent(context,
                    true,
                    CHANNEL_ID,
                    callerName,
                    "Incoming call");
        } else {
            NotificationUtil.getInstance(context).showNotificationWithFullScreenIntent(context,
                    false,
                    CHANNEL_ID,
                    callerName,
                    "Incoming call");
        }
    }

    public static Intent build(Context context,Boolean isLockScreen,String callerName) {

        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.putExtra(LOCK_SCREEN_KEY, isLockScreen);
        intent.putExtra(CALLER_NAME,callerName);

        return intent;
    }
}
