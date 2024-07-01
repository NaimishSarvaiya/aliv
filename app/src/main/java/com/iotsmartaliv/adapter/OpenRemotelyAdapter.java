package com.iotsmartaliv.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.intelligoo.sdk.LibDevModel;
import com.intelligoo.sdk.LibInterface;
import com.intelligoo.sdk.ScanCallback;
import com.iotsmartaliv.R;
import com.iotsmartaliv.activity.OpenDoorRemotelyActivity;
import com.iotsmartaliv.apiCalling.models.DeviceObject;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.roomDB.AccessLogModel;
import com.iotsmartaliv.utils.ErrorMsgDoorMasterSDK;
import com.iotsmartaliv.utils.SaveAccessLogTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.iotsmartaliv.activity.OpenDoorRemotelyActivity.flag;
import static com.iotsmartaliv.apiCalling.models.DeviceObject.getLibDev;
import static com.iotsmartaliv.constants.Constant.deviceLIST;

/**
 * This adapter class is used to open a specific door.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 18Jan 2019
 */
public class OpenRemotelyAdapter extends BaseAdapter {
    private static final boolean SCANED = true;
    private static final boolean SCANED_NULL = false;
    String openingDoorDeviceSN;
    private ArrayList<SortDev> sortDevList = new ArrayList<SortDev>();
    private Context context;
    private LayoutInflater mInflater = null;
    private ArrayList<SortDev> permitedList = new ArrayList<SortDev>();
    //private ArrayList<Integer> permitedRssiList = new ArrayList<Integer>();
    private boolean pressed = false;
    /**
     * Callback method to open the door successfully.
     */
    final LibInterface.ManagerCallback callback = new LibInterface.ManagerCallback() {
        @Override
        public void setResult(final int result, Bundle bundle) {
            ((OpenDoorRemotelyActivity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Constant.hideLoader();
                    try {
                        pressed = false;
                        if (result == 0x00) {
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
                            new SaveAccessLogTask(context, new AccessLogModel("", openingDoorDeviceSN, "device list", dateFormat.format(new Date()))).execute();

                            Toast.makeText(context, "Door open successfully.", Toast.LENGTH_SHORT).show();
                        } else {
                            if (result == 48) {
                                //Toast.makeText(context, "Result Error Timer Out", Toast.LENGTH_SHORT).show();
                                Toast.makeText(context, "Device out-of range.", Toast.LENGTH_SHORT).show();
                            } else {
                                //Toast.makeText(context, "Failure:" + result, Toast.LENGTH_SHORT).show();
                                Toast.makeText(context, "Device out-of range.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };
    private String devSn;
    /**
     * Callback method to check scanned device and to open the nearest device lock on click.
     */
    ScanCallback oneKeyScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(ArrayList<String> deviceList,
                                 ArrayList<Integer> rssi) {
            try {
                //permitedList.clear();
                //permitedRssiList.clear();
                if (deviceList.size() == 0) {
                    Constant.hideLoader();
                    pressed = false;
                    //Toast.makeText(context, "Scan 0 device", Toast.LENGTH_SHORT).show();
                    Toast.makeText(context, "Device out-of range.", Toast.LENGTH_SHORT).show();
                    return;
                }
                DeviceObject dev = null;
                for (int i = 0; i < deviceList.size(); i++) {
                    if ((deviceLIST.get(i).getDeviceSno()).equalsIgnoreCase(devSn)) {
                        dev = deviceLIST.get(i);
                    }
                }
                pressed = true;
                LibDevModel libDev = getLibDev(dev);
                openingDoorDeviceSN = deviceLIST.get(0).getDeviceSno();
                int ret = LibDevModel.openDoor(context, libDev, callback);
                if (ret == 0) {
                    return;
                } else {
                    Constant.hideLoader();
                    pressed = false;
//                    Toast.makeText(context, "RET：" + ret, Toast.LENGTH_SHORT).show();
                    Toast.makeText(context, "Something weng wrong! Please try again.", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Constant.hideLoader();
                e.printStackTrace();
            }
        }

        @Override
        public void onScanResultAtOnce(final String devSn, int rssi) {
            pressed = false;
        }
    };

    /**
     * @param activity
     * @param data
     */
    public OpenRemotelyAdapter(Activity activity, List<DeviceObject> data) {
        context = activity;
        flag = 0;
        mInflater = LayoutInflater.from(activity);
        for (DeviceObject device : data) {
            SortDev sortDev = new SortDev(device, SCANED_NULL);
            sortDevList.add(sortDev);
        }
        if (permitedList.size() == 0) {
            for (int j = 0; j < sortDevList.size(); j++) {
                permitedList.add(sortDevList.get(j));
            }
        }
    }


    @Override
    public int getCount() {
        return permitedList.size();
    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.device_list_row, null);
            viewHolder = new ViewHolder();
            viewHolder.devName = convertView.findViewById(R.id.item_text);
            viewHolder.item = convertView.findViewById(R.id.ll_device);
            viewHolder.image = convertView.findViewById(R.id.item_img);
//            final LibDevModel libDev = getDev(position);
//            viewHolder.devName.setText(libDev.devSn);
            viewHolder.devName.setText(permitedList.get(position).device.getDeviceSno());
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String strSn = permitedList.get(position).device.getDeviceSno();
        for (int k = 0; k < sortDevList.size(); k++) {
            if (strSn.equalsIgnoreCase(sortDevList.get(k).device.getDeviceSno())) {
                if (sortDevList.get(k).scaned) {
                    viewHolder.devName.setTextColor(context.getResources().getColor(R.color.Orange));
                } else {
                    viewHolder.devName.setTextColor(context.getResources().getColor(R.color.White));
                }
                break;
            }
        }

//        viewHolder.devName.setText((sortDevList.get(position).device.getDevSn()));
//        if (sortDevList.get(position).scaned) {
//            viewHolder.devName.setTextColor(context.getResources().getColor(R.color.Orange));
//        } else {
//            viewHolder.devName.setTextColor(context.getResources().getColor(R.color.White));
//        }
        convertView.setOnClickListener(v -> {
            Constant.showLoader(context);
            if (pressed) {
                Toast.makeText(context, "Operationing...",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            int ret1 = LibDevModel.scanDevice(context, true, 1300, oneKeyScanCallback);         // A key to open the door
            pressed = true;
            if (ret1 != 0) {
                Constant.hideLoader();
                Toast.makeText(context, ErrorMsgDoorMasterSDK.getErrorMsg(ret1), Toast.LENGTH_SHORT).show();
                pressed = false;
            }
            devSn = getDev(position).devSn;
//                devSn = sortDevList.get(position).device.getDevSn();
        });
        return convertView;
    }

    /**
     * @param position
     * @return
     */
    public LibDevModel getDev(int position) {
        DeviceObject device = permitedList.get(position).device;
        LibDevModel libDev = getLibDev(device);
        return libDev;
    }

    /**
     * @param device
     */
    public void sortList(DeviceObject device) {
        if (device == null || device.getDeviceSno() == null
                || sortDevList == null || sortDevList.isEmpty()) {
            Log.e("doormaster ", "sortList null");
            return;
        }
        SortDev scanDoor = new SortDev(device, SCANED);
        ArrayList<SortDev> temp_list = new ArrayList<SortDev>();

        for (SortDev door : sortDevList) {
            if (door.scaned == SCANED &&
                    (!door.device.getDeviceSno().equals(device.getDeviceSno()))) {
                temp_list.add(door);
            }
        }
        temp_list.add(scanDoor);

        for (SortDev door : sortDevList) {
            if ((!door.device.getDeviceSno().equals(device.getDeviceSno())) && door.scaned == SCANED_NULL) {
                temp_list.add(door);
            }
        }

        sortDevList = new ArrayList<SortDev>(temp_list);
        if (flag == 0) {
            flag = 1;
            if (permitedList.size() == 0) {
                for (int j = 0; j < sortDevList.size(); j++) {
                    permitedList.add(sortDevList.get(j));
                }
            }
        }
        notifyDataSetChanged();
//        }
    }

    class ViewHolder {
        TextView devName;
        LinearLayout item;
        ImageView image;
    }

    public class SortDev {
        DeviceObject device = null;
        boolean scaned = false;

        public SortDev(DeviceObject device, boolean scaned) {
            this.device = device;
            this.scaned = scaned;
        }
    }
}