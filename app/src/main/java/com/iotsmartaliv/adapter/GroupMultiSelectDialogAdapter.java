package com.iotsmartaliv.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.iotsmartaliv.R;
import com.iotsmartaliv.apiCalling.models.GroupData;

import java.util.List;

public class GroupMultiSelectDialogAdapter extends RecyclerView.Adapter<GroupMultiSelectDialogAdapter.DeviceViewHolder> {
    private List<GroupData> mDataset;

    public GroupMultiSelectDialogAdapter(List<GroupData> mDataseta) {
        mDataset = mDataseta;
    }

    @NonNull
    @Override
    public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new DeviceViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.device_multi_select_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceViewHolder viewHolder, int position) {
        viewHolder.checkbox.setText(mDataset.get(position).getGroupName());
        viewHolder.checkbox.setOnCheckedChangeListener(null);
        viewHolder.checkbox.setChecked(mDataset.get(position).isSelected());
        viewHolder.checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> mDataset.get(position).setSelected(isChecked));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    public void updateList(List<GroupData> temp) {
        mDataset = temp;
        notifyDataSetChanged();
    }

    public class DeviceViewHolder extends RecyclerView.ViewHolder {
        public CheckBox checkbox;

        public DeviceViewHolder(View v) {
            super(v);
            checkbox = v.findViewById(R.id.checkbox);
        }

    }
}


