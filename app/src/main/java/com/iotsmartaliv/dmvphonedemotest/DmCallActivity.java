package com.iotsmartaliv.dmvphonedemotest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.doormaster.vphone.config.DMCallState;
import com.doormaster.vphone.inter.DMModelCallBack.DMCallStateListener;
import com.doormaster.vphone.inter.DMPhoneMsgListener;
import com.doormaster.vphone.inter.DMVPhoneModel;
import com.iotsmartaliv.R;

public class DmCallActivity extends Activity implements View.OnClickListener {

    private static String TAG = "DmCallActivity";

    private LinearLayout mTalkingLayout = null;

    private TextView textViewCallState = null;
    private TextView textViewCountDown = null;

    private SurfaceView mVideoView;
    private SurfaceView mCaptureView;

    private TimeCount time;
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
    private ImageView iv_speaker, iv_hungup;
    private boolean isSpeakerEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_dm_call);

        DMVPhoneModel.addCallStateListener(callStateListener);

        DMVPhoneModel.addMsgListener(new DMPhoneMsgListener() {
            @Override
            public void messageReceived(String msg) {
                Log.d(TAG, "msg=" + msg);
            }
        });

        mVideoView = findViewById(R.id.videoSurface);
        mCaptureView = findViewById(R.id.videoCaptureSurface);
        mCaptureView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        iv_hungup = findViewById(R.id.iv_hungup);
        iv_speaker = findViewById(R.id.iv_speaker);

        iv_hungup.setOnClickListener(this);
        iv_speaker.setOnClickListener(this);

        Intent intent = getIntent();
        int type = intent.getIntExtra("type", -1);
        time = new TimeCount(60000, 1000);
        initView(type);
        // set this flag so this activity will stay in front of the keyguard
        int flags = WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON;
        getWindow().addFlags(flags);

        DMVPhoneModel.fixZOrder(mVideoView, mCaptureView);
    }

    @Override
    protected void onResume() {
        DMVPhoneModel.onVideoResume();
        DMVPhoneModel.addCallStateListener(callStateListener);
        super.onResume();
    }

    //初始化界面
    private void initView(int type) {
        textViewCallState = findViewById(R.id.textViewCallState);
        textViewCountDown = findViewById(R.id.textViewCountDown);
        mTalkingLayout = findViewById(R.id.llayoutTalking);

        if (type == 1 || type == 3) {//IncomingReceived

            textViewCallState.setText(R.string.calling);
            mTalkingLayout.setVisibility(View.VISIBLE);

            DMVPhoneModel.answerCall();
            time.start();
        } else {
            if (type == 2) {//OutgoingInit
                mTalkingLayout.setVisibility(View.INVISIBLE);
            }
        }

        isSpeakerEnabled = DMVPhoneModel.isSpeakerEnable();
        if (isSpeakerEnabled) {
            iv_speaker.setImageResource(R.drawable.volume_speaker);
        } else {
            iv_speaker.setImageResource(R.drawable.muted_speaker);
        }
    }

    @Override
    protected void onDestroy() {
        DMVPhoneModel.onVideoDestroy();
        stopCountDownTimer();
        super.onDestroy();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    protected void onPause() {

        DMVPhoneModel.onVideoPause();
        DMVPhoneModel.removeCallStateListener(callStateListener);
        super.onPause();
    }

    private void decline() {
        DMVPhoneModel.refuseCall();
        finish();
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
        }
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

        if ((keyCode == KeyEvent.KEYCODE_BACK
                || keyCode == KeyEvent.KEYCODE_HOME) || keyCode == KeyEvent.KEYCODE_POUND) {
            decline();
        }
        return super.onKeyDown(keyCode, event);
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
}
