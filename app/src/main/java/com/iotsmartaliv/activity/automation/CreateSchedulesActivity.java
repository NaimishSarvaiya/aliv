package com.iotsmartaliv.activity.automation;

import static com.iotsmartaliv.constants.Constant.LOGIN_DETAIL;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iotsmartaliv.R;
import com.iotsmartaliv.activity.DeviceDetailActivity;
import com.iotsmartaliv.adapter.automation.TimeSlotsMondayAdapter;
import com.iotsmartaliv.apiCalling.listeners.RetrofitListener;
import com.iotsmartaliv.apiCalling.models.ErrorObject;
import com.iotsmartaliv.apiCalling.models.TimeSlot;
import com.iotsmartaliv.apiCalling.retrofit.ApiServiceProvider;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.databinding.CreateSchedulesActivityBinding;
import com.iotsmartaliv.dialog_box.RangeTimePickerDialog;
import com.iotsmartaliv.model.AutomationRoomsResponse;
import com.iotsmartaliv.utils.CommanUtils;
import com.iotsmartaliv.utils.Util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;

/**
 * This class is used as .
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 19/8/19 :August : 2019 on 14 : 29.
 */
public class CreateSchedulesActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    CreateSchedulesActivityBinding binding;
    ApiServiceProvider apiServiceProvider;
