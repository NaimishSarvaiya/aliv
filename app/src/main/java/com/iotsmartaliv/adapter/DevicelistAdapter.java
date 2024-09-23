package com.iotsmartaliv.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.intelligoo.sdk.LibDevModel;
import com.intelligoo.sdk.ScanCallback;
import com.iotsmartaliv.R;
import com.iotsmartaliv.activity.DeviceDetailActivity;
import com.iotsmartaliv.apiAndSocket.listeners.RetrofitListener;
import com.iotsmartaliv.apiAndSocket.models.DeviceObject;
import com.iotsmartaliv.apiAndSocket.models.ErrorObject;
import com.iotsmartaliv.apiAndSocket.retrofit.ApiServiceProvider;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.roomDB.AccessLogModel;
import com.iotsmartaliv.utils.ErrorMsgDoorMasterSDK;
import com.iotsmartaliv.utils.SaveAccessLogTask;
import com.iotsmartaliv.utils.SharePreference;
import com.iotsmartaliv.utils.Util;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;

import static com.iotsmartaliv.activity.DeviceListActivity.flagDeviceList;
import static com.iotsmartaliv.apiAndSocket.models.DeviceObject.getLibDev;
import static com.iotsmartaliv.constants.Constant.COMMUNITY_ID;
import static com.iotsmartaliv.constants.Constant.DEVICE_ID;
import static com.iotsmartaliv.constants.Constant.LOGIN_DETAIL;
import static com.iotsmartaliv.constants.Constant.deviceLIST;
import static com.iotsmartaliv.utils.CommanUtils.accessWithinRange;
import static com.iotsmartaliv.utils.CommanUtils.utcToLocalTimeZone;

/**
 * This activity class is used to display device-list row.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2018-10-23
 */

public class DevicelistAdapter extends BaseAdapter {
    // private static final boolean SCANED = true;
    // private static final boolean SCANED_NULL = false;
    public static DeviceObject selectDevice;
    String openingDoorDeviceSN;
    //  private ArrayList<SortDev> sortDevList = new ArrayList<SortDev>();
    private Activity context;
    private LayoutInflater mInflater = null;
    private List<DeviceObject> data = new ArrayList<>();
    private boolean pressed = false;
    private ProgressDialog progress;
    private boolean goInsideToOpenDoor = false;
    /**
     * Callback method to open the door successfully.
     */

    private String devSn;
    /**
     *
     * Callback method to check scanned device and to open the nearest device lock on click.
     */

    /**
     * @param activity
     * @param data
     */
    public DevicelistAdapter(Activity activity, List<DeviceObject> data) {
        context = activity;
        progress = new ProgressDialog(context);
        progress.setMessage("Door Open On Process");
        progress.setCancelable(false);
        flagDeviceList = 0;
        this.data = data;
        mInflater = LayoutInflater.from(activity);
    }

    @Override
    public int getCount() {
        return data.size();
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
            viewHolder.image_click_lay = convertView.findViewById(R.id.device_detail);
            viewHolder.image = convertView.findViewById(R.id.item_img);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (data.get(position).getCdeviceName().length() > 0) {
            viewHolder.devName.setText(data.get(position).getCdeviceName());
        } else {
            viewHolder.devName.setText(data.get(position).getDeviceName());
        }
        if (data.get(position).getRssi() != -500) {
            viewHolder.devName.setTextColor(context.getResources().getColor(R.color.Orange));
        } else {
            viewHolder.devName.setTextColor(context.getResources().getColor(R.color.White));
        }
        viewHolder.image_click_lay.setOnClickListener(v -> {
            selectDevice = data.get(position);
            String communityId = data.get(position).getCommunityID();
            String deviceId = data.get(position).getDeviceID();
            Intent intent = new Intent(context, DeviceDetailActivity.class);
            intent.putExtra(DEVICE_ID, deviceId);
            intent.putExtra(COMMUNITY_ID, communityId);
            context.startActivity(intent);
            // context.startActivity(new Intent(context, DeviceDetailActivity.class));

        });
        convertView.setOnClickListener(v -> {
            progress.show();
            final LibDevModel libDev = getDev(position);
            int ret1 = LibDevModel.scanDevice(context, true, 1300, new ScanCallback() {
                @Override
                public void onScanResult(ArrayList<String> arrayList, ArrayList<Integer> arrayList1) {
                    boolean isOnline = false;
                    for (int i = 0; i < arrayList.size(); i++) {
                        if (libDev.devSn.equalsIgnoreCase(arrayList.get(i))) {
                            if (arrayList1.get(i) > -70) {
                                isOnline = true;
                            }
                        }
                    }
                    performOpenDoorOperation(isOnline, libDev, position);
                    progress.dismiss();
                    /* if (isOnline) {
                        openingDoorDeviceSN = deviceLIST.get(position).getDeviceSno();
                        int ret = LibDevModel.openDoor(context, libDev, (result, bundle) -> context.runOnUiThread(() -> {
                            progress.dismiss();
                            if (result == 0x00) {
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
                                new SaveAccessLogTask(context, new AccessLogModel("", openingDoorDeviceSN, "open door from device list", dateFormat.format(new Date()))).execute();

                                Toast.makeText(context, "Door open successfully.", Toast.LENGTH_SHORT).show();
                            } else {
                                if (result == 48) {
                                    Toast.makeText(context, "Result Error Time Out", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "Failure: Error Code" + result, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }));
                        if (ret == 0) {
                            return;
                        } else {
                            progress.dismiss();
                            Toast.makeText(context, "RET：" + ret, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        progress.dismiss();
                        Toast.makeText(context, "Device not in range.", Toast.LENGTH_SHORT).show();

                    }*/
                }

                @Override
                public void onScanResultAtOnce(String s, int i) {

                }
            });
            if (ret1 != 0) {
                Toast.makeText(context, "Opening Distance is too far. Kindly go nearer the device. Thank you", Toast.LENGTH_SHORT).show();
                progress.dismiss();
            }

        });
        return convertView;
    }

