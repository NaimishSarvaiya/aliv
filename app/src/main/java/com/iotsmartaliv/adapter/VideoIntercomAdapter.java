package com.iotsmartaliv.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iotsmartaliv.R;
import com.iotsmartaliv.activity.DeviceDetailActivity;
import com.iotsmartaliv.apiCalling.listeners.RetrofitListener;
import com.iotsmartaliv.apiCalling.models.DeviceObject;
import com.iotsmartaliv.apiCalling.models.ErrorObject;
import com.iotsmartaliv.apiCalling.retrofit.ApiServiceProvider;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.interfaces.VideoIntercomItemClick;
import com.iotsmartaliv.utils.SharePreference;
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
public class VideoIntercomAdapter extends ArrayAdapter<DeviceObject> {
    Context mContext;
    VideoIntercomItemClick videoIntercomItemClick;
    private ArrayList<DeviceObject> dataSet;
    private boolean goToOpenDoor=false;

    /**
     * @param data                   is list of device item
     * @param context                this is a context of activity
     * @param videoIntercomItemClick this is listener for onclick item.
     */
    public VideoIntercomAdapter(ArrayList<DeviceObject> data, Context context, VideoIntercomItemClick videoIntercomItemClick) {
        super(context, R.layout.intercom_list_item, data);
        this.dataSet = data;
        this.videoIntercomItemClick = videoIntercomItemClick;
        this.mContext = context;
    }
/*
    @Override
    public int getCount() {
        return dataSet.size();
    }*/

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
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        DeviceObject guestList = dataSet.get(position);

        if (guestList.getCdeviceName().length() > 0) {
            holder.tvIotName.setText(guestList.getCdeviceName());
        } else {
            holder.tvIotName.setText(guestList.getDeviceName());
        }
        holder.tvInternetName.setText(guestList.getDeviceSno());
        holder.remotely_open_btn.setOnClickListener(view -> {

            performOpenDoorOperation(position);
            //  Toast.makeText(mContext, ""+position, Toast.LENGTH_SHORT).show();
//            callGetServerAPI(position);
//            videoIntercomItemClick.onRemotelyOpenDoor(dataSet.get(position).getDeviceSno());
        });
        holder.call_btn.setOnClickListener(view -> videoIntercomItemClick.onClickIntercomDevice(dataSet.get(position)));

        return row;









/*




        // Get the data item for this position
        DeviceObject guestList = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        VideoIntercomAdapter.ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new VideoIntercomAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.intercom_list_item, parent, false);
            viewHolder.relInboxItemsContainer = convertView.findViewById(R.id.rel_item_show);
            viewHolder.tvIotName = convertView.findViewById(R.id.tv_iot_name);
            viewHolder.call_btn = convertView.findViewById(R.id.call_btn);
            viewHolder.tvInternetName = convertView.findViewById(R.id.tv_internet_name);
            viewHolder.remotely_open_btn = convertView.findViewById(R.id.remotely_open_btn);
            viewHolder.call_btn.setTag(position);
            viewHolder.remotely_open_btn.setTag(position);


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (VideoIntercomAdapter.ViewHolder) convertView.getTag();
        }
        if (guestList.getCdeviceName().length() > 0) {
            viewHolder.tvIotName.setText(guestList.getCdeviceName());
        } else {
            viewHolder.tvIotName.setText(guestList.getDeviceName());
        }
        viewHolder.tvInternetName.setText(guestList.getDeviceSno());
        viewHolder.remotely_open_btn.setOnClickListener(view -> videoIntercomItemClick.onRemotelyOpenDoor(dataSet.get((Integer) view.getTag()).getDeviceSno()));
        viewHolder.call_btn.setOnClickListener(view -> videoIntercomItemClick.onClickIntercomDevice(dataSet.get((Integer) view.getTag())));

        // Return the completed view to render on screen
        return convertView;*/
    }

    private void performOpenDoorOperation(int position) {

        String isAcessible = SharePreference.getInstance(mContext).getString("isAccessable");

        if (isAcessible.equals("1")) {

            callGetServerAPI(position);

        } else {

            goToOpenDoor = true;

            callOpenDoor(position);

        }
    }


    private void callGetServerAPI(int position) {
        ApiServiceProvider apiServiceProvider = ApiServiceProvider.getInstance(mContext);
            Util.checkInternet(mContext, new Util.NetworkCheckCallback() {
                @Override
                public void onNetworkCheckComplete(boolean isAvailable) {
                  if (isAvailable){
                      apiServiceProvider.callGetServerCurrentTime(new RetrofitListener<ResponseBody>() {
                          @Override
                          public void onResponseSuccess(ResponseBody sucessRespnse, String apiFlag) {
                              try {
                                  JSONObject jsonObject = new JSONObject(sucessRespnse.string());
                                  String dateTime = jsonObject.optString("date");
                                  Date serverDate = utcToLocalTimeZone(dateTime);
                                  String isAcessible = SharePreference.getInstance(mContext).getString("isAccessable");

                                  if (!SharePreference.getInstance(mContext).getString("deviceStartTime").equalsIgnoreCase("")
                                          && !SharePreference.getInstance(mContext).getString("deviceEndTime").equalsIgnoreCase("")) {

                                      Date startTime = utcToLocalTimeZone(SharePreference.getInstance(mContext).getString("deviceStartTime"));
                                      Date endTime = utcToLocalTimeZone(SharePreference.getInstance(mContext).getString("deviceEndTime"));

                                      goToOpenDoor = accessWithinRange(isAcessible, startTime, endTime, serverDate);
//                    SharePreference.getInstance(getActivity()).putString(getResources().getString(R.string.server_current_time), dateTime);
                                      Log.d("EndTimeeCheck: ", serverDate + " EndTime:" + endTime + " StartTime:" + startTime);
                                  } else {
                                      goToOpenDoor = true;
                                  }
                                  callOpenDoor(position);
                              } catch (Exception e) {
                                  e.printStackTrace();
                              }
                          }

                          @Override
                          public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                              Util.firebaseEvent(Constant.APIERROR, mContext,Constant.UrlPath.SERVER_URL+apiFlag, LOGIN_DETAIL.getUsername(),LOGIN_DETAIL.getAppuserID(),errorObject.getStatus());


                          }
                      });

                  }
                }
            });
        }


    private void callOpenDoor(int position){
        if (goToOpenDoor) {
            videoIntercomItemClick.onRemotelyOpenDoor(dataSet.get(position).getDeviceSno());
        }else {
            Toast.makeText(mContext, "User can not access at this time", Toast.LENGTH_SHORT).show();
        }

    }


    // View lookup cache
    static class ViewHolder {
        RelativeLayout relInboxItemsContainer;
        TextView tvIotName, tvInternetName;
        ImageView remotely_open_btn, call_btn;
    }
}