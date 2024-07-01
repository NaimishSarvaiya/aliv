package com.iotsmartaliv.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.intelligoo.sdk.LibDevModel;
import com.iotsmartaliv.R;
import com.iotsmartaliv.apiCalling.listeners.RetrofitListener;
import com.iotsmartaliv.apiCalling.models.DeviceObject;
import com.iotsmartaliv.apiCalling.models.ErrorObject;
import com.iotsmartaliv.apiCalling.retrofit.ApiServiceProvider;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.modules.cardManager.CardManagerActivity;
import com.iotsmartaliv.utils.ErrorMsgDoorMasterSDK;
import com.iotsmartaliv.utils.Util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.iotsmartaliv.adapter.DevicelistAdapter.selectDevice;
import static com.iotsmartaliv.constants.Constant.COMMUNITY_ID;
import static com.iotsmartaliv.constants.Constant.DEVICE_ID;
import static com.iotsmartaliv.constants.Constant.LOGIN_DETAIL;

/**
 * This activity class is used to look device-details.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2019-01-16
 */
public class DeviceDetailActivity extends AppCompatActivity implements RetrofitListener<String> {
    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.deviceNameValueTv)
    public TextView tvDeviceName;
    @BindView(R.id.deviceModelValueTv)
    public TextView deviceModelValueTv;
    @BindView(R.id.deviceTypeValueTv)
    public TextView deviceTypeValueTv;
    @BindView(R.id.serialNumberTv)
    public TextView tvSerialNumber;
    @BindView(R.id.deviceValidity)
    public TextView tvDeviceValidity;
    ApiServiceProvider apiServiceProvider;
    /* @BindView(R.id.ll_visitor_auth)
     LinearLayout llVisitorAuth;*/
    @BindView(R.id.ll_time_sync)
    LinearLayout llTimeSync;
    @BindView(R.id.ll_card_manager)
    LinearLayout llCardManager;
    @BindView(R.id.ll_key_manager)
    LinearLayout llKeyManager;
    @BindView(R.id.ll_device_setting)
    LinearLayout llDeviceSetting;
    @BindView(R.id.ll_config_read_sector_key)
    LinearLayout llConfigReadSectorKey;
    @BindView(R.id.lay_device_ip)
    LinearLayout layDeviceIp;
    @BindView(R.id.lay_wiegant)
    LinearLayout layWiegant;
    String communityId, deviceId;
    ProgressDialog progress;


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

  /*  @OnClick(R.id.ll_visitor_auth)
    void clickVisitorAuthorization() {
        startActivity(new Intent(this, VisitorAuthorizationActivity.class));
    }*/

    @OnClick(R.id.ll_card_manager)
    void clickCardManager() {
        Intent intent = new Intent(this, CardManagerActivity.class);
        intent.putExtra(DEVICE_ID, deviceId);
        intent.putExtra(COMMUNITY_ID, communityId);
        startActivity(intent);
        //startActivity(new Intent(this, CardManagerActivity.class));
    }

    @OnClick(R.id.ll_key_manager)
    void clickKeyManager() {
        startActivity(new Intent(this, ManagerKeyActivity.class));
    }

    @OnClick(R.id.ll_device_setting)
    void clickDeviceSettings() {
        startActivity(new Intent(this, DeviceSettingActivity.class));
    }

    @OnClick(R.id.ll_time_sync)
    public void onViewClicked() {

            Util.checkInternet(DeviceDetailActivity.this, new Util.NetworkCheckCallback() {
                @Override
                public void onNetworkCheckComplete(boolean isAvailable) {
                    if (isAvailable){
                        apiServiceProvider.callForGetCurrentServerTime(DeviceDetailActivity.this);
                    }
                }
            });

    }

    @Override
    public void onResponseSuccess(String sucessRespnse, String apiFlag) {
        switch (apiFlag) {
            case Constant.UrlPath.SERVERTIMESYNC:
                if (sucessRespnse.length() > 0) {
                    // Toast.makeText(this, sucessRespnse, Toast.LENGTH_LONG).show();
                    syncronazationOnCurrentDevice(sucessRespnse);
                } else {
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
        switch (apiFlag) {
            case Constant.UrlPath.SERVERTIMESYNC:
                Util.firebaseEvent(Constant.APIERROR, DeviceDetailActivity.this,Constant.UrlPath.SERVER_URL+apiFlag, LOGIN_DETAIL.getUsername(),LOGIN_DETAIL.getAppuserID(),errorObject.getStatus());

                try {
                    Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        apiServiceProvider = ApiServiceProvider.getInstance(this);
        deviceTypeValueTv.setText(selectDevice.getDeviceType());
        tvDeviceName.setText(selectDevice.getCdeviceName());
        communityId = getIntent().getStringExtra(Constant.COMMUNITY_ID);
        deviceId = getIntent().getStringExtra(Constant.DEVICE_ID);
        if (selectDevice.getCdeviceName().length() > 0) {
            tvDeviceName.setText(selectDevice.getCdeviceName());
        } else {
            tvDeviceName.setText(selectDevice.getDeviceName());
        }
        tvSerialNumber.setText(selectDevice.getDeviceSno());
        deviceModelValueTv.setText(selectDevice.getDeviceModel());

        if (selectDevice.getIsAccessTimeEnabled().equalsIgnoreCase("1")){

            tvDeviceValidity.setText(convertServerDateToUserTimeZone(selectDevice.getAccessStarttime())
                    +"\n"+convertServerDateToUserTimeZone(selectDevice.getAccessEndtime()));

        }else {
            tvDeviceValidity.setText("Forever");
        }



        boolean isSubAdmin = false;
     /*   for (String rolid : LOGIN_DETAIL.getRoleIDs()) {
            if (rolid.equalsIgnoreCase("1")) {
                isSubAdmin = true;
                break;
            }
        }
        if (LOGIN_DETAIL.getAppuserType().equalsIgnoreCase("1") || isSubAdmin) {*/
        if (selectDevice.getAdminDevice() == 1) {
            // llDeviceSetting.setVisibility(View.VISIBLE);
            //  llKeyManager.setVisibility(View.VISIBLE);
            if (selectDevice.getNetworking().equalsIgnoreCase("0")) {
                llTimeSync.setVisibility(View.VISIBLE);
                //   llVisitorAuth.setVisibility(View.VISIBLE);
                if (selectDevice.getDeviceModel().contains("100E")) {
                    llCardManager.setVisibility(View.VISIBLE);
                }
                //llConfigReadSectorKey.setVisibility(View.VISIBLE);
            } else {
                //  layWiegant.setVisibility(View.VISIBLE);
                layDeviceIp.setVisibility(View.VISIBLE);
            }
        } else {
            if (selectDevice.getNetworking().equalsIgnoreCase("0")) {
                // llVisitorAuth.setVisibility(View.VISIBLE);
            }
        }
    }



    private String convertServerDateToUserTimeZone(String serverDate) {
        String serverdateFormat = "yyyy-MM-dd HH:mm:ss";
        String ourdate;
        try {

            SimpleDateFormat formatter = new SimpleDateFormat(serverdateFormat, Locale.UK);
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date value = formatter.parse(serverDate);
            TimeZone timeZone = TimeZone.getDefault();
            SimpleDateFormat dateFormatter = new SimpleDateFormat(serverdateFormat, Locale.UK); //this format changeable
            dateFormatter.setTimeZone(timeZone);
            ourdate = dateFormatter.format(value);

            //Log.d("OurDate", OurDate);
        } catch (Exception e) {
            ourdate = "0000-00-00 00:00:00";
        }
        return ourdate;
    }

    public void syncronazationOnCurrentDevice(String time) {
        progress = new ProgressDialog(this);
        progress.setMessage("Processing.....");
        progress.setCancelable(false);
        progress.show();
        LibDevModel libDev = DeviceObject.getLibDev(selectDevice);
        int ret = LibDevModel.syncDeviceTime(this, libDev, time, (result, bundle) -> runOnUiThread(() -> {
            progress.dismiss();

            if (result == 0x00) {
                Toast.makeText(DeviceDetailActivity.this, "Sync Device Time Successfully.", Toast.LENGTH_SHORT).show();
            } else {
                String test_str = "failed result:" + ErrorMsgDoorMasterSDK.getErrorMsg(result);
//							Constants.tips(result);
                Toast.makeText(DeviceDetailActivity.this, test_str, Toast.LENGTH_SHORT).show();

            }
        }));
        if (ret != 0) {
            progress.dismiss();
            Toast.makeText(this, "Error:" + ret, Toast.LENGTH_SHORT).show();
        }

    }

    @OnClick(R.id.lay_device_ip)
    public void onViewClickedDeviceIP() {
        startActivity(new Intent(this, DeviceIPConfigActivity.class));

    }
}