package com.iotsmartaliv.fcm;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.doormaster.vphone.inter.DMVPhoneModel;
//import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.iotsmartaliv.R;
import com.iotsmartaliv.activity.MainActivity;
import com.iotsmartaliv.activity.SplashActivity;
import com.iotsmartaliv.activity.ViewPager.BroadcastCommunityActivity;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.services.RefreshDeviceListService;
import com.iotsmartaliv.services.ShakeOpenService;
import com.iotsmartaliv.utils.NotificationUtil;
import com.iotsmartaliv.utils.SharePreference;
import com.thinmoo.utils.ChangeServerUtil;
import com.thinmoo.utils.ServerContainer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.ExecutionException;

//import io.fabric.sdk.android.services.concurrency.AsyncTask;

import static com.iotsmartaliv.constants.Constant.LOGIN_DETAIL;
import static com.iotsmartaliv.constants.Constant.VO_IP;
import static com.iotsmartaliv.constants.Constant.VO_PORT;
//import static com.iotsmartaliv.twilio.activity.VideoActivity.isCallActive;

/**
 * This class is used as
 *
 * @author CanopusInfosystems
 * @version 1.0
 * @since 14 Jun 2019
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";

    public void wakeScreen() {
        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH
                ? pm.isInteractive()
                : pm.isScreenOn(); // check if screen is on
        if (!isScreenOn) {
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP |
                    PowerManager.ON_AFTER_RELEASE, "appname::Aliv");
            wl.acquire(6000); //set your time in milliseconds
            wl.release();
        }
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());
        Log.e(TAG, "Message Received");


        if (remoteMessage.getData().size() > 0) {
            wakeScreen();

            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
//            try {

//                JSONObject jsonObject = new JSONObject(remoteMessage.getData().get("data"));
            String notification_type = remoteMessage.getData().get("type");
            Log.i(TAG, "ybbonMessageReceived: " + notification_type);

//            if (notification_type.equalsIgnoreCase("")) {
//                Log.i(TAG, "ybbbonMessageReceived: App Update Notification Received");
////                    appUpdateMessage(jsonObject);
//            } else {
//                Log.i(TAG, "ybbbonMessageReceived: App Update Notification Not Received");
//            }
            if ((notification_type.equalsIgnoreCase("broadcast update"))) {
                broadcastUpdateMessage(remoteMessage);
            } else if ((notification_type.equalsIgnoreCase("end call"))) {
                Intent intent = new Intent("custom-event-name");
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
            } else if (notification_type.equalsIgnoreCase("Version Update")) {
                forground(remoteMessage);
            } else if (notification_type.equalsIgnoreCase("updatedevice")) {
                forground(remoteMessage);
//                    JSONObject notification_data = (JSONObject)jsonObject.get("data");
//                    String notification_title = notification_data.optString("message_title");
//                    String notification_body = notification_data.optString("body");
//
//                    forGroundNotification(notification_title,notification_body);
//                    Intent intent = new Intent(this, RefreshDeviceListService.class);
//                    startService(intent);

            } else if ((notification_type.equalsIgnoreCase("login in different device"))) {
                LOGIN_DETAIL = null;
                DMVPhoneModel.exit();
                stopService(new Intent(this, ShakeOpenService.class));
                SharePreference.getInstance(this).clearPref();
                SharePreference.getInstance(this).putBoolean(Constant.HAS_ON_BOARDING_SHOWN, true);
//                    new AsyncTask<Void, Void, Void>() {
//                        @Override
//                        protected Void doInBackground(Void... params) {
                try {
                    FirebaseMessaging.getInstance().deleteToken();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("FCMTOKEN", "doInBackground: " + e.getLocalizedMessage());
                }
//                            return null;
//                        }
//
//                        @Override
//                        protected void onPostExecute(Void result) {
//                            Log.d("FCMTOKEN", "doInBackground: Done");
//                        }
//                    }.execute();

                boolean isForeground = false;
                try {
                    isForeground = new ForegroundCheckTask().execute(this).get();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
                if (isForeground) {
                    // App is running
                    startActivity(new Intent(this, SplashActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                } else {
                    // App is not running
                }
            } else {

            }
//                else {
//                    Log.e(TAG, "Call Notification Receive.");
//                    JSONObject messagejsonObject = new JSONObject(remoteMessage.getData().get("message"));
//                    String push_type = messagejsonObject.optString("push_type");
//                    if (push_type.equalsIgnoreCase("calling")) {
//                        String data = messagejsonObject.optString("data");
//                        if (data == null) {
//                            Log.e("GTdemo", "receiver payload = null");
//                        } else {
//                            boolean isForeground = false;
//                            try {
//                                isForeground = new ForegroundCheckTask().execute(this).get();
//                            } catch (ExecutionException | InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                            if (!isForeground) {
//
//                                JSONObject dataJson = new JSONObject(data);
//                                String callerName = dataJson.optString("dev_name");
//
//
//                                //初始化sdk
//
//                                try {
//                                    Context context = getApplicationContext();
//                                    String ip = SharePreference.getInstance(this).getString(VO_IP);
//                                    String port = SharePreference.getInstance(this).getString(VO_PORT);
//
//                                    if (ip == null && ip.isEmpty() && ip.equals("")) {
//                                        ip = "113.197.36.196";
//                                    }
//
//                                    if (port == null && port.isEmpty() && port.equals("")) {
//                                        port = "5061";
//                                    }
//                                    //  DMVPhoneModel.initDMVPhoneSDK(this);
////                                    ChangeServerUtil.getInstance().initConfig(this);
//                                    ServerContainer serverContainer2 = new ServerContainer("43.229.85.122", "8099", "自定义应用服务器");
//                                    ChangeServerUtil.getInstance().setAppServer(serverContainer2);
//                                    ServerContainer sipContainer = new ServerContainer(ip, port, "CustomVideoServer");
//                                    ChangeServerUtil.getInstance().setVideoServer(sipContainer);
//                                    DMVPhoneModel.initConfig(context);
//                                    DMVPhoneModel.initDMVPhoneSDK(context, "DDemo", false, false);
//                                    DMVPhoneModel.enableCallPreview(true, this);//打开预览消息界面显示
//                                    DMVPhoneModel.setLogSwitch(true);
//                                    DMVPhoneModel.setCameraId(1, context);
//                                    DMVPhoneModel.receivePushNotification(callerName != null ? callerName : "Incoming call from unidentified");
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
//                                    NotificationUtil.getInstance(this).scheduleNotification(this, false, callerName);
//                            }
//
//                        }
//                    }
//                }

//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
        }

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }

    private void forGroundNotification(String title, String messageBody) {
        // Create an intent that will be fired when the user taps the notification.
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        String channelId = "default_notification_channel_id";
        // Setup the NotificationBuilder as usual
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.app_logo)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        // Notification manager to issue the notification
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private void broadcastUpdateMessage(RemoteMessage remoteMessage) {
        try {

            JSONObject jsonObject = new JSONObject(remoteMessage.getData().get("data"));
//            JSONObject jsonAdditionalParams = new JSONObject(remoteMessage.getData().get("additionalParam"));
            String mTitle = remoteMessage.getNotification().getTitle();
            String mBody = remoteMessage.getNotification().getBody();


            String appuserID = jsonObject.optString("appuser_ID");
            String broadcastID = jsonObject.optString("broadcast_ID");

            if (!mTitle.equalsIgnoreCase("") && !mBody.equalsIgnoreCase("")) {
                sendBroadcastNotification(mTitle, mBody, appuserID, broadcastID, remoteMessage);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void forground(RemoteMessage remoteMessage) {
        try {

//            JSONObject jsonObject = new JSONObject(remoteMessage.getData().get("data"));
            String mTitle = remoteMessage.getNotification().getTitle();
            String mBody = remoteMessage.getNotification().getBody();


//            if (!mTitle.equalsIgnoreCase("") && !mBody.equalsIgnoreCase("")){

            sendForgroundNotification(mTitle, mBody);

//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void appUpdateMessage(JSONObject jsonObject) {

        try {
            JSONObject notification_data = (JSONObject) jsonObject.get("data");
            String notification_title = notification_data.optString("message_title");
            String notification_body = notification_data.optString("body");

            Log.i(TAG, "ybbbappUpdateMessage:\n notification data-> " + notification_data.toString()
                    + "\n notification Title-> " + notification_title
                    + "\n notification Body-> " + notification_body);

            if (!notification_title.equalsIgnoreCase("") && !notification_body.equalsIgnoreCase("")) {

                sendNotification(notification_title, notification_body);

                Log.e("Called notif", "True");

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);
        //sendRegistrationToServer(token);
    }


    /**
     * Persist token to third-party servers.
     * <p>
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
    }

    /**
     * @param strTitle
     * @param strMessage
     */
    private void sendNotification(String strTitle, String strMessage) {

        Log.e("SEND NOTIF", "TRUE");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.iotsmart&hl=en"));

        /*Intent intent = new Intent(this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);*/
        PendingIntent pendingIntent;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        } else {
            pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
        }

        String channelId = "default_notification_channel_id";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.app_logo)
                        .setContentTitle(strTitle)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(strMessage))
                        .setContentText(strMessage)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0, notificationBuilder.build());

        Log.e("SENT", "TRUE");
    }

    void sendForgroundNotification(String strTitle, String strBody) {
        Intent intent = new Intent(this, RefreshDeviceListService.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        PendingIntent pendingIntent;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        } else {
            pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
        }

//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
//                PendingIntent.FLAG_ONE_SHOT);

        String channelId = "default_notification_channel_id";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.app_logo)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setContentTitle(strTitle)
                        .setContentText(strBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

    }

    private void sendBroadcastNotification(String strTitle, String strBody, String appuserID, String broadcastID, RemoteMessage remoteMessage) {

        Intent intent = new Intent(this, BroadcastCommunityActivity.class);
        intent.putExtra("APP_USER_ID", appuserID);
        intent.putExtra("BROADCAST_ID", broadcastID);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        PendingIntent pendingIntent;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        } else {
            pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
        }

//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
//                PendingIntent.FLAG_ONE_SHOT);

        String channelId = "default_notification_channel_id";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.app_logo)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setContentTitle(strTitle)
                        .setContentText(strBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
        Intent intent2 = new Intent("com.iotsmartaliv.UPDATE_BROADCAST_ACTIVITY");
        intent.putExtra("data", remoteMessage.getData().get("data"));
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent2);
    }


    class ForegroundCheckTask extends AsyncTask<Context, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Context... params) {
            final Context context = params[0].getApplicationContext();
            return isAppOnForeground(context);
        }

        private boolean isAppOnForeground(Context context) {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
            if (appProcesses == null) {
                return false;
            }
            final String packageName = context.getPackageName();
            for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
                    return true;
                }
            }
            return false;
        }
    }
}