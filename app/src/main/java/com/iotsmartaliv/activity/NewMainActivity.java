package com.iotsmartaliv.activity;

import static com.iotsmartaliv.apiAndSocket.models.DeviceObject.getLibDev;
import static com.iotsmartaliv.constants.Constant.API_AUTH;
import static com.iotsmartaliv.constants.Constant.FROM_HOME;
import static com.iotsmartaliv.constants.Constant.LOGIN_DETAIL;
import static com.iotsmartaliv.constants.Constant.LOGIN_PREFRENCE;
import static com.iotsmartaliv.constants.Constant.SHAKE_ENABLE;
import static com.iotsmartaliv.constants.Constant.deviceLIST;
import static com.iotsmartaliv.constants.Constant.hideLoader;
import static com.iotsmartaliv.constants.Constant.showLoader;
import static com.iotsmartaliv.utils.CommanUtils.accessWithinRange;
import static com.iotsmartaliv.utils.CommanUtils.sgtToUtc;
import static com.iotsmartaliv.utils.CommanUtils.utcToLocalTimeZone;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
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
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.installreferrer.BuildConfig;
import com.bugfender.sdk.Bugfender;
import com.doormaster.vphone.config.DMErrorReturn;
import com.doormaster.vphone.exception.DMException;
import com.doormaster.vphone.inter.DMModelCallBack.DMCallback;
import com.doormaster.vphone.inter.DMVPhoneModel;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.gson.Gson;
import com.intelligoo.sdk.LibDevModel;
import com.intelligoo.sdk.LibInterface;
import com.intelligoo.sdk.ScanCallback;
import com.iotsmartaliv.R;
import com.iotsmartaliv.activity.ViewPager.BroadcastCommunityActivity;
import com.iotsmartaliv.activity.automation.HomeAutomationActivity;
import com.iotsmartaliv.activity.booking.BookingActivity;
import com.iotsmartaliv.activity.booking.CardActivity;
import com.iotsmartaliv.activity.digitalForm.DigitalFormActivity;
import com.iotsmartaliv.activity.feedback.FeedBackActivity;
import com.iotsmartaliv.adapter.ImagePagerAdapter;
import com.iotsmartaliv.adapter.booking.NoticeBoardAdapter;
import com.iotsmartaliv.adapter.booking.RoomImageViewPagerAdapter;
import com.iotsmartaliv.apiAndSocket.listeners.RetrofitListener;
import com.iotsmartaliv.apiAndSocket.models.DeviceObject;
import com.iotsmartaliv.apiAndSocket.models.ErrorObject;
import com.iotsmartaliv.apiAndSocket.models.ResponseData;
import com.iotsmartaliv.apiAndSocket.models.SuccessDeviceListResponse;
import com.iotsmartaliv.apiAndSocket.retrofit.ApiServiceProvider;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.databinding.ActivityNewMainBinding;
import com.iotsmartaliv.dialog_box.GpsEnableDialog;
import com.iotsmartaliv.fragments.MyAccountFragment;
import com.iotsmartaliv.fragments.community.CommunityListFragment;
import com.iotsmartaliv.model.AppFeatureModel;
import com.iotsmartaliv.model.AuthTokenModel;
import com.iotsmartaliv.model.CheckBookingRequest;
import com.iotsmartaliv.model.DeviceBean;
import com.iotsmartaliv.onboarding.OnBoardingActivity;
import com.iotsmartaliv.roomDB.AccessLogModel;
import com.iotsmartaliv.roomDB.DatabaseClient;
import com.iotsmartaliv.services.DeviceLogSyncService;
import com.iotsmartaliv.services.ShakeOpenService;
import com.iotsmartaliv.utils.CircleMenuLayout;
import com.iotsmartaliv.utils.ErrorMsgDoorMasterSDK;
import com.iotsmartaliv.utils.RippleBackground;
import com.iotsmartaliv.utils.SaveAccessLogTask;
import com.iotsmartaliv.utils.SharePreference;
import com.iotsmartaliv.utils.Util;
import com.rd.PageIndicatorView;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.ResponseBody;

public class NewMainActivity extends AppCompatActivity implements RetrofitListener<SuccessDeviceListResponse>, GpsEnableDialog.LocationListener {
    ActivityNewMainBinding binding;

    // Main Activity Varialbles
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
    private static final int REQUEST_SCHEDULE_EXACT_ALARM_PERMISSION = 100;
    private static final int REQUEST_CODE_UPDATE = 12;
    private String manufacturer = "";
    String sCurrentVersion, sLatestVersion;
    NoticeBoardAdapter adapter;
    ApiServiceProvider apiServiceProvider;

    // Home Fragment
    String openingDoorDeviceSN;
    private RippleBackground rippleBackground;
    private CircleMenuLayout mCircleMenuLayout;
    private PageIndicatorView pageIndicatorView;
    private Map<String, DeviceBean> tempDevDic = new HashMap<String, DeviceBean>();
    private ImageView imgChat, imageViewCommunity, imgInfo;
    ;
    //    private String serverDate = "";
    private boolean goInsideToOpenDoor = false;
    OnJoinCommunityFragmentInListener onJoinCommunityFragmentInListener;

    private GoogleApiClient googleApiClient;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    Date serverDate;

    TextView tvTodayDate;
    ArrayList<String> appFeture;
    private static final int REQUEST_BLUETOOTH_PERMISSIONS = 2;

    private boolean pressed = false;
    ImagePagerAdapter BannerViewpagerAdapter;

