package com.iotsmartaliv.services;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.PowerManager;
import android.os.Vibrator;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import android.util.Log;
import android.widget.Toast;

import com.intelligoo.sdk.BluetoothLeService;
import com.intelligoo.sdk.LibDevModel;
import com.intelligoo.sdk.LibInterface;
import com.intelligoo.sdk.ScanCallBackSort;
import com.iotsmartaliv.R;
import com.iotsmartaliv.apiCalling.models.DeviceObject;
import com.iotsmartaliv.apiCalling.retrofit.ApiServiceProvider;
import com.iotsmartaliv.apiCalling.retrofit.ApiServices;
import com.iotsmartaliv.roomDB.AccessLogModel;
import com.iotsmartaliv.roomDB.DatabaseClient;
import com.iotsmartaliv.utils.ErrorMsgDoorMasterSDK;
import com.iotsmartaliv.utils.NetworkAvailability;
import com.iotsmartaliv.utils.SaveAccessLogTask;
import com.iotsmartaliv.utils.SharePreference;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.iotsmartaliv.apiCalling.models.DeviceObject.getLibDev;
import static com.iotsmartaliv.constants.Constant.SHAKE_DISTANCE;
import static com.iotsmartaliv.constants.Constant.SHAKE_ENABLE;
import static com.iotsmartaliv.constants.Constant.deviceLIST;
import static com.iotsmartaliv.utils.CommanUtils.accessWithinRange;
import static com.iotsmartaliv.utils.CommanUtils.utcToLocalTimeZone;

public class ShakeOpenService extends Service implements SensorEventListener {
    private static final String TAG = "ShakeOpenService";
    private long current_time;
    private ApiServiceProvider apiServiceProvider;
    private boolean isReadyToOpenDoor = false;
    private String openingDoorDeviceSN;
    private SensorManager sensorManager;
    private Vibrator vibrator;
    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;
    private NotificationChannel notificationChannel;
    private static final String NOTIFICATION_CHANNEL_ID = "1";
    private int distance;
    private int rssi;

