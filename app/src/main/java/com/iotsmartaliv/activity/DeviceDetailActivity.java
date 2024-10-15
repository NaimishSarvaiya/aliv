package com.iotsmartaliv.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Toast;

import com.intelligoo.sdk.LibDevModel;
import com.iotsmartaliv.apiAndSocket.listeners.RetrofitListener;
import com.iotsmartaliv.apiAndSocket.models.DeviceObject;
import com.iotsmartaliv.apiAndSocket.models.ErrorObject;
import com.iotsmartaliv.apiAndSocket.retrofit.ApiServiceProvider;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.databinding.ActivityDeviceDetailBinding;
import com.iotsmartaliv.modules.cardManager.CardManagerActivity;
import com.iotsmartaliv.utils.ErrorMsgDoorMasterSDK;
import com.iotsmartaliv.utils.Util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static com.iotsmartaliv.adapter.DevicelistAdapter.selectDevice;
import static com.iotsmartaliv.constants.Constant.LOGIN_DETAIL;

/**
 * This activity class is used to look device-details.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2019-01-16
 */
public class DeviceDetailActivity extends AppCompatActivity implements RetrofitListener<String> {

    ActivityDeviceDetailBinding binding;

    ApiServiceProvider apiServiceProvider;
    /* @BindView(R.id.ll_visitor_auth)
     LinearLayout llVisitorAuth;*/
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
        binding = ActivityDeviceDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        apiServiceProvider = ApiServiceProvider.getInstance(this,false);
        binding.deviceTypeValueTv.setText(selectDevice.getDeviceType());
        binding.deviceNameValueTv.setText(selectDevice.getCdeviceName());
        communityId = getIntent().getStringExtra(Constant.COMMUNITY_ID);
        deviceId = getIntent().getStringExtra(Constant.DEVICE_ID);
        if (selectDevice.getCdeviceName().length() > 0) {
            binding.deviceNameValueTv.setText(selectDevice.getCdeviceName());
        } else {
            binding.deviceNameValueTv.setText(selectDevice.getDeviceName());
        }
        binding.serialNumberTv.setText(selectDevice.getDeviceSno());
        binding.deviceModelValueTv.setText(selectDevice.getDeviceModel());

        if (selectDevice.getIsAccessTimeEnabled().equalsIgnoreCase("1")){

            binding.deviceValidity.setText(convertServerDateToUserTimeZone(selectDevice.getAccessStarttime())
                    +"\n"+convertServerDateToUserTimeZone(selectDevice.getAccessEndtime()));

        }else {
            binding.deviceValidity.setText("Forever");
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
                binding.llTimeSync.setVisibility(View.VISIBLE);
                //   llVisitorAuth.setVisibility(View.VISIBLE);
                if (selectDevice.getDeviceModel().contains("100E")) {
                    binding.llCardManager.setVisibility(View.VISIBLE);
                }
                //llConfigReadSectorKey.setVisibility(View.VISIBLE);
            } else {
                //  layWiegant.setVisibility(View.VISIBLE);
                binding.layDeviceIp.setVisibility(View.VISIBLE);
            }
        } else {
            if (selectDevice.getNetworking().equalsIgnoreCase("0")) {
                // llVisitorAuth.setVisibility(View.VISIBLE);
            }
        }


        binding.llCardManager.setOnClickListener(v ->
            clickCardManager()
        );
       binding.llKeyManager.setOnClickListener(v->
           clickKeyManager()
       );

       binding.llDeviceSetting.setOnClickListener(v ->
           clickDeviceSettings()
       );

       binding.llTimeSync.setOnClickListener(v->
           Util.checkInternet(DeviceDetailActivity.this, new Util.NetworkCheckCallback() {
               @Override
               public void onNetworkCheckComplete(boolean isAvailable) {
                   if (isAvailable){
                       apiServiceProvider.callForGetCurrentServerTime(DeviceDetailActivity.this);
                   }
               }
           })
       );

       binding.layDeviceIp.setOnClickListener(v ->
               startActivity(new Intent(this, DeviceIPConfigActivity.class)));

    }


    private void clickCardManager() {
        Intent intent = new Intent(this, CardManagerActivity.class);
        intent.putExtra(Constant.DEVICE_ID, deviceId);
        intent.putExtra(Constant.COMMUNITY_ID, communityId);
        startActivity(intent);
    }

    private void clickKeyManager() {
        startActivity(new Intent(this, ManagerKeyActivity.class));
    }

    private void clickDeviceSettings() {
        startActivity(new Intent(this, DeviceSettingActivity.class));
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


}
