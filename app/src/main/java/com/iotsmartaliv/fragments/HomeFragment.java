package com.iotsmartaliv.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import com.bugfender.sdk.Bugfender;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.gson.Gson;
import com.intelligoo.sdk.LibDevModel;
import com.intelligoo.sdk.LibInterface;
import com.intelligoo.sdk.ScanCallback;
import com.iotsmartaliv.R;
import com.iotsmartaliv.activity.ChatBoxActivity;
import com.iotsmartaliv.activity.DeviceListActivity;
import com.iotsmartaliv.activity.EnrollmentActivity;
import com.iotsmartaliv.activity.VideoIntercomActivity;
import com.iotsmartaliv.activity.VisitorActivity;
import com.iotsmartaliv.activity.VisitorAuthorizationActivity;
import com.iotsmartaliv.activity.automation.HomeAutomationActivity;
import com.iotsmartaliv.activity.booking.BookingFacilityActivity;
import com.iotsmartaliv.adapter.HomePageSliderAdpter;
import com.iotsmartaliv.apiCalling.listeners.RetrofitListener;
import com.iotsmartaliv.apiCalling.models.DeviceObject;
import com.iotsmartaliv.apiCalling.models.ErrorObject;
import com.iotsmartaliv.apiCalling.models.SuccessDeviceListResponse;
import com.iotsmartaliv.apiCalling.retrofit.ApiServiceProvider;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.constants.Request;
import com.iotsmartaliv.dialog_box.GpsEnableDialog;
import com.iotsmartaliv.model.AutomationRoomsResponse;
import com.iotsmartaliv.model.BookingResponse;
import com.iotsmartaliv.model.CheckBookingRequest;
import com.iotsmartaliv.model.DeviceBean;
import com.iotsmartaliv.onboarding.OnBoardingActivity;
import com.iotsmartaliv.roomDB.AccessLogModel;
import com.iotsmartaliv.services.ShakeOpenService;
import com.iotsmartaliv.utils.CircleMenuLayout;
import com.iotsmartaliv.utils.ErrorMsgDoorMasterSDK;
import com.iotsmartaliv.utils.NetworkAvailability;
import com.iotsmartaliv.utils.RippleBackground;
import com.iotsmartaliv.utils.SaveAccessLogTask;
import com.iotsmartaliv.utils.SharePreference;
import com.iotsmartaliv.utils.Util;
import com.rd.PageIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;

import static com.iotsmartaliv.activity.LoginActivity.devList;
import static com.iotsmartaliv.apiCalling.models.DeviceObject.getLibDev;
import static com.iotsmartaliv.constants.Constant.LOGIN;
import static com.iotsmartaliv.constants.Constant.LOGIN_DETAIL;
import static com.iotsmartaliv.constants.Constant.SHAKE_ENABLE;
import static com.iotsmartaliv.constants.Constant.deviceLIST;
import static com.iotsmartaliv.utils.CommanUtils.accessWithinRange;
import static com.iotsmartaliv.utils.CommanUtils.sgtToUtc;
import static com.iotsmartaliv.utils.CommanUtils.utcToLocalTimeZone;

/**
 * This fragment class is used for home fragment.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2018-10-24
 */
public class HomeFragment extends Fragment implements GpsEnableDialog.LocationListener {
    private static final String TAG = "HomeFragment";
    OnJoinCommunityFragmentInListener onJoinCommunityFragmentInListener;
    String openingDoorDeviceSN;
    private RippleBackground rippleBackground;
    private CircleMenuLayout mCircleMenuLayout;
    private PageIndicatorView pageIndicatorView;
    private Map<String, DeviceBean> tempDevDic = new HashMap<String, DeviceBean>();
    private ImageView imgChat, imageViewCommunity, imgInfo;
    ;
    private ApiServiceProvider apiServiceProvider;
    //    private String serverDate = "";
    private boolean goInsideToOpenDoor = false;

    private GoogleApiClient googleApiClient;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    Date serverDate;

