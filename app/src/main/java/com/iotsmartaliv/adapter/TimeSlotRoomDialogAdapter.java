package com.iotsmartaliv.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.iotsmartaliv.R;
import com.iotsmartaliv.apiAndSocket.models.TimeSlot;

import java.util.List;

public class TimeSlotRoomDialogAdapter extends RecyclerView.Adapter<TimeSlotRoomDialogAdapter.DeviceViewHolder> {
    private List<TimeSlot> mDataset;

    public TimeSlotRoomDialogAdapter(List<TimeSlot> mDataseta) {
        mDataset = mDataseta;
    }

    @NonNull
    @Override
    public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new DeviceViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.select_time_slot_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceViewHolder viewHolder, int position) {
        viewHolder.checkbox_time_slot.setText((mDataset.get(position).getStartTime() + " TO " + mDataset.get(position).getEndTime()));
        viewHolder.checkbox_time_slot.setOnCheckedChangeListener(null);
        viewHolder.checkbox_time_slot.setChecked(mDataset.get(position).isSelected());
        viewHolder.checkbox_time_slot.setOnCheckedChangeListener((buttonView, isChecked) -> mDataset.get(position).setSelected(isChecked));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class DeviceViewHolder extends RecyclerView.ViewHolder {
        public CheckBox checkbox_time_slot;

        public DeviceViewHolder(View v) {
            super(v);
            checkbox_time_slot = v.findViewById(R.id.checkbox_time_slot);
        }

    }
}


