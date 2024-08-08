package com.iotsmartaliv.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.intelligoo.sdk.ConstantsUtils;
import com.intelligoo.sdk.LibDevModel;
import com.iotsmartaliv.R;
import com.iotsmartaliv.databinding.ActivityDeviceIpconfigBinding;
import com.iotsmartaliv.utils.ErrorMsgDoorMasterSDK;



import static com.iotsmartaliv.adapter.DevicelistAdapter.selectDevice;
import static com.iotsmartaliv.apiCalling.models.DeviceObject.getLibDev;

/**
 * This class is use for Device IP Config of the device.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 3/5/19 :May : 2019 on 14 : 53.
 */

public class DeviceIPConfigActivity extends AppCompatActivity {
    private ProgressDialog mProgressDialog;

    ActivityDeviceIpconfigBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDeviceIpconfigBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        ButterKnife.bind(this);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Server Ip Config on Process...");
        mProgressDialog.setCanceledOnTouchOutside(false);
    }

    public void configClick(View view) {
        String serverIP = binding.edServerIp.getText().toString().trim();
        String serverPORT = binding.edPort.getText().toString().trim();
        if (serverIP.length() > 0 || serverPORT.length() > 0) {
            mProgressDialog.show();
            Bundle bundle = new Bundle();
            bundle.putString(ConstantsUtils.SERVER_IP, serverIP);
            bundle.putInt(ConstantsUtils.SERVER_PORT, Integer.parseInt(serverPORT));
            LibDevModel libDev = getLibDev(selectDevice);
            int configWifiRet = LibDevModel.setServerIP(this, libDev, bundle, (result, bundle1) -> {
                runOnUiThread(() -> {
                    if (result == 0x00) {
                        Toast.makeText(DeviceIPConfigActivity.this, "IP config successfully.",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(DeviceIPConfigActivity.this, ErrorMsgDoorMasterSDK.getErrorMsg(result),
                                Toast.LENGTH_SHORT).show();
                    }
                });
                mProgressDialog.dismiss();
            });

            if (configWifiRet != 0) {
                mProgressDialog.dismiss();
                Toast.makeText(this, "config_IP_fail :" + configWifiRet, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void goBack(View view) {
        onBackPressed();
    }
}
