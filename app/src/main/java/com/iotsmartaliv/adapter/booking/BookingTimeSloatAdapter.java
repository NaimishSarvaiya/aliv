package com.iotsmartaliv.adapter.booking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.iotsmartaliv.R;
import com.iotsmartaliv.model.booking.TimeSlotDataModel;
import com.iotsmartaliv.model.booking.TimeSlotModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class BookingTimeSloatAdapter extends RecyclerView.Adapter<BookingTimeSloatAdapter.BookListViewHolder> {
    private Context context;
    private List<TimeSlotDataModel> timeSlotList;
    private int selectedPosition = -1; // Track the selected time slot
    private OnTimeSlotClickListener listener;

    public BookingTimeSloatAdapter(Context context, List<TimeSlotDataModel> timeSlotList, OnTimeSlotClickListener listener) {
        this.context = context;
        this.timeSlotList = timeSlotList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BookListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.time_slot_item, parent, false);
        return new BookListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookListViewHolder holder, int position) {
        TimeSlotDataModel timeSlot = timeSlotList.get(position);
        SimpleDateFormat inputFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        String formattedEndTime;
        String formattedStartTime;
        try {
             formattedEndTime = outputFormat.format(inputFormat.parse(timeSlot.getEndTime()));
             formattedStartTime = outputFormat.format(inputFormat.parse(timeSlot.getStartTime()));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }


        // Bind the time slot data to UI elements
        holder.tvStartTime.setText(formattedStartTime);
        holder.tvEndTime.setText(formattedEndTime);

        // Set the background based on the slot's state
        if (timeSlot.getIsBooked() != null && timeSlot.getIsBooked()) {
            // Unavailable time slot (Booked)
            holder.rlTimeSlot.setBackgroundResource(R.drawable.bg_unavailable_slot);
            holder.rlTimeSlot.setOnClickListener(null);// Disable clicking on unavailable slots
            holder.tvStartTime.setTextColor(ContextCompat.getColor(context, R.color.white));
            holder.tvEndTime.setTextColor(ContextCompat.getColor(context, R.color.white));
        } else {
            // Available slot
            if (selectedPosition == position) {
                // Slot is selected
                holder.rlTimeSlot.setBackgroundResource(R.drawable.bg_selected_time_slot);
                holder.tvStartTime.setTextColor(ContextCompat.getColor(context, R.color.white));
                holder.tvEndTime.setTextColor(ContextCompat.getColor(context, R.color.white));
            } else {
                // Slot is available but not selected
                holder.rlTimeSlot.setBackgroundResource(R.drawable.bg_available_time_slot);
                holder.tvStartTime.setTextColor(ContextCompat.getColor(context, R.color.newNavyBuleBaseColor));
                holder.tvEndTime.setTextColor(ContextCompat.getColor(context, R.color.newNavyBuleBaseColor));
            }

            // Set click listener for available time slot
            holder.rlTimeSlot.setOnClickListener(v -> {
                if (selectedPosition == holder.getAdapterPosition()) {
                    // Deselect if the same slot is clicked
                    int previousPosition = selectedPosition;
                    selectedPosition = -1; // Clear selection
                    notifyItemChanged(previousPosition);
                    listener.onTimeSlotSelected(null); // Notify listener of deselection
                } else {
                    // Select the new slot
                    int previousPosition = selectedPosition;
                    selectedPosition = holder.getAdapterPosition();
                    notifyItemChanged(previousPosition); // Update old selection
                    notifyItemChanged(selectedPosition); // Update new selection
                    listener.onTimeSlotSelected(timeSlot); // Notify listener of selection
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return timeSlotList != null ? timeSlotList.size() : 0;
    }

    // Method to update the adapter with new data
    public void setTimeSlotList(List<TimeSlotDataModel> timeSlotList) {
        this.timeSlotList = timeSlotList;
        notifyDataSetChanged();
    }

    public void removeTimes() {
        timeSlotList.clear();
        selectedPosition = -1;
        notifyDataSetChanged();
    }

    public class BookListViewHolder extends RecyclerView.ViewHolder {
        LinearLayout rlTimeSlot;
        TextView tvStartTime, tvEndTime;

        public BookListViewHolder(@NonNull View itemView) {
            super(itemView);
            rlTimeSlot = itemView.findViewById(R.id.rl_timeSlot);
            tvStartTime = itemView.findViewById(R.id.tv_timestart);
            tvEndTime = itemView.findViewById(R.id.tv_timeEnd);
        }
    }

    // Listener interface inside the adapter
    public interface OnTimeSlotClickListener {
        void onTimeSlotSelected(TimeSlotDataModel timeSlotData);
    }
}
