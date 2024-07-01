package com.iotsmartaliv.dialog_box;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iotsmartaliv.R;
import com.iotsmartaliv.adapter.automation.TimeSlotsMondayAdapter;
import com.iotsmartaliv.apiCalling.models.AutomationScheduleData;
import com.iotsmartaliv.apiCalling.models.Reccurence;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ShowScheduleDetailDialog extends Dialog {
    @BindView(R.id.recyclerView_time_slot)
    RecyclerView recyclerViewItemMon;
    @BindView(R.id.tv_addTimeSlot)
    TextView tvAddSlotMon;
    @BindView(R.id.addSlotTue)
    TextView tvAddSlotTue;
    @BindView(R.id.tv_addTimeSlot_wed)
    TextView tvAddSlotWed;
    @BindView(R.id.tv_addTimeSlot_thurs)
    TextView tvAddSlotThurs;
    @BindView(R.id.tv_addTimeSlot_fri)
    TextView tvAddSlotFri;
    @BindView(R.id.tv_addTimeSlot_sat)
    TextView tvAddSlotSat;
    @BindView(R.id.tv_addTimeSlot_sun)
    TextView tvAddSlotSun;
    @BindView(R.id.checkboxMon)
    TextView checkboxMon;
    @BindView(R.id.checkboxTue)
    TextView checkBoxTue;
    @BindView(R.id.checkbox_wed)
    TextView checkBoxWed;
    @BindView(R.id.checkbox_thurs)
    TextView checkBoxThurs;
    @BindView(R.id.checkbox_fri)
    TextView checkBoxFri;
    @BindView(R.id.checkbox_sat)
    TextView checkBoxSat;
    @BindView(R.id.checkbox_sun)
    TextView checkBoxSun;
    @BindView(R.id.recyclerView_time_tues)
    RecyclerView recyclerViewTimeTues;
    @BindView(R.id.recyclerView_time_wed)
    RecyclerView recyclerViewTimeWed;
    @BindView(R.id.recyclerView_time_thurs)
    RecyclerView recyclerViewTimeThurs;
    @BindView(R.id.recyclerView_time_fri)
    RecyclerView recyclerViewtimeFri;
    @BindView(R.id.recyclerView_time_sat)
    RecyclerView recyclerViewTimeSat;
    @BindView(R.id.recyclerView_time_sun)
    RecyclerView recyclerViewTimeSun;
    @BindView(R.id.tv_start_date)
    TextView tvStartDate;
    @BindView(R.id.tv_end_date)
    TextView tvEndDate;
    @BindView(R.id.ll_select_date)
    LinearLayout llSelectDate;
    @BindView(R.id.rl_first)
    RelativeLayout rlFirst;
    @BindView(R.id.rl_firstTue)
    RelativeLayout rlFirstTue;
    @BindView(R.id.rl_firstWed)
    RelativeLayout rlFirstWed;
    @BindView(R.id.rl_first_thurs)
    RelativeLayout rlFirstThurs;
    @BindView(R.id.rl_first_fri)
    RelativeLayout rlFirstFri;
    @BindView(R.id.rl_first_sat)
    RelativeLayout rlFirstSat;
    @BindView(R.id.rl_first_sun)
    RelativeLayout rlFirstSun;
    @BindView(R.id.buttonOk)
    Button buttonOk;
    @BindView(R.id.monday_lay)
    RelativeLayout mondayLay;
    @BindView(R.id.tue_lay)
    RelativeLayout tueLay;
    @BindView(R.id.wed_lay)
    RelativeLayout wedLay;
    @BindView(R.id.thur_lay)
    RelativeLayout thurLay;
    @BindView(R.id.fri_lay)
    RelativeLayout friLay;
    @BindView(R.id.sat_lay)
    RelativeLayout satLay;
    @BindView(R.id.sun_lay)
    RelativeLayout sunLay;

    Activity activity;
    private AutomationScheduleData automationScheduleData;

    public ShowScheduleDetailDialog(Activity activity, AutomationScheduleData automationScheduleData) {
        super(activity);
        this.activity = activity;
        this.automationScheduleData = automationScheduleData;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.show_chedule_detail_dialog);
        ButterKnife.bind(this);
        Window window = getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        buttonOk.setOnClickListener(v -> dismiss());
        TimeSlotsMondayAdapter timeSlotsMondayAdapter = new TimeSlotsMondayAdapter(true);
        recyclerViewItemMon.setLayoutManager(new GridLayoutManager(activity, 2)); // set LayoutManager to RecyclerView
        recyclerViewItemMon.setAdapter(timeSlotsMondayAdapter);

        TimeSlotsMondayAdapter timeSlotsTuesdayAdapter = new TimeSlotsMondayAdapter(true);
        recyclerViewTimeTues.setLayoutManager(new GridLayoutManager(activity, 2)); // set LayoutManager to RecyclerView
        recyclerViewTimeTues.setAdapter(timeSlotsTuesdayAdapter);

        TimeSlotsMondayAdapter timeSlotsWednesdayAdapter = new TimeSlotsMondayAdapter(true);
        recyclerViewTimeWed.setLayoutManager(new GridLayoutManager(activity, 2)); // set LayoutManager to RecyclerView
        recyclerViewTimeWed.setAdapter(timeSlotsWednesdayAdapter);

        TimeSlotsMondayAdapter timeSlotsThursdayAdapter = new TimeSlotsMondayAdapter(true);
        recyclerViewTimeThurs.setLayoutManager(new GridLayoutManager(activity, 2)); // set LayoutManager to RecyclerView
        recyclerViewTimeThurs.setAdapter(timeSlotsThursdayAdapter);

        TimeSlotsMondayAdapter timeSlotsFridayAdapter = new TimeSlotsMondayAdapter(true);
        recyclerViewtimeFri.setLayoutManager(new GridLayoutManager(activity, 2)); // set LayoutManager to RecyclerView
        recyclerViewtimeFri.setAdapter(timeSlotsFridayAdapter);

        TimeSlotsMondayAdapter timeSlotsSaturdayAdapter = new TimeSlotsMondayAdapter(true);
        recyclerViewTimeSat.setLayoutManager(new GridLayoutManager(activity, 2)); // set LayoutManager to RecyclerView
        recyclerViewTimeSat.setAdapter(timeSlotsSaturdayAdapter);

        TimeSlotsMondayAdapter timeSlotsSundayAdapter = new TimeSlotsMondayAdapter(true);
        recyclerViewTimeSun.setLayoutManager(new GridLayoutManager(activity, 2)); // set LayoutManager to RecyclerView
        recyclerViewTimeSun.setAdapter(timeSlotsSundayAdapter);


        tvStartDate.setText(automationScheduleData.getStartDate());
        tvEndDate.setText(automationScheduleData.getEndDate());
        if (automationScheduleData.getReccurence() != null) {
            for (Reccurence reccurence : automationScheduleData.getReccurence()) {
                switch (reccurence.getRecurrenceDay()) {
                    case "0":
                        sunLay.setVisibility(View.VISIBLE);
                        timeSlotsSundayAdapter.addItemAll(reccurence.getTimeSlots());

                        break;
                    case "1":
                        mondayLay.setVisibility(View.VISIBLE);
                        timeSlotsMondayAdapter.addItemAll(reccurence.getTimeSlots());
                        break;
                    case "2":
                        tueLay.setVisibility(View.VISIBLE);
                        timeSlotsTuesdayAdapter.addItemAll(reccurence.getTimeSlots());
                        break;
                    case "3":
                        wedLay.setVisibility(View.VISIBLE);
                        timeSlotsWednesdayAdapter.addItemAll(reccurence.getTimeSlots());
                        break;
                    case "4":
                        thurLay.setVisibility(View.VISIBLE);
                        timeSlotsThursdayAdapter.addItemAll(reccurence.getTimeSlots());
                        break;
                    case "5":
                        friLay.setVisibility(View.VISIBLE);
                        timeSlotsFridayAdapter.addItemAll(reccurence.getTimeSlots());
                        break;
                    case "6":
                        satLay.setVisibility(View.VISIBLE);
                        timeSlotsSaturdayAdapter.addItemAll(reccurence.getTimeSlots());
                        break;
                }
            }
        }
    }
}