    private String[] mItemTexts = new String[]{
            "Face Enroll", "Video Intercom",
            /* "Rewards",*/  "Automation", /*"Market Place",*/
            /*   "Services & Maintenance",*/ "Visitor",
            "Facility Booking", "Device List"
    };
    private boolean pressed = false;
    /**
     * Callback method to open the door successfully.
     */
    final LibInterface.ManagerCallback callback = (result, bundle) -> getActivity().runOnUiThread(() -> {
        pressed = false;
        //mHandler.sendEmptyMessage(OPEN_AGAIN);
        if (result == 0x00) {
            Bugfender.d("CanoHomeFragment", "Door Open Successfully.");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            new SaveAccessLogTask(getContext(), new AccessLogModel("", openingDoorDeviceSN, "home page key", dateFormat.format(new Date()))).execute();
            Toast.makeText(getContext(), "Door Open Successfully.", Toast.LENGTH_SHORT).show();
        } else {
            if (result == 48) {
                Bugfender.d("CanoHomeFragment", "Result Error Time Out");
                Toast.makeText(getContext(), "Result Error Time Out", Toast.LENGTH_SHORT).show();
            } else {
                Bugfender.d("CanoHomeFragment", "Failure:" + result);
                Toast.makeText(getContext(), "Failure:" + result, Toast.LENGTH_SHORT).show();
            }
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
                    apiServiceProvider = ApiServiceProvider.getInstance(getActivity());
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
                                if (deviceListNearby.get(0).getRssi() > -70) {
                                    LibDevModel libDev = getLibDev(deviceListNearby.get(0));
                                    openingDoorDeviceSN = deviceListNearby.get(0).getDeviceSno();

                                    int ret = LibDevModel.openDoor(getContext(), libDev, callback);
                                    if (ret == 0) {
                                        return;
                                    } else {
                                        pressed = false;
                                        Bugfender.d("CanoHomeFragment", "RET：" + ret);
                                        Toast.makeText(getContext(), ErrorMsgDoorMasterSDK.getErrorMsg(ret), Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Bugfender.d("CanoHomeFragment", "Opening Distance is too far. Kindly go nearer the device. Thank you");
                                    pressed = false;
                                    Toast.makeText(getContext(), "Opening Distance is too far. Kindly go nearer the device. Thank you", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getContext(), sucessRespnse.getMsg(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                            Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
//                chagefailSatus(isOnline);
                        }
                    });

                } else {
                    Toast.makeText(getContext(), "No Nearby device found.", Toast.LENGTH_SHORT).show();
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
                                if (device.getAccessStarttime() != null &&
                                               device.getAccessEndtime() != null
                                && !device.getAccessStarttime().isEmpty() && !device.getAccessEndtime().isEmpty()){
                                    Date startTime = sgtToUtc(device.getAccessStarttime());
                                    Date endTime = sgtToUtc(device.getAccessEndtime());
                                    goInsideToOpenDoor = accessWithinRange(device.getIsAccessTimeEnabled(), startTime, endTime, serverDate);
                                }else {
                                    goInsideToOpenDoor = false;
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
                            if (deviceLIST.get(0).getRssi() > -70) {
                                LibDevModel libDev = getLibDev(deviceLIST.get(0));
                                openingDoorDeviceSN = deviceLIST.get(0).getDeviceSno();
//                changeStatus(  LOGIN_DETAIL.getAppuserID(),deviceLIST.get(0).getDeviceSno());
                                int ret = LibDevModel.openDoor(getContext(), libDev, callback);
                                if (ret == 0) {
                                    return;
                                } else {
                                    pressed = false;
                                    Bugfender.d("CanoHomeFragment", "RET：" + ret);
                                    Toast.makeText(getContext(), ErrorMsgDoorMasterSDK.getErrorMsg(ret), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Bugfender.d("CanoHomeFragment", "Opening Distance is too far. Kindly go nearer the device. Thank you");
                                pressed = false;
                                Toast.makeText(getContext(), "Opening Distance is too far. Kindly go nearer the device. Thank you", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "User can not access at this time", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        apiServiceProvider = ApiServiceProvider.getInstance(getActivity());
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
                                    if (deviceListNearby.get(0).getRssi() > -70) {
                                        LibDevModel libDev = getLibDev(deviceListNearby.get(0));
                                        openingDoorDeviceSN = deviceListNearby.get(0).getDeviceSno();

                                        int ret = LibDevModel.openDoor(getContext(), libDev, callback);
                                        if (ret == 0) {
                                            return;
                                        } else {
                                            pressed = false;
                                            Bugfender.d("CanoHomeFragment", "RET：" + ret);
                                            Toast.makeText(getContext(), ErrorMsgDoorMasterSDK.getErrorMsg(ret), Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Bugfender.d("CanoHomeFragment", "Opening Distance is too far. Kindly go nearer the device. Thank you");
                                        pressed = false;
                                        Toast.makeText(getContext(), "Opening Distance is too far. Kindly go nearer the device. Thank you", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(getContext(), sucessRespnse.getMsg(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                                Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
//                chagefailSatus(isOnline);
                            }
                        });

                    }
                } else {
                    Toast.makeText(getContext(), "No Nearby device found.", Toast.LENGTH_SHORT).show();
                }

            }
        }


        @Override
        public void onScanResultAtOnce(final String devSn, int rssi) {
            pressed = false;
        }
    };
    private int[] mItemImgs = new int[]{
            R.drawable.ic_face_recognition, R.drawable.ic_webcam,
            /* R.drawable.ic_record,*/ R.drawable.ic_home_automation,/* R.drawable.ic_markets,*/
            /*    R.drawable.ic_maintenance, */R.drawable.ic_guest,
            R.drawable.ic_booking, R.drawable.ic_devicelist
    };

    public void setOnonJoinCommunityFragmentInListener(OnJoinCommunityFragmentInListener onJoinCommunityFragmentInListener) {
        this.onJoinCommunityFragmentInListener = onJoinCommunityFragmentInListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        rippleBackground = view.findViewById(R.id.id_circle_menu_item_center);
        mCircleMenuLayout = view.findViewById(R.id.id_menulayout);
        imgChat = view.findViewById(R.id.imageView);
        imageViewCommunity = view.findViewById(R.id.imageViewCommunity);
        imgInfo = view.findViewById(R.id.imgInfo);

        setShakeSettings();


        Boolean isShakeToOpen = SharePreference.getInstance(getActivity()).getBoolean(SHAKE_ENABLE);

        if (isShakeToOpen) {

            if (!isShakeServiceRunning(ShakeOpenService.class)) {

                Intent shakeService = new Intent(getContext(), ShakeOpenService.class);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                    getContext().startForegroundService(shakeService);

                } else {

                    getContext().startService(shakeService);

                }
            }
        }

        startBlink();

        googleApiClient = getAPIClientInstance();
        if (googleApiClient != null) {
            googleApiClient.connect();
        }

        imgInfo.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), OnBoardingActivity.class);
            intent.putExtra(Constant.FROM_DRAWER, true);
            startActivity(intent);
        });

        imgChat.setOnClickListener(view1 -> startActivity(new Intent(getActivity(), ChatBoxActivity.class)));

        progress = new ProgressDialog(getContext());
        progress.setMessage("Door Open On Process");
        progress.setCancelable(false);

        imageViewCommunity.setOnClickListener(view12 -> onJoinCommunityFragmentInListener.addChangeJoinCommunityFaragment());
        mCircleMenuLayout.setMenuItemIconsAndTexts(mItemImgs, mItemTexts);
        HomePageSliderAdpter adapter = new HomePageSliderAdpter(getContext());
        rippleBackground.startRippleAnimation();

        final ViewPager pager = view.findViewById(R.id.viewPager);
        pager.setAdapter(adapter);

        mCircleMenuLayout.setOnMenuItemClickListener(new CircleMenuLayout.OnMenuItemClickListener() {
            @Override
            public void itemClick(View view, int pos) {
                switch (pos) {
                    case 0:
                        startActivity(new Intent(getActivity(), EnrollmentActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(getActivity(), VideoIntercomActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(getActivity(), HomeAutomationActivity.class));
                        break;
                  /*  case 3:
                        startActivity(new Intent(getActivity(), MarketPlaceActivity.class));
                        break;*/
                  /*  case 3:
                        startActivity(new Intent(getActivity(), ServicesMaintenanceActivity.class));
                        break;*/
                    case 3:
                        // startActivity(new Intent(getActivity(), GuestActivity.class));
                        startActivity(new Intent(getActivity(), VisitorActivity.class));
                        break;
                    case 4:
                        startActivity(new Intent(getActivity(), BookingFacilityActivity.class));
                        break;
                    case 5:
                      /*  if (deviceLIST.size() == 0) {
                            Toast.makeText(getActivity(), "No device configured.", Toast.LENGTH_SHORT).show();
                        } else {*/
                        startActivity(new Intent(getActivity(), DeviceListActivity.class));
                        // }

                     /*   if (devList.size() == 0) {
                            Constant.showLoader(getActivity());
                            Thread getDevList_th = new Thread(new Runnable() {
                                public void run() {
                                    if (isGotList())
                                        startActivity(new Intent(getActivity(), DeviceListActivity.class));
                                }
                            });
                            getDevList_th.start();
                        } else {
                            startActivity(new Intent(getActivity(), DeviceListActivity.class));
                        }
*/
                        break;

                  /*  case 8:
                        startActivity(new Intent(getActivity(), HomeAutomationActivity.class));
                        break;*/
                }
            }


            @Override
            public void itemCenterClick(View view) {
                Log.e("Initiate Unlock", "TRUE");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestGPSSettings();
                } else {
                    performOpenDoorOperation();
                }

            }
        });
        return view;
    }

    private void setShakeSettings() {

        Boolean isShakeToOpen = SharePreference.getInstance(getActivity()).getBoolean(SHAKE_ENABLE);

        if (isShakeToOpen) {

            if (!isShakeServiceRunning(ShakeOpenService.class)) {

                Intent shakeService = new Intent(getContext(), ShakeOpenService.class);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                    getContext().startForegroundService(shakeService);

                } else {

                    getContext().startService(shakeService);

                }
            }
        }
    }

    private boolean isShakeServiceRunning(Class<?> serviceClass) {

        ActivityManager manager = (ActivityManager) getContext().getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {

            if (serviceClass.getName().equals(service.service.getClassName())) {

                return true;

            }
        }
        return false;
    }

    private void startBlink() {
        Animation animation = new AlphaAnimation(1, 0); //to change visibility from visible to invisible
        animation.setDuration(800); //1 second duration for each animation cycle
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE); //repeating indefinitely
        animation.setRepeatMode(Animation.REVERSE); //animation will start from end point once ended.
        imgInfo.startAnimation(animation);
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
                    if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 2);
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
            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isBluetoothEnabled() {
        return BluetoothAdapter.getDefaultAdapter().isEnabled();
    }

    private void callGetServerAPI() {

        apiServiceProvider = ApiServiceProvider.getInstance(getActivity());

        try {
            Util.checkInternet(requireActivity(), new Util.NetworkCheckCallback() {
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
                                    String isAcessible = SharePreference.getInstance(getActivity()).getString("isAccessable");

                                    if (!SharePreference.getInstance(getActivity()).getString("deviceStartTime").equalsIgnoreCase("")
                                            && !SharePreference.getInstance(getActivity()).getString("deviceEndTime").equalsIgnoreCase("")) {
                                        Date startTime = utcToLocalTimeZone(SharePreference.getInstance(getActivity()).getString("deviceStartTime"));
                                        Date endTime = utcToLocalTimeZone(SharePreference.getInstance(getActivity()).getString("deviceEndTime"));
                                        goInsideToOpenDoor = accessWithinRange(isAcessible, startTime, endTime, serverDate);
//                    SharePreference.getInstance(getActivity()).putString(getResources().getString(R.string.server_current_time), dateTime);
                                        Log.d("EndTimeeCheck: ", serverDate + " EndTime:" + endTime + " StartTime:" + startTime);
                                    } else {
                                        goInsideToOpenDoor = true;
                                    }
                                    callOpenDoor();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                                Util.firebaseEvent(Constant.APIERROR, requireActivity(), Constant.UrlPath.SERVER_URL + apiFlag, LOGIN_DETAIL.getUsername(), LOGIN_DETAIL.getAppuserID(), errorObject.getStatus());

                            }
                        });

                    }
                }
            });

