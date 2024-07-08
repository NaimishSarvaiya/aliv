package com.iotsmartaliv.utils;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.iotsmartaliv.R;
import com.iotsmartaliv.dmvphonedemotest.DmCallIncomingActivity;

import java.util.concurrent.TimeUnit;

public class NotificationUtil {

    private static NotificationUtil notificationUtil;

    private  Long SCHEDULE_TIME = 6L;
    public static String CHANNEL_ID = "channelId";

    public static NotificationUtil getInstance(Context context) {
        if (notificationUtil == null) {
            notificationUtil = new NotificationUtil();
        }
        return notificationUtil;
    }


    public void showNotificationWithFullScreenIntent(
            Context context,
            Boolean isLockScreen,
            String channelId,
            String title,
            String description

    ) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.app_logo)
                .setContentTitle(title)
                .setContentText(description)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setFullScreenIntent(getFullScreenIntent(context,isLockScreen), true)
                .setAutoCancel(true);


        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        buildChannel(notificationManager);

        Notification notification = builder.build();

        notificationManager.notify(0, notification);
    }

    private void buildChannel(NotificationManager notificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name = "Example Notification Channel";
            String descriptionText = "This is used to demonstrate the Full Screen Intent";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(descriptionText);

            notificationManager.createNotificationChannel(channel);
        }
    }

    private PendingIntent getFullScreenIntent(Context context, Boolean isLockScreen){

        /*if (isLockScreen)
            LockScreenActivity::class.java
        else
        FullScreenActivity::class.java*/
        Intent intent = new Intent(context, DmCallIncomingActivity.class);

        // flags and request code are 0 for the purpose of demonstration
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        } else {
            return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
    }

    public void scheduleNotification(Context context,Boolean isLockScreen, String callerName) {
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Long timeInMillis = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(SCHEDULE_TIME);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, getReceiver(context,isLockScreen,callerName));
    }

    private PendingIntent getReceiver(Context context,Boolean isLockScreen, String callerName) {
        // for demo purposes no request code and no flags

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return PendingIntent.getBroadcast(
                    context,
                    0,
                    NotificationReceiver.build(context, isLockScreen, callerName),
                    PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
                    );
        } else {
            return PendingIntent.getBroadcast(
                    context,
                    0,
                    NotificationReceiver.build(context, isLockScreen, callerName),
                    PendingIntent.FLAG_UPDATE_CURRENT
                    );
        }
    }
}
