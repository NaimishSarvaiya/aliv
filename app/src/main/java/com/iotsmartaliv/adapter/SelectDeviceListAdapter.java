package com.iotsmartaliv.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iotsmartaliv.R;
import com.iotsmartaliv.apiAndSocket.models.DeviceObject;

import java.util.List;

public class SelectDeviceListAdapter extends RecyclerView.Adapter<SelectDeviceListAdapter.ViewHolder> {
    NotifyDialogSelectList notifyDialogSelectList;
    private List<DeviceObject> deviceObjects;

    public SelectDeviceListAdapter(List<DeviceObject> deviceObjects, NotifyDialogSelectList notifyDialogSelectList) {
        this.deviceObjects = deviceObjects;
        this.notifyDialogSelectList = notifyDialogSelectList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.device_row_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        if (deviceObjects.get(position).getCdeviceName().length() > 0) {
            viewHolder.tv_device_name.setText(deviceObjects.get(position).getCdeviceName());
        } else {
            viewHolder.tv_device_name.setText(deviceObjects.get(position).getDeviceName());
        }
    }

    @Override
    public int getItemCount() {
        return deviceObjects.size();
    }

    public void updateItem(List<DeviceObject> deviceObject) {
        this.deviceObjects = deviceObject;
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        deviceObjects.get(position).setRssi(0);
        deviceObjects.remove(position);
        notifyDataSetChanged();
        notifyDialogSelectList.removeNotify();
    }

    public interface NotifyDialogSelectList {
        void removeNotify();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_device_name;
        ImageView imageView_remove;

        public ViewHolder(View view) {
            super(view);
            tv_device_name = view.findViewById(R.id.tv_device_name);
            imageView_remove = view.findViewById(R.id.imageView_remove);
            imageView_remove.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            removeItem(getAdapterPosition());
        }
    }
}