package com.iotsmartaliv.activity.automation;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iotsmartaliv.R;
import com.iotsmartaliv.adapter.automation.TimeSlotsMondayAdapter;
import com.iotsmartaliv.apiAndSocket.listeners.RetrofitListener;
import com.iotsmartaliv.apiAndSocket.models.AutomationScheduleData;
import com.iotsmartaliv.apiAndSocket.models.ErrorObject;
import com.iotsmartaliv.apiAndSocket.models.Reccurence;
import com.iotsmartaliv.apiAndSocket.models.TimeSlot;
import com.iotsmartaliv.apiAndSocket.retrofit.ApiServiceProvider;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.databinding.ActivityEditScheduleBinding;
import com.iotsmartaliv.dialog_box.RangeTimePickerDialog;
import com.iotsmartaliv.model.AutomationRoomsResponse;
import com.iotsmartaliv.utils.CommanUtils;
import com.iotsmartaliv.utils.Util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;


import static com.iotsmartaliv.constants.Constant.LOGIN_DETAIL;
import static com.iotsmartaliv.fragments.automation.RoomOneFragment.SCHEDULE_DATA;

public class EditScheduleActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, TimeSlotsMondayAdapter.DeleteTimeSlotListener {

    ApiServiceProvider apiServiceProvider;
    ActivityEditScheduleBinding binding;

    TimeSlotsMondayAdapter timeSlotsMondayAdapter;
    AutomationScheduleData automationScheduleData;
    private TimeSlotsMondayAdapter timeSlotsTuesdayAdapter;
    private TimeSlotsMondayAdapter timeSlotsWednesdayAdapter;
    private TimeSlotsMondayAdapter timeSlotsThursdayAdapter;
    private TimeSlotsMondayAdapter timeSlotsFridayAdapter;
    private TimeSlotsMondayAdapter timeSlotsSaturdayAdapter;
    private TimeSlotsMondayAdapter timeSlotsSundayAdapter;
    DateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");