//            else {
//                Toast.makeText(getActivity(), "No Internet", Toast.LENGTH_SHORT).show();
//            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Failed to open door", Toast.LENGTH_SHORT).show();
        }
    }

    private void callOpenDoor() {
        Log.e("UNLOCK", "TRUE");

        try {
//            if (goInsideToOpenDoor) {
                pressed = true;
                progress.show();
                int ret1 = LibDevModel.scanDevice(getContext(), false, 1300, oneKeyScanCallback);         // A key to open the door
                //Naimish
                if (ret1 != 0) {
                    Toast.makeText(getContext(), ErrorMsgDoorMasterSDK.getErrorMsg(ret1), Toast.LENGTH_SHORT).show();
                    pressed = false;
                    progress.dismiss();
                }
                // startActivity(new Intent(getActivity(), OpenDoorActivity.class));
//                }
//            } else {
//                int ret1 = LibDevModel.scanDevice(getContext(), false, 1300, oneKeyScanCallback);         // A key to open the door
////                Toast.makeText(getActivity(), "User can not access at this time", Toast.LENGTH_SHORT).show();
//            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isGotList() {
        String clientId = SharePreference.getInstance(getActivity()).getString("CLIENTID");
        try {
            devList = Request.reqDeviceList(clientId);
            if (devList == null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "No device configured.", Toast.LENGTH_SHORT);
                    }
                });
                devList = new ArrayList<DeviceBean>();
                tempDevDic = new HashMap<String, DeviceBean>();
            } else {
                for (DeviceBean devBean : devList) {
                    tempDevDic.put(devBean.getDevSn(), devBean);
                }
            }
            Constant.hideLoader();
            return true;
        } catch (JSONException e) {
            Constant.hideLoader();
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void googleLocationEnable(Status locationStatus) {

        try {
            locationStatus.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }

    }

    public interface OnJoinCommunityFragmentInListener {
        void addChangeJoinCommunityFaragment();

    }

    private GoogleApiClient getAPIClientInstance() {

        return new GoogleApiClient.Builder(getActivity())
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

    private void showGpsEnableDialog(Status status) {

        new GpsEnableDialog(getActivity(), this, status).show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.i("GpsOnOff", "onActivityResult: RESULT_OK");
                        performOpenDoorOperation();
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.i("GpsOnOff", "onActivityResult: RESULT_CANCELED");
                        Toast.makeText(getActivity(), "Please enable Bluetooth and location to open the door", Toast.LENGTH_SHORT).show();

                        break;
                    default:
                        break;
                }
                break;
        }
    }

    public void changeStatus(String appUserId, String deviceSN) {
        apiServiceProvider = ApiServiceProvider.getInstance(getActivity());
        CheckBookingRequest checkBookingRequest = new CheckBookingRequest(appUserId, deviceSN);
        apiServiceProvider.checkDeviceBooking(checkBookingRequest, new RetrofitListener<BookingResponse>() {
            @Override
            public void onResponseSuccess(BookingResponse sucessRespnse, String apiFlag) {
                if (sucessRespnse.getStatus().equalsIgnoreCase("OK")) {
                    Toast.makeText(getContext(), sucessRespnse.getMsg(), Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getContext(), sucessRespnse.getMsg(), Toast.LENGTH_SHORT).show();

//                    chagefailSatus(isOnline);
                }
            }

            @Override
            public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
//                chagefailSatus(isOnline);
            }
        });
    }

}