    List<Integer> imageList = Arrays.asList(
            R.drawable.sample_banner,
            R.drawable.sample_banner,
            R.drawable.sample_banner
    );
    /**
     * Callback method to open the door successfully.
     */
    final LibInterface.ManagerCallback callback = (result, bundle) -> runOnUiThread(() -> {
        pressed = false;
        //mHandler.sendEmptyMessage(OPEN_AGAIN);
        if (result == 0x00) {
            Bugfender.d("CanoHomeFragment", "Door Open Successfully.");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            new SaveAccessLogTask(this, new AccessLogModel("", openingDoorDeviceSN, "home page key", dateFormat.format(new Date()))).execute();
            Toast.makeText(this, "Door Open Successfully.", Toast.LENGTH_SHORT).show();
            logs(LOGIN_DETAIL.getAppuserID(), new AccessLogModel("", openingDoorDeviceSN, "home page key", dateFormat.format(new Date())));
//            Util.logDoorOpenEvent("GreenKey", true, LOGIN_DETAIL.getAppuserID(), openingDoorDeviceSN);
        } else {
            if (result == 48) {
                Bugfender.d("CanoHomeFragment", "Result Error Time Out");
                Toast.makeText(this, "Result Error Time Out", Toast.LENGTH_SHORT).show();
            } else {
                Bugfender.d("CanoHomeFragment", "Failure:" + result);
                Toast.makeText(this, "Failure:" + result, Toast.LENGTH_SHORT).show();
            }
            Util.logDoorOpenEvent("GreenKey", false, LOGIN_DETAIL.getAppuserID(), openingDoorDeviceSN);
        }
    });
    private ProgressDialog progress;
    /**
     * Callback method to check scanned device and to open the nearest device lock on click.
     */
    ScanCallback oneKeyScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(ArrayList<String> deviceList,
                                 ArrayList<Integer> rssi) {
            progress.dismiss();
            if (deviceLIST.size() == 0) {
                pressed = false;
                if (deviceList.size() != 0) {
                    apiServiceProvider = ApiServiceProvider.getInstance(NewMainActivity.this, false);
                    CheckBookingRequest checkBookingRequest = new CheckBookingRequest(LOGIN_DETAIL.getAppuserID(), deviceList.get(0));
                    apiServiceProvider.checkDeviceBooking(checkBookingRequest, new RetrofitListener<SuccessDeviceListResponse>() {
                        @Override
                        public void onResponseSuccess(SuccessDeviceListResponse sucessRespnse, String apiFlag) {
                            List<DeviceObject> deviceListNearby;
                            if (sucessRespnse.getStatus().equalsIgnoreCase("OK")) {
                                deviceListNearby = sucessRespnse.getData();
                                for (DeviceObject deviceObject : deviceListNearby) {
                                    deviceObject.setRssi(-500);
                                }
                                for (DeviceObject device : deviceListNearby) {
                                    for (int i = 0; i < deviceList.size(); i++) {
                                        String devSn = deviceList.get(i);
                                        if (device.getDeviceSnoWithoutAlphabet().equalsIgnoreCase(devSn)) {
                                            device.setRssi(rssi.get(i));
                                        }
                                    }
                                }
                                Gson gson = new Gson();
                                String deviceObjectListJson = gson.toJson(deviceListNearby);
                                String scanDeviceObjectJson = gson.toJson(deviceList);
                                Bugfender.d("CanoHomeFragment", " USER DEVICE LIST --> " + deviceObjectListJson);
                                Bugfender.d("CanoHomeFragment", "DEVICE SCANNED BY USER --> " + scanDeviceObjectJson);
                                Collections.sort(deviceListNearby, (o1, o2) -> {
                                    Integer x1 = o1.getRssi();
                                    Integer x2 = o2.getRssi();
                                    return x2.compareTo(x1);
                                });
                                if (deviceListNearby.get(0).getRssi() > -80) {
                                    LibDevModel libDev = getLibDev(deviceListNearby.get(0));
                                    openingDoorDeviceSN = deviceListNearby.get(0).getDeviceSno();
//                                    Util.showNoDefaultCaedAlertDialog(requireActivity(),"deviceSno :" + libDev.devSn + "," + "deviceMAC :" + libDev.devMac + "," + "devType" + libDev.devType + "," + "eKey" + libDev.eKey);
                                    int ret = LibDevModel.openDoor(NewMainActivity.this, libDev, callback);
                                    if (ret == 0) {
                                        return;
                                    } else {
                                        pressed = false;
                                        Bugfender.d("CanoHomeFragment", "RET：" + ret);
                                        Toast.makeText(NewMainActivity.this, ErrorMsgDoorMasterSDK.getErrorMsg(ret), Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Bugfender.d("CanoHomeFragment", "Opening Distance is too far. Kindly go nearer the device. Thank you");
                                    pressed = false;
                                    Toast.makeText(NewMainActivity.this, "Opening Distance is too far. Kindly go nearer the device. Thank you", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(NewMainActivity.this, sucessRespnse.getMsg(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                            Toast.makeText(NewMainActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
//                chagefailSatus(isOnline);
                        }
                    });

                } else {
                    Toast.makeText(NewMainActivity.this, "No Nearby device found.", Toast.LENGTH_SHORT).show();
                }
            } else {

            /*if (pressed) {
                Toast.makeText(getContext(), "Operating...", Toast.LENGTH_SHORT).show();
                return;
            }*/
                if (deviceList.size() != 0) {
                    for (DeviceObject deviceObject : deviceLIST) {
                        deviceObject.setRssi(-500);
                    }
                    String commanDevice = "";
                    for (DeviceObject device : deviceLIST) {
                        for (int i = 0; i < deviceList.size(); i++) {
                            String devSn = deviceList.get(i);
                            if (device.getDeviceSnoWithoutAlphabet().equalsIgnoreCase(devSn)) {
                                device.setRssi(rssi.get(i));
                                commanDevice = devSn;
                                if (device.getIsAccessTimeEnabled().equals("1")) {
                                    if (device.getAccessStarttime() != null &&
                                            device.getAccessEndtime() != null
                                            && !device.getAccessStarttime().isEmpty() && !device.getAccessEndtime().isEmpty()) {
                                        Date startTime = sgtToUtc(device.getAccessStarttime());
                                        Date endTime = sgtToUtc(device.getAccessEndtime());
                                        goInsideToOpenDoor = accessWithinRange(device.getIsAccessTimeEnabled(), startTime, endTime, serverDate);
                                    }
                                } else {
                                    goInsideToOpenDoor = true;
                                }

                            }
                        }
                    }

                    if (!commanDevice.equalsIgnoreCase("")) {
                        if (goInsideToOpenDoor) {
                            Gson gson = new Gson();
                            String deviceObjectListJson = gson.toJson(deviceLIST);
                            String scanDeviceObjectJson = gson.toJson(deviceList);
                            Bugfender.d("CanoHomeFragment", " USER DEVICE LIST --> " + deviceObjectListJson);
                            Bugfender.d("CanoHomeFragment", "DEVICE SCANNED BY USER --> " + scanDeviceObjectJson);
                            Collections.sort(deviceLIST, (o1, o2) -> {
                                Integer x1 = o1.getRssi();
                                Integer x2 = o2.getRssi();
                                return x2.compareTo(x1);
                            });
                            if (deviceLIST.get(0).getRssi() > -80) {
                                LibDevModel libDev = getLibDev(deviceLIST.get(0));
                                openingDoorDeviceSN = deviceLIST.get(0).getDeviceSno();
//                changeStatus(  LOGIN_DETAIL.getAppuserID(),deviceLIST.get(0).getDeviceSno());
//                                Util.showNoDefaultCaedAlertDialog(requireActivity(),"deviceSno :" + libDev.devSn + "," + "deviceMAC :" + libDev.devMac + "," + "devType" + libDev.devType + "," + "eKey" + libDev.eKey);
                                int ret = LibDevModel.openDoor(NewMainActivity.this, libDev, callback);
                                if (ret == 0) {
                                    return;
                                } else {
                                    pressed = false;
                                    Bugfender.d("CanoHomeFragment", "RET：" + ret);
                                    Toast.makeText(NewMainActivity.this, ErrorMsgDoorMasterSDK.getErrorMsg(ret), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Bugfender.d("CanoHomeFragment", "Opening Distance is too far. Kindly go nearer the device. Thank you");
                                pressed = false;
                                Toast.makeText(NewMainActivity.this, "Opening Distance is too far. Kindly go nearer the device. Thank you", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(NewMainActivity.this, "User can not access at this time", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        apiServiceProvider = ApiServiceProvider.getInstance(NewMainActivity.this, false);
                        CheckBookingRequest checkBookingRequest = new CheckBookingRequest(LOGIN_DETAIL.getAppuserID(), deviceList.get(0));
                        apiServiceProvider.checkDeviceBooking(checkBookingRequest, new RetrofitListener<SuccessDeviceListResponse>() {
                            @Override
                            public void onResponseSuccess(SuccessDeviceListResponse sucessRespnse, String apiFlag) {
                                List<DeviceObject> deviceListNearby;
                                if (sucessRespnse.getStatus().equalsIgnoreCase("OK")) {
                                    deviceListNearby = sucessRespnse.getData();
                                    for (DeviceObject deviceObject : deviceListNearby) {
                                        deviceObject.setRssi(-500);
                                    }
                                    for (DeviceObject device : deviceListNearby) {
                                        for (int i = 0; i < deviceList.size(); i++) {
                                            String devSn = deviceList.get(i);
                                            if (device.getDeviceSnoWithoutAlphabet().equalsIgnoreCase(devSn)) {
                                                device.setRssi(rssi.get(i));
                                            }
                                        }
                                    }
                                    Gson gson = new Gson();
                                    String deviceObjectListJson = gson.toJson(deviceListNearby);
                                    String scanDeviceObjectJson = gson.toJson(deviceList);
                                    Bugfender.d("CanoHomeFragment", " USER DEVICE LIST --> " + deviceObjectListJson);
                                    Bugfender.d("CanoHomeFragment", "DEVICE SCANNED BY USER --> " + scanDeviceObjectJson);
                                    Collections.sort(deviceListNearby, (o1, o2) -> {
                                        Integer x1 = o1.getRssi();
                                        Integer x2 = o2.getRssi();
                                        return x2.compareTo(x1);
                                    });
                                    if (deviceListNearby.get(0).getRssi() > -80) {
                                        LibDevModel libDev = getLibDev(deviceListNearby.get(0));
                                        openingDoorDeviceSN = deviceListNearby.get(0).getDeviceSno();
//                                        Util.showNoDefaultCaedAlertDialog(requireActivity(),"deviceSno :" + libDev.devSn + "," + "deviceMAC :" + libDev.devMac + "," + "devType" + libDev.devType + "," + "eKey" + libDev.eKey);
                                        int ret = LibDevModel.openDoor(NewMainActivity.this, libDev, callback);
                                        if (ret == 0) {
                                            return;
                                        } else {
                                            pressed = false;
                                            Bugfender.d("CanoHomeFragment", "RET：" + ret);
                                            Toast.makeText(NewMainActivity.this, ErrorMsgDoorMasterSDK.getErrorMsg(ret), Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Bugfender.d("CanoHomeFragment", "Opening Distance is too far. Kindly go nearer the device. Thank you");
                                        pressed = false;
                                        Toast.makeText(NewMainActivity.this, "Opening Distance is too far. Kindly go nearer the device. Thank you", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(NewMainActivity.this, sucessRespnse.getMsg(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                                Toast.makeText(NewMainActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
//                chagefailSatus(isOnline);
                            }
                        });

                    }
                } else {
                    Toast.makeText(NewMainActivity.this, "No Nearby device found.", Toast.LENGTH_SHORT).show();
                }

            }
        }


        @Override
        public void onScanResultAtOnce(final String devSn, int rssi) {
            pressed = false;
        }
    };

    public interface OnJoinCommunityFragmentInListener {
        void addChangeJoinCommunityFaragment();

    }

    public void setOnonJoinCommunityFragmentInListener(OnJoinCommunityFragmentInListener onJoinCommunityFragmentInListener) {
        this.onJoinCommunityFragmentInListener = onJoinCommunityFragmentInListener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Wait until the layout is rendered
        binding.llBottom.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Remove the listener to prevent it from being called multiple times
                binding.llBottom.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                // Calculate the width of each child in LinearLayout with weight 1
                int totalWidth = binding.llBottom.getWidth();
                int totalheight = binding.llBottom.getWidth();
                int childWidth = (totalWidth / 5);
                childWidth = (int) (childWidth * 1.1);
                // Since weightSum is 5, and we want width of 2// Since weightSum is 5, and we want width of 2

                // Set the width of the center ImageView
                ViewGroup.LayoutParams params = binding.rlKey.getLayoutParams();
                params.width = childWidth;
                binding.rlKey.setLayoutParams(params); // Update the layout with the new width
            }
        });
        binding.tvProfileName.setText(LOGIN_DETAIL.getUsername());
        //Main Activity

        manufacturer = Build.MANUFACTURER;
        apiServiceProvider = ApiServiceProvider.getInstance(this, false);

        Intent service;
        service = new Intent(this, DeviceLogSyncService.class);

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

        try {
            intercomLogin();
        } catch (Exception e) {
            finish();
        }
        try {
            listApicall();
        } catch (Exception e) {
            finish();
        }

        try {
            if (SharePreference.getInstance(NewMainActivity.this).getString(API_AUTH) == null || SharePreference.getInstance(NewMainActivity.this).getString(API_AUTH).equalsIgnoreCase("")) {
                getAuthToken();
            } else {
                Log.e("Auth", SharePreference.getInstance(NewMainActivity.this).getString(API_AUTH));
            }
        } catch (Exception e) {
            finish();
        }
        getFeture();

//        initViews();
//        initListeners();
        if (!SharePreference.getInstance(this).getBoolean(Constant.SKIP_PROTECTIONAPPCHECK)) {
            for (Intent intent : POWERMANAGER_INTENTS) {
                if (!manufacturer.equalsIgnoreCase("Realme")) {
                    if (getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(NewMainActivity.this);
                        alertDialogBuilder.setTitle("Alert!");
                        alertDialogBuilder.setMessage("App needs to enable background mode. If app is disabled, then in background mode some functionalities will not work. Go to setting and enable background mode.")
                                .setCancelable(false)
                                .setPositiveButton("Open Setting", (dialog, id) -> {
                                    try {
                                        startActivity(intent);
                                        SharePreference.getInstance(NewMainActivity.this).putBoolean(Constant.SKIP_PROTECTIONAPPCHECK, true);
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
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(NewMainActivity.this);
                    alertDialogBuilder.setTitle("Alert!");
                    alertDialogBuilder.setMessage("App needs to enable background mode. if app is disabled, then in background mode some functionalities will not work. Please click on Enable.")
                            .setCancelable(false)
                            .setPositiveButton("Enable", (dialog, id) -> {
                                try {
                                    SharePreference.getInstance(NewMainActivity.this).putBoolean(Constant.SKIP_PROTECTIONAPPCHECK, true);
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

        //Home Fragment

//        imgChat = view.findViewById(R.id.imageView);
//        imageViewCommunity = view.findViewById(R.id.imageViewCommunity);
//        imgInfo = view.findViewById(R.id.imgInfo);
//        tvTodayDate = view.findViewById(R.id.tv_todaysDate);
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd MMM yyyy", Locale.getDefault());
        String formattedDate = dateFormat.format(new Date());
        binding.tvTodaysDate.setText(formattedDate);
        setShakeSettings();
        appFeture = (ArrayList<String>) SharePreference.getInstance(this).getFeatureForApp();

        Boolean isShakeToOpen = SharePreference.getInstance(this).getBoolean(SHAKE_ENABLE);

        if (isShakeToOpen) {

            if (!isShakeServiceRunning(ShakeOpenService.class)) {

                Intent shakeService = new Intent(this, ShakeOpenService.class);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                    startForegroundService(shakeService);

                } else {

                    startService(shakeService);

                }
            }
        }

//        startBlink();

        googleApiClient = getAPIClientInstance();
        if (googleApiClient != null) {
            googleApiClient.connect();
        }

//        imgInfo.setOnClickListener(v -> {
//            Intent intent = new Intent(this, OnBoardingActivity.class);
//            intent.putExtra(Constant.FROM_DRAWER, true);
//            startActivity(intent);
//        });

//        imgChat.setOnClickListener(view1 -> startActivity(new Intent(getActivity(), ChatBoxActivity.class)));

        progress = new ProgressDialog(this);
        progress.setMessage("Door Open On Process");
        progress.setCancelable(false);

//        imageViewCommunity.setOnClickListener(view12 -> onJoinCommunityFragmentInListener.addChangeJoinCommunityFaragment());
//        mCircleMenuLayout.setMenuItemIconsAndTexts(mItemImgs, mItemTexts);
//        HomePageSliderAdpter adapter = new HomePageSliderAdpter(getContext());
//        rippleBackground.startRippleAnimation();
//
//        final ViewPager pager = view.findViewById(R.id.viewPager);
//        pager.setAdapter(adapter);
//
//            @Override
//            public void itemCenterClick(View view) {
//                Log.e("Initiate Unlock", "TRUE");

        setClick();
        binding.rlKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestGPSSettings();
                } else {
                    performOpenDoorOperation();
                }
            }
        });


//        });
        BannerViewpagerAdapter = new ImagePagerAdapter(this, imageList);
        binding.viewPager.setAdapter(BannerViewpagerAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.rvNoticeBoard.setLayoutManager(layoutManager);
        adapter = new NoticeBoardAdapter(this);
        binding.rvNoticeBoard.setAdapter(adapter);

//        binding.rlSetting.setOnClickListener(v -> {
//         Intent intent = new Intent(this, MyAccountFragment.class);
//         startActivity(intent);
//        });

    }

    private void setClick() {
        binding.llBooking.setOnClickListener(v -> {
            if (appFeture.contains(Constant.BOOKING_MANAGMENT)) {
//                startActivity(new Intent(NewMainActivity.this, BookingFacilityActivity.class));
                startActivity(new Intent(NewMainActivity.this, BookingActivity.class));
            } else {
                Toast.makeText(NewMainActivity.this, "Booking of Facilities is not enabled for your community. Please contact your admin. Thanks!", Toast.LENGTH_LONG).show();
            }
        });

        binding.llNoticeBoard.setOnClickListener(v -> {
            if (appFeture.contains(Constant.BROADCAST_MANAGMENT)) {
                Intent intent1 = new Intent(NewMainActivity.this, BroadcastCommunityActivity.class);
                startActivity(intent1);
            } else {
                Toast.makeText(NewMainActivity.this, "Notice Board is not enabled for your community. Please contact your admin. Thanks!", Toast.LENGTH_LONG).show();
            }
        });
        binding.llFeedback.setOnClickListener(v -> {
            if (appFeture.contains(Constant.FEEDBACK_MANAGMENT)) {
                Intent feddbackintent = new Intent(NewMainActivity.this, FeedBackActivity.class);
                startActivity(feddbackintent);
            } else {
                Toast.makeText(NewMainActivity.this, "Feedback Management is not enabled for your community. Please contact your admin. Thanks!", Toast.LENGTH_LONG).show();
            }
        });
        binding.llPayment.setOnClickListener(v -> startActivity(new Intent(NewMainActivity.this, CardActivity.class)));
        binding.llVisitor.setOnClickListener(v -> startActivity(new Intent(NewMainActivity.this, VisitorActivity.class)));
        binding.llAutomation.setOnClickListener(v -> {
            if (appFeture.contains(Constant.AUTOMATION_MANAGMENT)) {

                // startActivity(new Intent(getActivity(), GuestActivity.class));
                startActivity(new Intent(NewMainActivity.this, HomeAutomationActivity.class));
            } else {
                Toast.makeText(NewMainActivity.this, "Automation Management is not enabled for your community. Please contact your admin. Thanks!", Toast.LENGTH_LONG).show();

            }
        });
// need to discuss
        binding.llForm.setOnClickListener(v -> startActivity(new Intent(NewMainActivity.this, DigitalFormActivity.class)));
        binding.llDevice.setOnClickListener(v -> startActivity(new Intent(NewMainActivity.this, DeviceListActivity.class)));


//Bottom Menu
        binding.llFaceEnrollmentBottomMenu.setOnClickListener(v -> startActivity(new Intent(NewMainActivity.this, EnrollmentActivity.class)));
        binding.llDeviceBottomMenu.setOnClickListener(v -> startActivity(new Intent(NewMainActivity.this, DeviceListActivity.class)));
        binding.llInterComBottomMenu.setOnClickListener(v -> startActivity(new Intent(NewMainActivity.this, VideoIntercomActivity.class)));
        binding.llCommunityBottomMenu.setOnClickListener(v -> startActivity(new Intent(NewMainActivity.this, CommunityListFragment.class)));

// Profile Menu
        binding.llMyAccount.setOnClickListener(v -> startActivity(new Intent(NewMainActivity.this, MyAccountFragment.class)));

// Top Menu
        binding.rlInfo.setOnClickListener(v -> {
            Intent oNBordingIntent = new Intent(NewMainActivity.this, OnBoardingActivity.class);
            oNBordingIntent.putExtra(Constant.PATH, FROM_HOME);
            startActivity(oNBordingIntent);
        });
        binding.rlCommunity.setOnClickListener(v -> startActivity(new Intent(NewMainActivity.this, CommunityListFragment.class)));
        binding.rlSetting.setOnClickListener(v -> startActivity(new Intent(NewMainActivity.this, SettingActivity.class)));
    }


    // Main Activity
    private void intercomLogin() {
        if (!SharePreference.getInstance(this).getBoolean(Constant.IS_INTERCOM_LOGIN)) {
            LOGIN_DETAIL = new Gson().fromJson(SharePreference.getInstance(NewMainActivity.this).getString(LOGIN_PREFRENCE), ResponseData.class);
            showLoader(this);
            //  todo uncomment the following code when you want to login in video Intercom server
            Util.checkInternet(this, new Util.NetworkCheckCallback() {
                @Override
                public void onNetworkCheckComplete(boolean isAvailable) {
                    if (isAvailable) {
//                        DMVPhoneModel.loginVPhoneServer(LOGIN_DETAIL.getUserEmail(),LOGIN_DETAIL.getAccountTokenPwd(),"test123",1,MainActivity.this, new DMCallback() {

                        DMVPhoneModel.loginVPhoneServer(LOGIN_DETAIL.getUserEmail(), LOGIN_DETAIL.getAccountTokenPwd(), 1, NewMainActivity.this, new DMCallback() {
                            //                            DMVPhoneModel.loginVPhoneServer("ashishagrawal0108@gmail.com", "c5be8bcL88496f2bd778bfebeabc78208801efe3", 1, this, new DMModelCallBack.DMCallback() {
                            @Override
                            public void setResult(int errorCode, DMException e) {
                                hideLoader();
                                Log.d(TAG, "DMCallback: " + "errorCode=" + errorCode);
                                if (e == null) {
                                    SharePreference.getInstance(NewMainActivity.this).putBoolean(Constant.IS_INTERCOM_LOGIN, true);

                                } else {
                                    Util.firebaseEvent(Constant.DEVICEAPIERRO, NewMainActivity.this, "", LOGIN_DETAIL.getUsername(), LOGIN_DETAIL.getAppuserID(), errorCode);
                                    Toast.makeText(NewMainActivity.this, "Login failed，errorCode=" + errorCode + ",e=" + e.toString(), Toast.LENGTH_SHORT).show();
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

                    } else {
                        hideLoader();
                    }
                }
            });
        } else {
            hideLoader();
        }
    }

    void getAuthToken() {
        Util.checkInternet(this, new Util.NetworkCheckCallback() {
            @Override
            public void onNetworkCheckComplete(boolean isAvailable) {
                if (isAvailable) {
                    String userIdApp = "";
                    SharedPreferences sharePreferenceNew = NewMainActivity.this.getSharedPreferences("ALIV_NEW", Context.MODE_PRIVATE);
                    if (LOGIN_DETAIL.getAppuser() == null) {
                        userIdApp = sharePreferenceNew.getString("APP_USER_ID", "");
                    } else {
                        userIdApp = LOGIN_DETAIL.getAppuserID();
                    }
                    apiServiceProvider.getAuthToken(userIdApp, new RetrofitListener<AuthTokenModel>() {
                        @Override
                        public void onResponseSuccess(AuthTokenModel sucessRespnse, String apiFlag) {
                            if (sucessRespnse.getStatusCode() == 200) {
                                if (sucessRespnse.getAuthToken() != null && !sucessRespnse.getAuthToken().isEmpty()) {
                                    SharePreference.getInstance(NewMainActivity.this).putString(API_AUTH, sucessRespnse.getAuthToken());
                                    LOGIN_DETAIL.setApiAuthToken(sucessRespnse.getAuthToken());
                                    Log.e("Auth", sucessRespnse.getAuthToken());
                                }
//
                            }
                        }

                        @Override
                        public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                            try {
                                Toast.makeText(NewMainActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Toast.makeText(NewMainActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }

    void listApicall() {
        Util.checkInternet(this, new Util.NetworkCheckCallback() {
            @Override
            public void onNetworkCheckComplete(boolean isAvailable) {
                if (isAvailable) {
                    String userIdApp = "";
                    SharedPreferences sharePreferenceNew = getSharedPreferences("ALIV_NEW", Context.MODE_PRIVATE);
                    if (LOGIN_DETAIL.getAppuserID() != null) {
                        userIdApp = LOGIN_DETAIL.getAppuserID();
                    } else {
                        userIdApp = sharePreferenceNew.getString("APP_USER_ID", "");
                    }
                    Log.e("UserId", LOGIN_DETAIL.getAppuserID());
                    apiServiceProvider.callForDeviceList(userIdApp, BuildConfig.VERSION_NAME.toString(), NewMainActivity.this);

                }
            }
        });

    }

    void getFeture() {
        Util.checkInternet(NewMainActivity.this, new Util.NetworkCheckCallback() {
            @Override
            public void onNetworkCheckComplete(boolean isAvailable) {
                if (isAvailable) {
                    String userIdApp = "";
                    SharedPreferences sharePreferenceNew = NewMainActivity.this.getSharedPreferences("ALIV_NEW", Context.MODE_PRIVATE);
                    if (LOGIN_DETAIL.getAppuser() == null) {
                        userIdApp = sharePreferenceNew.getString("APP_USER_ID", "");
                    } else {
                        userIdApp = LOGIN_DETAIL.getAppuserID();
                    }
                    Log.e("UserId", LOGIN_DETAIL.getAppuserID());
//                    Log.e("UserAuthToken",LOGIN_DETAIL.getApiAuthToken());
                    apiServiceProvider.callFeature(userIdApp, new RetrofitListener<AppFeatureModel>() {
                        @Override
                        public void onResponseSuccess(AppFeatureModel sucessRespnse, String apiFlag) {
                            if (sucessRespnse.getStatusCode() == 200) {
                                if (sucessRespnse.getMsg().equals("Features empty")) {
                                    ArrayList<String> emptyList = new ArrayList<>();
                                    SharePreference.getInstance(NewMainActivity.this).putFeatureForApp(emptyList);
                                } else {
                                    SharePreference.getInstance(NewMainActivity.this).putFeatureForApp(sucessRespnse.getData());
//                                Toast.makeText(MainActivity.this, sucessRespnse.getMsg(), Toast.LENGTH_LONG).show();
                                }
                            }
                        }

                        @Override
                        public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                            try {
                                Toast.makeText(NewMainActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Toast.makeText(NewMainActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onResponseSuccess(SuccessDeviceListResponse sucessRespnse, String apiFlag) {
        switch (apiFlag) {
            case Constant.UrlPath.DEVICE_LIST_API:
                if (sucessRespnse.getStatus().equalsIgnoreCase("OK")) {
                    if (sucessRespnse.getData().size() > 0) {
                        deviceLIST = sucessRespnse.getData();
                        isDeviceAllowedToOpenDoor();
                        SaveTask st = new SaveTask();
                        st.execute();
                    } else {
                        deviceLIST = new ArrayList<>();
                        Toast.makeText(this, "Device List Empty", Toast.LENGTH_LONG).show();
                        SaveTask st = new SaveTask();
                        st.execute();
                    }
                } else {
                    Toast.makeText(this, sucessRespnse.getMsg(), Toast.LENGTH_LONG).show();
                }
                break;
        }

    }

    @Override
    public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
        switch (apiFlag) {
            case Constant.UrlPath.DEVICE_LIST_API:
                Util.firebaseEvent(Constant.APIERROR, NewMainActivity.this, Constant.UrlPath.SERVER_URL + apiFlag, LOGIN_DETAIL.getUsername(), LOGIN_DETAIL.getAppuserID(), errorObject.getStatus());
                try {
                    Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
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
                            NewMainActivity.this,
                            REQUEST_CODE_UPDATE);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }
        }).addOnFailureListener(e -> {
            Log.e("MainActivity", "Update check failed", e);
        });
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
    protected void onResume() {
        super.onResume();
        checkAndPromptExactAlarmPermission();
    }
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        if (fragment instanceof CommunityJoinFragment) {
//            tvHeader.setText("Communities");
//        } else if (fragment instanceof CommunitySubListFragment) {
//            tvHeader.setText("Communities");
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case REQUEST_CODE_UPDATE:
                if (resultCode != RESULT_OK) {
                    Log.e("MainActivity", "Update flow failed! Result code: " + resultCode);
                    // If the update is cancelled or fails, you can request to start the update again.
                    checkForAppUpdate();
                }
                break;
            case 1:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.i("GpsOnOff", "onActivityResult: RESULT_OK");
                        performOpenDoorOperation();
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.i("GpsOnOff", "onActivityResult: RESULT_CANCELED");
                        Toast.makeText(NewMainActivity.this, "Please enable Bluetooth and location to open the door", Toast.LENGTH_SHORT).show();

                        break;
                    default:
                        break;
                }
                break;
        }


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
        if (requestCode == REQUEST_BLUETOOTH_PERMISSIONS) {
            boolean allPermissionsGranted = true;

            // Check if all requested permissions were granted
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }

            if (allPermissionsGranted) {
                // Permissions granted, proceed with the door unlocking process
                callOpenDoor();
            } else {
                // Permissions denied, show a message to the user
                Toast.makeText(NewMainActivity.this, "Bluetooth permissions are required to unlock the door.", Toast.LENGTH_SHORT).show();
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
        new AlertDialog.Builder(NewMainActivity.this)
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

    // Home fragment
    public void logs(String userId, AccessLogModel accessLogModel) {
        Util.checkInternet(NewMainActivity.this, new Util.NetworkCheckCallback() {
            @Override
            public void onNetworkCheckComplete(boolean isAvailable) {
                if (isAvailable) {
                    apiServiceProvider.postAccessLog(userId, accessLogModel, new RetrofitListener<AccessLogModel>() {

                        @Override
                        public void onResponseSuccess(AccessLogModel accessLogModel1, String apiFlag) {
                            if (Constant.UrlPath.POST_ACCESS_LOG.equals(apiFlag)) {
//                                new DeviceLogSyncService.DeleteAccessLogTask(DeviceLogSyncService.this, accessLogModel1).execute();
                            }
                        }

                        @Override
                        public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
//                            Util.firebaseEvent(Constant.APIERROR, DeviceLogSyncService.this, Constant.UrlPath.SERVER_URL + apiFlag, LOGIN_DETAIL.getUsername(), LOGIN_DETAIL.getAppuserID(), errorObject.getStatus());
                        }
                    });
                }
            }
        });
    }

    private void setShakeSettings() {

        Boolean isShakeToOpen = SharePreference.getInstance(NewMainActivity.this).getBoolean(SHAKE_ENABLE);

        if (isShakeToOpen) {

            if (!isShakeServiceRunning(ShakeOpenService.class)) {

                Intent shakeService = new Intent(NewMainActivity.this, ShakeOpenService.class);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                    startForegroundService(shakeService);

                } else {

                    startService(shakeService);

                }
            }
        }
    }

    private boolean isShakeServiceRunning(Class<?> serviceClass) {

        ActivityManager manager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {

            if (serviceClass.getName().equals(service.service.getClassName())) {

                return true;

            }
        }
        return false;
    }

    private GoogleApiClient getAPIClientInstance() {

        return new GoogleApiClient.Builder(NewMainActivity.this)
                .addApi(LocationServices.API).build();
    }

    private void requestGPSSettings() {

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setInterval(2000);
        locationRequest.setFastestInterval(500);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i(TAG, "All location settings are satisfied.");
                        performOpenDoorOperation();
//                        Toast.makeText(getApplication(), "GPS is already enable", Toast.LENGTH_SHORT).show();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to" + "upgrade location settings ");
                        try {
                            showGpsEnableDialog(status);
//                            status.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
                        } catch (Exception e) {
                            Log.e("Applicationsett", e.toString());
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog " + "not created.");
                        break;
                }
            }
        });
    }

    private void performOpenDoorOperation() {
        try {
//            String isAcessible = SharePreference.getInstance(getActivity()).getString("isAccessable");
//            if (isAcessible.equals("1")) {
//                callGetServerAPI();
//            } else {
//                goInsideToOpenDoor = true;
//                Log.e("UNLOCK FIRST", "TRUE");
            if (!isBluetoothEnabled()) {
                if (ContextCompat.checkSelfPermission(NewMainActivity.this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        ActivityCompat.requestPermissions(NewMainActivity.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 2);
                        return;
                    }
                }

                Intent eintent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(eintent, 1);
            } else {
//                    Toast.makeText(OnBoardingActivity.this, "Already Enabled", Toast.LENGTH_SHORT).show();
                callGetServerAPI();
            }

//            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(NewMainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isBluetoothEnabled() {
        return BluetoothAdapter.getDefaultAdapter().isEnabled();
    }

    private void callGetServerAPI() {

        apiServiceProvider = ApiServiceProvider.getInstance(NewMainActivity.this, false);

        try {
            Util.checkInternet(NewMainActivity.this, new Util.NetworkCheckCallback() {
                @Override
                public void onNetworkCheckComplete(boolean isAvailable) {
                    if (isAvailable) {
                        apiServiceProvider.callGetServerCurrentTime(new RetrofitListener<ResponseBody>() {
                            @Override
                            public void onResponseSuccess(ResponseBody sucessRespnse, String apiFlag) {
                                try {
                                    JSONObject jsonObject = new JSONObject(sucessRespnse.string());
                                    String dateTime = jsonObject.optString("date");
                                    serverDate = utcToLocalTimeZone(dateTime);
                                    String isAcessible = SharePreference.getInstance(NewMainActivity.this).getString("isAccessable");

                                    if (!SharePreference.getInstance(NewMainActivity.this).getString("deviceStartTime").equalsIgnoreCase("")
                                            && !SharePreference.getInstance(NewMainActivity.this).getString("deviceEndTime").equalsIgnoreCase("")) {
                                        Date startTime = utcToLocalTimeZone(SharePreference.getInstance(NewMainActivity.this).getString("deviceStartTime"));
                                        Date endTime = utcToLocalTimeZone(SharePreference.getInstance(NewMainActivity.this).getString("deviceEndTime"));
                                        goInsideToOpenDoor = accessWithinRange(isAcessible, startTime, endTime, serverDate);
//                    SharePreference.getInstance(getActivity()).putString(getResources().getString(R.string.server_current_time), dateTime);
                                        Log.d("EndTimeeCheck: ", serverDate + " EndTime:" + endTime + " StartTime:" + startTime);
                                    } else {
                                        goInsideToOpenDoor = true;
                                    }
                                    checkAndRequestBluetoothPermissions();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                                Util.firebaseEvent(Constant.APIERROR, NewMainActivity.this, Constant.UrlPath.SERVER_URL + apiFlag, LOGIN_DETAIL.getUsername(), LOGIN_DETAIL.getAppuserID(), errorObject.getStatus());

                            }
                        });

                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(NewMainActivity.this, "Failed to open door", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkAndRequestBluetoothPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            List<String> permissionsToRequest = new ArrayList<>();

            // Check BLUETOOTH_CONNECT and BLUETOOTH_SCAN permissions
            if (ContextCompat.checkSelfPermission(NewMainActivity.this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED) {
                permissionsToRequest.add(Manifest.permission.BLUETOOTH_CONNECT);
            }
            if (ContextCompat.checkSelfPermission(NewMainActivity.this, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_DENIED) {
                permissionsToRequest.add(Manifest.permission.BLUETOOTH_SCAN);
            }

            if (!permissionsToRequest.isEmpty()) {
                ActivityCompat.requestPermissions(NewMainActivity.this, permissionsToRequest.toArray(new String[0]), REQUEST_BLUETOOTH_PERMISSIONS);
                return;
            }
        }

        // If permissions are already granted, proceed with the door opening
        callOpenDoor();
    }

    private void callOpenDoor() {
        Log.e("UNLOCK", "TRUE");

        try {
            pressed = true;
            progress.show();

            int ret1 = LibDevModel.scanDevice(NewMainActivity.this, false, 1300, oneKeyScanCallback);         // A key to open the door
            if (ret1 != 0) {
                Toast.makeText(NewMainActivity.this, ErrorMsgDoorMasterSDK.getErrorMsg(ret1), Toast.LENGTH_SHORT).show();
                pressed = false;
                progress.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(NewMainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    private void showGpsEnableDialog(Status status) {

        new GpsEnableDialog(NewMainActivity.this, this, status).show();

    }

    @Override
    public void googleLocationEnable(Status locationStatus) {

        try {
            locationStatus.startResolutionForResult(NewMainActivity.this, REQUEST_CHECK_SETTINGS);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }

    }

}