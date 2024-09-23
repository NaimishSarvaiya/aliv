package com.iotsmartaliv.adapter.automation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iotsmartaliv.R;
import com.iotsmartaliv.apiAndSocket.models.IntercomRelayData;
import com.iotsmartaliv.interfaces.VideoIntercomItemClick;

import java.util.List;

/**
 * This adapter class is used for setup cards.
 *
 * @version 1.0
 * @since 2018-10-23
 */
public class IntercomeRelayAdapter extends RecyclerView.Adapter<IntercomeRelayAdapter.DeviceViewHolder> {
    private List<IntercomRelayData> relayData;
    private LayoutInflater inflater;
    private Context context;
    VideoIntercomItemClick videoIntercomItemClick;
    public IntercomeRelayAdapter(Context context, List<IntercomRelayData> relayData,VideoIntercomItemClick videoIntercomItemClick) {
        this.context = context;
        this.relayData = relayData;
        this.videoIntercomItemClick = videoIntercomItemClick;
        inflater = LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public IntercomeRelayAdapter.DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new IntercomeRelayAdapter.DeviceViewHolder(inflater.inflate(R.layout.intercome_relay_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceViewHolder holder, int position) {
        IntercomRelayData relayItem = relayData.get(position);
//        if (position == (relayData.size()-1)){
//            holder.lineView.setVisibility(View.GONE);
//        }else {
//            holder.lineView.setVisibility(View.VISIBLE);
//        }
        holder.tv_rely.setText( relayItem.getRelayName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoIntercomItemClick.onRelayClick(relayItem);
//                Toast.makeText(context, relayItem.getAutomationDeviceId(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return relayData.size();
    }

    public class DeviceViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_rely;
        public View lineView;

        public DeviceViewHolder(View v) {
            super(v);
            tv_rely = v.findViewById(R.id.tv_rely);
            lineView = v.findViewById(R.id.view_line);
        }
    }


}
