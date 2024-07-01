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
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.intelligoo.sdk.BluetoothLeService;
import com.intelligoo.sdk.LibDevModel;
import com.intelligoo.sdk.LibInterface;
import com.intelligoo.sdk.ScanCallBackSort;
import com.intelligoo.sdk.ScanCallback;
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

/**
 * This is class use as Service for open door on device Shake.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2018-10-23
 */
public class ShakeOpenService extends Service implements SensorEventListener {
    private static final String TAG = "ShakeOpenService";
    // private static boolean shaked = false;
    public long current_time;
    private ApiServiceProvider apiServiceProvider;
    private boolean isReadyToOpenDoor = false;
    private Activity context;
    String openingDoorDeviceSN;

    /**
     * Callback method to open the door successfully.
     */
    final LibInterface.ManagerCallback callback = (result, bundle) -> {
        //mHandler.sendEmptyMessage(OPEN_AGAIN);
        Log.d(TAG, "callback: Device");
        if (result == 0x00) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            new SaveAccessLogTask(ShakeOpenService.this, new AccessLogModel("", openingDoorDeviceSN, "Shake to open door", dateFormat.format(new Date()))).execute();
            Toast.makeText(ShakeOpenService.this, "Door Open Successfully.", Toast.LENGTH_SHORT).show();
        } else {
            if (result == 48) {
                new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(ShakeOpenService.this, "Result Error Timer Out", Toast.LENGTH_SHORT).show());
            } else {
                new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(ShakeOpenService.this, "Failure:" + result, Toast.LENGTH_SHORT).show());
            }
        }
        registerListener();
    };
    public long open_door_time;
    int distance;
    int rssi;
    NotificationManager mNotifyManager;
    NotificationCompat.Builder mBuilder;
    NotificationChannel notificationChannel;
    String NOTIFICATION_CHANNEL_ID = "1";
    /**
     * Callback method to check scanned device and to open the nearest device lock on click.
     */
    ScanCallback oneKeyScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(ArrayList<String> deviceList, ArrayList<Integer> rssi) {
            Log.d(TAG, "Device SCna: " + deviceLIST.size());
            Log.e("ScanCallback", "size=" + deviceList.size() + "," + deviceList.toString() + "," + rssi.toString());

            if (deviceLIST.size() == 0) {
                registerListener();
                Log.d(TAG, "deviceLIST.size() Device SCna: " + deviceLIST.size());
                Toast.makeText(ShakeOpenService.this, "Device is not assign", Toast.LENGTH_SHORT).show();
                return;
            }
            if (deviceList.size() == 0) {
                registerListener();
                Log.d(TAG, "Device deviceList.size() SCna: " + deviceList.size());
                Toast.makeText(ShakeOpenService.this, "No Device Found On Search.", Toast.LENGTH_SHORT).show();
                return;
            }


            for (DeviceObject deviceObject : deviceLIST) {
                deviceObject.setRssi(-500);
            }
            for (DeviceObject device : deviceLIST) {
                for (int i = 0; i < deviceList.size(); i++) {
                    String devSn = deviceList.get(i);
                    if (device.getDeviceSnoWithoutAlphabet().equalsIgnoreCase(devSn)) {
                        device.setRssi(rssi.get(i));
                    }
                }
            }
            Collections.sort(deviceLIST, (o1, o2) -> {
                Integer x1 = o1.getRssi();
                Integer x2 = o2.getRssi();
                return x2.compareTo(x1);
            });


            if (deviceLIST.get(0).getRssi() > -70) {
                LibDevModel libDev = getLibDev(deviceLIST.get(0));
                openingDoorDeviceSN = deviceLIST.get(0).getDeviceSno();
                int ret = LibDevModel.openDoor(ShakeOpenService.this, libDev, callback);
                if (ret != 0) {
                    registerListener();
                    Toast.makeText(ShakeOpenService.this, ErrorMsgDoorMasterSDK.getErrorMsg(ret)
                            , Toast.LENGTH_SHORT).show();
                }
            } else {
                registerListener();
                Toast.makeText(ShakeOpenService.this, "Opening Distance is too far. Kindly go nearer the device. Thank you", Toast.LENGTH_SHORT).show();
            }


         /*   int ret = LibDevModel.controlDevice(OpenDoorActivity.this, 0x00, libDev, null, callback);
            if (ret == 0) {
                return;
            } else {
                pressed = false;
                Toast.makeText(OpenDoorActivity.this, "RET：" + ret, Toast.LENGTH_SHORT).show();
            }*/
        }

        @Override
        public void onScanResultAtOnce(final String devSn, int rssi) {
            //  shaked = false;
        }
    };
    //摇一摇参数
    private SensorManager sensorManager;
    private Vibrator vibrator;


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
        Intent intent = new Intent(getApplicationContext(), BluetoothLeService.class);
        SharePreference.getInstance(ShakeOpenService.this).putBoolean(SHAKE_ENABLE, true);

        Log.d("RUNNER : ", "OnCreate... \n");

        Bitmap IconLg = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_foreground);

        mNotifyManager = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(this, "26");
        mBuilder.setContentTitle(getString(R.string.app_name))
                .setContentText("Aliv Shake Service is running...")
                .setTicker("Aliv Shake Service is running...")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(IconLg)
                .setPriority(Notification.PRIORITY_HIGH)
                .setVibrate(new long[]{1000})
//                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setOngoing(true)
                .setAutoCancel(false);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel("26", "My Notifications", NotificationManager.IMPORTANCE_HIGH);

            // Configure the notification channel.
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{1000});
            notificationChannel.enableVibration(true);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            mNotifyManager.createNotificationChannel(notificationChannel);

            mBuilder.setChannelId("26");
            startForeground(12, mBuilder.build());
        } else {
            //  mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            //   mNotifyManager.notify(1, mBuilder.build());
        }
