package com.iotsmartaliv.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.intelligoo.sdk.LibDevModel;
import com.intelligoo.sdk.ScanCallback;
import com.iotsmartaliv.BuildConfig;
import com.iotsmartaliv.R;
import com.iotsmartaliv.adapter.DevicelistAdapter;
import com.iotsmartaliv.apiCalling.listeners.RetrofitListener;
import com.iotsmartaliv.apiCalling.models.Country;
import com.iotsmartaliv.apiCalling.models.DeviceObject;
import com.iotsmartaliv.apiCalling.models.ErrorObject;
import com.iotsmartaliv.apiCalling.models.SuccessDeviceListResponse;
import com.iotsmartaliv.apiCalling.retrofit.ApiServiceProvider;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.databinding.ActivityDeviceListBinding;
import com.iotsmartaliv.roomDB.DatabaseClient;
import com.iotsmartaliv.utils.ErrorMsgDoorMasterSDK;
import com.iotsmartaliv.utils.NetworkAvailability;
import com.iotsmartaliv.utils.SharePreference;
import com.iotsmartaliv.utils.Util;
import com.iotsmartaliv.utils.faceenroll.ConnectionManager;
import com.iotsmartaliv.utils.mail.EmailViewModel;
import com.iotsmartaliv.utils.mail.MailSender;

import java.util.ArrayList;
import java.util.List;


//import butterknife.OnClick;

import static com.iotsmartaliv.constants.Constant.LOGIN_DETAIL;
import static com.iotsmartaliv.constants.Constant.deviceLIST;
import static com.iotsmartaliv.constants.Constant.hideLoader;


/*
 * This activity class is used for getting device list from DB.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2018-10-23
 */
public class DeviceListActivity extends AppCompatActivity implements RetrofitListener<SuccessDeviceListResponse> {
    public static int flagDeviceList = 0;
    ApiServiceProvider apiServiceProvider;
    private DevicelistAdapter myAdapter;
    EmailViewModel emailViewModel;
    ActivityDeviceListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDeviceListBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_device_list);
//        ButterKnife.bind(this);
        for (DeviceObject deviceObject : deviceLIST) {
            deviceObject.setRssi(-500);
        }


//        EmailViewModel viewModel = new ViewModelProvider(this).get(EmailViewModel.class);
//
//        viewModel.getEmailResult().observe(this, isSuccess -> {
//            if (isSuccess) {
//                Toast.makeText(this, "Email sent successfully", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(this, "Failed to send email", Toast.LENGTH_SHORT).show();
//            }
//        });

        // Trigger email sending (example)
//        viewModel.sendEmail("naimishsarvaiya2930@gmail.com", "Subject of the Email", "Email content goes here.");
//        MailSender.sendMail("naimishsarvaiya2930@gmail.com", "Subject of the Email", "Email content goes here.");

        ConnectionManager.performApiCallWithNetworkCheck(this, false);

        myAdapter = new DevicelistAdapter(this, deviceLIST);
        binding.lvDevice.setAdapter(myAdapter);

        apiServiceProvider = ApiServiceProvider.getInstance(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 2);
                return;
            }
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_DENIED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_SCAN}, 2);
                return;
            }
        }