    public void updateList(List<DeviceObject> dataList) {
        data = dataList;
        notifyDataSetChanged();
    }

    private void performOpenDoorOperation(boolean isOnline, LibDevModel libDev, int position) {

        String isAcessible = SharePreference.getInstance(context).getString("isAccessable");

        if (isAcessible.equals("1")) {

            callGetServerAPI(isOnline, libDev, position);

        } else {

            goInsideToOpenDoor = true;

            furtherProcess(isOnline, libDev, position);
        }
    }

    private void callGetServerAPI(boolean isOnline, LibDevModel libDev, int position) {
        ApiServiceProvider apiServiceProvider = ApiServiceProvider.getInstance(context);
        apiServiceProvider.callGetServerCurrentTime(new RetrofitListener<ResponseBody>() {
            @Override
            public void onResponseSuccess(ResponseBody sucessRespnse, String apiFlag) {
                try {
                    JSONObject jsonObject = new JSONObject(sucessRespnse.string());
                    String dateTime = jsonObject.optString("date");
                    Date serverDate = utcToLocalTimeZone(dateTime);
                    String isAcessible = SharePreference.getInstance(context).getString("isAccessable");
                    if (!SharePreference.getInstance(context).getString("deviceStartTime").equalsIgnoreCase("")
                            && !SharePreference.getInstance(context).getString("deviceEndTime").equalsIgnoreCase("")) {
                        Date startTime = utcToLocalTimeZone(SharePreference.getInstance(context).getString("deviceStartTime"));
                        Date endTime = utcToLocalTimeZone(SharePreference.getInstance(context).getString("deviceEndTime"));
                        goInsideToOpenDoor = accessWithinRange(isAcessible, startTime, endTime, serverDate);
                    } else {
                        goInsideToOpenDoor = true;
                    }
//                    notifyDataSetChanged();
                    furtherProcess(isOnline, libDev, position);
//                    SharePreference.getInstance(getActivity()).putString(getResources().getString(R.string.server_current_time), dateTime);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {

            }
        });


    }

    private void furtherProcess(boolean isOnline, LibDevModel libDev, int position) {
        if (goInsideToOpenDoor) {
            if (isOnline) {
                openingDoorDeviceSN = deviceLIST.get(position).getDeviceSno();
                int ret = LibDevModel.openDoor(context, libDev, (result, bundle) -> context.runOnUiThread(() -> {
                    progress.dismiss();
                    if (result == 0x00) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
                        new SaveAccessLogTask(context, new AccessLogModel("", openingDoorDeviceSN, "open door from device list", dateFormat.format(new Date()))).execute();
                        logs(LOGIN_DETAIL.getAppuserID(),new AccessLogModel("", openingDoorDeviceSN, "open door from device list", dateFormat.format(new Date())));
                        Toast.makeText(context, "Door open successfully", Toast.LENGTH_SHORT).show();
//                        Util.logDoorOpenEvent("DeviceList", true, LOGIN_DETAIL.getAppuserID(), openingDoorDeviceSN);
                    } else {
                        if (result == 48) {
                            Toast.makeText(context, "Result Error Time Out", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Failure: Error Code" + result, Toast.LENGTH_SHORT).show();
                        }
                        Util.logDoorOpenEvent("DeviceList", false, LOGIN_DETAIL.getAppuserID(), openingDoorDeviceSN);

                    }
                }));
                if (ret == 0) {
                    return;
                } else {
                    progress.dismiss();
                    Toast.makeText(context, ErrorMsgDoorMasterSDK.getErrorMsg(ret), Toast.LENGTH_SHORT).show();
                }
            } else {
                progress.dismiss();
//                fa
                Toast.makeText(context, "Opening Distance is too far. Kindly go nearer the device. Thank you", Toast.LENGTH_SHORT).show();

//                Toast.makeText(context, "Device not in range.", Toast.LENGTH_SHORT).show();

            }
        } else {
            Toast.makeText(context, "User can not access at this time", Toast.LENGTH_SHORT).show();
            progress.dismiss();
        }
    }

    /**
     * @param position
     * @return
     */
    public LibDevModel getDev(int position) {
        DeviceObject device = data.get(position);
        LibDevModel libDev = getLibDev(device);
        return libDev;
    }

    /**
     * @param deviceLIST
     */
    /*public void sortList(DeviceObject device) {
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
        if (flagDeviceList == 0) {
            flagDeviceList = 1;
            if (permitedList.size() == 0) {
                for (int j = 0; j < sortDevList.size(); j++) {
                    permitedList.add(sortDevList.get(j));
                }
            }
        }
        notifyDataSetChanged();
    }*/
    public void sortListRssi(List<DeviceObject> deviceLIST) {
        Collections.sort(deviceLIST, (o1, o2) -> {
            Integer x1 = o1.getRssi();
            Integer x2 = o2.getRssi();
            return x2.compareTo(x1);
        });

        data = deviceLIST;
        notifyDataSetChanged();
    }

    class ViewHolder {
        TextView devName;
        LinearLayout item, image_click_lay;
        ImageView image;
    }

    public void logs(String userId,AccessLogModel accessLogModel){
        Util.checkInternet(context, new Util.NetworkCheckCallback() {
            @Override
            public void onNetworkCheckComplete(boolean isAvailable) {
                if (isAvailable){
                    ApiServiceProvider apiServiceProvider = ApiServiceProvider.getInstance(context);
                    apiServiceProvider.postAccessLog(userId, accessLogModel, new RetrofitListener<AccessLogModel>() {

                        @Override
                        public void onResponseSuccess(AccessLogModel accessLogModel1, String apiFlag) {
                            if (Constant.UrlPath.POST_ACCESS_LOG.equals(apiFlag)) {
//                                new DeviceLogSyncService.DeleteAccessLogTask(DeviceLogSyncService.this, accessLogModel1).execute();
                            }
                        }

                        @Override
                        public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
//                            Util.firebaseEvent(Constant.APIERROR, DeviceLogSyncService.this, Constant.UrlPath.SERVER_URL + apiFlag, LOGIN_DETAIL.getUsername(), LOGIN_DETAIL.getAppuserID(), errorObject.getStatus());
                        }
                    });
                }
            }
        });
    }


}


