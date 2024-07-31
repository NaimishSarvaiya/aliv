/*
CallIncomingActivity.java
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

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.doormaster.vphone.config.DMCallState;
import com.doormaster.vphone.inter.DMModelCallBack.DMCallStateListener;
import com.doormaster.vphone.inter.DMVPhoneModel;
import com.iotsmartaliv.R;
import com.iotsmartaliv.constants.Constant;

import jp.wasabeef.blurry.Blurry;

public class DmCallIncomingActivity extends Activity implements View.OnClickListener {

    private static final String[] PERMISSION_SHOWCAMERA = new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA};
    private static DmCallIncomingActivity instance;
    private final int REQUEST_SHOWCAMERA = 0;
    int REQUEST_CODE_REQUEST_PERMISSION = 0x0001;
    private ImageView contactPicture, accept, decline;
    private DMCallStateListener mListener;
    private boolean isActive;

    private TextView tv_title;
    ImageView incoming_call_image, blurView;

    /**
     * 获取应用详情页面intent
     *
     * @return
     */
    public static Intent getAppDetailSettingIntent(Context context) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        return localIntent;
    }

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_dm_call_incoming);

        // set this flag so this activity will stay in front of the keyguard
        int flags = WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON;
        getWindow().addFlags(flags);

        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            isActive = pm.isInteractive();
        } else {
            isActive = pm.isScreenOn();
        }
        final int screenWidth = getResources().getDisplayMetrics().widthPixels;
        accept = findViewById(R.id.accept);
        decline = findViewById(R.id.decline);
        tv_title = findViewById(R.id.tv_title);
        blurView = findViewById(R.id.blurView);
        incoming_call_image = findViewById(R.id.incoming_call_image);
        accept.setOnClickListener(this);
        decline.setOnClickListener(this);

        mListener = new DMCallStateListener() {
            @Override
            public void callState(DMCallState state, String message) {
                if (DMCallState.CallEnd == state) {
                    finish();
                }
                if (state == DMCallState.StreamsRunning) {
                    // The following should not be needed except some devices need it.
                    DMVPhoneModel.enableSpeaker(DMVPhoneModel.isSpeakerEnable());
                }
            }

        };

        instance = this;
        Blurry.with(this).radius(25).sampling(2).from(BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_background)).into(blurView);
        Glide.with(this).asGif().load(R.raw.cancel_call).into(incoming_call_image);
    }

    @Override
    protected void onResume() {
        super.onResume();
        instance = this;
        DMVPhoneModel.addCallStateListener(mListener);
        if(!DMVPhoneModel.hasIncomingCall()) {
            Log.e("CallOutGoingActivity", "Couldn't find outgoing call");
            finish();
        }
        String displayName = DMVPhoneModel.getDisplayName(this);
        if (tv_title != null) {
            tv_title.setText("Incoming Call.....\n" + ((displayName == null) ? "" : displayName));
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
            decline();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void decline() {
        DMVPhoneModel.refuseCall();
        finish();
    }

    private void answer() {
        DMVPhoneModel.answerCall();
        Intent intent = new Intent(DmCallIncomingActivity.this, YJCallActivity.class);
        intent.putExtra(Constant.CALL_PATH, Constant.ICOMING_CALL);
        startActivity(intent);
        finish();
    }

    /**
     * 请求权限
     */
    /**
     * private void requestPermissAndAnswer() {
     * if (PermissionUtils.hasSelfPermissions(this, PERMISSION_SHOWCAMERA)) {
     * answer();
     * } else {
     * if (PermissionUtils.shouldShowRequestPermissionRationale(this, PERMISSION_SHOWCAMERA)) {
     * showRationaleForCamera(new ShowCameraPermissionRequest(this));
     * } else {
     * ActivityCompat.requestPermissions(this, PERMISSION_SHOWCAMERA, REQUEST_CODE_REQUEST_PERMISSION);
     * show(getString(R.string.dm_requset_camera));
     * }
     * }
     * }
     * <p>
     * //3.拒绝之后调用：（小米的直接走这里不走第2步，华为的可以先走2，）
     * private void onCameraDenied() {
     * show(getString(R.string.dm_requset_camera_failure));
     * }
     * //4.拒绝之后，再次请求，小米不会调用这里，华为可以走这里
     * private void onCameraNeverAskAgain() {
     * //		show("44相机再次请求，弹出对话框");
     * new AlertDialog.Builder(currentActivity).setTitle(getString(R.string.dm_need_permissions_to_use)).setIcon(android.R.drawable.ic_dialog_info)
     * .setPositiveButton(getString(R.string.dm_go_set), new DialogInterface.OnClickListener() {
     *
     * @Override public void onClick(DialogInterface dialog, int which) {
     * //退出时将门禁列表清空
     * Intent intent=getAppDetailSettingIntent(Act_CallIncoming.this);
     * startActivity(intent);
     * }
     * }).setNegativeButton(getString(R.string.dm_cancel), null).show();
     * <p>
     * }
     * //2.2.拒绝之后，再次请求，小米不会调用这里，华为可以走这里，这是弹出一个弹窗，然后可以允许，允许之后就再次跳出系统的请求权限的对话框
     * private void showRationaleForCamera(PermissionRequest request) {
     * //2.拒绝之后，再次请求，小米不会调用这里，华为可以走这里，这是弹出一个弹窗，然后可以允许，允许之后就再次跳出系统的请求权限的对话框
     * showRationaleDialog(R.string.dm_requset_camera, request);
     * show("22====相机请求权限对话框");
     * }
     * <p>
     * private void show(String string) {
     * Toast.makeText(Act_CallIncoming.this, string, Toast.LENGTH_SHORT).show();
     * }
     * @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
     * if (requestCode == REQUEST_CODE_REQUEST_PERMISSION) {
     * if (PermissionUtils.getTargetSdkVersion(this) < 23 && !PermissionUtils.hasSelfPermissions(this, PERMISSION_SHOWCAMERA)) {
     * onCameraDenied();
     * return;
     * }
     * if (PermissionUtils.verifyPermissions(grantResults)) {
     * answer();
     * } else {
     * if (!PermissionUtils.shouldShowRequestPermissionRationale(this, PERMISSION_SHOWCAMERA)) {
     * onCameraNeverAskAgain();
     * //                    show("44相机再次请求，弹出对话框");
     * } else {
     * onCameraDenied();
     * //                    show("33-22相机启动失败");
     * }
     * }
     * }
     * }
     * <p>
     * <p>
     * private  final class ShowCameraPermissionRequest implements PermissionRequest {
     * private final WeakReference<Activity> weakTarget;
     * <p>
     * private ShowCameraPermissionRequest(Activity target) {
     * this.weakTarget = new WeakReference<>(target);
     * }
     * @Override public void proceed() {
     * Activity target = weakTarget.get();
     * if (target == null) return;
     * ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);
     * }
     * @Override public void cancel() {
     * Activity target = weakTarget.get();
     * if (target == null) return;
     * //            target.onCameraDenied();
     * }
     * }
     * <p>
     * <p>
     * private void showRationaleDialog(@StringRes int messageResId, final PermissionRequest request) {
     * new AlertDialog.Builder(this)
     * .setPositiveButton("允许", new DialogInterface.OnClickListener() {
     * @Override public void onClick(@NonNull DialogInterface dialog, int which) {
     * request.proceed();
     * }
     * })
     * .setNegativeButton("不允许", new DialogInterface.OnClickListener() {
     * @Override public void onClick(@NonNull DialogInterface dialog, int which) {
     * request.cancel();
     * }
     * })
     * .setCancelable(false)
     * .setMessage(messageResId)
     * .show();
     * }
     */

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.ll_answer || id == R.id.accept) {
//			requestPermissAndAnswer();
            answer();
        } else if (id == R.id.ll_ignore || id == R.id.decline) {
            decline();
        }
    }

}