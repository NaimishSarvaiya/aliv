package com.iotsmartaliv.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import android.util.Log;

import com.google.gson.Gson;
import com.iotsmartaliv.R;
import com.iotsmartaliv.apiCalling.listeners.RetrofitListener;
import com.iotsmartaliv.apiCalling.models.ErrorObject;
import com.iotsmartaliv.apiCalling.models.ResponseData;
import com.iotsmartaliv.apiCalling.retrofit.ApiServiceProvider;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.roomDB.AccessLogModel;
import com.iotsmartaliv.roomDB.DatabaseClient;
import com.iotsmartaliv.roomDB.DeviceDao;
import com.iotsmartaliv.utils.SharePreference;
import com.iotsmartaliv.utils.Util;

import java.util.List;

import static com.iotsmartaliv.constants.Constant.LOGIN_DETAIL;
import static com.iotsmartaliv.constants.Constant.LOGIN_PREFRENCE;

/**
 * This class is used for package monitor that is manage package install and uninstall.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 30 Nov,2018
 */


public class DeviceLogSyncService extends Service {

    public static final String UPDATE_APPS_PACKAGE = "com.iotsmart.services.UPDATE_DEVICE_SYNC";
    DeviceDao deviceDao;
    ApiServiceProvider apiServiceProvider;
    String userId;
    private BroadcastReceiver broadcastReceiverPkgMoniter, connectivityChnageBroadcast;
    private String TAG = DeviceLogSyncService.class.getName();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    NotificationChannel notificationChannel;
    NotificationManager mNotifyManager;
    NotificationCompat.Builder mBuilder;

    @Override
    public void onCreate() {
        super.onCreate();
        deviceDao = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().deviceDao();
        apiServiceProvider = ApiServiceProvider.getInstance(this);

        connectivityChnageBroadcast = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {
                    if (isOnline(context)) {
                        Intent localIntent = new Intent(DeviceLogSyncService.UPDATE_APPS_PACKAGE);
                        context.sendBroadcast(localIntent);
                    } else {
                        Log.e("NetworkChangeReceiver", "Conectivity Failure !!! ");
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        };

        this.broadcastReceiverPkgMoniter = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (!isOnline(DeviceLogSyncService.this)) {
                    return;
                }
                ResponseData responseData = new Gson().fromJson(SharePreference.getInstance(DeviceLogSyncService.this).getString(LOGIN_PREFRENCE), ResponseData.class);
                try {
                    userId = responseData.getAppuserID();
                } catch (Exception e) {
                    e.getStackTrace();
                    return;
                }
                List<AccessLogModel> list = deviceDao.getAllDeviceLog();
                for (AccessLogModel accessLogModel : list) {
//                    if (Util.checkInternet(context)) {
                        Util.checkInternet(context, new Util.NetworkCheckCallback() {
                            @Override
                            public void onNetworkCheckComplete(boolean isAvailable) {
                                if (isAvailable){
                                    apiServiceProvider.postAccessLog(userId, accessLogModel, new RetrofitListener<AccessLogModel>() {

                                        @Override
                                        public void onResponseSuccess(AccessLogModel accessLogModel1, String apiFlag) {
                                            switch (apiFlag) {
                                                case Constant.UrlPath.POST_ACCESS_LOG:
                                                    new DeleteAccessLogTask(DeviceLogSyncService.this, accessLogModel1).execute();
                                                    break;
                                            }
                                        }

                                        @Override
                                        public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                                            Util.firebaseEvent(Constant.APIERROR, DeviceLogSyncService.this,Constant.UrlPath.SERVER_URL+apiFlag, LOGIN_DETAIL.getUsername(),LOGIN_DETAIL.getAppuserID(),errorObject.getStatus());
                                        }
                                    });
                                }
                            }
                        });

//                    }
                }
            }
        };
        IntentFilter intentFilterNetwork = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connectivityChnageBroadcast, intentFilterNetwork);
        final IntentFilter theFilter = new IntentFilter();
        theFilter.addAction(UPDATE_APPS_PACKAGE);
        registerReceiver(this.broadcastReceiverPkgMoniter, theFilter);

        Bitmap IconLg = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_foreground);

        mNotifyManager = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(this, "30");
        mBuilder.setContentTitle(getString(R.string.app_name))
                .setContentText("Aliv Background Service")
                .setTicker("Aliv is running in the background")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(IconLg)
                .setPriority(Notification.PRIORITY_HIGH)
                .setVibrate(new long[]{1000})
//                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setOngoing(true)
                .setAutoCancel(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel("30", "My Notifications", NotificationManager.IMPORTANCE_HIGH);

            // Configure the notification channel.
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{1000});
            notificationChannel.enableVibration(true);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            mNotifyManager.createNotificationChannel(notificationChannel);

            mBuilder.setChannelId("30");
            startForeground(20, mBuilder.build());
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(this.broadcastReceiverPkgMoniter);
        unregisterReceiver(connectivityChnageBroadcast);

    }

    private boolean isOnline(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            //should check null because in airplane mode it will be null
            return (netInfo != null && netInfo.isConnected());
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
    }

    public class DeleteAccessLogTask extends AsyncTask<Void, Void, Void> {

        private Context context;
        private AccessLogModel accessLogModel;

        public DeleteAccessLogTask(Context context, AccessLogModel accessLogModel) {
            this.context = context;
            this.accessLogModel = accessLogModel;

        }

        @Override
        protected Void doInBackground(Void... voids) {
            DatabaseClient.getInstance(context).getAppDatabase()
                    .deviceDao()
                    .deleteAccessLog(accessLogModel);
            Log.d("DeleteAccessLogTask", "doInBackground: " + accessLogModel.getDevice_SN() + ":Time:- " + accessLogModel.getEvent_time());

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}
