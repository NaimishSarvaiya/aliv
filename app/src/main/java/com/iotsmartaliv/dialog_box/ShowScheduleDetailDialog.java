package com.iotsmartaliv.dialog_box;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.iotsmartaliv.adapter.automation.TimeSlotsMondayAdapter;
import com.iotsmartaliv.apiAndSocket.models.AutomationScheduleData;
import com.iotsmartaliv.apiAndSocket.models.Reccurence;
import com.iotsmartaliv.databinding.ShowCheduleDetailDialogBinding;


public class ShowScheduleDetailDialog extends Dialog {

    Activity activity;
    private AutomationScheduleData automationScheduleData;

    public ShowScheduleDetailDialog(Activity activity, AutomationScheduleData automationScheduleData) {
        super(activity);
        this.activity = activity;
        this.automationScheduleData = automationScheduleData;
    }
    ShowCheduleDetailDialogBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = ShowCheduleDetailDialogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        ButterKnife.bind(this);
        Window window = getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        binding.buttonOk.setOnClickListener(v -> dismiss());
        TimeSlotsMondayAdapter timeSlotsMondayAdapter = new TimeSlotsMondayAdapter(true);
        binding.recyclerViewTimeSlot.setLayoutManager(new GridLayoutManager(activity, 2)); // set LayoutManager to RecyclerView
        binding.recyclerViewTimeSlot.setAdapter(timeSlotsMondayAdapter);

        TimeSlotsMondayAdapter timeSlotsTuesdayAdapter = new TimeSlotsMondayAdapter(true);
        binding.recyclerViewTimeTues.setLayoutManager(new GridLayoutManager(activity, 2)); // set LayoutManager to RecyclerView
        binding.recyclerViewTimeTues.setAdapter(timeSlotsTuesdayAdapter);

        TimeSlotsMondayAdapter timeSlotsWednesdayAdapter = new TimeSlotsMondayAdapter(true);
        binding.recyclerViewTimeWed.setLayoutManager(new GridLayoutManager(activity, 2)); // set LayoutManager to RecyclerView
        binding.recyclerViewTimeWed.setAdapter(timeSlotsWednesdayAdapter);

        TimeSlotsMondayAdapter timeSlotsThursdayAdapter = new TimeSlotsMondayAdapter(true);
        binding.recyclerViewTimeThurs.setLayoutManager(new GridLayoutManager(activity, 2)); // set LayoutManager to RecyclerView
        binding.recyclerViewTimeThurs.setAdapter(timeSlotsThursdayAdapter);

        TimeSlotsMondayAdapter timeSlotsFridayAdapter = new TimeSlotsMondayAdapter(true);
        binding.recyclerViewTimeFri.setLayoutManager(new GridLayoutManager(activity, 2)); // set LayoutManager to RecyclerView
        binding.recyclerViewTimeFri.setAdapter(timeSlotsFridayAdapter);

        TimeSlotsMondayAdapter timeSlotsSaturdayAdapter = new TimeSlotsMondayAdapter(true);
        binding.recyclerViewTimeSat.setLayoutManager(new GridLayoutManager(activity, 2)); // set LayoutManager to RecyclerView
        binding.recyclerViewTimeSat.setAdapter(timeSlotsSaturdayAdapter);

        TimeSlotsMondayAdapter timeSlotsSundayAdapter = new TimeSlotsMondayAdapter(true);
        binding.recyclerViewTimeSun.setLayoutManager(new GridLayoutManager(activity, 2)); // set LayoutManager to RecyclerView
        binding.recyclerViewTimeSun.setAdapter(timeSlotsSundayAdapter);


        binding.tvStartDate.setText(automationScheduleData.getStartDate());
        binding.tvEndDate.setText(automationScheduleData.getEndDate());
        if (automationScheduleData.getReccurence() != null) {
            for (Reccurence reccurence : automationScheduleData.getReccurence()) {
                switch (reccurence.getRecurrenceDay()) {
                    case "0":
                        binding.sunLay.setVisibility(View.VISIBLE);
                        timeSlotsSundayAdapter.addItemAll(reccurence.getTimeSlots());

                        break;
                    case "1":
                        binding.mondayLay.setVisibility(View.VISIBLE);
                        timeSlotsMondayAdapter.addItemAll(reccurence.getTimeSlots());
                        break;
                    case "2":
                        binding.tueLay.setVisibility(View.VISIBLE);
                        timeSlotsTuesdayAdapter.addItemAll(reccurence.getTimeSlots());
                        break;
                    case "3":
                        binding.wedLay.setVisibility(View.VISIBLE);
                        timeSlotsWednesdayAdapter.addItemAll(reccurence.getTimeSlots());
                        break;
                    case "4":
                        binding.thurLay.setVisibility(View.VISIBLE);
                        timeSlotsThursdayAdapter.addItemAll(reccurence.getTimeSlots());
                        break;
                    case "5":
                        binding.friLay.setVisibility(View.VISIBLE);
                        timeSlotsFridayAdapter.addItemAll(reccurence.getTimeSlots());
                        break;
                    case "6":
                        binding.satLay.setVisibility(View.VISIBLE);
                        timeSlotsSaturdayAdapter.addItemAll(reccurence.getTimeSlots());
                        break;
                }
            }
        }
    }
}