package com.iotsmartaliv.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.iotsmartaliv.R;
import com.iotsmartaliv.apiCalling.models.DeviceObject;

import java.util.ArrayList;
import java.util.List;

public class DeviceMultiSelectDialogAdapter extends RecyclerView.Adapter<DeviceMultiSelectDialogAdapter.DeviceViewHolder> {
    private List<DeviceObject> mDataset;

    public DeviceMultiSelectDialogAdapter(List<DeviceObject> mDataseta) {
        mDataset = mDataseta;
    }

    @NonNull
    @Override
    public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new DeviceViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.device_multi_select_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceViewHolder viewHolder, int position) {
        if (mDataset.get(position).getCdeviceName().length() > 0) {
            viewHolder.checkbox.setText(mDataset.get(position).getCdeviceName());
        } else {
            viewHolder.checkbox.setText(mDataset.get(position).getDeviceName());
        }
        viewHolder.checkbox.setOnCheckedChangeListener(null);
        viewHolder.checkbox.setChecked(mDataset.get(position).getRssi() != 0);
        viewHolder.checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> mDataset.get(position).setRssi(isChecked ? 1 : 0));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public List<DeviceObject> getSelectItem() {
        List<DeviceObject> list = new ArrayList<>();
        for (DeviceObject deviceObject : mDataset) {
            if (deviceObject.getRssi() != 0) {
                list.add(deviceObject);
            }
        }
        return list;
    }


    public class DeviceViewHolder extends RecyclerView.ViewHolder {
        public CheckBox checkbox;

        public DeviceViewHolder(View v) {
            super(v);
            checkbox = v.findViewById(R.id.checkbox);
        }

    }
}