//        bindService(intent, conn, BIND_AUTO_CREATE);
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //  distance = intent.getIntExtra("distance", 25);
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
        Log.d(TAG, "onDestroy: ");
        if (sensorManager != null) {
            unRegisterListener();
        }
        sensorManager = null;
        vibrator = null;
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
        float shakelimit = 30.0f;
        float average = (float) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
        if (average > shakelimit) {
            unRegisterListener();
            vibrator.vibrate(200);
            current_time = System.currentTimeMillis();
            Toast.makeText(ShakeOpenService.this, "Shake", Toast.LENGTH_SHORT).show();

            performOpenDoorOperation();

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
                super.onPostExecute(deviceObjects);
                deviceLIST = deviceObjects;
                Log.d(TAG, "onPostExecute: sensor " + deviceLIST.size());
                LibDevModel.stopScanDevice();
             /*       PowerManager pm = (PowerManager)getSystemService(POWER_SERVICE);
                    PowerManager.WakeLock lock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "com.iotsmart:IOT_SensorRead");
                    lock.acquire(60*1000L *//*1 minutes*//*);*/
                PowerManager.WakeLock screenLock = ((PowerManager) getSystemService(POWER_SERVICE)).newWakeLock(
                        PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "com.iotsmart:IOT_SensorRead");
                screenLock.acquire(20 * 1000L/* 20 sec.*/);

                //  int ret1 = LibDevModel.scanDevice(ShakeOpenService.this, true, 1300, oneKeyScanCallback);         // A key to open the door
                int ret1 = LibDevModel.scanDeviceBG(ShakeOpenService.this, 1300, new ScanCallBackSort() {
                    @Override
                    public void onScanResult(ArrayList<Map<String, Integer>> devSnRssiList) {
                        if (deviceLIST == null || deviceLIST.size() == 0) {
                            registerListener();
                            Toast.makeText(getApplicationContext(), "Device is not assign.", Toast.LENGTH_SHORT).show();
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
                        Collections.sort(deviceLIST, (o1, o2) -> {
                            Integer x1 = o1.getRssi();
                            Integer x2 = o2.getRssi();
                            return x2.compareTo(x1);
                        });


                        if (deviceLIST.get(0).getRssi() > -70) {
                            LibDevModel libDev = getLibDev(deviceLIST.get(0));
                            openingDoorDeviceSN = deviceLIST.get(0).getDeviceSno();
                            if (isReadyToOpenDoor) {
                                int ret = LibDevModel.openDoor(ShakeOpenService.this, libDev, callback);
                                if (ret != 0) {
                                    Log.d(TAG, "onScanResult: if -> ret != 0");
                                    registerListener();
                                    Toast.makeText(ShakeOpenService.this, ErrorMsgDoorMasterSDK.getErrorMsg(ret), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                registerListener();
                                Toast.makeText(getApplicationContext(), "User can not access at this time", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            registerListener();
                            Toast.makeText(ShakeOpenService.this, "Opening Distance is too far. Kindly go nearer the device. Thank you", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onScanResultAtOnce(String s, int i) {

                    }
                });         // A key to open the door
                if (ret1 != 0) {
                    //  Toast.makeText(ShakeOpenService.this, "scanFailure" + ret1, Toast.LENGTH_SHORT).show();
                    registerListener();
                }

            }
        }.execute();
    }

    private void performOpenDoorOperation() {

        String isAcessible = SharePreference.getInstance(ShakeOpenService.this).getString("isAccessable");

        if (isAcessible.equals("1")) {

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
                    .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                    .build();

            ApiServices api = retrofit.create(ApiServices.class);

            Call<ResponseBody> call = api.getCurrentTime();

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    ResponseBody sucessRespnse = response.body();
                    try {
                        JSONObject jsonObject = new JSONObject(sucessRespnse.string());
                        String dateTime = jsonObject.optString("date");
                        Date serverDate = utcToLocalTimeZone(dateTime);
                        String isAcessible = SharePreference.getInstance(ShakeOpenService.this).getString("isAccessable");

                        if (!SharePreference.getInstance(ShakeOpenService.this).getString("deviceStartTime").equalsIgnoreCase("")
                                && !SharePreference.getInstance(ShakeOpenService.this).getString("deviceEndTime").equalsIgnoreCase("")) {

                            Date startTime = utcToLocalTimeZone(SharePreference.getInstance(ShakeOpenService.this).getString("deviceStartTime"));
                            Date endTime = utcToLocalTimeZone(SharePreference.getInstance(ShakeOpenService.this).getString("deviceEndTime"));

                            isReadyToOpenDoor = accessWithinRange(isAcessible, startTime, endTime, serverDate);
//                    SharePreference.getInstance(getActivity()).putString(getResources().getString(R.string.server_current_time), dateTime);
                            Log.d("EndTimeeCheck: ", serverDate + " EndTime:" + endTime + " StartTime:" + startTime);
                        } else {
                            isReadyToOpenDoor = true;
                        }

                        callToOpenDoor();

                    } catch (Exception e) {

                        try {
                        Thread.sleep(750);
                        } catch (InterruptedException error) {
                            error.printStackTrace();
                        }
                        registerListener();

                        e.printStackTrace();

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(ShakeOpenService.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    try {
                        Thread.sleep(750);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    registerListener();
                }
            });

        }
        else {
            Toast.makeText(ShakeOpenService.this, "No Internet", Toast.LENGTH_SHORT).show();
            try {
                Thread.sleep(750);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            registerListener();
        }
   }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
