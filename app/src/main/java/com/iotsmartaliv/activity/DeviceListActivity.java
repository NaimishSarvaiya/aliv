package com.iotsmartaliv.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.intelligoo.sdk.LibDevModel;
import com.intelligoo.sdk.ScanCallback;
import com.iotsmartaliv.BuildConfig;
import com.iotsmartaliv.adapter.DevicelistAdapter;
import com.iotsmartaliv.apiAndSocket.listeners.RetrofitListener;
import com.iotsmartaliv.apiAndSocket.models.DeviceObject;
import com.iotsmartaliv.apiAndSocket.models.ErrorObject;
import com.iotsmartaliv.apiAndSocket.models.SuccessDeviceListResponse;
import com.iotsmartaliv.apiAndSocket.retrofit.ApiServiceProvider;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.databinding.ActivityDeviceListBinding;
import com.iotsmartaliv.roomDB.DatabaseClient;
import com.iotsmartaliv.utils.Util;
import com.iotsmartaliv.utils.faceenroll.ConnectionManager;

import java.util.ArrayList;
import java.util.List;

import static com.iotsmartaliv.constants.Constant.LOGIN_DETAIL;
import static com.iotsmartaliv.constants.Constant.deviceLIST;
import static com.iotsmartaliv.constants.Constant.hideLoader;

public class DeviceListActivity extends AppCompatActivity implements RetrofitListener<SuccessDeviceListResponse> {
    public static int flagDeviceList = 0;
    private static final int REQUEST_BLUETOOTH_PERMISSIONS = 1001;
    private boolean isScanning = false;

    private DevicelistAdapter myAdapter;
    private ActivityDeviceListBinding binding;
    private ApiServiceProvider apiServiceProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDeviceListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initialize();
        checkAndRequestBluetoothPermissions();
        setupListeners();
    }

    private void initialize() {
        for (DeviceObject deviceObject : deviceLIST) {
            deviceObject.setRssi(-500);
        }

        myAdapter = new DevicelistAdapter(this, deviceLIST);
        binding.lvDevice.setAdapter(myAdapter);

        apiServiceProvider = ApiServiceProvider.getInstance(this, false);
        ConnectionManager.performApiCallWithNetworkCheck(this, false);
    }

    private void checkAndRequestBluetoothPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            List<String> permissions = new ArrayList<>();
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.BLUETOOTH_CONNECT);
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.BLUETOOTH_SCAN);
            }
            if (!permissions.isEmpty()) {
                ActivityCompat.requestPermissions(this, permissions.toArray(new String[0]), REQUEST_BLUETOOTH_PERMISSIONS);
            }
        }
    }

    private void setupListeners() {
        binding.pullToRefresh.setOnRefreshListener(() -> {
            fetchDeviceList();
            binding.pullToRefresh.setRefreshing(false);
        });

        binding.floatingActionButton.setOnClickListener(v -> startActivity(new Intent(DeviceListActivity.this, AddDeviceActivity.class)));

        binding.imgBackOpenDoor.setOnClickListener(v -> {
            flagDeviceList = 0;
            onBackPressed();
        });

        binding.tvRefresh.setOnClickListener(v -> fetchDeviceList());
    }

    private void fetchDeviceList() {
        Util.checkInternet(this, isAvailable -> {
            if (isAvailable) {
                apiServiceProvider.callForDeviceList(LOGIN_DETAIL.getAppuserID(), BuildConfig.VERSION_NAME, this);
            } else {
                hideLoader();
            }
        });
    }

    private void refreshScanList() {
        if (!isScanning) {
            isScanning = true;
            int ret = LibDevModel.scanDevice(this, true, 2000, new ScanCallback() {
                @Override
                public void onScanResult(ArrayList<String> deviceList, ArrayList<Integer> rssi) {
                    Log.e("ScanCallback", "Device List: " + deviceList + ", RSSI: " + rssi);
                    runOnUiThread(() -> updateDeviceListWithRSSI(deviceList, rssi));
                    isScanning = false;
                }

                @Override
                public void onScanResultAtOnce(String devSn, int rssi) {
                    Log.e("Immediate Scan Result", devSn + ": [" + rssi + "]");
                }
            });
            if (ret != 0x00) {
                Toast.makeText(this, "Error initializing scan", Toast.LENGTH_SHORT).show();
                isScanning = false;
            }
        }
    }

    private void updateDeviceListWithRSSI(ArrayList<String> deviceList, ArrayList<Integer> rssi) {
        for (DeviceObject device : deviceLIST) {
            for (int i = 0; i < deviceList.size(); i++) {
                if (device.getDeviceSnoWithoutAlphabet().equalsIgnoreCase(deviceList.get(i))) {
                    device.setRssi(rssi.get(i));
                }
            }
        }
        myAdapter.sortListRssi(deviceLIST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_BLUETOOTH_PERMISSIONS) {
            boolean allGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }
            if (allGranted) {
                refreshScanList();
            } else {
                Toast.makeText(this, "Bluetooth permissions are required for scanning.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onResponseSuccess(SuccessDeviceListResponse response, String apiFlag) {
        if (Constant.UrlPath.DEVICE_LIST_API.equals(apiFlag)) {
            if ("OK".equalsIgnoreCase(response.getStatus())) {
                deviceLIST = response.getData();
                myAdapter.updateList(deviceLIST);
                new SaveTask().execute();
            } else {
                Toast.makeText(this, response.getMsg(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
        if (Constant.UrlPath.DEVICE_LIST_API.equals(apiFlag)) {
            Toast.makeText(this, throwable != null ? throwable.getMessage() : "Something went wrong", Toast.LENGTH_LONG).show();
        }
    }

    class SaveTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().deviceDao().deleteAll();
            DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().deviceDao().insert(deviceLIST);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            refreshScanList();
        }
    }
}
