package com.iotsmartaliv.adapter.booking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.iotsmartaliv.R;

import java.util.Calendar;
import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.DateViewHolder> {

    private final List<Calendar> dateList;
    private final Calendar startDateCalendar;
    private final Calendar endDateCalendar;
    private Calendar selectedDate = null;

    public CalendarAdapter(List<Calendar> dateList, Calendar startDateCalendar, Calendar endDateCalendar) {
        this.dateList = dateList;
        this.startDateCalendar = startDateCalendar;
        this.endDateCalendar = endDateCalendar;
    }

    @Override
    public DateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_calendar_day, parent, false);
        return new DateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DateViewHolder holder, int position) {
        Calendar date = dateList.get(position);
        int dayOfMonth = date.get(Calendar.DAY_OF_MONTH);
        holder.dayTextView.setText(String.valueOf(dayOfMonth));

        if (date.before(startDateCalendar) || date.after(endDateCalendar)) {
            holder.dayTextView.setBackgroundResource(R.drawable.bg_unavailable_date);
            holder.dayTextView.setTextColor(ContextCompat.getColor(holder.dayTextView.getContext(), R.color.white));
        } else {
            if (selectedDate != null && date.equals(selectedDate)) {
                holder.dayTextView.setBackgroundResource(R.drawable.bg_selected_date);
                holder.dayTextView.setTextColor(ContextCompat.getColor(holder.dayTextView.getContext(), R.color.black));
            } else {
                holder.dayTextView.setBackgroundResource(R.drawable.bg_available_date);
                holder.dayTextView.setTextColor(ContextCompat.getColor(holder.dayTextView.getContext(), R.color.white));
            }
        }

        holder.itemView.setOnClickListener(v -> {
            if ((date.after(startDateCalendar) && date.before(endDateCalendar)) || date.equals(startDateCalendar) || date.equals(endDateCalendar)) {
                if (selectedDate != null && selectedDate.equals(date)) {
                    selectedDate = null; // Deselect if clicked again
                } else {
                    selectedDate = (Calendar) date.clone(); // Select new date
                }
                notifyDataSetChanged(); // Refresh selection state
            }
        });
    }

    @Override
    public int getItemCount() {
        return dateList.size();
    }

    static class DateViewHolder extends RecyclerView.ViewHolder {
        TextView dayTextView;

        DateViewHolder(View itemView) {
            super(itemView);
            dayTextView = itemView.findViewById(R.id.tv_day);
        }
    }
}
