package com.iotsmartaliv.dmvphonedemotest;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
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

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE_MAIN = 999;
    private static final int CODE_REQUEST = 921;
    public static boolean isRegSuccess = false;
    public static Handler mhandler;
    private TextView tv_status;
    private EditText et_account;
    private EditText et_password;
    private EditText et_call_account;
    private EditText et_add_account;
    private EditText et_remove_account;
    private EditText et_login_type;
    private EditText et_call_type;
    private String account = "";
    private String password;
    private String callAccount;
    private int login_type;
    private int call_type;
    private DMCallback loginCallback = new DMCallback() {
        @Override
        public void setResult(int errorCode, DMException e) {
            Log.e("loginCallback main", "errorCode=" + errorCode);
            if (e == null) {
                Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "登录失败，errorCode=" + errorCode + ",e=" + e.toString()
                        , Toast.LENGTH_SHORT).show();
            }
        }
    };
    private DMCallback statusCallback = new DMCallback() {
        @Override
        public void setResult(int errorCode, DMException e) {
            if (e == null) {
                Log.e("statusCallback main", getResources().getString(R.string.status_connected));
                tv_status.setText(getResources().getString(R.string.status_connected));
            } else if (errorCode == DMErrorReturn.ERROR_RegistrationProgress) {
                Log.e("statusCallback main", getResources().getString(R.string.status_in_progress));
                tv_status.setText(getResources().getString(R.string.status_in_progress));
            } else if (errorCode == DMErrorReturn.ERROR_RegistrationFailed) {
                Log.e("statusCallback main", getResources().getString(R.string.status_error));
                tv_status.setText(getResources().getString(R.string.status_error));
            } else {
                Log.e("statusCallback main", getResources().getString(R.string.status_not_connected));
                tv_status.setText(getResources().getString(R.string.status_not_connected));
            }
        }
    };
    private DMCallStateListener callStateListener = new DMCallStateListener() {

        @Override
        public void callState(DMCallState state, String message) {

            Log.d("CallStateLis main", "value=" + state.value() + ",message=" + message);

            if (state == DMCallState.IncomingReceived) {

//                intent.putExtra("type", 1);
//                startActivity(intent);
            } else if (state == DMCallState.OutgoingInit) {
                Intent intent = new Intent(MainActivity.this, DmCallOutgoingActivity.class);
                startActivity(intent);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化SDK在BaseApplication里面
        //呼叫类型



    /*    tv_status = findViewById(R.id.tv_status);
        et_account = findViewById(R.id.et_account);
        et_password = findViewById(R.id.et_password);
        et_call_account = findViewById(R.id.et_call_account);
        et_add_account = findViewById(R.id.et_add_account);
        et_remove_account = findViewById(R.id.et_remove_account);
        et_login_type = findViewById(R.id.et_login_type);
        et_call_type = findViewById(R.id.et_call_type);
        Button loginBtn = findViewById(R.id.btn_login);
        loginBtn.setOnClickListener(this);
        findViewById(R.id.btn_call).setOnClickListener(this);
        findViewById(R.id.btn_answer).setOnClickListener(this);
        findViewById(R.id.btn_refuse).setOnClickListener(this);
        findViewById(R.id.btn_logout).setOnClickListener(this);
        findViewById(R.id.btn_add_account).setOnClickListener(this);
        findViewById(R.id.btn_remove_account).setOnClickListener(this);*/

        SharedPreferences sharedPre = this.getSharedPreferences("PREFS_CONF",
                Context.MODE_PRIVATE);
        String str = sharedPre.getString("account", "");
        String call_str = sharedPre.getString("callAccount", "");
        call_type = sharedPre.getInt("callAccountType", 1);
        if (str.length() > 0) {
            et_account.setText(str);
        } else {
            et_account.setText("13986001110");
        }
        if (call_str.length() > 0) {
            et_call_account.setText(call_str);
        } else {
            et_call_account.setText("18816764052");
        }
//        et_call_account.setText("roc020x55z");

//        et_call_account.setText(call_str);
//        et_call_account.setText("442c05e66fdd");//演示架13寸
//        et_call_account.setText("00000213058185319");//演示架13寸
//        et_call_account.setText("V0348226811");//V500
//        et_call_type.setText("2");
//        et_call_account.setText("liukebing@foxmail.com");
//        et_call_account.setText("13929528100");
//        et_call_account.setText("13533754937");
//        et_call_account.setText("18816764052");
        et_call_type.setText(String.valueOf(call_type));

        Toolbar toolbar = findViewById(R.id.toolbar);
        mhandler = new Handler();
//        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
//                Intent intent = new Intent(MainActivity.this, Act_Welcom.class);
//                startActivity(intent);
            }
        });

        requestPermissiontest();
        //自定义来电时的接听界面
        DMVPhoneModel.setActivityToLaunchOnIncomingReceived(DmCallIncomingActivity.class);

        DMVPhoneModel.addMsgListener(new DMPhoneMsgListener() {
            @Override
            public void messageReceived(String msg) {
                Log.d(TAG, "addMsgListener msg=" + msg);
                Log.d(TAG, "addMsgListener msg.length=" + msg.length());
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
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

        DMVPhoneModel.setLogSwitch(true);
        DMVPhoneModel.addLoginCallBack(statusCallback);

        Log.e(TAG, "version=" + DMVPhoneModel.getVersion());


    }
/*
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                login();
                mhandler.postDelayed(new Runnable() {
                    public void run() {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (imm != null && getCurrentFocus() != null) {
                            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                        }
                    }
                }, 100);

                SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences("PREFS_CONF",
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.putString("account", account);
                edit.apply();

                break;
            case R.id.btn_call:

                call();
                SharedPreferences settings = MainActivity.this.getSharedPreferences("PREFS_CONF",
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor edtor = settings.edit();
                edtor.putString("callAccount", callAccount);
                edtor.putInt("callAccountType", call_type);
                edtor.apply();
                break;
            case R.id.btn_add_account:
                String addAccount = et_add_account.getText().toString().trim();
                if (TextUtils.isEmpty(addAccount)) {
                    Toast.makeText(this, R.string.account_cannot_be_empty, Toast.LENGTH_SHORT).show();
                    return;
                }
                DMVPhoneModel.addAccountToBlackList(this, addAccount, 2);
                break;
            case R.id.btn_remove_account:
                String removeAccount = et_remove_account.getText().toString().trim();
                if (TextUtils.isEmpty(removeAccount)) {
                    Toast.makeText(this, R.string.account_cannot_be_empty, Toast.LENGTH_SHORT).show();
                    return;
                }
                DMVPhoneModel.removeAccountFromBlackList(this, removeAccount);
                break;
            case R.id.btn_answer:
                DMVPhoneModel.answerCall();
                break;
            case R.id.btn_refuse:
                DMVPhoneModel.refuseCall();
                break;
            case R.id.btn_logout:
                DMVPhoneModel.exit();
                break;
            default:
                break;
        }
    }*/

    /**
     * 登录
     */
    private void login() {
        account = et_account.getText().toString().trim();
        password = et_password.getText().toString().trim();
        String type = et_login_type.getText().toString().trim();
        if (TextUtils.isEmpty(type)) {
            Toast.makeText(this, R.string.type_is_wrong, Toast.LENGTH_SHORT).show();
            return;
        }
        login_type = Integer.parseInt(type);
        if (TextUtils.isEmpty(account)) {
            Toast.makeText(this, R.string.account_cannot_be_empty, Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, R.string.password_cannot_be_empty, Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d(TAG, "account=" + account + ",password=" + password);

        String token;
        if (account.equals("18816764052")) {
            token = "ceb2bd26cb33c4d8cce85a85a0fdaf7bd13ea20d";
        } else if (account.equals("13533754937")) {
            token = "22539ebb31d7c0383fc4e81c43334d477ae94703";
        } else if (account.equals("13986001110")) {
            token = "1c168bd8f8f4dc8L5e8ca85a3e1c68d0fc922b3a";
        } else {
            token = password;
        }
        //参数：呼叫号码、sdk-token、呼叫类型、上下文、回调callBack
        // DMVPhoneModel.loginVPhoneServer(account, token, login_type, this, loginCallback);
        DMVPhoneModel.loginVPhoneServer("18june@mailinator.com", "179f76dLeb2c9a8ea72aa895f427f8c745b73490", login_type, this, loginCallback);

//        DMVPhoneModel.setCameraId(1,this);

        //720p(1280*720),vga(640*480),qvga(320*240),qcif(176*144)
//        DMVPhoneModel.setVideoSize("vga",this);
//        DMVPhoneModel.setVideoRotation(90, this);
    }

    /**
     * 呼叫
     */
    public void call() {
        callAccount = et_call_account.getText().toString().trim();
        String type = et_call_type.getText().toString().trim();
        if (TextUtils.isEmpty(type)) {
            Toast.makeText(this, R.string.type_is_wrong, Toast.LENGTH_SHORT).show();
            return;
        }
        call_type = Integer.parseInt(type);
        if (TextUtils.isEmpty(callAccount)) {
            Toast.makeText(this, R.string.call_account_cannot_be_empty, Toast.LENGTH_SHORT).show();
            return;
        }
        //参数：帐号、类型、上下文
        // DMVPhoneModel.callAccount(callAccount, call_type, this, account);
        DMVPhoneModel.callAccount("V4112562222", 2, this, account);
//        DMVPhoneModel.callAccount(callAccount, "");
//        DMVPhoneModel.callAccount("00000218816764052", "");
    }

    public void addToList() {
        String addAccount = et_add_account.getText().toString().trim();
        if (TextUtils.isEmpty(callAccount)) {
            Toast.makeText(this, R.string.caller_cannot_be_empty, Toast.LENGTH_SHORT).show();
            return;
        }
//        DMVoipModel.setCallAccountDict(addAccount);
    }

    @Override
    protected void onPause() {
        super.onPause();
        DMVPhoneModel.removeCallStateListener(callStateListener);
    }

    /**
     * 请求权限
     */
    public void requestPermissiontest() {
        // you needer permissions
        String[] permissions = {
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA};
        // check it is needed
        permissions = CheckPermissionUtils.getNeededPermission(MainActivity.this, permissions);
        // requestPermissions
        if (permissions.length > 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissions, REQUEST_CODE_MAIN);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_MAIN:
                Log.d(TAG, "grantResults=" + Arrays.toString(grantResults));
                if (grantResults.length > 0) {
                    return;
                }
                if (!CheckPermissionUtils.isNeedAddPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO)) {
                    Toast.makeText(MainActivity.this, "申请权限成功:" + Manifest.permission.RECORD_AUDIO, Toast.LENGTH_LONG).show();
                }
                if (!CheckPermissionUtils.isNeedAddPermission(MainActivity.this, Manifest.permission.CAMERA)) {
                    Toast.makeText(MainActivity.this, "申请权限成功:" + Manifest.permission.CAMERA, Toast.LENGTH_LONG).show();
                }
                break;

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {

    }
}
