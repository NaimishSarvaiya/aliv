package com.iotsmartaliv.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iotsmartaliv.R;
import com.iotsmartaliv.apiAndSocket.listeners.RetrofitListener;
import com.iotsmartaliv.apiAndSocket.models.ErrorObject;
import com.iotsmartaliv.apiAndSocket.models.VideoDeviceData;
import com.iotsmartaliv.apiAndSocket.retrofit.ApiServiceProvider;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.interfaces.VideoIntercomItemClick;
import com.iotsmartaliv.utils.Util;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import okhttp3.ResponseBody;

import static com.iotsmartaliv.constants.Constant.LOGIN_DETAIL;
import static com.iotsmartaliv.utils.CommanUtils.accessWithinRange;
import static com.iotsmartaliv.utils.CommanUtils.utcToLocalTimeZone;

/**
 * This adapter class is used for video intercom device list.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2018-10-23
 */
public class VideoIntercomAdapter extends ArrayAdapter<VideoDeviceData> {
    Context mContext;
    VideoIntercomItemClick videoIntercomItemClick;

    private ArrayList<VideoDeviceData> dataSet;
    private boolean goToOpenDoor = false;

    /**
     * @param data                   is list of device item
     * @param context                this is a context of activity
     * @param videoIntercomItemClick this is listener for onclick item.
     */
    public VideoIntercomAdapter(ArrayList<VideoDeviceData> data, Context context, VideoIntercomItemClick videoIntercomItemClick) {
        super(context, R.layout.intercom_list_item, data);
        this.dataSet = data;
        this.videoIntercomItemClick = videoIntercomItemClick;
        this.mContext = context;
    }
    @Override
    public int getCount() {
        return dataSet.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View row = convertView;
        ViewHolder holder = null;
        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            row = inflater.inflate(R.layout.intercom_list_item, parent, false);
            holder = new ViewHolder();

            holder.relInboxItemsContainer = row.findViewById(R.id.rel_item_show);
            holder.tvIotName = row.findViewById(R.id.tv_iot_name);
            holder.call_btn = row.findViewById(R.id.call_btn);
            holder.tvInternetName = row.findViewById(R.id.tv_internet_name);
            holder.remotely_open_btn = row.findViewById(R.id.remotely_open_btn);
            holder.ll_intercomeCallandOpenDoor = row.findViewById(R.id.ll_callAndOpenDoor);
            holder.ll_intercomeOption = row.findViewById(R.id.ll_option);
            holder.img_option = row.findViewById(R.id.img_option);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        VideoDeviceData guestList = dataSet.get(position);

        if (guestList.getCdeviceName().length() > 0) {
            holder.tvIotName.setText(guestList.getCdeviceName());
        } else {
            holder.tvIotName.setText(guestList.getDeviceName());
        }
        holder.tvInternetName.setText(guestList.getDeviceSno());
        if (guestList.getRelayData() != null) {
            holder.ll_intercomeOption.setVisibility(View.VISIBLE);
            holder.ll_intercomeCallandOpenDoor.setVisibility(View.GONE);
        } else {
            holder.ll_intercomeOption.setVisibility(View.GONE);
            holder.ll_intercomeCallandOpenDoor.setVisibility(View.VISIBLE);
        }

        holder.remotely_open_btn.setOnClickListener(view -> {
            performOpenDoorOperation(position);
            //  Toast.makeText(mContext, ""+position, Toast.LENGTH_SHORT).show();
//            callGetServerAPI(position);
//            videoIntercomItemClick.onRemotelyOpenDoor(dataSet.get(position).getDeviceSno());
        });
        holder.call_btn.setOnClickListener(view -> videoIntercomItemClick.onClickIntercomDevice(dataSet.get(position)));
        holder.img_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String isAcessible = dataSet.get(position).getIsAccessTimeEnabled();

                if (isAcessible.equals("1")) {

                    callGetServerAPI(position, 1);
                } else {
                    videoIntercomItemClick.onOptionClickIntercomeDevice(guestList);
                }

            }
        });
        return row;
    }

    public void updateList(ArrayList<VideoDeviceData>  dataList) {
        dataSet = dataList;
        notifyDataSetChanged();
    }

    private void performOpenDoorOperation(int position) {

        String isAcessible = dataSet.get(position).getIsAccessTimeEnabled();

        if (isAcessible.equals("1")) {

            callGetServerAPI(position, 0);

        } else {

            goToOpenDoor = true;

            callOpenDoor(position, 0);

        }
    }


    private void callGetServerAPI(int position, int type) {
        ApiServiceProvider apiServiceProvider = ApiServiceProvider.getInstance(mContext);
        Util.checkInternet(mContext, new Util.NetworkCheckCallback() {
            @Override
            public void onNetworkCheckComplete(boolean isAvailable) {
                if (isAvailable) {
                    apiServiceProvider.callGetServerCurrentTime(new RetrofitListener<ResponseBody>() {
                        @Override
                        public void onResponseSuccess(ResponseBody sucessRespnse, String apiFlag) {
                            try {
                                JSONObject jsonObject = new JSONObject(sucessRespnse.string());
                                String dateTime = jsonObject.optString("date");
                                Date serverDate = utcToLocalTimeZone(dateTime);
                                String isAcessible = dataSet.get(position).getIsAccessTimeEnabled();

                                if (dataSet.get(position).getAccessStarttime() != null && !dataSet.get(position).getAccessStarttime().equalsIgnoreCase("")
                                        && dataSet.get(position).getAccessEndtime() != null && !dataSet.get(position).getAccessEndtime().equalsIgnoreCase("")) {

                                    Date startTime = utcToLocalTimeZone(dataSet.get(position).getAccessStarttime());
                                    Date endTime = utcToLocalTimeZone(dataSet.get(position).getAccessEndtime());

                                    goToOpenDoor = accessWithinRange(isAcessible, startTime, endTime, serverDate);
//                    SharePreference.getInstance(getActivity()).putString(getResources().getString(R.string.server_current_time), dateTime);
                                    Log.d("EndTimeeCheck: ", serverDate + " EndTime:" + endTime + " StartTime:" + startTime);
                                } else {
                                    goToOpenDoor = true;
                                }
                                callOpenDoor(position, type);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                            Util.firebaseEvent(Constant.APIERROR, mContext, Constant.UrlPath.SERVER_URL + apiFlag, LOGIN_DETAIL.getUsername(), LOGIN_DETAIL.getAppuserID(), errorObject.getStatus());


                        }
                    });

                }
            }
        });
    }


    private void callOpenDoor(int position, int type) {
        if (goToOpenDoor) {
            if (type == 1) {
                videoIntercomItemClick.onOptionClickIntercomeDevice(dataSet.get(position));
            } else {
                videoIntercomItemClick.onRemotelyOpenDoor(dataSet.get(position).getDeviceSno());
            }
        } else {
            Toast.makeText(mContext, "User can not access at this time", Toast.LENGTH_SHORT).show();
        }

    }


    // View lookup cache
    static class ViewHolder {
        RelativeLayout relInboxItemsContainer;
        TextView tvIotName, tvInternetName;
        ImageView remotely_open_btn, call_btn, img_option;

        LinearLayout ll_intercomeCallandOpenDoor, ll_intercomeOption;
    }

}