package com.iotsmartaliv.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.doormaster.vphone.config.DMErrorReturn;
import com.doormaster.vphone.exception.DMException;
import com.doormaster.vphone.inter.DMModelCallBack.DMCallback;
import com.doormaster.vphone.inter.DMVPhoneModel;
//import com.google.firebase.iid.FirebaseInstanceId;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.iotsmartaliv.BuildConfig;
import com.iotsmartaliv.R;
import com.iotsmartaliv.apiCalling.listeners.RetrofitListener;
import com.iotsmartaliv.apiCalling.models.DeviceObject;
import com.iotsmartaliv.apiCalling.models.ErrorObject;
import com.iotsmartaliv.apiCalling.models.SuccessDeviceListResponse;
import com.iotsmartaliv.apiCalling.retrofit.ApiServiceProvider;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.fragments.HomeFragment;
import com.iotsmartaliv.fragments.community.CommunityJoinFragment;
import com.iotsmartaliv.fragments.community.CommunitySubListFragment;
import com.iotsmartaliv.roomDB.DatabaseClient;
import com.iotsmartaliv.services.DeviceLogSyncService;
import com.iotsmartaliv.utils.SharePreference;
import com.iotsmartaliv.utils.Util;

import java.io.IOException;
import java.util.ArrayList;

//import io.fabric.sdk.android.services.concurrency.AsyncTask;

import static com.iotsmartaliv.constants.Constant.LOGIN_DETAIL;
import static com.iotsmartaliv.constants.Constant.deviceLIST;
import static com.iotsmartaliv.constants.Constant.hideLoader;
import static com.iotsmartaliv.constants.Constant.showLoader;

import org.jsoup.Jsoup;

/**
 * This activity class is used for main activity with Nav-Drawer.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2018-10-23
 */
public class MainActivity extends AppCompatActivity implements RetrofitListener<SuccessDeviceListResponse>, View.OnClickListener {
    private static final String TAG = "MainActivity";
    public static final Intent[] POWERMANAGER_INTENTS = {
            new Intent().setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity")),
            new Intent().setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity")),
            new Intent().setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity")),
            new Intent().setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity")),
            new Intent().setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.appcontrol.activity.StartupAppControlActivity")),
            new Intent().setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity")),
            new Intent().setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.startupapp.StartupAppListActivity")),
            new Intent().setComponent(new ComponentName("com.oppo.safe", "com.oppo.safe.permission.startup.StartupAppListActivity")),
            new Intent().setComponent(new ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity")),
            new Intent().setComponent(new ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.BgStartUpManager")),
            new Intent().setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity")),
            new Intent().setComponent(new ComponentName("com.samsung.android.lool", "com.samsung.android.sm.ui.battery.BatteryActivity")),
            new Intent().setComponent(new ComponentName("com.htc.pitroad", "com.htc.pitroad.landingpage.activity.LandingPageActivity")),
            new Intent().setComponent(new ComponentName("com.asus.mobilemanager", "com.asus.mobilemanager.MainActivity"))
    };
    public static DrawerLayout drawerLayout;
    public TextView tvHeader;
    public ImageView imgDraweHeader;
    ApiServiceProvider apiServiceProvider;
    /*

        SwipeRefreshLayout pullToRefresh;
    */
    private ImageView imgDrawer;
    private static final int REQUEST_SCHEDULE_EXACT_ALARM_PERMISSION = 100;

    private Fragment fragment;
    private String manufacturer = "";
    String sCurrentVersion,sLatestVersion;
    private static final int REQUEST_CODE_UPDATE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manufacturer = Build.MANUFACTURER;
        apiServiceProvider = ApiServiceProvider.getInstance(this);

        Intent service = new Intent(this, DeviceLogSyncService.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(service);
        } else {
            startService(service);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);

            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.READ_MEDIA_IMAGES}, 1);

            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.READ_MEDIA_VIDEO}, 1);

            }
        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SCHEDULE_EXACT_ALARM) != PackageManager.PERMISSION_GRANTED) {
//                // Request the permission
//                requestPermissions(new String[]{Manifest.permission.SCHEDULE_EXACT_ALARM}, REQUEST_SCHEDULE_EXACT_ALARM_PERMISSION);
//            }
//        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                AlertDialog alertDialog = new AlertDialog.Builder(this)
                        .setTitle("Permission Alert")
                        .setMessage("Please allow overlay permission for Aliv to continue to receive calls in the background. Tap on 'Aliv' from the list after being navigated to settings.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                                startActivityForResult(intent, 0);
                            }
                        })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .create();

                alertDialog.show();


            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent();
            String packageName = getPackageName();
            PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                AlertDialog alertDialog = new AlertDialog.Builder(this)
                        .setTitle("Permission Alert")
                        .setMessage("Please allow battery optimization for Aliv to continue to receive calls in the background")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                                intent.setData(Uri.parse("package:" + packageName));
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .create();

                alertDialog.show();

            }
        }