//        if (Util.checkInternet(this)) {
        try {
            Util.checkInternet(DeviceListActivity.this, new Util.NetworkCheckCallback() {
                @Override
                public void onNetworkCheckComplete(boolean isAvailable) {
                    if (isAvailable) {
                        apiServiceProvider.callForDeviceList(LOGIN_DETAIL.getAppuserID(), BuildConfig.VERSION_NAME,DeviceListActivity.this);
                    } else {
                        hideLoader();
                    }
                }
            });
            binding.pullToRefresh.setOnRefreshListener(() -> {
                Util.checkInternet(DeviceListActivity.this, new Util.NetworkCheckCallback() {
                    @Override
                    public void onNetworkCheckComplete(boolean isAvailable) {
                        if (isAvailable) {
                            apiServiceProvider.callForDeviceList(LOGIN_DETAIL.getAppuserID(),  BuildConfig.VERSION_NAME,DeviceListActivity.this);
                        } else {
                            hideLoader();
                        }
                    }
                });
                binding.pullToRefresh.setRefreshing(false);
            });
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
//        }else {
//            hideLoader();
//        }

        binding.searchDevice.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // filter your list from your input
                filter(s.toString());
                //you can use runnable postDelayed like 500 ms to delay search text
            }
        });

        binding.imgBackOpenDoor.setOnClickListener(v-> clickBack());
        binding.tvRefresh.setOnClickListener(v-> clickRefresh());
        binding.floatingActionButton.setOnClickListener(v-> addDeviceToList());



    }

    void clickBack() {
        flagDeviceList = 0;
        onBackPressed();
    }

    void clickRefresh() {
        Util.checkInternet(DeviceListActivity.this, new Util.NetworkCheckCallback() {
            @Override
            public void onNetworkCheckComplete(boolean isAvailable) {
                if (isAvailable){
                    apiServiceProvider.callForDeviceList(LOGIN_DETAIL.getAppuserID(),  BuildConfig.VERSION_NAME,DeviceListActivity.this);
                }else {
                    hideLoader();
                }
            }
        }) ;
//        swipeToRefresh();
    }
    void addDeviceToList() {
        startActivity(new Intent(DeviceListActivity.this, AddDeviceActivity.class));
    }

    private void filter(String text) {
        List<DeviceObject> device  = new ArrayList<>();
        for (DeviceObject d : deviceLIST) {
            if (!d.getCdeviceName().isEmpty()){
                if (d.getCdeviceName().toLowerCase().contains(text.toLowerCase())) {
                    device.add(d);
                }
            }else {
                if (d.getDeviceName().toLowerCase().contains(text.toLowerCase()) ) {
                    device.add(d);
                }
            }
        }
        myAdapter.updateList(device);
    }



    /**
     * This method is for swipe to refresh.
     */
    private void swipeToRefresh() {
        if (deviceLIST == null || deviceLIST.size() == 0)
            return;
        // myAdapter.refreshList(deviceLIST);
        for (DeviceObject deviceObject : deviceLIST) {
            deviceObject.setRssi(-500);
        }
        myAdapter.sortListRssi(deviceLIST);
        refreshScanList();
    }

    boolean isScaning = false;

    /**
     * This method is used for refresh scan list
     */
    private void refreshScanList() {
        ScanCallback callback = new ScanCallback() {
            @Override
            public void onScanResult(ArrayList<String> deviceList, ArrayList<Integer> rssi) {
                Log.e("" + deviceList.toString(), "" + rssi.toString());
                Log.e("ScanCallback", "size=" + deviceList.size() + "," + deviceList.toString() + "," + rssi.toString());
                runOnUiThread(() -> {
                    if (deviceLIST == null || deviceLIST.isEmpty()) {
                        return;
                    }
                    for (DeviceObject device : deviceLIST) {
                        for (int i = 0; i < deviceList.size(); i++) {
                            //  for (String devSn:deviceList) {
                            String devSn = deviceList.get(i);
                            if (device.getDeviceSnoWithoutAlphabet().equalsIgnoreCase(devSn)) {
                                device.setRssi(rssi.get(i));
                            }
                        }
                    }
                    myAdapter.sortListRssi(deviceLIST);

                });
                isScaning = false;

            }

            @Override
            public void onScanResultAtOnce(final String devSn, int rssi) {
                Log.e(devSn, devSn + " :[" + rssi + "]");
            }
        };
        if (!isScaning) {
            isScaning = true;
            int ret = LibDevModel.scanDevice(DeviceListActivity.this, true, 2000, callback);
            if (ret != 0x00) {
//            Toast.makeText(DeviceListActivity.tsshis, "" + ret, Toast.LENGTH_SHORT).show();
//                Toast.makeText(DeviceListActivity.this, ErrorMsgDoorMasterSDK.getErrorMsg(ret), Toast.LENGTH_SHORT).show();
                myAdapter = new DevicelistAdapter(this, deviceLIST);
                binding.lvDevice.setAdapter(myAdapter);
                isScaning = false;
            }
            Log.e("ret", "ret" + ret);
        }
    /*    int ret = LibDevModel.scanDeviceSort(DeviceListActivity.this, true, 2000, new ScanCallBackSort() {
            @Override
            public void onScanResult(ArrayList<Map<String, Integer>> arrayList) {

            }

            @Override
            public void onScanResultAtOnce(String s, int i) {

            }
        });*/

    }



    @Override
    public void onResponseSuccess(SuccessDeviceListResponse successDeviceListResponse, String apiFlag) {
        switch (apiFlag) {
            case Constant.UrlPath.DEVICE_LIST_API:
                if (successDeviceListResponse.getStatus().equalsIgnoreCase("OK")) {
                    if (successDeviceListResponse.getData().size() > 0) {
                        deviceLIST = successDeviceListResponse.getData();
                        myAdapter.notifyDataSetChanged();
                        SaveTask st = new SaveTask();
                        st.execute();
                    } else {
                        deviceLIST = new ArrayList<>();
                        myAdapter.notifyDataSetChanged();
                        SaveTask st = new SaveTask();
                        st.execute();
                        Toast.makeText(this, "Device List Empty", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(this, successDeviceListResponse.getMsg(), Toast.LENGTH_LONG).show();
                }
                myAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
        if (Constant.UrlPath.DEVICE_LIST_API.equals(apiFlag)) {
            try {
                Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }
    }

    class SaveTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            //adding to database
            DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                    .deviceDao().deleteAll();
            DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                    .deviceDao()
                    .insert(deviceLIST);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            swipeToRefresh();

            //    Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
        }
    }
}