    public void popUpForTimePicker(TimeSlotsMondayAdapter timeSlotsAdapter) {
        RangeTimePickerDialog dialog = new RangeTimePickerDialog();
        dialog.setIs24HourView(true);
        dialog.setRadiusDialog(20);
        dialog.setTextTabStart("Start");
        dialog.setTextTabEnd("End");
        dialog.setTextBtnPositive("Accept");
        dialog.setTextBtnNegative("Close");
        dialog.setValidateRange(false);
        dialog.setColorBackgroundHeader(R.color.Orange);
        dialog.setColorBackgroundTimePickerHeader(R.color.Orange);
        dialog.setColorTextButton(R.color.colorPrimaryDark);
        dialog.enableMinutes(true);
        dialog.setStartTabIcon(R.drawable.ic_access_time_black_24dp);
        dialog.setEndTabIcon(R.drawable.ic_timelapse_black_24dp);
        dialog.setMinimumSelectedTimeInMinutes(1, true);
        dialog.setOnSelectListner((hourStart, minuteStart, hourEnd, minuteEnd) -> {
            timeSlotsAdapter.addItem(new TimeSlot(CommanUtils.convert24To12Time(hourStart + ":" + minuteStart + ":00"),
                    CommanUtils.convert24To12Time(hourEnd + ":" + minuteEnd + ":00")));
        });
        dialog.show(getFragmentManager(), "");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditScheduleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        ButterKnife.bind(this);
        apiServiceProvider = ApiServiceProvider.getInstance(this);
        automationScheduleData = (AutomationScheduleData) getIntent().getSerializableExtra(SCHEDULE_DATA);
        binding.checkboxSun.setOnCheckedChangeListener(this);
        binding.checkboxMon.setOnCheckedChangeListener(this);
        binding.checkboxTue.setOnCheckedChangeListener(this);
        binding.checkboxWed.setOnCheckedChangeListener(this);
        binding.checkboxThurs.setOnCheckedChangeListener(this);
        binding.checkboxFri.setOnCheckedChangeListener(this);
        binding.checkboxSat.setOnCheckedChangeListener(this);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        timeSlotsMondayAdapter = new TimeSlotsMondayAdapter();
        binding.recyclerViewTimeSlot.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2)); // set LayoutManager to RecyclerView
        binding.recyclerViewTimeSlot.setAdapter(timeSlotsMondayAdapter);

        timeSlotsTuesdayAdapter = new TimeSlotsMondayAdapter();
        binding.recyclerViewTimeTues.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2)); // set LayoutManager to RecyclerView
        binding.recyclerViewTimeTues.setAdapter(timeSlotsTuesdayAdapter);

        timeSlotsWednesdayAdapter = new TimeSlotsMondayAdapter();
        binding.recyclerViewTimeWed.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2)); // set LayoutManager to RecyclerView
        binding.recyclerViewTimeWed.setAdapter(timeSlotsWednesdayAdapter);

        timeSlotsThursdayAdapter = new TimeSlotsMondayAdapter();
        binding.recyclerViewTimeThurs.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2)); // set LayoutManager to RecyclerView
        binding.recyclerViewTimeThurs.setAdapter(timeSlotsThursdayAdapter);

        timeSlotsFridayAdapter = new TimeSlotsMondayAdapter();
        binding.recyclerViewTimeFri.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2)); // set LayoutManager to RecyclerView
        binding.recyclerViewTimeFri.setAdapter(timeSlotsFridayAdapter);

        timeSlotsSaturdayAdapter = new TimeSlotsMondayAdapter();
        binding.recyclerViewTimeSat.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2)); // set LayoutManager to RecyclerView
        binding.recyclerViewTimeSat.setAdapter(timeSlotsSaturdayAdapter);

        timeSlotsSundayAdapter = new TimeSlotsMondayAdapter();
        binding.recyclerViewTimeSun.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2)); // set LayoutManager to RecyclerView
        binding.recyclerViewTimeSun.setAdapter(timeSlotsSundayAdapter);

        timeSlotsMondayAdapter.setOnDeleteTimeSlotListener(this);
        timeSlotsTuesdayAdapter.setOnDeleteTimeSlotListener(this);
        timeSlotsWednesdayAdapter.setOnDeleteTimeSlotListener(this);
        timeSlotsThursdayAdapter.setOnDeleteTimeSlotListener(this);
        timeSlotsFridayAdapter.setOnDeleteTimeSlotListener(this);
        timeSlotsSaturdayAdapter.setOnDeleteTimeSlotListener(this);
        timeSlotsSundayAdapter.setOnDeleteTimeSlotListener(this);

        binding.tvStartDate.setText(automationScheduleData.getStartDate());
        binding.tvEndDate.setText(automationScheduleData.getEndDate());
        if (automationScheduleData.getReccurence() != null) {
            for (Reccurence reccurence : automationScheduleData.getReccurence()) {
                switch (reccurence.getRecurrenceDay()) {
                    case "0":
                        binding.checkboxSun.setChecked(true);
                        timeSlotsSundayAdapter.addItemAll(reccurence.getTimeSlots());
                        break;
                    case "1":
                        binding.checkboxMon.setChecked(true);
                        timeSlotsMondayAdapter.addItemAll(reccurence.getTimeSlots());
                        break;
                    case "2":
                        binding.checkboxTue.setChecked(true);
                        timeSlotsTuesdayAdapter.addItemAll(reccurence.getTimeSlots());
                        break;
                    case "3":
                        binding.checkboxWed.setChecked(true);
                        timeSlotsWednesdayAdapter.addItemAll(reccurence.getTimeSlots());
                        break;
                    case "4":
                        binding.checkboxThurs.setChecked(true);
                        timeSlotsThursdayAdapter.addItemAll(reccurence.getTimeSlots());
                        break;
                    case "5":
                        binding.checkboxFri.setChecked(true);
                        timeSlotsFridayAdapter.addItemAll(reccurence.getTimeSlots());
                        break;
                    case "6":
                        binding.checkboxSat.setChecked(true);
                        timeSlotsSaturdayAdapter.addItemAll(reccurence.getTimeSlots());
                        break;
                }
            }
        }

        binding.tvStartDate.setOnClickListener(v-> showDatePicker(true));
        binding.tvEndDate.setOnClickListener(v -> showDatePicker(true));
        binding.tvAddTimeSlot.setOnClickListener(this::onViewClicked);
        binding.addSlotTue.setOnClickListener(this::onViewClicked);
        binding.tvAddTimeSlotWed.setOnClickListener(this::onViewClicked);
        binding.tvAddTimeSlotThurs.setOnClickListener(this::onViewClicked);
        binding.tvAddTimeSlotFri.setOnClickListener(this::onViewClicked);
        binding.tvAddTimeSlotSat.setOnClickListener(this::onViewClicked);
        binding.tvAddTimeSlotSun.setOnClickListener(this::onViewClicked);
        binding.createScheduleBtn.setOnClickListener(v ->onViewClickedSubmit());
    }



    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_addTimeSlot:
                popUpForTimePicker(timeSlotsMondayAdapter);

                break;

            case R.id.addSlotTue:
                popUpForTimePicker(timeSlotsTuesdayAdapter);
                break;
            case R.id.tv_addTimeSlot_wed:
                popUpForTimePicker(timeSlotsWednesdayAdapter);
                break;
            case R.id.tv_addTimeSlot_thurs:
                popUpForTimePicker(timeSlotsThursdayAdapter);
                break;
            case R.id.tv_addTimeSlot_fri:
                popUpForTimePicker(timeSlotsFridayAdapter);
                break;
            case R.id.tv_addTimeSlot_sat:
                popUpForTimePicker(timeSlotsSaturdayAdapter);

                break;
            case R.id.tv_addTimeSlot_sun:
                popUpForTimePicker(timeSlotsSundayAdapter);
                break;

        }
    }

    private Calendar startDate = null;
    private Calendar endDate = null;
    public void showDatePicker(boolean isStartDate) {
        Calendar myCalendar = Calendar.getInstance(TimeZone.getDefault());

        DatePickerDialog datePicker = new DatePickerDialog(this, R.style.TimePickerTheme, (mDatePicker, year, month, dayOfMonth) -> {
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(year, month, dayOfMonth);

            // Validate dates
            if (isStartDate) {
                if (endDate != null && selectedDate.after(endDate)) {
                    // Show error message or handle invalid date
//                    Toast.makeText(getContext(), "Start date cannot be after end date", Toast.LENGTH_SHORT).show();
                    return;
                }
                startDate = selectedDate;
                binding.tvStartDate.setText(outputFormat.format(selectedDate.getTime()));
            } else {
                if (startDate != null && selectedDate.before(startDate)) {
                    // Show error message or handle invalid date
//                    Toast.makeText(getContext(), "End date cannot be before start date", Toast.LENGTH_SHORT).show();
                    return;
                }
                endDate = selectedDate;
                binding.tvEndDate.setText(outputFormat.format(selectedDate.getTime()));
            }
        }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));

        // Set min date to current date
        datePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        // Set max date based on whether it's start or end date
        if (isStartDate && endDate != null) {
            datePicker.getDatePicker().setMaxDate(endDate.getTimeInMillis());
        } else if (!isStartDate && startDate != null) {
            datePicker.getDatePicker().setMinDate(startDate.getTimeInMillis());
        }

        datePicker.show();
    }

    public void showSuccessfullDailog(String msg) {
        final AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
        dialogBuilder.setCancelable(false);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_successful_booked, null);
        RelativeLayout rlOk = dialogView.findViewById(R.id.rl_ok);
        TextView tvMessage = dialogView.findViewById(R.id.tv_message);
        tvMessage.setText(msg);
        rlOk.setOnClickListener(v -> {
            dialogBuilder.dismiss();
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        });
        dialogBuilder.setView(dialogView);
        Window window = dialogBuilder.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialogBuilder.show();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.checkboxMon:
                binding.recyclerViewTimeSlot.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                binding.tvAddTimeSlot.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                break;
            case R.id.checkboxTue:
                binding.recyclerViewTimeTues.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                binding.addSlotTue.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                break;
            case R.id.checkbox_wed:
                binding.recyclerViewTimeWed.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                binding.tvAddTimeSlotWed.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                break;
            case R.id.checkbox_thurs:
                binding.recyclerViewTimeThurs.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                binding.tvAddTimeSlotThurs.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                break;
            case R.id.checkbox_fri:
                binding.recyclerViewTimeFri.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                binding.tvAddTimeSlotFri.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                break;
            case R.id.checkbox_sat:
                binding.recyclerViewTimeSat.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                binding.tvAddTimeSlotSat.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                break;
            case R.id.checkbox_sun:
                binding.recyclerViewTimeSun.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                binding.tvAddTimeSlotSun.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                break;
        }
    }

    public void onViewClickedSubmit() {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("schedule_ID", automationScheduleData.getScheduleID());

        if (binding.tvStartDate.getText().toString().trim().length() > 0) {
            stringStringHashMap.put("start_date", binding.tvStartDate.getText().toString());
        } else {
            Toast.makeText(this, "Select Start Date.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (binding.tvEndDate.getText().toString().trim().length() > 0) {
            stringStringHashMap.put("end_date", binding.tvEndDate.getText().toString());
        } else {
            Toast.makeText(this, "Select End Date.", Toast.LENGTH_SHORT).show();
            return;
        }
        int i = 0;
        int h;
        if (binding.checkboxSun.isChecked()) {
            if (timeSlotsSundayAdapter.getItem().size() > 0) {
                stringStringHashMap.put("schedule[" + i + "][day]", "0");
                for (h = 0; h < timeSlotsSundayAdapter.getItem().size(); h++) {
                    TimeSlot time = timeSlotsSundayAdapter.getItem().get(h);
                    stringStringHashMap.put("schedule[" + i + "][slot][" + h + "][start_time]", CommanUtils.convert12To24Time(time.getStartTime()));
                    stringStringHashMap.put("schedule[" + i + "][slot][" + h + "][end_time]", CommanUtils.convert12To24Time(time.getEndTime()));
                }
                i++;
            } else {
                Toast.makeText(this, "Please add Time Slot On Sunday or Uncheck the day.", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if (binding.checkboxMon.isChecked()) {
            if (timeSlotsMondayAdapter.getItem().size() > 0) {
                stringStringHashMap.put("schedule[" + i + "][day]", "1");
                for (h = 0; h < timeSlotsMondayAdapter.getItem().size(); h++) {
                    TimeSlot time = timeSlotsMondayAdapter.getItem().get(h);
                    stringStringHashMap.put("schedule[" + i + "][slot][" + h + "][start_time]", CommanUtils.convert12To24Time(time.getStartTime()));
                    stringStringHashMap.put("schedule[" + i + "][slot][" + h + "][end_time]", CommanUtils.convert12To24Time(time.getEndTime()));
                }
                i++;
            } else {
                Toast.makeText(this, "Please add Time Slot On Monday or Uncheck the day.", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if (binding.checkboxTue.isChecked()) {
            if (timeSlotsTuesdayAdapter.getItem().size() > 0) {
                stringStringHashMap.put("schedule[" + i + "][day]", "2");
                for (h = 0; h < timeSlotsTuesdayAdapter.getItem().size(); h++) {
                    TimeSlot time = timeSlotsTuesdayAdapter.getItem().get(h);
                    stringStringHashMap.put("schedule[" + i + "][slot][" + h + "][start_time]", CommanUtils.convert12To24Time(time.getStartTime()));
                    stringStringHashMap.put("schedule[" + i + "][slot][" + h + "][end_time]", CommanUtils.convert12To24Time(time.getEndTime()));
                }
                i++;
            } else {
                Toast.makeText(this, "Please add Time Slot On Tuesday or Uncheck the day.", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if (binding.checkboxWed.isChecked()) {
            if (timeSlotsWednesdayAdapter.getItem().size() > 0) {
                stringStringHashMap.put("schedule[" + i + "][day]", "3");
                for (h = 0; h < timeSlotsWednesdayAdapter.getItem().size(); h++) {
                    TimeSlot time = timeSlotsWednesdayAdapter.getItem().get(h);
                    stringStringHashMap.put("schedule[" + i + "][slot][" + h + "][start_time]", CommanUtils.convert12To24Time(time.getStartTime()));
                    stringStringHashMap.put("schedule[" + i + "][slot][" + h + "][end_time]", CommanUtils.convert12To24Time(time.getEndTime()));
                }
                i++;
            } else {
                Toast.makeText(this, "Please add Time Slot On Wednesday or Uncheck the day.", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if (binding.checkboxThurs.isChecked()) {
            if (timeSlotsThursdayAdapter.getItem().size() > 0) {
                stringStringHashMap.put("schedule[" + i + "][day]", "4");
                for (h = 0; h < timeSlotsThursdayAdapter.getItem().size(); h++) {
                    TimeSlot time = timeSlotsThursdayAdapter.getItem().get(h);
                    stringStringHashMap.put("schedule[" + i + "][slot][" + h + "][start_time]", CommanUtils.convert12To24Time(time.getStartTime()));
                    stringStringHashMap.put("schedule[" + i + "][slot][" + h + "][end_time]", CommanUtils.convert12To24Time(time.getEndTime()));
                }
                i++;
            } else {
                Toast.makeText(this, "Please add Time Slot On Thursday or Uncheck the day.", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if (binding.checkboxFri.isChecked()) {
            if (timeSlotsFridayAdapter.getItem().size() > 0) {
                stringStringHashMap.put("schedule[" + i + "][day]", "5");
                for (h = 0; h < timeSlotsFridayAdapter.getItem().size(); h++) {
                    TimeSlot time = timeSlotsFridayAdapter.getItem().get(h);
                    stringStringHashMap.put("schedule[" + i + "][slot][" + h + "][start_time]", CommanUtils.convert12To24Time(time.getStartTime()));
                    stringStringHashMap.put("schedule[" + i + "][slot][" + h + "][end_time]", CommanUtils.convert12To24Time(time.getEndTime()));
                }
                i++;
            } else {
                Toast.makeText(this, "Please add Time Slot On Friday or Uncheck the day.", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if (binding.checkboxSat.isChecked()) {
            if (timeSlotsSaturdayAdapter.getItem().size() > 0) {
                stringStringHashMap.put("schedule[" + i + "][day]", "6");
                for (h = 0; h < timeSlotsSaturdayAdapter.getItem().size(); h++) {
                    TimeSlot time = timeSlotsSaturdayAdapter.getItem().get(h);
                    stringStringHashMap.put("schedule[" + i + "][slot][" + h + "][start_time]", CommanUtils.convert12To24Time(time.getStartTime()));
                    stringStringHashMap.put("schedule[" + i + "][slot][" + h + "][end_time]", CommanUtils.convert12To24Time(time.getEndTime()));
                }
                i++;
            } else {
                Toast.makeText(this, "Please add Time Slot On Saturday or Uncheck the day.", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (i == 0) {
            Toast.makeText(this, "You have to select at least One Day of Week.", Toast.LENGTH_SHORT).show();
            return;
        }
        Util.checkInternet(this, new Util.NetworkCheckCallback() {
            @Override
            public void onNetworkCheckComplete(boolean isAvailable) {
                if (isAvailable) {
                    apiServiceProvider.updateAutomationTimeSchedule(stringStringHashMap, new RetrofitListener<AutomationRoomsResponse>() {
                        @Override
                        public void onResponseSuccess(AutomationRoomsResponse sucessRespnse, String apiFlag) {
                            if (sucessRespnse.getStatus().equalsIgnoreCase("OK")) {
//                                Toast.makeText(EditScheduleActivity.this, sucessRespnse.getMsg(), Toast.LENGTH_SHORT).show();
                                showSuccessfullDailog(sucessRespnse.getMsg());
                            } else {
                                Toast.makeText(EditScheduleActivity.this, sucessRespnse.getMsg(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                            Util.firebaseEvent(Constant.APIERROR, EditScheduleActivity.this, Constant.UrlPath.SERVER_URL + apiFlag, LOGIN_DETAIL.getUsername(), LOGIN_DETAIL.getAppuserID(), errorObject.getStatus());

                            Toast.makeText(EditScheduleActivity.this, "Something Went Wrong.", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        });

    }

    @Override
    public void deleteTimeSlot(TimeSlot timeSlot, int position, TimeSlotsMondayAdapter timeSlotsMondayAdapter) {
        if (timeSlot.getTimeslot_ID() == null) {
            return;
        }
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("operation", "delete");
        stringStringHashMap.put("type", "1");
        stringStringHashMap.put("id", timeSlot.getTimeslot_ID());
        Util.checkInternet(this, new Util.NetworkCheckCallback() {
            @Override
            public void onNetworkCheckComplete(boolean isAvailable) {
                apiServiceProvider.deleteAutomationTimeSchedule(stringStringHashMap, new RetrofitListener<AutomationRoomsResponse>() {
                    @Override
                    public void onResponseSuccess(AutomationRoomsResponse sucessRespnse, String apiFlag) {
                        if (sucessRespnse.getStatus().equalsIgnoreCase("OK")) {
                            Toast.makeText(EditScheduleActivity.this, sucessRespnse.getMsg(), Toast.LENGTH_SHORT).show();
                            timeSlotsMondayAdapter.removeItem(position);
                        } else {
                            Toast.makeText(EditScheduleActivity.this, sucessRespnse.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                        Util.firebaseEvent(Constant.APIERROR, EditScheduleActivity.this, Constant.UrlPath.SERVER_URL + apiFlag, LOGIN_DETAIL.getUsername(), LOGIN_DETAIL.getAppuserID(), errorObject.getStatus());

                        Toast.makeText(EditScheduleActivity.this, "Something Went Wrong.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
