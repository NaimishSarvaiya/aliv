package com.iotsmartaliv.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.doormaster.vphone.config.DMCallState;
import com.doormaster.vphone.config.DMConstants;
import com.doormaster.vphone.config.DMErrorReturn;
import com.doormaster.vphone.entity.VideoDeviceEntity;
import com.doormaster.vphone.exception.DMException;
import com.doormaster.vphone.inter.DMModelCallBack.DMCallStateListener;
import com.doormaster.vphone.inter.DMModelCallBack.DMCallback;
import com.doormaster.vphone.inter.DMPhoneMsgListener;
import com.doormaster.vphone.inter.DMVPhoneModel;
import com.iotsmartaliv.R;
import com.iotsmartaliv.adapter.VideoIntercomAdapter;
import com.iotsmartaliv.apiCalling.listeners.RetrofitListener;
import com.iotsmartaliv.apiCalling.models.DeviceObject;
import com.iotsmartaliv.apiCalling.models.ErrorObject;
import com.iotsmartaliv.apiCalling.models.SuccessDeviceListResponse;
import com.iotsmartaliv.apiCalling.models.SuccessResponse;
import com.iotsmartaliv.apiCalling.retrofit.ApiServiceProvider;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.dmvphonedemotest.CheckPermissionUtils;
import com.iotsmartaliv.dmvphonedemotest.DmCallIncomingActivity;
import com.iotsmartaliv.dmvphonedemotest.DmCallOutgoingActivity;
import com.iotsmartaliv.interfaces.VideoIntercomItemClick;
import com.iotsmartaliv.utils.ConnectivityHelper;
import com.iotsmartaliv.utils.ConnectivityRv;
import com.iotsmartaliv.utils.Util;

import java.util.ArrayList;
import java.util.HashMap;

import static com.iotsmartaliv.constants.Constant.LOGIN_DETAIL;

/**
 * This activity class is used for video intercom.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2018-10-23
 */
public class VideoIntercomActivity extends AppCompatActivity implements View.OnClickListener, VideoIntercomItemClick, RetrofitListener<SuccessDeviceListResponse> {

    private static final int REQUEST_CODE_MAIN = 999;
    private static final String TAG = VideoIntercomActivity.class.getName();
    ApiServiceProvider apiServiceProvider;
    private VideoIntercomAdapter adapter;
    private ListView videoIntercom;
    private ArrayList<DeviceObject> videoIntercomArrayList = new ArrayList<>();
    private ImageView imgbackIntercom;
    DeviceObject activeCallDevice;

    Boolean internetConnected = false;

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
                    if (isAvailable){
                        apiServiceProvider.callForVideoDeviceList(userIdApp, VideoIntercomActivity.this);
                    }
                }
            });
//        }


        initViews();
        initListeners();
        //videoIntercom.setItemsCanFocus(false);
        adapter = new VideoIntercomAdapter(videoIntercomArrayList, this, this);
        videoIntercom.setAdapter(adapter);
        requestPermissiontest();
//        Constant.showLoader(this);

            Util.checkInternet(this, new Util.NetworkCheckCallback() {
                @Override
                public void onNetworkCheckComplete(boolean isAvailable) {
                    if (isAvailable){
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
    public void onClickIntercomDevice(DeviceObject videoIntercomModel) {
        activeCallDevice = videoIntercomModel;
        call(videoIntercomModel.getDeviceSno());
    }

    @Override
    public void onRemotelyOpenDoor(String device_sn) {
            Util.checkInternet(this, new Util.NetworkCheckCallback() {
                @Override
                public void onNetworkCheckComplete(boolean isAvailable) {
                    if (isAvailable){
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("dev_sn", device_sn);
                        hashMap.put("appuser_ID", LOGIN_DETAIL.getAppuserID());
                        apiServiceProvider.taskOpenDoorRemotely(hashMap, new RetrofitListener<SuccessResponse>() {
                            @Override
                            public void onResponseSuccess(SuccessResponse sucessRespnse, String apiFlag) {
                                Toast.makeText(VideoIntercomActivity.this, sucessRespnse.getMsg().equalsIgnoreCase("Gate open") ? "Door Open Successfully." : sucessRespnse.getMsg(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                                Util.firebaseEvent(Constant.APIERROR, VideoIntercomActivity.this,Constant.UrlPath.SERVER_URL+apiFlag, LOGIN_DETAIL.getUsername(),LOGIN_DETAIL.getAppuserID(),errorObject.getStatus());
                                Toast.makeText(VideoIntercomActivity.this, throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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
            Util.checkInternet(this, new Util.NetworkCheckCallback() {
                @Override
                public void onNetworkCheckComplete(boolean isAvailable) {
                    if (isAvailable){
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

    @Override
    public void onResponseSuccess(SuccessDeviceListResponse successDeviceListResponse, String apiFlag) {
        switch (apiFlag) {
            case Constant.UrlPath.DEVICE_VIDEO_LIST_API:
                videoIntercomArrayList.clear();

                if (successDeviceListResponse.getStatus().equalsIgnoreCase("OK")) {
                    if (successDeviceListResponse.getData().size() > 0) {
                        videoIntercomArrayList.addAll(successDeviceListResponse.getData());
                        adapter.notifyDataSetChanged();
                        //    Toast.makeText(this, "Video Device" + successDeviceListRes   ponse.getData().size(), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "Video Device List Empty", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(this, successDeviceListResponse.getMsg(), Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
        switch (apiFlag) {
            case Constant.UrlPath.DEVICE_VIDEO_LIST_API:
                Util.firebaseEvent(Constant.APIERROR, VideoIntercomActivity.this,Constant.UrlPath.SERVER_URL+apiFlag, LOGIN_DETAIL.getUsername(),LOGIN_DETAIL.getAppuserID(),errorObject.getStatus());

                try {
                    Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

}