package com.iotsmartaliv.dialog_box;

import android.app.Activity;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.iotsmartaliv.R;
import com.iotsmartaliv.utils.CommanUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class StartEndTimePickerDialog extends Dialog {

    Activity activity;
    int clickcount = 0;
    String selectedhour, selectedmin;
    String startTime, endTime, endHour, hourDay, startHour, time1, time2;
    StartEndTimePickerListner startEndTimePickerListner;
    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
    private TextView tvShowTime, tv_end_time, tv_start_time;
    private TimePicker.OnTimeChangedListener timeChangedListener = new TimePicker.OnTimeChangedListener() {
        @Override
        public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
            int hour = view.getCurrentHour();
            int min = view.getCurrentMinute();
            if (hour < 10) selectedhour = "0" + hour;
            else selectedhour = String.valueOf(hour);
            if (min < 10) selectedmin = "0" + min;
            else selectedmin = String.valueOf(min);
            if (hour == 0) selectedhour = "12";
            tvShowTime.setText(selectedhour + ":" + selectedmin);

           /* String selected= hourOfDay + ":" + minute;
            Date dt=new Date();
            try {
                dt = sdf.parse(selected);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String time = sdf.format(dt);
            tvShowTime.setText(time);*/
        }
    };

    public StartEndTimePickerDialog(Activity activity, StartEndTimePickerListner startEndTimePickerListner) {
        super(activity);
        this.activity = activity;
        this.startEndTimePickerListner = startEndTimePickerListner;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.time_picker_start_end);
        Window window = getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TimePicker timePicker = findViewById(R.id.timepicker);
        timePicker.setIs24HourView(true);
        LinearLayout llStartTime = findViewById(R.id.ll_start_time);
        LinearLayout llEndTime = findViewById(R.id.ll_end_time);
        tv_start_time = findViewById(R.id.tv_start_time);
        tv_end_time = findViewById(R.id.tv_end_time);
        TextView tvDone = findViewById(R.id.tv_done);
        TextView tvNext = findViewById(R.id.tv_next);
        tvShowTime = findViewById(R.id.tv_showTime);
        timePicker.setOnTimeChangedListener(timeChangedListener);
        tvDone.setOnClickListener(view -> {
            if (tv_start_time.getText().toString().trim().length() < 1) {
                Toast.makeText(activity, "Please select start time.", Toast.LENGTH_SHORT).show();
            } else if (tv_end_time.getText().toString().trim().length() < 1) {
                Toast.makeText(activity, "Please select end time", Toast.LENGTH_SHORT).show();
            } else {
                /*if (dateFormatValidation.parse(endTime1).before(dateFormatValidation.parse(startTime1))) {
                    Toast.makeText(getContext(), "End time should not be less than to start time.", Toast.LENGTH_SHORT).show();
                    return;
                }
                */
                startEndTimePickerListner.selectedTime(CommanUtils.convert24To12Time(tv_start_time.getText().toString() + ":00"), CommanUtils.convert24To12Time(tv_end_time.getText().toString() + ":00"));
                dismiss();
            }
        });
        tvNext.setOnClickListener(view -> {
            clickcount = clickcount + 1;
            if (clickcount > 2) clickcount = 1;
            int hour, minute;
            String hours = "", min = "";
            String am_pm;
            if (Build.VERSION.SDK_INT >= 23) {
                hour = timePicker.getHour();
                minute = timePicker.getMinute();
                hours = String.valueOf(hour);
                min = String.valueOf(minute);
                hourDay = String.valueOf(hour);
                if (hour < 10) {
                    hourDay = "0" + hour;
                    hours = "0" + hour;
                }
                if (minute < 10) {
                    min = "0" + minute;
                }
            } else {
                hour = timePicker.getCurrentHour();
                minute = timePicker.getCurrentMinute();
                hours = String.valueOf(hour);
                min = String.valueOf(minute);
                hourDay = String.valueOf(hour);
                if (hour < 10) {
                    hours = "0" + hour;
                    hourDay = "0" + hour;
                }
                if (minute < 10) {
                    min = "0" + minute;
                }
            }
            if (hour > 12) {
                am_pm = "PM";
                // hour = hour - 12;
                hours = "" + (hour - 12);
                if (Integer.parseInt(hours) < 10) hours = "0" + hours;
                if (hour < 10) {
                    //  hourDay = "0" + hour;
                }
                hourDay = String.valueOf(hour);
                //hours=String.valueOf(hour);
            } else {
                am_pm = "AM";
                if (hour == 12) {
                    am_pm = "PM";
                    hourDay = String.valueOf(hour);
                }
                if (hour < 10) {
                    hours = "0" + hour;
                    hourDay = "0" + hour;
                }
            }
            if (hour == 0) {
                hour = 12;
                hours = "12";
                am_pm = "AM";
                hourDay = String.valueOf(hour);
            }
            if (clickcount == 1) {
                startTime = hourDay + ":" + min + ":" + "00";
                tv_start_time.setText(hourDay + ":" + min);
                time1 = hourDay + ":" + min;
                startHour = hourDay;
                llStartTime.setBackgroundColor(activity.getResources().getColor(R.color.colorE));
                llEndTime.setBackgroundColor(activity.getResources().getColor(R.color.white));
                tvShowTime.setText(time1);

            } else if (clickcount == 2) {
                tv_end_time.setText(hourDay + ":" + min);
                endTime = hourDay + ":" + min + ":" + "00";
                time2 = hourDay + ":" + min;
                endHour = hourDay;
                llStartTime.setBackgroundColor(activity.getResources().getColor(R.color.white));
                llEndTime.setBackgroundColor(activity.getResources().getColor(R.color.colorE));
                tvShowTime.setText(time2);
            }
        });
        llEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llStartTime.setBackgroundColor(activity.getResources().getColor(R.color.white));
                llEndTime.setBackgroundColor(activity.getResources().getColor(R.color.colorE));
                Toast.makeText(activity, time2, Toast.LENGTH_SHORT).show();
                SimpleDateFormat sdf = new SimpleDateFormat("hh:ss");
                Date date = null;
                try {
                    //  if (clickcount == 2) {
                    if (time2 != null)
                        date = sdf.parse(time2);
                    //  }

                } catch (ParseException e) {
                    e.printStackTrace();
                    Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                Calendar c = Calendar.getInstance();
                if (date != null)
                    c.setTime(date);
                timePicker.setCurrentHour(c.get(Calendar.HOUR_OF_DAY));
                timePicker.setCurrentMinute(c.get(Calendar.MINUTE));
            }
        });
        llStartTime.setOnClickListener(v -> {
            llStartTime.setBackgroundColor(activity.getResources().getColor(R.color.colorE));
            llEndTime.setBackgroundColor(activity.getResources().getColor(R.color.white));
            Toast.makeText(activity, time1, Toast.LENGTH_SHORT).show();
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
            Date date = null;
            try {
                if (time1 != null)
                    date = sdf.parse(time1);

            } catch (ParseException e) {
                e.printStackTrace();
                Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            Calendar c = Calendar.getInstance();
            if (date != null)
                c.setTime(date);

            timePicker.setCurrentHour(c.get(Calendar.HOUR_OF_DAY));
            timePicker.setCurrentMinute(c.get(Calendar.MINUTE));
        });

    }

    public interface StartEndTimePickerListner {
        void selectedTime(String startTime, String endTime);

    }

}