//        if (LOGIN_DETAIL.getAppuserID() != null) {
//            SharePreference.getInstance(this).putString("APP_USER_ID", LOGIN_DETAIL.getAppuserID());
//            Log.e("APPUSERID", LOGIN_DETAIL.getAppuserID());
//        }
//
////        Log.e("APPUSERID", LOGIN_DETAIL.getAppuserID());
//
//        SharedPreferences sharePreferenceNew = getSharedPreferences("ALIV_NEW", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editShared = sharePreferenceNew.edit();
//        editShared.putString("APP_USER_ID", LOGIN_DETAIL.getAppuserID());
//        editShared.apply();


//        Log.e("APP_USER_ID", LOGIN_DETAIL.getAppuserID());

        try {
            intercomLogin();
        } catch (Exception e) {
            finish();
        }

        try {
            listApicall();
        } catch (Exception e) {
            hideLoader();
            finish();
        }

        initViews();
        initListeners();
        if (!SharePreference.getInstance(this).getBoolean(Constant.SKIP_PROTECTIONAPPCHECK)) {
            for (Intent intent : POWERMANAGER_INTENTS) {
                if (!manufacturer.equalsIgnoreCase("Realme")) {
                    if (getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                        alertDialogBuilder.setTitle("Alert!");
                        alertDialogBuilder.setMessage("App needs to enable background mode. If app is disabled, then in background mode some functionalities will not work. Go to setting and enable background mode.")
                                .setCancelable(false)
                                .setPositiveButton("Open Setting", (dialog, id) -> {
                                    try {
                                        startActivity(intent);
                                        SharePreference.getInstance(MainActivity.this).putBoolean(Constant.SKIP_PROTECTIONAPPCHECK, true);
                                    } catch (Exception e) {
//                                    Toast.makeText(this, "Exception:- " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.setCancelable(false);
                        alertDialog.show();
                        break;
                    }
                } else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                    alertDialogBuilder.setTitle("Alert!");
                    alertDialogBuilder.setMessage("App needs to enable background mode. if app is disabled, then in background mode some functionalities will not work. Please click on Enable.")
                            .setCancelable(false)
                            .setPositiveButton("Enable", (dialog, id) -> {
                                try {
                                    SharePreference.getInstance(MainActivity.this).putBoolean(Constant.SKIP_PROTECTIONAPPCHECK, true);
                                    startActivity(intent);
                                } catch (Exception e) {
//                                            Toast.makeText(this, "Exception:- " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.setCancelable(false);
                    alertDialog.show();
                    break;
                }
            }
        }
        checkForAppUpdate();
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        try {
//            listApicall();
//        } catch (Exception e) {
//            Constant.hideLoader();
//            finish();
//        }
//    }

    @Override
    protected void onResume() {
        super.onResume();
        checkAndPromptExactAlarmPermission();
    }
    void listApicall() {
        Util.checkInternet(this, new Util.NetworkCheckCallback() {
            @Override
            public void onNetworkCheckComplete(boolean isAvailable) {
                if (isAvailable) {
                    showLoader(MainActivity.this);
                    String userIdApp = "";
                    SharedPreferences sharePreferenceNew = getSharedPreferences("ALIV_NEW", Context.MODE_PRIVATE);
//                    if (LOGIN_DETAIL.getAppuser() == null){
                        userIdApp = sharePreferenceNew.getString("APP_USER_ID", "");
//                    }else {
//                      userIdApp =   LOGIN_DETAIL.getAppuser();
//                    }
                    Log.e("UserId",LOGIN_DETAIL.getAppuserID() );
                    apiServiceProvider.callForDeviceList(userIdApp, MainActivity.this);

                } else {
                    hideLoader();
                }
            }
        });

    }

    private void intercomLogin() {
        if (!SharePreference.getInstance(this).getBoolean(Constant.IS_INTERCOM_LOGIN)) {
            //  todo uncomment the following code when you want to login in video Intercom server
            Util.checkInternet(this, new Util.NetworkCheckCallback() {
                @Override
                public void onNetworkCheckComplete(boolean isAvailable) {
                    if (isAvailable) {
                        DMVPhoneModel.loginVPhoneServer(LOGIN_DETAIL.getUserEmail(), LOGIN_DETAIL.getAccountTokenPwd(), 1, MainActivity.this, new DMCallback() {
                            //DMVPhoneModel.loginVPhoneServer("ashishagrawal0108@gmail.com", "c5be8bcL88496f2bd778bfebeabc78208801efe3", 1, this, new DMModelCallBack.DMCallback() {
                            @Override
                            public void setResult(int errorCode, DMException e) {
                                Log.d(TAG, "DMCallback: " + "errorCode=" + errorCode);
                                if (e == null) {
                                    SharePreference.getInstance(MainActivity.this).putBoolean(Constant.IS_INTERCOM_LOGIN, true);

                                } else {
                                    Util.firebaseEvent(Constant.DEVICEAPIERRO, MainActivity.this, "", LOGIN_DETAIL.getUsername(), LOGIN_DETAIL.getAppuserID(), errorCode);
                                    Toast.makeText(MainActivity.this, "Login failed，errorCode=" + errorCode + ",e=" + e.toString(), Toast.LENGTH_SHORT).show();
                                }

                                if (e == null) {
                                    Log.e(TAG, getResources().getString(R.string.status_connected));
                                } else if (errorCode == DMErrorReturn.ERROR_RegistrationProgress) {
                                    Log.e(TAG, "statusCallback main" + getResources().getString(R.string.status_in_progress));
                                } else if (errorCode == DMErrorReturn.ERROR_RegistrationFailed) {
                                    Log.e(TAG, "statusCallback main" + getResources().getString(R.string.status_error));
                                } else {
                                    Log.e(TAG, "statusCallback main" + getResources().getString(R.string.status_not_connected));
                                }
                            }

                        });

                    }
                }
            });
        } else {
              hideLoader();
        }
    }

    /**
     * Initialize listeners.
     */
    private void initListeners() {
        imgDrawer.setOnClickListener(this);
    /*    pullToRefresh.setOnRefreshListener(() -> {
            apiServiceProvider.callForDeviceList(LOGIN_DETAIL.getAppuserID(), MainActivity.this);
            pullToRefresh.setRefreshing(false);
        });*/
    }

    /**
     * Initialize views.
     */
    private void initViews() {
        //  pullToRefresh = findViewById(R.id.pullToRefresh);
        drawerLayout = findViewById(R.id.drawer_layout_root);
        imgDrawer = findViewById(R.id.drawer_image);
        imgDraweHeader = findViewById(R.id.drawer_header_image);
        tvHeader = findViewById(R.id.tv_header);
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.drawer_image:
                drawerLayout.openDrawer(Gravity.START);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (fragment instanceof CommunityJoinFragment) {
            tvHeader.setText("Communities");
        } else if (fragment instanceof CommunitySubListFragment) {
            tvHeader.setText("Communities");
        }
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public void onResponseSuccess(SuccessDeviceListResponse successDeviceListResponse, String apiFlag) {
        hideLoader();
        switch (apiFlag) {
            case Constant.UrlPath.DEVICE_LIST_API:
                if (successDeviceListResponse.getStatus().equalsIgnoreCase("OK")) {
                    if (successDeviceListResponse.getData().size() > 0) {
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
                    Toast.makeText(this, successDeviceListResponse.getMsg(), Toast.LENGTH_LONG).show();
                }
                break;
        }
    }


    private void isDeviceAllowedToOpenDoor() {
        if (deviceLIST == null || deviceLIST.isEmpty()) {
            return;
        }
        boolean updated = false;
        for (DeviceObject deviceObject : deviceLIST) {
            String isaccessable = deviceObject.getIsAccessTimeEnabled();
            String serverStartTime = deviceObject.getAccessStarttime();
            String serverEndTime = deviceObject.getAccessEndtime();
            if (!(serverStartTime == null && serverEndTime == null)) {
                SharePreference.getInstance(this).putString("isAccessable", isaccessable);
                SharePreference.getInstance(this).putString("deviceStartTime", serverStartTime);
                SharePreference.getInstance(this).putString("deviceEndTime", serverEndTime);
                updated = true;
            }
        }
        if (!updated) {
            SharePreference.getInstance(this).putString("isAccessable", "");
            SharePreference.getInstance(this).putString("deviceStartTime", "");
            SharePreference.getInstance(this).putString("deviceEndTime", "");
        }

    }

    @Override
    public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
        hideLoader();
        switch (apiFlag) {
            case Constant.UrlPath.DEVICE_LIST_API:
                Util.firebaseEvent(Constant.APIERROR, MainActivity.this, Constant.UrlPath.SERVER_URL + apiFlag, LOGIN_DETAIL.getUsername(), LOGIN_DETAIL.getAppuserID(), errorObject.getStatus());
                try {
                    Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
                }
                break;
        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_UPDATE) {
            if (resultCode != RESULT_OK) {
                Log.e("MainActivity", "Update flow failed! Result code: " + resultCode);
                // If the update is cancelled or fails, you can request to start the update again.
                checkForAppUpdate();
            }
        }


        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.container);//getFragmentManager().findFragmentById(R.id.container);
        if (currentFragment instanceof HomeFragment)
            ((HomeFragment) currentFragment).onActivityResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_SCHEDULE_EXACT_ALARM_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // Permission granted, schedule the exact alarm
                Toast.makeText(this, " alarm permission Granted", Toast.LENGTH_SHORT).show();

            } else {
                showPermissionDialog();
                // Permission denied, show a message to the user
//                Toast.makeText(this, "Exact alarm permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void checkAndPromptExactAlarmPermission() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                showPermissionDialog();
            }
        }
    }
    private void showPermissionDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Permission Required")
                .setMessage("This app needs permission to schedule exact alarms. Please grant the permission in settings.")
                .setPositiveButton("Go to Settings", (dialog, which) -> {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                    intent.setData(Uri.fromParts("package", getPackageName(), null));
                    startActivity(intent);
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    Toast.makeText(this, "Exact alarm permission denied", Toast.LENGTH_SHORT).show();
                })
                .show();
    }

    private class GetLatestVersion extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                sLatestVersion= Jsoup
                        .connect("https://play.google.com//store/apps/details?id="
                                +getPackageName())
                        .timeout(30000)
                        .get()
                        .select("div.hAyfc:nth-child(4)>"+
                                "span:nth-child(2) > div:nth-child(1)"+
                                "> span:nth-child(1)")
                        .first()
                        .ownText();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return sLatestVersion;
        }

        @Override
        protected void onPostExecute(String s) {
            // Get current version
            sCurrentVersion= BuildConfig.VERSION_NAME;
            // Set current version on Text view
//            tvCurrentVersion.setText(sCurrentVersion);
//            // Set latest version on TextView
//            tvLatestVersion.setText(sLatestVersion);

            if(sLatestVersion != null)
            {
                // Version convert to float
                float cVersion=Float.parseFloat(sCurrentVersion);
                float lVersion=Float.parseFloat(sLatestVersion);

                // Check condition(latest version is
                // greater than the current version)
                if(lVersion > cVersion)
                {
                    // Create update AlertDialog
                    updateAlertDialog();
                }
            }
        }
    }

    private void updateAlertDialog() {
        // Initialize AlertDialog
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        // Set title
        builder.setTitle(getResources().getString(R.string.app_name));
        // set message
        builder.setMessage("");
        // Set non cancelable
        builder.setCancelable(false);

        // On update
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Open play store
                startActivity(new Intent(Intent .ACTION_VIEW,
                        Uri.parse("market://details?id"+getPackageName())));
                // Dismiss alert dialog
                dialogInterface.dismiss();
            }
        });

        // on cancel
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // cancel alert dialog
                dialogInterface.cancel();
            }
        });

        // show alert dialog
        builder.show();
    }

    private void checkForAppUpdate() {
        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(this);

        // Returns an intent object that you use to check for an update.
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                // Request the update.
                try {
                    appUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo,
                            AppUpdateType.IMMEDIATE,
                            MainActivity.this,
                            REQUEST_CODE_UPDATE);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }
        }).addOnFailureListener(e -> {
            Log.e("MainActivity", "Update check failed", e);
        });
    }
}
