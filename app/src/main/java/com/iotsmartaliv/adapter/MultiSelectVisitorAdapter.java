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

/**
 * This class is used as .
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 10/8/19 :August.
 */
public class MultiSelectVisitorAdapter extends RecyclerView.Adapter<MultiSelectVisitorAdapter.ViewHolder> {
    private List<VisitorData> mDataset, oldList;

    public MultiSelectVisitorAdapter(List<VisitorData> mDataseta, List<VisitorData> oldList) {
        mDataset = mDataseta;
        this.oldList = oldList;
        ifAlreadyCheck();

    }

    public void ifAlreadyCheck() {
        for (VisitorData visitorData : oldList) {
            if (visitorData.isCheck()) {
                for (VisitorData visitorData1 : mDataset) {
                    if (visitorData.getVisitorID().equalsIgnoreCase(visitorData1.getVisitorID())) {
                        visitorData1.setCheck(true);
                    }
                }
            }

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.device_multi_select_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.checkbox.setText(mDataset.get(i).getUvisitorName());
        viewHolder.checkbox.setOnCheckedChangeListener(null);
        viewHolder.checkbox.setChecked(mDataset.get(i).isCheck());
        viewHolder.checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> mDataset.get(viewHolder.getAdapterPosition()).setCheck(isChecked));

    }

    public void updateList(List<VisitorData> temp) {
        mDataset = temp;
        ifAlreadyCheck();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public CheckBox checkbox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkbox = itemView.findViewById(R.id.checkbox);

        }
    }
}
