package com.iotsmartaliv.adapter.automation;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iotsmartaliv.R;
import com.iotsmartaliv.apiCalling.models.TimeSlot;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used as .
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 19/8/19 :August : 2019 on 14 : 47.
 */
public class TimeSlotsMondayAdapter extends RecyclerView.Adapter<TimeSlotsMondayAdapter.ViewHolder> {
    ArrayList<TimeSlot> timeList = new ArrayList<>();
    DeleteTimeSlotListener deleteTimeSlotListener;
    boolean isDilog = false;

    public TimeSlotsMondayAdapter() {
    }

    public TimeSlotsMondayAdapter(boolean isDialog) {
        isDilog = isDialog;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_time_slot_schedules, viewGroup, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.tv_time.setText((timeList.get(i).getStartTime() + "-" + timeList.get(i).getEndTime()));
        viewHolder.rl_delete.setOnClickListener(v -> {
            if (deleteTimeSlotListener == null) {
                removeItem(i);
            } else {
                deleteTimeSlotListener.deleteTimeSlot(timeList.get(i), i, TimeSlotsMondayAdapter.this);
            }
        });
        viewHolder.rl_delete.setVisibility(isDilog ? View.GONE : View.VISIBLE);
    }

    public void setOnDeleteTimeSlotListener(DeleteTimeSlotListener deleteTimeSlotListener) {
        this.deleteTimeSlotListener = deleteTimeSlotListener;
    }

    public void removeItem(int pos) {
        timeList.remove(pos);
        notifyDataSetChanged();
    }

    public void addItem(TimeSlot item) {
        this.timeList.add(item);
        notifyDataSetChanged();
    }

    public void addItemAll(List<TimeSlot> timeList) {
        if (timeList != null) {
            this.timeList = new ArrayList<>(timeList);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return timeList.size();
    }

    public ArrayList<TimeSlot> getItem() {
        return timeList;
    }

    public interface DeleteTimeSlotListener {
        void deleteTimeSlot(TimeSlot timeSlot, int position, TimeSlotsMondayAdapter timeSlotsMondayAdapter);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_time;
        RelativeLayout rl_delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_time = itemView.findViewById(R.id.tv_time);
            rl_delete = itemView.findViewById(R.id.rl_delete);
        }
    }
}
