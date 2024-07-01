package com.iotsmartaliv.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iotsmartaliv.R;
import com.iotsmartaliv.model.VisitorData;

import java.util.List;

/**
 * This class is used as .
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 12/8/19 :August.
 */
public class SelectedVisitorListAdapter extends RecyclerView.Adapter<SelectedVisitorListAdapter.ViewHolder> {
    NotifyDialogSelectList notifyDialogSelectList;
    private List<VisitorData> visitorDataList;

    public SelectedVisitorListAdapter(List<VisitorData> deviceObjects, NotifyDialogSelectList notifyDialogSelectList) {
        this.visitorDataList = deviceObjects;
        this.notifyDialogSelectList = notifyDialogSelectList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.device_row_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.tv_visitor_name.setText(visitorDataList.get(i).getUvisitorName());
    }

    @Override
    public int getItemCount() {
        return visitorDataList.size();
    }

    public void updateItem(List<VisitorData> visitorData) {
        this.visitorDataList = visitorData;
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        //deviceObjects.get(position).setRssi(0);
        visitorDataList.remove(position);
        notifyDataSetChanged();
        notifyDialogSelectList.removeNotify();
    }

    public List<VisitorData> getVisitors() {
        return visitorDataList;
    }

    public interface NotifyDialogSelectList {
        void removeNotify();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_visitor_name;
        ImageView imageView_remove;

        public ViewHolder(View view) {
            super(view);
            tv_visitor_name = view.findViewById(R.id.tv_device_name);
            imageView_remove = view.findViewById(R.id.imageView_remove);
            imageView_remove.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            removeItem(getAdapterPosition());
        }
    }
}
