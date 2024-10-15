package com.iotsmartaliv.utils.booking;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CalendarView;
import android.widget.Toast;

public class CustomCalendarView extends CalendarView {

    public CustomCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeCalendarView();
    }

    private void initializeCalendarView() {
        // Customize the CalendarView appearance
        setShowWeekNumber(false);  // Hide the week number column
        setFirstDayOfWeek(1);      // Set Monday as the first day of the week

        // Set Min and Max date range (optional)
        setMinDate(System.currentTimeMillis() - 1000); // Today and onward

        // Set listener to handle date selection events
        setOnDateChangeListener(new OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // Handle date selection
                Toast.makeText(getContext(), "Selected Date: " + dayOfMonth + "/" + (month + 1) + "/" + year, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
