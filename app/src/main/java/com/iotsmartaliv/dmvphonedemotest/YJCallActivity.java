package com.iotsmartaliv.dmvphonedemotest;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.doormaster.vphone.config.DMCallState;
import com.doormaster.vphone.inter.DMModelCallBack.DMCallStateListener;
import com.doormaster.vphone.inter.DMPhoneMsgListener;
import com.doormaster.vphone.inter.DMVPhoneModel;
import com.iotsmartaliv.R;
import com.iotsmartaliv.adapter.automation.IntercomeRelayAdapter;
import com.iotsmartaliv.apiAndSocket.listeners.RetrofitListener;
import com.iotsmartaliv.apiAndSocket.models.ErrorObject;
import com.iotsmartaliv.apiAndSocket.models.IntercomRelayData;
import com.iotsmartaliv.apiAndSocket.models.VideoDeviceData;
import com.iotsmartaliv.apiAndSocket.models.VideoDeviceListModel;
import com.iotsmartaliv.apiAndSocket.retrofit.ApiServiceProvider;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.interfaces.VideoIntercomItemClick;
import com.iotsmartaliv.model.OpenVideoDeviceRelayRequest;
import com.iotsmartaliv.model.SuccessResponseModel;
import com.iotsmartaliv.utils.Util;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import okhttp3.ResponseBody;

import static com.iotsmartaliv.constants.Constant.LOGIN_DETAIL;
import static com.iotsmartaliv.constants.Constant.hideLoader;
import static com.iotsmartaliv.utils.CommanUtils.accessWithinRange;
import static com.iotsmartaliv.utils.CommanUtils.utcToLocalTimeZone;

public class YJCallActivity extends Activity implements View.OnClickListener, VideoIntercomItemClick {

    private static String TAG = "DmCallActivity";

    private LinearLayout mTalkingLayout = null;

    private TextView textViewCallState = null;
    private TextView textViewCountDown = null;

    private SurfaceView mVideoView;
    private SurfaceView mCaptureView;

    private TimeCount time;
    NotificationCompat.Builder mBuilder;
    NotificationManager notificationManager;
    private ApiServiceProvider apiServiceProvider;
    //    private String serverDate = "";
    private boolean goInsideToOpenDoor = false;
    private ArrayList<VideoDeviceData> videoIntercomArrayList = new ArrayList<>();
    VideoDeviceData matchedDevice = null;
    private AlertDialog dialog;
    String deviceSN;
    String callType;
    DMCallStateListener callStateListener = new DMCallStateListener() {
        @Override
        public void callState(DMCallState state, String message) {
            Log.d("CallStateLis calling", "value=" + state.value() + ",message=" + message);
            if (state == DMCallState.Connected || state == DMCallState.OutgoingRinging) {
                mTalkingLayout.setVisibility(View.VISIBLE);
                textViewCallState.setText(R.string.calling);
                time.start();

            } else if (state == DMCallState.StreamsRunning) {
            } else {
                if (state == DMCallState.Error) {
                    //通话结束
                    finish();
                } else if (state == DMCallState.CallEnd) {
                    //通话结束
                    finish();
                }
            }
        }
    };
    private boolean isSpeakerEnabled = false;

    private float mZoomFactor = 1.f;
    private TextView tv_title;
    private ImageView iv_speaker, iv_hungup, iv_opendoor;

    //初始化界面
    private void initView() {
        isSpeakerEnabled = DMVPhoneModel.isSpeakerEnable();
        if (isSpeakerEnabled) {
            iv_speaker.setImageResource(R.drawable.volume_speaker);
        } else {
            iv_speaker.setImageResource(R.drawable.muted_speaker);
        }

    }

