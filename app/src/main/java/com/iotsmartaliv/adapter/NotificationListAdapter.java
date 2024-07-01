package com.iotsmartaliv.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iotsmartaliv.R;
import com.iotsmartaliv.model.NotificationModel;

import java.util.ArrayList;

/**
 * This adapter class is used for notifications .
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2018-10-23
 */
public class NotificationListAdapter extends ArrayAdapter<NotificationModel> implements View.OnClickListener {

    Context mContext;
    private ArrayList<NotificationModel> dataSet;
    private int lastPosition = -1;

    /**
     * @param data
     * @param context
     */
    public NotificationListAdapter(ArrayList<NotificationModel> data, Context context) {
        super(context, R.layout.notification_list_item, data);
        this.dataSet = data;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return dataSet.size();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        NotificationModel guestList = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        NotificationListAdapter.ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {
            viewHolder = new NotificationListAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.notification_list_item, parent, false);
            viewHolder.relInboxItemsContainer = convertView.findViewById(R.id.rel_item_show);
            viewHolder.tvNotification = convertView.findViewById(R.id.tv_notification);
            viewHolder.tvNotificationTime = convertView.findViewById(R.id.tv_time);
            result = convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (NotificationListAdapter.ViewHolder) convertView.getTag();
            result = convertView;
        }

        lastPosition = position;

        viewHolder.tvNotification.setText(guestList.getNotificationString());
        viewHolder.tvNotificationTime.setText(guestList.getNotificationTime());

        // Return the completed view to render on screen
        return convertView;
    }

    // View lookup cache
    private static class ViewHolder {
        RelativeLayout relInboxItemsContainer;
        TextView tvNotification, tvNotificationTime;
    }
}
