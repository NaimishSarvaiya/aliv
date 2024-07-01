package com.iotsmartaliv.services;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.iotsmartaliv.apiCalling.listeners.RetrofitListener;
import com.iotsmartaliv.apiCalling.models.DeviceObject;
import com.iotsmartaliv.apiCalling.models.ErrorObject;
import com.iotsmartaliv.apiCalling.models.SuccessDeviceListResponse;
import com.iotsmartaliv.apiCalling.retrofit.ApiServiceProvider;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.roomDB.DatabaseClient;
import com.iotsmartaliv.utils.SharePreference;
import com.iotsmartaliv.utils.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.iotsmartaliv.constants.Constant.LOGIN_DETAIL;
import static com.iotsmartaliv.constants.Constant.deviceLIST;

public class RefreshDeviceListService extends Service implements RetrofitListener<SuccessDeviceListResponse> {
    private static final String TAG = "RefreshDeviceListServic";
    public static final String REFRESH_LIST_INTENT_FILTER = "REFRESH_LIST_INTENT_FILTER";

    ApiServiceProvider apiServiceProvider;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        apiServiceProvider = ApiServiceProvider.getInstance(this);
        Log.i(TAG, "onCreate: " + apiServiceProvider);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand: ");
        String loginUserId = SharePreference.getInstance(this).getString("login_user_id");
//        if (Util.checkInternet(this)) {
        Util.checkInternet(this, new Util.NetworkCheckCallback() {
            @Override
            public void onNetworkCheckComplete(boolean isAvailable) {
                if (isAvailable) {
                    apiServiceProvider.callForRefreshDeviceList(LOGIN_DETAIL.getAppuserID(), RefreshDeviceListService.this);
                }
            }
        });

//        }
        Log.i(TAG, "onStartCommand: ");
        return START_NOT_STICKY;
    }

    @Override
    public void onResponseSuccess(SuccessDeviceListResponse successDeviceListResponse, String apiFlag) {
        Log.i(TAG, "onResponseSuccess: " + apiFlag);
        switch (apiFlag) {
            case Constant.UrlPath.DEVICE_LIST_API:
                Log.i(TAG, "onResponseSuccess: DEVICE_LIST_API ----> " + successDeviceListResponse.getStatus());
                if (successDeviceListResponse.getStatus().equalsIgnoreCase("OK")) {
                    if (successDeviceListResponse.getData().size() > 0) {
                        Log.i(TAG, "onResponseSuccess: if");
                        deviceLIST = successDeviceListResponse.getData();
                        isDeviceAllowedToOpenDoor();
                        SaveTask st = new SaveTask();
                        st.execute();
                    } else {
                        deviceLIST = new ArrayList<>();
                        SaveTask st = new SaveTask();
                        st.execute();
                        Toast.makeText(this, "Device List Empty", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Log.i(TAG, "onResponseSuccess: else");
                    Toast.makeText(this, successDeviceListResponse.getMsg(), Toast.LENGTH_LONG).show();
                }
                try {
                    boolean foregroud = new ForegroundCheckTask().execute(this).get();
                    if (foregroud) {
                        Intent intent = new Intent(REFRESH_LIST_INTENT_FILTER);
                        sendBroadcast(intent);
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                break;
        }
        stopSelf();
    }

    private void isDeviceAllowedToOpenDoor() {
        Log.i(TAG, "isDeviceAllowedToOpenDoor: ");
        if (deviceLIST == null || deviceLIST.isEmpty()) {
            return;
        }
        for (DeviceObject deviceObject : deviceLIST) {
            String isaccessable = deviceObject.getIsAccessTimeEnabled();
            String serverStartTime = deviceObject.getAccessStarttime();
            String serverEndTime = deviceObject.getAccessEndtime();
            if (serverStartTime == null && serverEndTime == null) {
                SharePreference.getInstance(this).putString("isAccessable", "");
                SharePreference.getInstance(this).putString("deviceStartTime", "");
                SharePreference.getInstance(this).putString("deviceEndTime", "");
            } else {

                SharePreference.getInstance(this).putString("isAccessable", isaccessable);
                SharePreference.getInstance(this).putString("deviceStartTime", serverStartTime);
                SharePreference.getInstance(this).putString("deviceEndTime", serverEndTime);

            }
        }

    }

    @Override
        public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
        Log.i(TAG, "onResponseError:");
        switch (apiFlag) {
            case Constant.UrlPath.DEVICE_LIST_API:
                Util.firebaseEvent(Constant.APIERROR, RefreshDeviceListService.this, Constant.UrlPath.SERVER_URL + apiFlag, LOGIN_DETAIL.getUsername(), LOGIN_DETAIL.getAppuserID(), errorObject.getStatus());

                try {
                    Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
                }
                break;
        }
        stopSelf();
    }

    class SaveTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            //adding to database
            DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                    .deviceDao().deleteAll();
            DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                    .deviceDao()
                    .insert(deviceLIST);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //    Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
        }
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

    @Override
    public boolean onUnbind(Intent intent) {
        stopSelf();
        return super.onUnbind(intent);
    }
}