    @Override
    protected void onResume() {
        Log.i(TAG, "onResume: ");
        showCallRunningNotification();
        DMVPhoneModel.onVideoResume();
        DMVPhoneModel.addCallStateListener(callStateListener);
        String displayName = DMVPhoneModel.getDisplayName(this);
        deviceSN = DMVPhoneModel.getCurConnDevice().dev_sn;
        String deviceName = DMVPhoneModel.getCurConnDevice().dev_name;
        Toast.makeText(this, deviceName,Toast.LENGTH_LONG).show();
        tv_title.setText(displayName);
        super.onResume();
//        if (!DMVPhoneModel.hasCurrentCall()) {
//            Log.e("YJCallActivity","Couldn't find current call");
//            finish();
//        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_yj_call);

        DMVPhoneModel.addCallStateListener(callStateListener);

        DMVPhoneModel.addMsgListener(new DMPhoneMsgListener() {
            @Override
            public void messageReceived(String msg) {
                Log.d(TAG, "msg=" + msg);
                Toast.makeText(YJCallActivity.this, msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void dtmfMsgReceived(int dtmf) {
                Toast.makeText(YJCallActivity.this, String.valueOf(dtmf), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void controlMsgReceived(int command, String msg) {
                Log.d(TAG, "msg=" + msg);
                Toast.makeText(YJCallActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
        if (getIntent().getStringExtra(Constant.CALL_PATH) != null) {
            callType = getIntent().getStringExtra(Constant.CALL_PATH);
        }
        mVideoView = findViewById(R.id.videoSurface);
        mCaptureView = findViewById(R.id.videoCaptureSurface);
        mCaptureView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        iv_hungup = findViewById(R.id.iv_hungup);
        iv_speaker = findViewById(R.id.iv_speaker);
        iv_opendoor = findViewById(R.id.iv_opendoor);
        tv_title = findViewById(R.id.tv_title);

        iv_hungup.setOnClickListener(this);
        iv_speaker.setOnClickListener(this);
        iv_opendoor.setOnClickListener(this);


        time = new TimeCount(60000, 1000);
        initView();
        // set this flag so this activity will stay in front of the keyguard
        int flags = WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON;
        getWindow().addFlags(flags);

        DMVPhoneModel.fixZOrder(mVideoView, mCaptureView);
        mVideoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("YJCallActivity", "mVideoView onClick");
                if (mZoomFactor == 1.f) {
                    mZoomFactor = 1.5f;
                    DMVPhoneModel.zoomVideo(mZoomFactor, 0.5f, 0.5f);
                } else {
                    mZoomFactor = 1.f;
                    DMVPhoneModel.zoomVideo(mZoomFactor, 0.5f, 0.5f);
                }
            }
        });

        setUpNotification();
        getVideoDevice();

    }

    public void getVideoDevice() {
        SharedPreferences sharePreferenceNew = getSharedPreferences("ALIV_NEW", Context.MODE_PRIVATE);
        String userIdApp = sharePreferenceNew.getString("APP_USER_ID", "");
        apiServiceProvider = ApiServiceProvider.getInstance(this);
        apiServiceProvider.callForVideoDeviceWithoutProgres(userIdApp, new RetrofitListener<VideoDeviceListModel>() {
            @Override
            public void onResponseSuccess(VideoDeviceListModel sucessRespnse, String apiFlag) {
                videoIntercomArrayList.clear();
                if (sucessRespnse.getStatus().equalsIgnoreCase("OK")) {
                    if (!sucessRespnse.getData().isEmpty()) {
                        videoIntercomArrayList.addAll(sucessRespnse.getData());
                        for (VideoDeviceData device : videoIntercomArrayList) {
                            if (device.getDeviceSno().equals(deviceSN)) {
                                matchedDevice = device;
                                break;
                            }
                        }
                        //    Toast.makeText(this, "Video Device" + successDeviceListRes   ponse.getData().size(), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(YJCallActivity.this, "Video Device List Empty", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(YJCallActivity.this, sucessRespnse.getMsg(), Toast.LENGTH_LONG).show();
                }
//
            }

            @Override
            public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
//                                Util.firebaseEvent(Constant.APIERROR, VideoIntercomActivity.this,Constant.UrlPath.SERVER_URL+apiFlag, LOGIN_DETAIL.getUsername(),LOGIN_DETAIL.getAppuserID(),errorObject.getStatus());
//                                Toast.makeText(VideoIntercomActivity.this, throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                Util.firebaseEvent(Constant.APIERROR, YJCallActivity.this, Constant.UrlPath.SERVER_URL + apiFlag, LOGIN_DETAIL.getUsername(), LOGIN_DETAIL.getAppuserID(), errorObject.getStatus());

                try {
                    Toast.makeText(YJCallActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(YJCallActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy: ");
        if (notificationManager != null) {
            notificationManager.cancel(1221);
        } else {
            NotificationManagerCompat.from(this).cancel(1221);
        }

        DMVPhoneModel.onVideoDestroy();
        stopCountDownTimer();
        super.onDestroy();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    /*
     *
     * adjust Ring volumn
     *
     * Volumn + KeyEvent.KEYCODE_2
     *
     * Volumn - KeyEvent.KeyCode_8
     *
     * */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        // removed code for back disable:  keyCode == KeyEvent.KEYCODE_BACK
        if ((keyCode == KeyEvent.KEYCODE_HOME) || keyCode == KeyEvent.KEYCODE_POUND) {
            decline();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {

        DMVPhoneModel.onVideoPause();
        DMVPhoneModel.removeCallStateListener(callStateListener);
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_hungup:
                DMVPhoneModel.refuseCall();
                break;
            case R.id.iv_speaker:
                switchSpeaker();
                break;
            case R.id.iv_opendoor:
//                if (callType.equalsIgnoreCase(Constant.ICOMING_CALL)) {
                    if (matchedDevice.getRelayData() != null) {
                        String isAcessible = matchedDevice.getIsAccessTimeEnabled();
                        if (isAcessible.equals("1")) {
                            callGetServerAPI();
                        }else {
                            intercomeOptionDialog(matchedDevice);
                        }
                    } else {
                        String isAcessible = matchedDevice.getIsAccessTimeEnabled();

                        if (isAcessible.equals("1")) {
                            callGetServerAPI();
                        } else {
                            DMVPhoneModel.openDoor();
                            Toast.makeText(YJCallActivity.this, "Door Open Successfully.", Toast.LENGTH_SHORT).show();
//                            Util.logDoorOpenEvent("VideoCall", true, LOGIN_DETAIL.getAppuserID(), deviceSN);
                        }
                    }
//                } else {
//                    String isAcessible = matchedDevice.getIsAccessTimeEnabled();
//
//                    if (isAcessible.equals("1")) {
//                        callGetServerAPI();
//                    } else {
//                        DMVPhoneModel.openDoor();
//                        Toast.makeText(YJCallActivity.this, "Door Open Successfully.", Toast.LENGTH_SHORT).show();
//
//                    }
////                    String isAcessible = SharePreference.getInstance(this).getString("isAccessable");
////
////                    if (isAcessible.equals("1")) {
////                        callGetServerAPI();
//////                    } else {
////                    DMVPhoneModel.openDoor();
////                    Toast.makeText(YJCallActivity.this, "Door Open Successfully.", Toast.LENGTH_SHORT).show();
//
////                    }
//                }
                // DMVPhoneModel.openDoor();

                break;
        }
    }

    private void callGetServerAPI() {
        apiServiceProvider = ApiServiceProvider.getInstance(this);

        Util.checkInternet(YJCallActivity.this, new Util.NetworkCheckCallback() {
            @Override
            public void onNetworkCheckComplete(boolean isAvailable) {
                if (isAvailable) {
                    apiServiceProvider.callGetServerCurrentTime(new RetrofitListener<ResponseBody>() {
                        @Override
                        public void onResponseSuccess(ResponseBody sucessRespnse, String apiFlag) {
                            try {
                                JSONObject jsonObject = new JSONObject(sucessRespnse.string());
                                String dateTime = jsonObject.optString("date");
                                Date serverDate = utcToLocalTimeZone(dateTime);
                                String isAcessible = matchedDevice.getIsAccessTimeEnabled();

                                if (matchedDevice.getAccessStarttime() != null && !matchedDevice.getAccessStarttime().equalsIgnoreCase("")
                                        && matchedDevice.getAccessEndtime() != null && matchedDevice.getAccessEndtime().equalsIgnoreCase("")) {

                                    Date startTime = utcToLocalTimeZone(matchedDevice.getAccessStarttime());
                                    Date endTime = utcToLocalTimeZone(matchedDevice.getAccessEndtime());

//                                    goToOpenDoor = accessWithinRange(isAcessible, startTime, endTime, serverDate);
////                    SharePreference.getInstance(getActivity()).putString(getResources().getString(R.string.server_current_time), dateTime);
//                                    Log.d("EndTimeeCheck: ", serverDate + " EndTime:" + endTime + " StartTime:" + startTime);
//                                } else {
//                                    goToOpenDoor = true;
//                                }
//
//                                if (!SharePreference.getInstance(YJCallActivity.this).getString("deviceStartTime").equalsIgnoreCase("")
//                                        && !SharePreference.getInstance(YJCallActivity.this).getString("deviceEndTime").equalsIgnoreCase("")) {
//
//                                    Date startTime = utcToLocalTimeZone(SharePreference.getInstance(YJCallActivity.this).getString("deviceStartTime"));
//                                    Date endTime = utcToLocalTimeZone(SharePreference.getInstance(YJCallActivity.this).getString("deviceEndTime"));

                                    goInsideToOpenDoor = accessWithinRange(isAcessible, startTime, endTime, serverDate);
//                    SharePreference.getInstance(getActivity()).putString(getResources().getString(R.string.server_current_time), dateTime);
                                    Log.d("EndTimeeCheck: ", serverDate + " EndTime:" + endTime + " StartTime:" + startTime);
                                } else {
                                    goInsideToOpenDoor = true;
                                }

                                if (goInsideToOpenDoor) {
                                    if (matchedDevice.getRelayData() != null) {
                                        intercomeOptionDialog(matchedDevice);
                                    } else {
                                        DMVPhoneModel.openDoor();
                                        dialog.dismiss();
                                        Toast.makeText(YJCallActivity.this, "Door Open Successfully.", Toast.LENGTH_SHORT).show();
//                                        Util.logDoorOpenEvent("VideoCall", true, LOGIN_DETAIL.getAppuserID(), deviceSN);
                                    }
                                    //callOpenDoor();

                                }else {
                                    Toast.makeText(YJCallActivity.this, "User can not access at this time", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                            Util.firebaseEvent(Constant.APIERROR, YJCallActivity.this, Constant.UrlPath.SERVER_URL + apiFlag, LOGIN_DETAIL.getUsername(), LOGIN_DETAIL.getAppuserID(), errorObject.getStatus());


                        }
                    });

                }
            }
        });
    }


    private void decline() {
        DMVPhoneModel.refuseCall();
        finish();
    }

    private void switchSpeaker() {
        isSpeakerEnabled = !isSpeakerEnabled;
        if (isSpeakerEnabled) {
            iv_speaker.setImageResource(R.drawable.volume_speaker);
        } else {
            iv_speaker.setImageResource(R.drawable.muted_speaker);
        }
        DMVPhoneModel.enableSpeaker(isSpeakerEnabled);
    }

    private void stopCountDownTimer() {
        if (time != null) {
            time.cancel();
        }
        time = null;
    }


    private class TimeCount extends CountDownTimer {

        TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            // 计时过程
            String str = String.valueOf(millisUntilFinished / 1000);
            if (str.length() == 1) {
                str = "0" + str;
            }
            textViewCountDown.setText("00:" + str);
        }

        @Override
        public void onFinish() {
            // 计时完毕
            decline();
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    public void setUpNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("83",
                    "Call Running Notification", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{1000});
            notificationChannel.enableVibration(true);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            notificationChannel.setDescription("Call Running Notification");
            notificationChannel.setShowBadge(true);

            notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    public void showCallRunningNotification() {

        Intent intent = new Intent(getApplicationContext(), YJCallActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);//to tell that it is a fresh task
        PendingIntent pendingIntent;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            pendingIntent = PendingIntent.getActivity(getApplicationContext(), (int) System.currentTimeMillis(), intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        } else {
            pendingIntent = PendingIntent.getActivity(getApplicationContext(), (int) System.currentTimeMillis(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

//        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),(int)System.currentTimeMillis(),intent,PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);

        Bitmap IconLg = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_foreground);


        mBuilder = new NotificationCompat.Builder(this, "83");
        mBuilder.setContentTitle(getString(R.string.app_name))
                .setContentText("Ongoing Intercom Call")
                .setTicker("Call is Running...")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(IconLg)
                .setContentIntent(pendingIntent)
                .setPriority(Notification.PRIORITY_HIGH)
                .setVibrate(new long[]{1000})
//                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setOngoing(true);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(1221, mBuilder.build());
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
        View viewLine = (View) dialogView.findViewById(R.id.view_verticale_line);
        dialog = dialogBuilder.create();
        videoCall.setVisibility(View.GONE);
        viewLine.setVisibility(View.GONE);
//        videoCall.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                activeCallDevice = deviceData;
//                call(deviceData.getDeviceSno());
//            }
//        });

        openDoor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DMVPhoneModel.openDoor();
                dialog.dismiss();
                Toast.makeText(YJCallActivity.this, "Door Open Successfully.", Toast.LENGTH_SHORT).show();
//                Util.logDoorOpenEvent("VideoCall", true, LOGIN_DETAIL.getAppuserID(), deviceSN);
            }
        });

        if (deviceData.getCdeviceName().length() > 0) {
            tvDeviceTitle.setText(deviceData.getCdeviceName());
        } else {
            tvDeviceTitle.setText(deviceData.getDeviceName());
        }

        IntercomeRelayAdapter relayAdapter = new IntercomeRelayAdapter(this, deviceData.getRelayData(), this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(relayAdapter);


        dialog.show();
    }

    @Override
    public void onClickIntercomDevice(VideoDeviceData videoIntercomModel) {

    }

    @Override
    public void onRemotelyOpenDoor(String device_Sn) {

    }

    @Override
    public void onOptionClickIntercomeDevice(VideoDeviceData videoDeviceData) {

    }

    @Override
    public void onRelayClick(IntercomRelayData relayItem) {
        openVideoDeviceRelay(relayItem);
    }

    public void openVideoDeviceRelay(IntercomRelayData relayItem) {
        Util.checkInternet(this, new Util.NetworkCheckCallback() {
            @Override
            public void onNetworkCheckComplete(boolean isAvailable) {

                if (isAvailable) {
                    apiServiceProvider = ApiServiceProvider.getInstance(YJCallActivity.this);
                    OpenVideoDeviceRelayRequest relayRequest = new OpenVideoDeviceRelayRequest(relayItem.getAutomationDeviceID(), String.valueOf(relayItem.getAttachedRelay()));
                    apiServiceProvider.openVideoDeviceRelay(relayRequest, new RetrofitListener<SuccessResponseModel>() {
                        @Override
                        public void onResponseSuccess(SuccessResponseModel sucessRespnse, String apiFlag) {
                            dialog.dismiss();
                            Toast.makeText(YJCallActivity.this, sucessRespnse.getMsg(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                            dialog.dismiss();
                            Toast.makeText(YJCallActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
//                chagefailSatus(isOnline);
                        }
                    });
                } else {
                    hideLoader();
                }
            }
        });
    }

}
