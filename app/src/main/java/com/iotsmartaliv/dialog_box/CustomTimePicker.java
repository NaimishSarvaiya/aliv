package com.iotsmartaliv.dialog_box;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.TimePicker;

import com.iotsmartaliv.R;
import com.iotsmartaliv.utils.CommanUtils;


/**
 * This class is used as Custom Time picker.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 7/9/19 :September.
 */
public class CustomTimePicker extends Dialog {
    Activity activity;
    String title;
    private TimePicker timePicker;
    private TextView titleTv;
    private CustomTimePickerListener customTimePickerListener;

    public CustomTimePicker(Activity activity, CustomTimePickerListener customTimePickerListener, String title) {
        super(activity);
        this.activity = activity;
        this.title = title;
        this.customTimePickerListener = customTimePickerListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.time_picker);
        Window window = getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        timePicker = findViewById(R.id.timepicker);
        timePicker.setIs24HourView(true);
        titleTv = findViewById(R.id.tv_title);
        titleTv.setText(title);
        TextView tvOk = findViewById(R.id.tv_ok);
        TextView tvCancel = findViewById(R.id.tv_cancel);
        tvCancel.setOnClickListener(v -> dismiss());
        tvOk.setOnClickListener(v -> {
            customTimePickerListener.selectedTime(CommanUtils.convert24To12Time(timePicker.getCurrentHour() + ":" + timePicker.getCurrentMinute() + ":00"));
            dismiss();
        });
    }

    public interface CustomTimePickerListener {
        void selectedTime(String time);
    }
}
