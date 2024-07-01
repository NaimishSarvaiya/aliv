package com.iotsmartaliv.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
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

import java.util.ArrayList;

//import io.fabric.sdk.android.services.concurrency.AsyncTask;

import static com.iotsmartaliv.constants.Constant.LOGIN_DETAIL;
import static com.iotsmartaliv.constants.Constant.deviceLIST;
import static com.iotsmartaliv.constants.Constant.hideLoader;
import static com.iotsmartaliv.constants.Constant.showLoader;

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
    private Fragment fragment;
    private String manufacturer = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manufacturer = Build.MANUFACTURER;
        apiServiceProvider = ApiServiceProvider.getInstance(this);
         /*try {
            apiServiceProvider.callForDeviceList(LOGIN_DETAIL.getAppuserID(), this);
        } catch (Exception e) {
            finish();
        }*/

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

        if (LOGIN_DETAIL.getAppuserID() != null) {
            SharePreference.getInstance(this).putString("APP_USER_ID", LOGIN_DETAIL.getAppuserID());
            Log.e("APPUSERID", LOGIN_DETAIL.getAppuserID());
        }

//        Log.e("APPUSERID", LOGIN_DETAIL.getAppuserID());

        SharedPreferences sharePreferenceNew = getSharedPreferences("ALIV_NEW", Context.MODE_PRIVATE);
        SharedPreferences.Editor editShared = sharePreferenceNew.edit();
        editShared.putString("APP_USER_ID", LOGIN_DETAIL.getAppuserID());
        editShared.apply();


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

    void listApicall() {
        Util.checkInternet(this, new Util.NetworkCheckCallback() {
            @Override
            public void onNetworkCheckComplete(boolean isAvailable) {
                if (isAvailable) {
                    showLoader(MainActivity.this);
                    apiServiceProvider.callForDeviceList(LOGIN_DETAIL.getAppuserID(), MainActivity.this);

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
        super.onActivityResult(requestCode, resultCode, data);

        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.container);//getFragmentManager().findFragmentById(R.id.container);
        if (currentFragment instanceof HomeFragment)
            ((HomeFragment) currentFragment).onActivityResult(requestCode, resultCode, data);

    }
}
