package com.iotsmartaliv.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.iotsmartaliv.R;
import com.iotsmartaliv.model.RewardsModels;

import java.util.ArrayList;

/**
 * This adapter class is used for rewards.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2018-10-23
 */
public class RewardsAdapter extends ArrayAdapter<RewardsModels> implements View.OnClickListener {
    Context mContext;
    private ArrayList<RewardsModels> dataSet;
    private int lastPosition = -1;

    /**
     * @param data
     * @param context
     */
    public RewardsAdapter(ArrayList<RewardsModels> data, Context context) {
        super(context, R.layout.rewards_item, data);
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
        RewardsModels guestList = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        RewardsAdapter.ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {
            viewHolder = new RewardsAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.rewards_item, parent, false);
            //viewHolder.offerImage = (ImageView) convertView.findViewById(R.id.offer_image);

            result = convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (RewardsAdapter.ViewHolder) convertView.getTag();
            result = convertView;
        }

        lastPosition = position;
        /*Glide.with(mContext)
                .load(guestList.getOffersImageUrl())
                .into(viewHolder.offerImage);*/


        // Return the completed view to render on screen
        return convertView;
    }

    // View lookup cache
    private static class ViewHolder {
        ImageView offerImage;
    }
}