package com.iotsmartaliv.adapter;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.intelligoo.sdk.LibDevModel;
import com.intelligoo.sdk.ScanCallback;
import com.iotsmartaliv.R;
import com.iotsmartaliv.activity.DeviceDetailActivity;
import com.iotsmartaliv.apiAndSocket.models.DeviceObject;
import com.iotsmartaliv.utils.ErrorMsgDoorMasterSDK;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.iotsmartaliv.apiAndSocket.models.DeviceObject.getLibDev;
import static com.iotsmartaliv.constants.Constant.COMMUNITY_ID;
import static com.iotsmartaliv.constants.Constant.DEVICE_ID;

public class DevicelistAdapter extends BaseAdapter {
    public static DeviceObject selectDevice;
    private Activity context;
    private LayoutInflater mInflater;
    private List<DeviceObject> data;
    private ProgressDialog progress;

    public DevicelistAdapter(Activity activity, List<DeviceObject> data) {
        this.context = activity;
        this.data = data;
        this.mInflater = LayoutInflater.from(activity);
        this.progress = new ProgressDialog(context);
        progress.setMessage("Opening door...");
        progress.setCancelable(false);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.device_list_row, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.devName = convertView.findViewById(R.id.item_text);
            viewHolder.image_click_lay = convertView.findViewById(R.id.device_detail);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        DeviceObject device = data.get(position);
        viewHolder.devName.setText(device.getCdeviceName().isEmpty() ? device.getDeviceName() : device.getCdeviceName());

        viewHolder.image_click_lay.setOnClickListener(v -> {
            selectDevice = device;
            Intent intent = new Intent(context, DeviceDetailActivity.class);
            intent.putExtra(DEVICE_ID, device.getDeviceID());
            intent.putExtra(COMMUNITY_ID, device.getCommunityID());
            context.startActivity(intent);
        });

        convertView.setOnClickListener(v -> {
            if (hasBluetoothPermissions()) {
                startScanAndUnlockDoor(position);
            } else {
                Toast.makeText(context, "Bluetooth permissions are required.", Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }

    private boolean hasBluetoothPermissions() {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED;
    }

    private void startScanAndUnlockDoor(int position) {
        progress.show();
        final LibDevModel libDev = getLibDev(data.get(position));

        int ret1 = LibDevModel.scanDevice(context, true, 1300, new ScanCallback() {
            @Override
            public void onScanResult(ArrayList<String> deviceList, ArrayList<Integer> rssiList) {
                boolean isNearby = deviceList.contains(libDev.devSn);
                if (isNearby) {
                    Toast.makeText(context, "Door unlocked.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Device not in range.", Toast.LENGTH_SHORT).show();
                }
                progress.dismiss();
            }

            @Override
            public void onScanResultAtOnce(String devSn, int rssi) {
                // Optional: Handle immediate scan result if needed
            }
        });

        if (ret1 != 0) {
            Toast.makeText(context, ErrorMsgDoorMasterSDK.getErrorMsg(ret1), Toast.LENGTH_SHORT).show();
            progress.dismiss();
        }
    }

    public void sortListRssi(List<DeviceObject> deviceList) {
        // Sorting the list based on RSSI values in descending order
        Collections.sort(deviceList, (o1, o2) -> Integer.compare(o2.getRssi(), o1.getRssi()));
        data = deviceList;
        notifyDataSetChanged();
    }

    public void updateList(List<DeviceObject> newList) {
        data = newList;
        notifyDataSetChanged();
    }

    static class ViewHolder {
        TextView devName;
        LinearLayout image_click_lay;
    }
}