//public class DevicelistAdapter extends BaseAdapter {
//    private static final boolean SCANED = true;
//    private static final boolean SCANED_NULL = false;
//    private ArrayList<SortDev> devList = new ArrayList<SortDev>();
//    private Context context;
//    private LayoutInflater mInflater = null;
//    private ArrayList<SortDev> permitedList = new ArrayList<SortDev>();
//
//    /**
//     * @param activity
//     * @param data
//     */
//    public DevicelistAdapter(Activity activity, ArrayList<DeviceBean> data) {
//        context = activity;
//        flagDeviceList = 0;
//        mInflater = LayoutInflater.from(activity);
//        for (DeviceBean device : data) {
//            SortDev sortDev = new SortDev(device, SCANED_NULL);
//            devList.add(sortDev);
//        }
//
//        if (permitedList.size() == 0) {
//            for (int j = 0; j < devList.size(); j++) {
//                permitedList.add(devList.get(j));
//            }
//        }
//    }
//
//    /**
//     * @param dev
//     * @return
//     */
//    public static LibDevModel getLibDev(DeviceBean dev) {
//        LibDevModel device = new LibDevModel();
//        device.devSn = dev.getDevSn();
//        device.devMac = dev.getDevMac();
//        device.devType = dev.getDevType();
//        device.eKey = dev.geteKey();
//        device.endDate = dev.getEndDate();
//        device.openType = dev.getOpenType();
//        device.privilege = dev.getPrivilege();
//        device.startDate = dev.getStartDate();
//        device.useCount = dev.getUseCount();
//        device.verified = dev.getVerified();
//        device.cardno = "123";
//        return device;
//    }
//
//    @Override
//    public int getCount() {
//        return permitedList.size();
//    }
//
//    @Override
//    public Object getItem(int arg0) {
//        return null;
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public View getView(final int position, View convertView, ViewGroup parent) {
//        ViewHolder viewHolder = null;
//        if (convertView == null) {
//            convertView = mInflater.inflate(R.layout.device_list_row, null);
//            viewHolder = new ViewHolder();
//            viewHolder.devName = (TextView) convertView.findViewById(R.id.item_text);
//            viewHolder.item = (LinearLayout) convertView.findViewById(R.id.ll_device);
//            viewHolder.image = (ImageView) convertView.findViewById(R.id.item_img);
//            viewHolder.devName.setText(permitedList.get(position).device.getDevSn());
//            convertView.setTag(viewHolder);
//        } else {
//            viewHolder = (ViewHolder) convertView.getTag();
//        }
////        final LibDevModel libDev = getDev(position);
//
//        String strSn = permitedList.get(position).device.getDevSn();
//        for (int k = 0; k < devList.size(); k++) {
//            if (strSn.equalsIgnoreCase(devList.get(k).device.getDevSn())) {
//                if (devList.get(k).scaned) {
//                    viewHolder.devName.setTextColor(context.getResources().getColor(R.color.Orange));
//                } else {
//                    viewHolder.devName.setTextColor(context.getResources().getColor(R.color.White));
//                }
//                break;
//            }
//        }
////        viewHolder.devName.setText(libDev.devSn);
//        if (devList.get(position).scaned) {
//            viewHolder.devName.setTextColor(context.getResources().getColor(R.color.Orange));
//        } else {
//            viewHolder.devName.setTextColor(context.getResources().getColor(R.color.White));
//        }
//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final LibDevModel libDev = getDev(position);
//                DeviceBean deviceBean = new DeviceBean(libDev);
//                context.startActivity(new Intent(context, DeviceDetailActivity.class).putExtra("DEVICEINFO", deviceBean));
//            }
//        });
//        return convertView;
//    }
//
//    public LibDevModel getDev(int position) {
//        DeviceBean device = devList.get(position).device;
//        LibDevModel libDev = getLibDev(device);
//        return libDev;
//    }
//
//    /**
//     * @param list
//     */
//    public void refreshList(ArrayList<DeviceBean> list) {
//        devList.clear();
//        for (int i = 0; i < list.size(); i++) {
//            SortDev sortDev = new SortDev(list.get(i), SCANED_NULL);
//            devList.add(sortDev);
//        }
//        notifyDataSetChanged();
//    }
//
//    /**
//     * @param device
//     */
//    public void sortList(DeviceBean device) {
//        if (device == null || device.getDevSn() == null
//                || devList == null || devList.isEmpty()) {
//            Log.e("doormaster ", "sortList null");
//            return;
//        }
//        SortDev scanDoor = new SortDev(device, SCANED);
//        ArrayList<SortDev> temp_list = new ArrayList<SortDev>();
//
//        for (SortDev door : devList) {
//            if (door.scaned == SCANED &&
//                    (!door.device.getDevSn().equals(device.getDevSn()))) {
//                temp_list.add(door);
//            }
//        }
//        temp_list.add(scanDoor);
//
//        for (SortDev door : devList) {
//            if ((!door.device.getDevSn().equals(device.getDevSn())) && door.scaned == SCANED_NULL) {
//                temp_list.add(door);
//            }
//        }
//
//        devList = new ArrayList<SortDev>(temp_list);
//        if (flagDeviceList == 0) {
//            flagDeviceList = 1;
//            if (permitedList.size() == 0) {
//                for (int j = 0; j < devList.size(); j++) {
//                    permitedList.add(devList.get(j));
//                }
//            }
//            notifyDataSetChanged();
//        }
//    }
//
//    class ViewHolder {
//        TextView devName;
//        LinearLayout item;
//        ImageView image;
//    }
//
//    public class SortDev {
//        DeviceBean device = null;
//        boolean scaned = false;
//
//        public SortDev(DeviceBean device, boolean scaned) {
//            this.device = device;
//            this.scaned = scaned;
//        }
//    }
//}

//package com.iotsmart.adapter;
//
//import android.app.Activity;
//import android.content.Context;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.intelligoo.sdk.LibDevModel;
//import com.intelligoo.sdk.LibInterface;
//import com.intelligoo.sdk.ScanCallback;
//import com.iotsmart.R;
//import com.iotsmart.activity.OpenDoorRemotelyActivity;
//import com.iotsmart.constants.Constant;
//import com.iotsmart.model.DeviceBean;
//
//import java.util.ArrayList;
//
//import static com.iotsmart.activity.LoginActivity.devList;
//import static com.iotsmart.activity.OpenDoorRemotelyActivity.flag;
//
///**
// * This class is used to open a specific door.
// *
// * @author CanopusInfoSystems
// * @version 1.0
// * @since 18Jan 2019
// */