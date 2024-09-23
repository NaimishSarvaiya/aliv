package com.iotsmartaliv.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.intelligoo.sdk.LibDevModel;
import com.intelligoo.sdk.LibInterface;
import com.intelligoo.sdk.ScanCallback;
import com.iotsmartaliv.R;
import com.iotsmartaliv.apiAndSocket.models.DeviceObject;
import com.iotsmartaliv.roomDB.AccessLogModel;
import com.iotsmartaliv.utils.ErrorMsgDoorMasterSDK;
import com.iotsmartaliv.utils.SaveAccessLogTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.iotsmartaliv.apiAndSocket.models.DeviceObject.getLibDev;
import static com.iotsmartaliv.constants.Constant.deviceLIST;


/**
 * This activity class is used for opening the Door.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2018-10-23
 */
public class OpenDoorActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    /**
     * Callback method to check scanned device and to open the nearest device lock on click.
     */
    String openingDoorDeviceSN;
    private RelativeLayout relScan, relOPenDoorRemotely;
    private ImageView imgBackOpenDoor, ivUnlock;
    private boolean pressed = false;
    /**
     * Callback method to open the door successfully.
     */
    final LibInterface.ManagerCallback callback = new LibInterface.ManagerCallback() {
        @Override
        public void setResult(final int result, Bundle bundle) {
            runOnUiThread(() -> {
                pressed = false;
                //mHandler.sendEmptyMessage(OPEN_AGAIN);
                if (result == 0x00) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
                    new SaveAccessLogTask(OpenDoorActivity.this, new AccessLogModel("", openingDoorDeviceSN, "device list", dateFormat.format(new Date()))).execute();
                    Toast.makeText(OpenDoorActivity.this, "Door open successfully.", Toast.LENGTH_SHORT).show();
                } else {
                    if (result == 48) {
                        Toast.makeText(OpenDoorActivity.this, "Result Error Time Out", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(OpenDoorActivity.this, "Failure:" + result, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    };
    private ProgressDialog progress;
    private Map<String, DeviceObject> tempDevDic = new HashMap<String, DeviceObject>();
    private ArrayList<DeviceObject> permitedList = new ArrayList<DeviceObject>();
    private ArrayList<Integer> permitedRssiList = new ArrayList<Integer>();
    ScanCallback oneKeyScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(ArrayList<String> deviceList,
                                 ArrayList<Integer> rssi) {
            progress.dismiss();
            permitedList.clear();
            permitedRssiList.clear();
            if (deviceList.size() == 0) {
                pressed = false;
                Toast.makeText(OpenDoorActivity.this, "Scan 0 device", Toast.LENGTH_SHORT).show();
                return;
            }
            for (int i = 0; i < deviceList.size(); i++) {
                for (int j = 0; j < deviceLIST.size(); j++) {
                    if (deviceList.get(i).equals(deviceLIST.get(j).getDeviceSno())) {
                        permitedList.add(deviceLIST.get(j));
                        permitedRssiList.add(rssi.get(i));
                    }
                }
            }
            if (permitedList.size() == 0) {
                pressed = false;
                Toast.makeText(OpenDoorActivity.this, "No Permitted", Toast.LENGTH_SHORT).show();
                return;
            }

            Integer max = permitedRssiList.get(0);
            int maxIndex = 0;
            //Get most-nearest device to open the door by using rssi-strength.
            for (int i = 1; i < permitedRssiList.size(); i++) {
                if (max < permitedRssiList.get(i)) {
                    max = permitedRssiList.get(i);
                    maxIndex = i;
                }
            }
            pressed = false;
            DeviceObject dev = permitedList.get(maxIndex);
            System.out.println(dev.getDeviceSno());
            if (pressed) {
                Toast.makeText(OpenDoorActivity.this, "Operationing...",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            pressed = true;
            LibDevModel libDev = getLibDev(dev);
            openingDoorDeviceSN = deviceLIST.get(0).getDeviceSno();

            int ret = LibDevModel.openDoor(OpenDoorActivity.this, libDev, callback);
            if (ret == 0) {
                return;
            } else {
                pressed = false;
                Toast.makeText(OpenDoorActivity.this,ErrorMsgDoorMasterSDK.getErrorMsg(ret), Toast.LENGTH_SHORT).show();
            }


         /*   int ret = LibDevModel.controlDevice(OpenDoorActivity.this, 0x00, libDev, null, callback);
            if (ret == 0) {
                return;
            } else {
                pressed = false;
                Toast.makeText(OpenDoorActivity.this, "RET：" + ret, Toast.LENGTH_SHORT).show();
            }*/
        }

        @Override
        public void onScanResultAtOnce(final String devSn, int rssi) {
            pressed = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_door);
        initView();
        initListener();

        progress = new ProgressDialog(this);
        progress.setMessage("Door Open On Process");
        progress.setCancelable(false);

    }

    /**
     * Initialize views.
     */
    private void initView() {
        relScan = findViewById(R.id.rel_scan);
        relOPenDoorRemotely = findViewById(R.id.rel_open);
        imgBackOpenDoor = findViewById(R.id.img_back_open_door);
        ivUnlock = findViewById(R.id.iv_unlock);
    }

    /**
     * Initialize listener.
     */
    private void initListener() {
        relScan.setOnClickListener(this);
        relOPenDoorRemotely.setOnClickListener(this);
        imgBackOpenDoor.setOnClickListener(this);
        ivUnlock.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rel_scan:
                startActivity(new Intent(OpenDoorActivity.this, QRCodeScanActivity.class));
                break;
            case R.id.rel_open:
                if (deviceLIST.size() == 0) {
                    Thread getDevList_th = new Thread(() -> {
                        Toast.makeText(OpenDoorActivity.this, "Pending Task", Toast.LENGTH_SHORT).show();
                        //todo When Open Remotely
                        //  getDeviceList();
                    });
                    getDevList_th.start();
                } else {
                    startActivity(new Intent(this, OpenDoorRemotelyActivity.class));
                }
                break;
            case R.id.img_back_open_door:
                onBackPressed();
                break;
            case R.id.iv_unlock:
                if (pressed) {
                    Toast.makeText(OpenDoorActivity.this, "Operationing...", Toast.LENGTH_SHORT).show();
                    return;
                }
                progress.show();
                int ret1 = LibDevModel.scanDevice(OpenDoorActivity.this, true, 1300, oneKeyScanCallback);         // A key to open the door
                pressed = true;
                if (ret1 != 0) {
                    Toast.makeText(OpenDoorActivity.this, ErrorMsgDoorMasterSDK.getErrorMsg(ret1), Toast.LENGTH_SHORT).show();
                    pressed = false;
                    progress.dismiss();
                }
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    /**
     * This method is used for getting device list
     *//*
    private void getDeviceList() {
        String clientId = SharePreference.getInstance(this).getString("CLIENTID");
        try {
            devList = Request.reqDeviceList(clientId);
            if (devList == null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(OpenDoorActivity.this, "No device configured.", Toast.LENGTH_SHORT);
                    }
                });
                devList = new ArrayList<DeviceBean>();
                tempDevDic = new HashMap<String, DeviceBean>();
            } else {
                for (DeviceBean devBean : devList) {
                    tempDevDic.put(devBean.getDevSn(), devBean);
                }
            }
            startActivity(new Intent(this, OpenDoorRemotelyActivity.class));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }*/
}