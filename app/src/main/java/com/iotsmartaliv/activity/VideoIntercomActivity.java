package com.iotsmartaliv.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bugfender.sdk.Bugfender;
import com.doormaster.vphone.config.DMCallState;
import com.doormaster.vphone.config.DMConstants;
import com.doormaster.vphone.config.DMErrorReturn;
import com.doormaster.vphone.entity.VideoDeviceEntity;
import com.doormaster.vphone.exception.DMException;
import com.doormaster.vphone.inter.DMModelCallBack.DMCallStateListener;
import com.doormaster.vphone.inter.DMModelCallBack.DMCallback;
import com.doormaster.vphone.inter.DMPhoneMsgListener;
import com.doormaster.vphone.inter.DMVPhoneModel;
import com.google.gson.Gson;
import com.intelligoo.sdk.LibDevModel;
import com.iotsmartaliv.R;
import com.iotsmartaliv.adapter.VideoIntercomAdapter;
import com.iotsmartaliv.adapter.automation.IntercomeRelayAdapter;
import com.iotsmartaliv.apiCalling.listeners.RetrofitListener;
import com.iotsmartaliv.apiCalling.models.DeviceObject;
import com.iotsmartaliv.apiCalling.models.ErrorObject;
import com.iotsmartaliv.apiCalling.models.IntercomRelayData;
import com.iotsmartaliv.apiCalling.models.SuccessDeviceListResponse;
import com.iotsmartaliv.apiCalling.models.SuccessResponse;
import com.iotsmartaliv.apiCalling.models.VideoDeviceData;
import com.iotsmartaliv.apiCalling.models.VideoDeviceListModel;
import com.iotsmartaliv.apiCalling.retrofit.ApiServiceProvider;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.dmvphonedemotest.CheckPermissionUtils;
import com.iotsmartaliv.dmvphonedemotest.DmCallIncomingActivity;
import com.iotsmartaliv.dmvphonedemotest.DmCallOutgoingActivity;
import com.iotsmartaliv.interfaces.VideoIntercomItemClick;
import com.iotsmartaliv.model.CheckBookingRequest;
import com.iotsmartaliv.model.OpenVideoDeviceRelayRequest;
import com.iotsmartaliv.model.RelayItemModel;
import com.iotsmartaliv.model.SuccessResponseModel;
import com.iotsmartaliv.utils.ErrorMsgDoorMasterSDK;
import com.iotsmartaliv.utils.SharePreference;
import com.iotsmartaliv.utils.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.iotsmartaliv.apiCalling.models.DeviceObject.getLibDev;
import static com.iotsmartaliv.constants.Constant.LOGIN_DETAIL;
import static com.iotsmartaliv.constants.Constant.deviceLIST;
import static com.iotsmartaliv.constants.Constant.hideLoader;
import static com.iotsmartaliv.utils.CommanUtils.accessWithinRange;
import static com.iotsmartaliv.utils.CommanUtils.utcToLocalTimeZone;

import org.json.JSONObject;

import okhttp3.ResponseBody;

/**
 * This activity class is used for video intercom.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2018-10-23
 */
public class VideoIntercomActivity extends AppCompatActivity implements View.OnClickListener, VideoIntercomItemClick {

    private static final int REQUEST_CODE_MAIN = 999;
    private static final String TAG = VideoIntercomActivity.class.getName();
    ApiServiceProvider apiServiceProvider;
    private VideoIntercomAdapter adapter;
    private ListView videoIntercom;
    private ArrayList<VideoDeviceData> videoIntercomArrayList = new ArrayList<>();
    private ImageView imgbackIntercom;
    VideoDeviceData activeCallDevice;

    Boolean internetConnected = false;
    private boolean goToOpenDoor=false;
    private AlertDialog dialog;
    EditText searchDevice;


