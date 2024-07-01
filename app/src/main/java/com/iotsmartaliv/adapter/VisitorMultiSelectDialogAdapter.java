package com.iotsmartaliv.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.iotsmartaliv.R;
import com.iotsmartaliv.model.VisitorData;

import java.util.List;

public class VisitorMultiSelectDialogAdapter extends RecyclerView.Adapter<VisitorMultiSelectDialogAdapter.DeviceViewHolder> {
    private List<VisitorData> mDataset;

    public VisitorMultiSelectDialogAdapter(List<VisitorData> mDataseta) {
        mDataset = mDataseta;
    }

    @NonNull
    @Override
    public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new DeviceViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.device_multi_select_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceViewHolder viewHolder, int position) {
        viewHolder.checkbox.setText(mDataset.get(position).getUvisitorName());
        viewHolder.checkbox.setOnCheckedChangeListener(null);
        viewHolder.checkbox.setChecked(mDataset.get(position).isCheck());
        viewHolder.checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> mDataset.get(position).setCheck(isChecked));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    public void updateList(List<VisitorData> temp) {
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