    /**
     * Callback method to open the door successfully.
     */
    final LibInterface.ManagerCallback callback = (result, bundle) -> {
        Log.d(TAG, "callback: Device");
        if (result == 0x00) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            new SaveAccessLogTask(ShakeOpenService.this, new AccessLogModel("", openingDoorDeviceSN, "Shake to open door", dateFormat.format(new Date()))).execute();
            Toast.makeText(ShakeOpenService.this, "Door Open Successfully.", Toast.LENGTH_SHORT).show();
        } else {
            new Handler(Looper.getMainLooper()).post(() -> {
                String message = (result == 48) ? "Result Error Timer Out" : "Failure: " + result;
                Toast.makeText(ShakeOpenService.this, message, Toast.LENGTH_SHORT).show();
            });
        }
        registerListener();
    };

    public ShakeOpenService() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        SharePreference.getInstance(ShakeOpenService.this).putBoolean(SHAKE_ENABLE, true);

        Log.d(TAG, "OnCreate...");

        setupNotification();
    }

    private void setupNotification() {
        Bitmap IconLg = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_foreground);
        mNotifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("Aliv Shake Service is running...")
                .setTicker("Aliv Shake Service is running...")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(IconLg)
                .setPriority(Notification.PRIORITY_HIGH)
                .setVibrate(new long[]{1000})
                .setOngoing(true)
                .setAutoCancel(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{1000});
            notificationChannel.enableVibration(true);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            mNotifyManager.createNotificationChannel(notificationChannel);

            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            startForeground(12, mBuilder.build());
        } else {
            mNotifyManager.notify(12, mBuilder.build());
        }
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        distance = SharePreference.getInstance(this).getIntForShake(SHAKE_DISTANCE);
        rssi = -50 - distance;
        Toast.makeText(this, "Shake to open door ON", Toast.LENGTH_SHORT).show();
        registerListener();
        return START_STICKY;
    }

    public void registerListener() {
        if (sensorManager != null) {
            sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI);
        }
    }

    public void unRegisterListener() {
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        unRegisterListener();
        SharePreference.getInstance(ShakeOpenService.this).putBoolean(SHAKE_ENABLE, false);
        Toast.makeText(this, "Shake to open door OFF.", Toast.LENGTH_SHORT).show();
        stopForeground(true);
        mNotifyManager.cancel(12);
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onSensorChanged(SensorEvent event) {
        float[] values = event.values;
        float x = values[0];
        float y = values[1];
        float z = values[2];
        float shakeLimit = 30.0f;
        float average = (float) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
        if (average > shakeLimit) {
            unRegisterListener();
            vibrator.vibrate(200);
            current_time = System.currentTimeMillis();
            Toast.makeText(ShakeOpenService.this, "Shake", Toast.LENGTH_SHORT).show();
            performOpenDoorOperation();
        }
    }

    private void performOpenDoorOperation() {
        String isAccessible = SharePreference.getInstance(ShakeOpenService.this).getString("isAccessible");

        if ("1".equals(isAccessible)) {
            callServerAPI();
        } else {
            isReadyToOpenDoor = true;
            callToOpenDoor();
        }
    }

    private void callServerAPI() {
        if (NetworkAvailability.getInstance(ShakeOpenService.this).checkNetworkStatus()) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://aliv.com.sg/alivapp/getCurrentTime/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiServices api = retrofit.create(ApiServices.class);

            Call<ResponseBody> call = api.getCurrentTime();

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        String dateTime = jsonObject.optString("date");
                        Date serverDate = utcToLocalTimeZone(dateTime);

                        if (!SharePreference.getInstance(ShakeOpenService.this).getString("deviceStartTime").isEmpty() &&
                                !SharePreference.getInstance(ShakeOpenService.this).getString("deviceEndTime").isEmpty()) {

                            Date startTime = utcToLocalTimeZone(SharePreference.getInstance(ShakeOpenService.this).getString("deviceStartTime"));
                            Date endTime = utcToLocalTimeZone(SharePreference.getInstance(ShakeOpenService.this).getString("deviceEndTime"));

                            isReadyToOpenDoor = accessWithinRange(SharePreference.getInstance(ShakeOpenService.this).getString("isAccessible"), startTime, endTime, serverDate);
                            Log.d("EndTimeCheck: ", serverDate + " EndTime:" + endTime + " StartTime:" + startTime);
                        } else {
                            isReadyToOpenDoor = true;
                        }
                        callToOpenDoor();
                    } catch (Exception e) {
                        handleAPIError();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(ShakeOpenService.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    handleAPIError();
                }
            });

        } else {
            Toast.makeText(ShakeOpenService.this, "No Internet", Toast.LENGTH_SHORT).show();
            handleAPIError();
        }
    }

    private void callToOpenDoor() {
        new AsyncTask<Void, Void, List<DeviceObject>>() {
            @Override
            protected List<DeviceObject> doInBackground(Void... voids) {
                return DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().deviceDao().getAllDeviceList();
            }

            @Override
            protected void onPostExecute(List<DeviceObject> deviceObjects) {
                deviceLIST = deviceObjects;
                Log.d(TAG, "onPostExecute: sensor " + deviceLIST.size());
                LibDevModel.stopScanDevice();

                PowerManager.WakeLock screenLock = ((PowerManager) getSystemService(POWER_SERVICE)).newWakeLock(
                        PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "com.iotsmart:IOT_SensorRead");
                screenLock.acquire(20 * 1000L /* 20 sec.*/);

                int ret1 = LibDevModel.scanDeviceBG(ShakeOpenService.this, 1300, new ScanCallBackSort() {
                    @Override
                    public void onScanResult(ArrayList<Map<String, Integer>> devSnRssiList) {
                        handleScanResults(devSnRssiList);
                    }

                    @Override
                    public void onScanResultAtOnce(String s, int i) {
                    }
                });

                if (ret1 != 0) {
                    registerListener();
                }
            }
        }.execute();
    }

    private void handleScanResults(ArrayList<Map<String, Integer>> devSnRssiList) {
        if (deviceLIST == null || deviceLIST.size() == 0) {
            registerListener();
            Toast.makeText(getApplicationContext(), "Device is not assigned.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (devSnRssiList == null || devSnRssiList.size() == 0) {
            registerListener();
            Toast.makeText(getApplicationContext(), "No Device Found On Scan.", Toast.LENGTH_SHORT).show();
            return;
        }

        for (DeviceObject deviceObject : deviceLIST) {
            deviceObject.setRssi(-500);
        }
        for (DeviceObject devBean : deviceLIST) {
            for (Map<String, Integer> devRssiDict : devSnRssiList) {
                if (devRssiDict.containsKey(devBean.getDeviceSnoWithoutAlphabet())) {
                    devBean.setRssi(devRssiDict.get(devBean.getDeviceSnoWithoutAlphabet()));
                }
            }
        }
        Collections.sort(deviceLIST, (o1, o2) -> Integer.compare(o2.getRssi(), o1.getRssi()));

        if (deviceLIST.get(0).getRssi() > -70) {
            LibDevModel libDev = getLibDev(deviceLIST.get(0));
            openingDoorDeviceSN = deviceLIST.get(0).getDeviceSno();
            if (isReadyToOpenDoor) {
                int ret = LibDevModel.openDoor(ShakeOpenService.this, libDev, callback);
                if (ret != 0) {
                    registerListener();
                    Toast.makeText(ShakeOpenService.this, ErrorMsgDoorMasterSDK.getErrorMsg(ret), Toast.LENGTH_SHORT).show();
                }
            } else {
                registerListener();
                Toast.makeText(getApplicationContext(), "User cannot access at this time", Toast.LENGTH_SHORT).show();
            }
        } else {
            registerListener();
            Toast.makeText(ShakeOpenService.this, "Opening Distance is too far. Kindly go nearer the device. Thank you", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleAPIError() {
        try {
            Thread.sleep(750);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        registerListener();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