//    @BindView(R.id.recyclerView_time_slot)
//    RecyclerView recyclerViewItemMon;
//    @BindView(R.id.tv_addTimeSlot)
//    TextView tvAddSlotMon;
//    @BindView(R.id.addSlotTue)
//    TextView tvAddSlotTue;
//    @BindView(R.id.tv_addTimeSlot_wed)
//    TextView tvAddSlotWed;
//    @BindView(R.id.tv_addTimeSlot_thurs)
//    TextView tvAddSlotThurs;
//    @BindView(R.id.tv_addTimeSlot_fri)
//    TextView tvAddSlotFri;
//    @BindView(R.id.tv_addTimeSlot_sat)
//    TextView tvAddSlotSat;
//    @BindView(R.id.tv_addTimeSlot_sun)
//    TextView tvAddSlotSun;
//    @BindView(R.id.checkboxMon)
//    CheckBox checkboxMon;
//    @BindView(R.id.checkboxTue)
//    CheckBox checkBoxTue;
//    @BindView(R.id.checkbox_wed)
//    CheckBox checkBoxWed;
//    @BindView(R.id.checkbox_thurs)
//    CheckBox checkBoxThurs;
//    @BindView(R.id.checkbox_fri)
//    CheckBox checkBoxFri;
//    @BindView(R.id.checkbox_sat)
//    CheckBox checkBoxSat;
//    @BindView(R.id.checkbox_sun)
//    CheckBox checkBoxSun;
//    @BindView(R.id.recyclerView_time_tues)
//    RecyclerView recyclerViewTimeTues;
//    @BindView(R.id.recyclerView_time_wed)
//    RecyclerView recyclerViewTimeWed;
//    @BindView(R.id.recyclerView_time_thurs)
//    RecyclerView recyclerViewTimeThurs;
//    @BindView(R.id.recyclerView_time_fri)
//    RecyclerView recyclerViewtimeFri;
//    @BindView(R.id.recyclerView_time_sat)
//    RecyclerView recyclerViewTimeSat;
//    @BindView(R.id.recyclerView_time_sun)
//    RecyclerView recyclerViewTimeSun;
//
//
//    @BindView(R.id.toolbar_title)
//    TextView toolbarTitle;
//    @BindView(R.id.toolbar)
//    Toolbar toolbar;
//    @BindView(R.id.tv_start_date)
//    TextView tvStartDate;
//    @BindView(R.id.tv_end_date)
//    TextView tvEndDate;
//    @BindView(R.id.ll_select_date)
//    LinearLayout llSelectDate;
//    @BindView(R.id.rl_first)
//    RelativeLayout rlFirst;
//    @BindView(R.id.rl_firstTue)
//    RelativeLayout rlFirstTue;
//    @BindView(R.id.rl_firstWed)
//    RelativeLayout rlFirstWed;
//    @BindView(R.id.rl_first_thurs)
//    RelativeLayout rlFirstThurs;
//    @BindView(R.id.rl_first_fri)
//    RelativeLayout rlFirstFri;
//    @BindView(R.id.rl_first_sat)
//    RelativeLayout rlFirstSat;
//    @BindView(R.id.rl_first_sun)
//    RelativeLayout rlFirstSun;
//    @BindView(R.id.create_schedule_btn)
//    Button createScheduleBtn;
    TimeSlotsMondayAdapter timeSlotsMondayAdapter;
    private TimeSlotsMondayAdapter timeSlotsTuesdayAdapter;
    private TimeSlotsMondayAdapter timeSlotsWednesdayAdapter;
    private TimeSlotsMondayAdapter timeSlotsThursdayAdapter;
    private TimeSlotsMondayAdapter timeSlotsFridayAdapter;
    private TimeSlotsMondayAdapter timeSlotsSaturdayAdapter;
    private TimeSlotsMondayAdapter timeSlotsSundayAdapter;



    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = CreateSchedulesActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        apiServiceProvider = ApiServiceProvider.getInstance(this);

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


    public void popUpForTimePicker(TimeSlotsMondayAdapter timeSlotsAdapter) {
        RangeTimePickerDialog dialog = new RangeTimePickerDialog();
        dialog.setIs24HourView(true);
        dialog.setRadiusDialog(20);
        dialog.setTextTabStart("Start");
        dialog.setTextTabEnd("End");
        dialog.setTextBtnPositive("Accept");
        dialog.setTextBtnNegative("Close");
        dialog.setValidateRange(false);
        dialog.setColorBackgroundHeader(R.color.orange);
        dialog.setColorBackgroundTimePickerHeader(R.color.orange);
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

    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_addTimeSlot:
                popUpForTimePicker(timeSlotsMondayAdapter);

               /* new StartEndTimePickerDialog(CreateSchedulesActivity.this, (startTime, endTime) -> {
                    Toast.makeText(CreateSchedulesActivity.this, startTime, Toast.LENGTH_SHORT).show();
                    timeSlotsMondayAdapter.addItem(new TimeSlot(startTime, endTime));
                }).show();*/
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
    public void onViewClickedSubmit() {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("automation_ID", getIntent().getStringExtra("automation_ID"));
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

                    if (isAvailable){
                        apiServiceProvider.addAutomationTimeSchedule(stringStringHashMap, new RetrofitListener<AutomationRoomsResponse>() {
                            @Override
                            public void onResponseSuccess(AutomationRoomsResponse sucessRespnse, String apiFlag) {
                                if (sucessRespnse.getStatus().equalsIgnoreCase("OK")) {
                                    Toast.makeText(CreateSchedulesActivity.this, sucessRespnse.getMsg(), Toast.LENGTH_SHORT).show();
                                    showSuccessfullDailog(sucessRespnse.getMsg());

                                } else {
                                    Toast.makeText(CreateSchedulesActivity.this, sucessRespnse.getMsg(), Toast.LENGTH_SHORT).show();

                                }
                            }

                            @Override
                            public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                                Util.firebaseEvent(Constant.APIERROR, CreateSchedulesActivity.this,Constant.UrlPath.SERVER_URL+apiFlag, LOGIN_DETAIL.getUsername(),LOGIN_DETAIL.getAppuserID(),errorObject.getStatus());
                                Toast.makeText(CreateSchedulesActivity.this, "Something Went Wrong.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });

    }

    public void showDatePicker(boolean isStartDate) {
        Calendar myCalendar = Calendar.getInstance(TimeZone.getDefault());
        DatePickerDialog datePicker = new DatePickerDialog(CreateSchedulesActivity.this, R.style.TimePickerTheme, (mDatePicker, year, month, dayOfMonth) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth);
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String strDate = dateFormat.format(calendar.getTime());
            if (isStartDate) {
                binding.tvStartDate.setText(strDate);
            } else {
                binding.tvEndDate.setText(strDate);
            }

        }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        datePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
