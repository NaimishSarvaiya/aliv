package com.iotsmartaliv.activity;

import android.bluetooth.BluetoothAdapter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.intelligoo.sdk.LibDevModel;
import com.intelligoo.sdk.ScanCallback;
import com.iotsmartaliv.R;
import com.iotsmartaliv.adapter.ScanDevListAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * This activity class is used for adding the device.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2018-10-23
 */
public class AddDeviceActivity extends AppCompatActivity {
    private static final String TAG = "Act_AddList";
    private static final int BLE_OPEN_REQUEST = 1;
    private static final int SCAN_STOP_DELAY = 8000;
    String jSONResponse = "{" +
            "Notifications" +
            "[]" +
            "}";
    private BluetoothAdapter mAdapter;
    private ImageButton mBack;
    private Handler mHandler;
    private Button mButton;
    private boolean mScanning = false;
    private ScanDevListAdapter adapter;
    private TextView mTvScan = null;
    private ListView scanListView;
    private RecyclerView rv_scan_list;
    private String username;
    private Map<String, Integer> deviceMap;
    private ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(ArrayList<String> arrayList, ArrayList<Integer> arrayList1) {
            for (int i = 0; i < arrayList.size(); i++) {
                deviceMap.put(arrayList.get(i), arrayList1.get(i));
            }
            List<Map.Entry<String, Integer>> list = new ArrayList<>(deviceMap.entrySet());
            Collections.sort(list, (t1, t2) -> t2.getValue().compareTo(t1.getValue()));
            mTvScan.setVisibility(View.GONE);

            adapter = new ScanDevListAdapter(AddDeviceActivity.this, list);
            adapter.setOnItemClickListener((view, sn) -> Toast.makeText(AddDeviceActivity.this, "Can't add.", Toast.LENGTH_SHORT).show());
            rv_scan_list.setAdapter(adapter);
            mScanning = false;
        }

        @Override
        public void onScanResultAtOnce(String s, int i) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);
//        username = SPUtils.getString(Constant.USERNAME);
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "Bluetooth not supported.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        deviceMap = new TreeMap<>();
        initList();

        mButton.setOnClickListener(v -> {
            if (!mScanning) {
                scanDevice();
            } else {
//                    ToastUtils.showMessage(Act_AddList.this, R.string.activity_scanning);
            }
        });

        mBack.setOnClickListener(v -> finish());
    }

    /**
     * initializing list
     */
    private void initList() {
        mButton = findViewById(R.id.activity_scan_cancel);
        rv_scan_list = findViewById(R.id.rv_scan_list);
        mTvScan = findViewById(R.id.activity_scan_tv_mark);
        mBack = findViewById(R.id.ib_scan_back);
        mHandler = new Handler();

        //recyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_scan_list.setLayoutManager(linearLayoutManager);
        scanDevice();
    }

    /**
     * This method is used for scan device.
     */
    private void scanDevice() {
        mScanning = true;
        LibDevModel.scanDevice(this, true, 3000, scanCallback);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * This method is used for get device serial number.
     *
     * @param name
     * @return
     */
    protected String getDevSn(String name) {
        int pos = name.indexOf("-");
        String dev_sn = name.substring(pos + 1);
        return dev_sn;
    }

/*    private void saveDevice(final BluetoothDevice device,
                            final Bundle bundle) {

        DeviceBean deviceSave = new DeviceBean();
        AccessDevMetaData deviceData = new AccessDevMetaData(getApplicationContext()
        );
        deviceSave.setUsername(username);
        deviceSave.setDevType(bundle.getIntForShake(DeviceBean.DEVICE_TYPE));
        deviceSave.setPrivilege(bundle.getIntForShake(DeviceBean.PRIVILEGE));
        deviceSave.setDevSn(getDevSn(device.getName()));

        deviceSave.setDevMac(device.getAddress());
        deviceSave.seteKey(bundle.getString(DeviceBean.DEVICE_KEY));
        int manager_pwd = bundle.getIntForShake(DeviceBean.DEVICE_MANAGER_PWD);
        deviceSave.setDevManagerPassword(Integer.toString(manager_pwd));
        deviceData.saveAccessDev(deviceSave);
    }

    private void saveScanDevice(final DeviceBean deviceSave) {
        AccessDevMetaData deviceData = new AccessDevMetaData(BaseApplication.getInstance());
        deviceData.saveAccessDev(deviceSave);
    }

    private DeviceBean getDeviceBean(final BluetoothDevice device, final Bundle bundle) {
        DeviceBean devDom = new DeviceBean();
        devDom.setUsername(username);
        devDom.setDevType(bundle.getIntForShake(DeviceBean.DEVICE_TYPE));

        UserData userData = new UserData(BaseApplication.getInstance());
        UserBean user = userData.getUser(username);

        devDom.setPrivilege(bundle.getIntForShake(DeviceBean.PRIVILEGE));
        devDom.setDevSn(getDevSn(device.getName()));
        devDom.setDevMac(device.getAddress());
        devDom.seteKey(bundle.getString(DeviceBean.DEVICE_KEY));

        int manager_pwd = bundle.getIntForShake(DeviceBean.DEVICE_MANAGER_PWD);
        devDom.setDevManagerPassword(Integer.toString(manager_pwd));
        return devDom;
    }*/

   /* class ViewHolder {
        TextView devName;
        TextView devRssi;
    }*/

  /*  //适配器类
    private class ScanListAdapter extends BaseAdapter {
        private List<Map.Entry<String, Integer>> list = new ArrayList<>();
        private LayoutInflater inflater;

        public ScanListAdapter(Activity activity) {
            inflater = LayoutInflater.from(activity);
        }

        private void addDevices(List<Map.Entry<String, Integer>> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {

            return list.size();
        }

        @Override
        public Object getItem(int position) {

            return list.get(position);
        }

        @Override
        public long getItemId(int position) {

            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_activity_scanlist, null);
                viewHolder = new ViewHolder();
                viewHolder.devName = (TextView) convertView.findViewById(R.id.tv_iten_name);
                viewHolder.devRssi = (TextView) convertView.findViewById(R.id.tv_iten_rssi);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.devName.setText(list.get(position).getKey());
            viewHolder.devRssi.setText(list.get(position).getValue());
            viewHolder.devName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            return convertView;
        }
    }*/
}