    private DMCallback loginCallback = (errorCode, e) -> {
        Log.d(TAG, "DMCallback: " + "errorCode=" + errorCode);
        Constant.hideLoader();
        if (e == null) {
            Toast.makeText(VideoIntercomActivity.this, "login successful", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(VideoIntercomActivity.this, "Login failed，errorCode=" + errorCode + ",e=" + e.toString(), Toast.LENGTH_SHORT).show();
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
    };

    private DMCallStateListener callStateListener = (state, message) -> {
        Log.d("CallStateLis main", "value=" + state.value() + ",message=" + message);
        if (state == DMCallState.IncomingReceived) {
            //intent.putExtra("type", 1);
            //startActivity(intent);
        } else if (state == DMCallState.OutgoingInit) {
            Intent intent = new Intent(VideoIntercomActivity.this, DmCallOutgoingActivity.class);
            String deviceName;
            if (activeCallDevice.getCdeviceName().length() > 0) {
                deviceName = activeCallDevice.getCdeviceName();
            } else {
                deviceName = activeCallDevice.getDeviceName();
            }
            intent.putExtra("DeviceName", deviceName);
            startActivity(intent);
        }
    };
    private DMCallback statusCallback = (errorCode, e) -> {
        if (e == null) {
            Log.e("statusCallback main", getResources().getString(R.string.status_connected));
        } else if (errorCode == DMErrorReturn.ERROR_RegistrationProgress) {
            Log.e("statusCallback main", getResources().getString(R.string.status_in_progress));
        } else if (errorCode == DMErrorReturn.ERROR_RegistrationFailed) {
            Log.e("statusCallback main", getResources().getString(R.string.status_error));
        } else {
            Log.e("statusCallback main", getResources().getString(R.string.status_not_connected));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_intercom);
        apiServiceProvider = ApiServiceProvider.getInstance(this);
        Log.e("INTERCOM", "TRUE");
        SharedPreferences sharePreferenceNew = getSharedPreferences("ALIV_NEW", Context.MODE_PRIVATE);
        String userIdApp = sharePreferenceNew.getString("APP_USER_ID", "");

//        if (Util.checkInternet(this)) {
        Util.checkInternet(this, new Util.NetworkCheckCallback() {
            @Override
            public void onNetworkCheckComplete(boolean isAvailable) {
                if (isAvailable) {

                    apiServiceProvider.callForVideoDeviceList(userIdApp, new RetrofitListener<VideoDeviceListModel>() {
                        @Override
                        public void onResponseSuccess(VideoDeviceListModel sucessRespnse, String apiFlag) {
                            videoIntercomArrayList.clear();
                            if (sucessRespnse.getStatus().equalsIgnoreCase("OK")) {
                                if (!sucessRespnse.getData().isEmpty()) {
                                    videoIntercomArrayList.addAll(sucessRespnse.getData());
                                    adapter.notifyDataSetChanged();
                                    //    Toast.makeText(this, "Video Device" + successDeviceListRes   ponse.getData().size(), Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(VideoIntercomActivity.this, "Video Device List Empty", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(VideoIntercomActivity.this, sucessRespnse.getMsg(), Toast.LENGTH_LONG).show();
                            }
//
                        }

                        @Override
                        public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
//                                Util.firebaseEvent(Constant.APIERROR, VideoIntercomActivity.this,Constant.UrlPath.SERVER_URL+apiFlag, LOGIN_DETAIL.getUsername(),LOGIN_DETAIL.getAppuserID(),errorObject.getStatus());
//                                Toast.makeText(VideoIntercomActivity.this, throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            Util.firebaseEvent(Constant.APIERROR, VideoIntercomActivity.this, Constant.UrlPath.SERVER_URL + apiFlag, LOGIN_DETAIL.getUsername(), LOGIN_DETAIL.getAppuserID(), errorObject.getStatus());

                            try {
                                Toast.makeText(VideoIntercomActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Toast.makeText(VideoIntercomActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
//                        apiServiceProvider.callForVideoDeviceList(userIdApp, VideoIntercomActivity.this);
                }
            }
        });
//        }


        initViews();
        initListeners();
        searchDevice = findViewById(R.id.search_videoDevice);
        //videoIntercom.setItemsCanFocus(false);
        adapter = new VideoIntercomAdapter(videoIntercomArrayList, this, this);
        videoIntercom.setAdapter(adapter);
        requestPermissiontest();
//        Constant.showLoader(this);

        searchDevice.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // filter your list from your input
                filter(s.toString());
                //you can use runnable postDelayed like 500 ms to delay search text
            }
        });

        Util.checkInternet(this, new Util.NetworkCheckCallback() {
            @Override
            public void onNetworkCheckComplete(boolean isAvailable) {
                if (isAvailable) {
                    DMVPhoneModel.loginVPhoneServer(LOGIN_DETAIL.getUserEmail(), LOGIN_DETAIL.getAccountTokenPwd(), 1, VideoIntercomActivity.this, new DMCallback() {
                        //DMVPhoneModel.loginVPhoneServer("ashishagrawal0108@gmail.com", "c5be8bcL88496f2bd778bfebeabc78208801efe3", 1, this, new DMModelCallBack.DMCallback() {
                        @Override
                        public void setResult(int errorCode, DMException e) {
                            Log.d(TAG, "DMCallback: " + "errorCode=" + errorCode);
                            Constant.hideLoader();
                            if (e == null) {
//                    Toast.makeText(VideoIntercomActivity.this, "login successful", Toast.LENGTH_SHORT).show();
                            } else {
                                //Toast.makeText(VideoIntercomActivity.this, "Login failed，errorCode=" + errorCode + ",e=" + e.toString(), Toast.LENGTH_SHORT).show();
                                Toast.makeText(VideoIntercomActivity.this, "You can't make intercom due to server login Issue.", Toast.LENGTH_SHORT).show();
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


        DMVPhoneModel.addMsgListener(new DMPhoneMsgListener() {
            @Override
            public void messageReceived(String msg) {
                Log.d(TAG, "addMsgListener msg=" + msg);
                Log.d(TAG, "addMsgListener msg.length=" + msg.length());
                Toast.makeText(VideoIntercomActivity.this, msg, Toast.LENGTH_LONG).show();
            }

            @Override
            public void dtmfMsgReceived(int dtmf) {
                Log.d(TAG, "dtmf=" + dtmf);
            }

            @Override
            public void onCallPreviewMsgReceived(VideoDeviceEntity device) {
                Log.e(TAG, device.toString());
                if (device.bitmap != null) {
                    //预览消息处理，或者调用接口使用sdk内部的预览处理
                }
            }
        });
        DMVPhoneModel.addHandleListener(status -> {
            if (status == DMConstants.HOOK_OFF) {
                Log.d(TAG, "onHandleStateReceived status=" + status + ",手柄挂下");
            } else if (status == DMConstants.HOOK_ON) {
                Log.d(TAG, "onHandleStateReceived status=" + status + ",手柄拿起");
            }
        });

//        if (Util.checkInternet(this)) {

        DMVPhoneModel.setLogSwitch(true);
        DMVPhoneModel.addLoginCallBack(statusCallback);
//        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        DMVPhoneModel.removeCallStateListener(callStateListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * Initialize views.
     */
    private void initViews() {
        videoIntercom = findViewById(R.id.list_item_video);
        imgbackIntercom = findViewById(R.id.img_back_intercom);

    }

    private void filter(String text) {
        ArrayList<VideoDeviceData>  device  = new ArrayList<>();
        for (VideoDeviceData d : videoIntercomArrayList) {

            if (!d.getCdeviceName().isEmpty()){
                if (d.getCdeviceName().toLowerCase().contains(text.toLowerCase())) {
                    device.add(d);
                }
            }else {
                if (d.getDeviceName().toLowerCase().contains(text.toLowerCase()) ) {
                    device.add(d);
                }
            }
        }
        adapter.updateList(device);
    }


    /**
     * Initialize listeners.
     */
    private void initListeners() {
        imgbackIntercom.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back_intercom:
                finish();
                break;
        }
    }

    /**
     * Request permission
     */
    public void requestPermissiontest() {
        // you needer permissions
        String[] permissions = {
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA};
        // check it is needed
        permissions = CheckPermissionUtils.getNeededPermission(VideoIntercomActivity.this, permissions);
        // requestPermissions
        if (permissions.length > 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissions, REQUEST_CODE_MAIN);
            }
        }
    }

    /**
     * This is click callback we are perform the calling option.
     *
     * @param videoIntercomModel object of video device.
     */
    @Override
    public void onClickIntercomDevice(VideoDeviceData videoIntercomModel) {
        activeCallDevice = videoIntercomModel;
        call(videoIntercomModel.getDeviceSno());
    }

    private void callOpenDoor(String device_sn){
//        dialog.dismiss();
        if (dialog!=null) {
            dialog.dismiss();
        }
        Util.checkInternet(this, new Util.NetworkCheckCallback() {
            @Override
            public void onNetworkCheckComplete(boolean isAvailable) {
                if (isAvailable) {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("dev_sn", device_sn);
                    hashMap.put("appuser_ID", LOGIN_DETAIL.getAppuserID());
                    apiServiceProvider.taskOpenDoorRemotely(hashMap, new RetrofitListener<SuccessResponse>() {
                        @Override
                        public void onResponseSuccess(SuccessResponse sucessRespnse, String apiFlag) {
                            Toast.makeText(VideoIntercomActivity.this,sucessRespnse.getMsg(), Toast.LENGTH_SHORT).show();
                            Util.logDoorOpenEvent("VideoDeviceList", true, LOGIN_DETAIL.getAppuserID(), device_sn);
                        }

                        @Override
                        public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                            Util.firebaseEvent(Constant.APIERROR, VideoIntercomActivity.this, Constant.UrlPath.SERVER_URL + apiFlag, LOGIN_DETAIL.getUsername(), LOGIN_DETAIL.getAppuserID(), errorObject.getStatus());
                            Toast.makeText(VideoIntercomActivity.this, throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            Util.logDoorOpenEvent("VideoDeviceList", false, LOGIN_DETAIL.getAppuserID(), device_sn);
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onRemotelyOpenDoor(String device_sn) {

        callOpenDoor(device_sn);
    }

    @Override
    public void onOptionClickIntercomeDevice(VideoDeviceData videoDeviceData) {
        intercomeOptionDialog(videoDeviceData);
    }

    @Override
    public void onRelayClick(IntercomRelayData relayItem) {
        openVideoDeviceRelay(relayItem);
    }

    public void openVideoDeviceRelay(IntercomRelayData relayItem){
        Util.checkInternet(this, new Util.NetworkCheckCallback() {
            @Override
            public void onNetworkCheckComplete(boolean isAvailable) {
                if (isAvailable) {
                    apiServiceProvider = ApiServiceProvider.getInstance(VideoIntercomActivity.this);
                    OpenVideoDeviceRelayRequest relayRequest = new OpenVideoDeviceRelayRequest( relayItem.getAutomationDeviceID(),String.valueOf(relayItem.getAttachedRelay()));
                    apiServiceProvider.openVideoDeviceRelay(relayRequest, new RetrofitListener<SuccessResponseModel>() {
                        @Override
                        public void onResponseSuccess(SuccessResponseModel sucessRespnse, String apiFlag) {
                            if (dialog!=null) {
                                dialog.dismiss();
                            }
                            Toast.makeText(VideoIntercomActivity.this, sucessRespnse.getMsg(), Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                            if (dialog!=null) {
                                dialog.dismiss();
                            }
                            Toast.makeText(VideoIntercomActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
//                chagefailSatus(isOnline);
                        }
                    });
                } else {
                    hideLoader();
                }
            }
        });
    }

    public void intercomeOptionDialog(VideoDeviceData deviceData) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this,R.style.CustomDialogTheme);

        // ...Irrelevant code for customizing the buttons and title

        LayoutInflater inflater = this.getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.intercome_options_dialog, null);
        dialogView.setBackgroundResource(R.drawable.intercome_white_border_blue_bg);
        dialogBuilder.setView(dialogView);
        ImageView openDoor = (ImageView) dialogView.findViewById(R.id.img_openDoor);
        ImageView videoCall = (ImageView) dialogView.findViewById(R.id.img_videocall);
        RecyclerView recyclerView = (RecyclerView) dialogView.findViewById(R.id.rv_relaysforIntercom);
        TextView tvDeviceTitle = (TextView) dialogView.findViewById(R.id.tv_deviceTitle);
        dialog = dialogBuilder.create();
        videoCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeCallDevice = deviceData;
                call(deviceData.getDeviceSno());
            }
        });

        openDoor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performOpenDoorOperation(deviceData);
            }
        });

        if (deviceData.getCdeviceName().length() > 0) {
            tvDeviceTitle.setText(deviceData.getCdeviceName());
        } else {
            tvDeviceTitle.setText(deviceData.getDeviceName());
        }

        IntercomeRelayAdapter relayAdapter = new IntercomeRelayAdapter(this, deviceData.getRelayData(),this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(relayAdapter);


        dialog.show();
    }

    private void performOpenDoorOperation(VideoDeviceData deviceData) {

        String isAcessible = deviceData.getIsAccessTimeEnabled();

        if (isAcessible.equals("1")) {

            callGetServerAPI(deviceData);

        } else {

            goToOpenDoor = true;

            callOpenDoor(deviceData.getDeviceSno());

        }
    }

    private void callGetServerAPI(VideoDeviceData deviceData) {
        ApiServiceProvider apiServiceProvider = ApiServiceProvider.getInstance(this);
        Util.checkInternet(this, new Util.NetworkCheckCallback() {
            @Override
            public void onNetworkCheckComplete(boolean isAvailable) {
                if (isAvailable){
                    apiServiceProvider.callGetServerCurrentTime(new RetrofitListener<ResponseBody>() {
                        @Override
                        public void onResponseSuccess(ResponseBody sucessRespnse, String apiFlag) {
                            try {
                                JSONObject jsonObject = new JSONObject(sucessRespnse.string());
                                String dateTime = jsonObject.optString("date");
                                Date serverDate = utcToLocalTimeZone(dateTime);
                                String isAcessible = SharePreference.getInstance(VideoIntercomActivity.this).getString("isAccessable");

                                if (!SharePreference.getInstance(VideoIntercomActivity.this).getString("deviceStartTime").equalsIgnoreCase("")
                                        && !SharePreference.getInstance(VideoIntercomActivity.this).getString("deviceEndTime").equalsIgnoreCase("")) {

                                    Date startTime = utcToLocalTimeZone(SharePreference.getInstance(VideoIntercomActivity.this).getString("deviceStartTime"));
                                    Date endTime = utcToLocalTimeZone(SharePreference.getInstance(VideoIntercomActivity.this).getString("deviceEndTime"));

                                    goToOpenDoor = accessWithinRange(isAcessible, startTime, endTime, serverDate);
//                    SharePreference.getInstance(getActivity()).putString(getResources().getString(R.string.server_current_time), dateTime);
                                    Log.d("EndTimeeCheck: ", serverDate + " EndTime:" + endTime + " StartTime:" + startTime);
                                } else {
                                    goToOpenDoor = true;
                                }
                                callOpenDoor(deviceData.getDeviceSno());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                            Util.firebaseEvent(Constant.APIERROR, VideoIntercomActivity.this,Constant.UrlPath.SERVER_URL+apiFlag, LOGIN_DETAIL.getUsername(),LOGIN_DETAIL.getAppuserID(),errorObject.getStatus());


                        }
                    });

                }
            }
        });
    }
    /**
     * calling on iot device.
     *
     * @param deviceSno
     */
    public void call(String deviceSno) {
        if (dialog!=null){
        dialog.dismiss();
        }
        Util.checkInternet(this, new Util.NetworkCheckCallback() {
            @Override
            public void onNetworkCheckComplete(boolean isAvailable) {
                if (isAvailable) {
                    DMVPhoneModel.callAccount(deviceSno, 2, VideoIntercomActivity.this, LOGIN_DETAIL.getUserEmail());
                }
            }
        });
    }

    @Override
    protected void onResume() {
        DMVPhoneModel.addCallStateListener(callStateListener);
        if (DMVPhoneModel.hasIncomingCall()) {
            Log.e(TAG, "find IncomingCall call");
            startActivity(new Intent(this, DmCallIncomingActivity.class));
        }
        super.onResume();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_MAIN:
                if (grantResults.length > 0) {
                    return;
                }
                if (!CheckPermissionUtils.isNeedAddPermission(VideoIntercomActivity.this, Manifest.permission.RECORD_AUDIO)) {
                    Toast.makeText(VideoIntercomActivity.this, "Apply for permission to success:" + Manifest.permission.RECORD_AUDIO, Toast.LENGTH_LONG).show();
                }
                if (!CheckPermissionUtils.isNeedAddPermission(VideoIntercomActivity.this, Manifest.permission.CAMERA)) {
                    Toast.makeText(VideoIntercomActivity.this, "申请权限成功:" + Manifest.permission.CAMERA, Toast.LENGTH_LONG).show();
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


}