package com.iotsmartaliv.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.intelligoo.sdk.LibDevModel;
import com.intelligoo.sdk.ScanCallback;
import com.iotsmartaliv.adapter.OpenRemotelyAdapter;
import com.iotsmartaliv.apiAndSocket.models.DeviceObject;
import com.iotsmartaliv.databinding.ActivityOpenDoorRemotelyBinding;

import java.util.ArrayList;


import static com.iotsmartaliv.constants.Constant.deviceLIST;

/**
 * This activity class is used for opening the Door remotely.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2018-10-23
 */
public class OpenDoorRemotelyActivity extends AppCompatActivity {
    public static int flag = 0;
    private OpenRemotelyAdapter myAdapter;
    ActivityOpenDoorRemotelyBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOpenDoorRemotelyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        myAdapter = new OpenRemotelyAdapter(this, deviceLIST);
        binding.lvDevice.setAdapter(myAdapter);
        swipeToRefresh();

        binding.imgBackOpenrDoor.setOnClickListener(v->onClickBack() );
        binding.tvRefresh.setOnClickListener(v-> onClickRefresh());
    }

    /**
     * This method is used for swipe to refresh
     */
    private void swipeToRefresh() {
        if (deviceLIST == null || deviceLIST.size() == 0)
            return;
        refreshScanList();

        // Get the last device to test
        DeviceObject dev = deviceLIST.get(0);
        String devSn = dev.getDeviceSno();
    }

    /**
     * This method is used for refresh scan list.
     */
    private void refreshScanList() {
        ScanCallback callback = new ScanCallback() {
            @Override
            public void onScanResult(final ArrayList<String> deviceList, ArrayList<Integer> rssi) {
//                Log.e("ScanCallback", "size=" + deviceList.size() + "," + deviceList.toString() + "," + rssi.toString());
//                Log.e(deviceList.get(0), deviceList.get(0) + " :[" + rssi + "]");
            }

            @Override
            public void onScanResultAtOnce(final String devSn, int rssi) {
                Log.e(devSn, devSn + " :[" + rssi + "]");
                runOnUiThread(new Runnable() {
                    public void run() {
                        if (deviceLIST == null || deviceLIST.isEmpty()) {
                            return;
                        }
                        for (DeviceObject device : deviceLIST) {
                            if (device.getDeviceSno().equalsIgnoreCase(devSn)) {
                                myAdapter.sortList(device);
                            }
                        }
                    }
                });
            }
        };
        int ret = LibDevModel.scanDevice(OpenDoorRemotelyActivity.this, true, 2000, callback);
        if (ret != 0x00) {
//            Toast.makeText(OpenDoorRemotelyActivity.this, "" + ret, Toast.LENGTH_SHORT).show();
            Toast.makeText(OpenDoorRemotelyActivity.this, "No nearby device found.", Toast.LENGTH_SHORT).show();
            myAdapter = new OpenRemotelyAdapter(this, deviceLIST);
            binding.lvDevice.setAdapter(myAdapter);
        }
        Log.e("ret", "ret" + ret);
    }

    void onClickBack() {
        flag = 0;
        onBackPressed();
    }

    void onClickRefresh() {
        swipeToRefresh();
    }
}