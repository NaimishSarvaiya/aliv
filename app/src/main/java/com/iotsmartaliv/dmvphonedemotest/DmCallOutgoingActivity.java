/*
CallOutgoingActivity.java
Copyright (C) 2015  Belledonne Communications, Grenoble, France

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
*/
package com.iotsmartaliv.dmvphonedemotest;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.doormaster.vphone.config.DMCallState;
import com.doormaster.vphone.inter.DMModelCallBack.DMCallStateListener;
import com.doormaster.vphone.inter.DMVPhoneModel;
import com.iotsmartaliv.R;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.utils.Util;

import jp.wasabeef.blurry.Blurry;


public class DmCallOutgoingActivity extends Activity implements OnClickListener {
    private static final String TAG = "DmCallOutgoingActivity";
    private static DmCallOutgoingActivity instance;

    RelativeLayout topLayout;
    ImageView blurView;
    TextView callingDeviceName;
    private ImageView speaker, hangUp, contact_picture;
    private DMCallStateListener mListener;
    private boolean isSpeakerEnabled;

    private LinearLayout ll_handfree;
    private LinearLayout ll_handset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        instance = this;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_dm_call_outgoing);

        isSpeakerEnabled = false;

        speaker = findViewById(R.id.speaker);
        contact_picture = findViewById(R.id.contact_picture);
        callingDeviceName = findViewById(R.id.callingDeviceName);
        blurView = findViewById(R.id.blurView);
        topLayout = findViewById(R.id.topLayout);
        speaker.setOnClickListener(this);

        if (!DMVPhoneModel.isSpeakerEnable()) {
            speaker.setImageResource(R.drawable.muted_speaker);
        }

        // set this flag so this activity will stay in front of the keyguard
        int flags = WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON;
        getWindow().addFlags(flags);

        hangUp = findViewById(R.id.hang_up);
        hangUp.setOnClickListener(this);
        mListener = new DMCallStateListener() {
            @Override
            public void callState(DMCallState state, String message) {

                Log.i(TAG, "-----------state=" + state.toString());
                if (DMCallState.CallEnd == state) {
                    Log.i(TAG, "-----------电话被挂断了");
                    finish();
                }

                if (DMCallState.Connected == state) {
                    // LogUtils.i(TAG, "-----------电话被接听了");
                    Util.logVideoCallEvent("OUTGOING",DMVPhoneModel.getCurConnDevice().dev_sn,DMVPhoneModel.getDisplayName(DmCallOutgoingActivity.this));
                    Intent intent = new Intent(DmCallOutgoingActivity.this, YJCallActivity.class);
                    intent.putExtra(Constant.CALL_PATH, Constant.OUTGOING_CALL);
                    startActivity(intent);
                    finish();
                }
            }
        };

        DMVPhoneModel.addCallStateListener(mListener);
        super.onCreate(savedInstanceState);
        callingDeviceName.setText("Calling....\n" + getIntent().getStringExtra("DeviceName"));
        Blurry.with(this).radius(25).sampling(2).from(BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_background)).into(blurView);
        Glide.with(this).asGif().load(R.raw.incoming_call).into(contact_picture);


    }

    @Override
    protected void onResume() {
        super.onResume();
        instance = this;
        if (!DMVPhoneModel.hasOutgoingCall()) {
            Log.e("CallOutGoingActivity", "Couldn't find outgoing call");
            finish();
        }
    }

    @Override
    protected void onPause() {
        DMVPhoneModel.removeCallStateListener(mListener);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        instance = null;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.speaker) {
            isSpeakerEnabled = !isSpeakerEnabled;
            if (isSpeakerEnabled) {
                speaker.setImageResource(R.drawable.volume_speaker);
            } else {
                speaker.setImageResource(R.drawable.muted_speaker);
            }
            DMVPhoneModel.enableSpeaker(isSpeakerEnabled);
        } else if (id == R.id.hang_up || id == R.id.ll_hang_up) {
            decline();
        } else if (id == R.id.ll_logs) {
            Log.d("OutGoingActivity", "View logs");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
            decline();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void decline() {
        DMVPhoneModel.refuseCall();
        if (!isFinishing()) {
            finish();
        }
    }
}
