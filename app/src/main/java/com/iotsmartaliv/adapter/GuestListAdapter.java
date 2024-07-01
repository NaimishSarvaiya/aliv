package com.iotsmartaliv.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iotsmartaliv.R;
import com.iotsmartaliv.model.GuestListModel;

import java.util.ArrayList;

/**
 * This adapter class is used for guest list item.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2018-10-25
 */

public class GuestListAdapter extends ArrayAdapter<GuestListModel> implements View.OnClickListener {

    Context mContext;
    private ArrayList<GuestListModel> dataSet;
    private int lastPosition = -1;


    public GuestListAdapter(ArrayList<GuestListModel> data, Context context) {
        super(context, R.layout.guest_list_item, data);
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
        GuestListModel guestList = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        GuestListAdapter.ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {
            viewHolder = new GuestListAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.guest_list_item, parent, false);
            viewHolder.relInboxItemsContainer = convertView.findViewById(R.id.rel_item_show);
            viewHolder.tvGuest = convertView.findViewById(R.id.tv_guest);
            viewHolder.tvDeviceName = convertView.findViewById(R.id.tv_device_name);

            result = convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (GuestListAdapter.ViewHolder) convertView.getTag();
            result = convertView;
        }

        lastPosition = position;

        viewHolder.tvGuest.setText(guestList.getGuestName());
        viewHolder.tvDeviceName.setText(guestList.getDeviceName());

        // Return the completed view to render on screen
        return convertView;
    }

    // View lookup cache
    private static class ViewHolder {
        RelativeLayout relInboxItemsContainer;
        TextView tvGuest;
        TextView tvDeviceName;
    }
}