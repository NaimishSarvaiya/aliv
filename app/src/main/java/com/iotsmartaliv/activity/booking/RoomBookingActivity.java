package com.iotsmartaliv.activity.booking;

import static com.iotsmartaliv.constants.Constant.LOGIN_DETAIL;
import static com.iotsmartaliv.constants.Constant.PAYMENT_FEATURE_ARRAY;
import static com.iotsmartaliv.constants.Constant.RESCHEDUL;
import static com.iotsmartaliv.constants.Constant.hideLoader;
import static com.iotsmartaliv.constants.Constant.showLoader;
import static com.iotsmartaliv.utils.Util.convertDateFormatForBooking;
import static com.iotsmartaliv.utils.Util.filterEmoji;
import static com.iotsmartaliv.utils.Util.getDateCount;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.iotsmartaliv.R;
import com.iotsmartaliv.adapter.booking.BookingTimeSloatAdapter;
import com.iotsmartaliv.apiAndSocket.listeners.RetrofitListener;
import com.iotsmartaliv.apiAndSocket.models.ErrorObject;
import com.iotsmartaliv.apiAndSocket.retrofit.ApiServiceProvider;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.databinding.ActivityRoomBookingBinding;
import com.iotsmartaliv.dmvphonedemotest.YJCallActivity;
import com.iotsmartaliv.model.booking.BookingDetailsModel;
import com.iotsmartaliv.model.booking.BookingSlotDetailData;
import com.iotsmartaliv.model.booking.ConfirmBookingRequest;
import com.iotsmartaliv.model.booking.ConfirmBookingResponse;
import com.iotsmartaliv.model.booking.PaymentMethodModel;
import com.iotsmartaliv.model.booking.PaymentResponseModel;
import com.iotsmartaliv.model.booking.TimeSlotDataModel;
import com.iotsmartaliv.model.booking.TimeSlotModel;
import com.iotsmartaliv.model.booking.UpdateBookingDatesRequest;
import com.iotsmartaliv.model.booking.UpdateBookingResponse;
import com.iotsmartaliv.model.feedback.FeedbackModel;
import com.iotsmartaliv.model.feedback.MessageHistory;
import com.iotsmartaliv.model.feedback.MessageHistoryData;
import com.iotsmartaliv.utils.Util;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class RoomBookingActivity extends AppCompatActivity {

    private Calendar calendar;
    ActivityRoomBookingBinding binding;
    BookingTimeSloatAdapter adapter;
    String roomId = "";
    private String startDate = "";
    private String endDate = "";
    private String roomTitle = "";
    private boolean isFullMonthView = false; // Track if full month view is active
    private static final int FULL_MONTH_HEIGHT = 900; // Full month view height
    private static final int TWO_WEEK_HEIGHT = 380;

    // Define startDateCalendar and endDateCalendar as class variables
    private Calendar startDateCalendar = Calendar.getInstance();
    private Calendar endDateCalendar = Calendar.getInstance();

    private Calendar selectedStartDate = null;
    private Calendar selectedEndDate = null;
    private TextView lastSelectedDateView = null;
    private ApiServiceProvider apiServiceProvider;

    List<TimeSlotDataModel> timeSlotDataModels = new ArrayList<>();
    String selectedSlotId;
    String roomType;
    String selectedSlotTiming;
    String path;

    int selectedDaysFromReSchedule;
    BookingSlotDetailData bookingData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRoomBookingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        calendar = Calendar.getInstance();
        init();
    }

    void init() {
        getdata();
        loadDayTitles();
        loadCurrentMonthCalendar();
        apiServiceProvider = ApiServiceProvider.getInstance(this, true);

        // RecyclerView for time slots
        binding.rvTimeSlot.setLayoutManager(new GridLayoutManager(RoomBookingActivity.this, 4));
        adapter = new BookingTimeSloatAdapter(RoomBookingActivity.this, timeSlotDataModels, timeSlotData -> {
            if (timeSlotData != null) {
                selectedSlotId = timeSlotData.getSlotsID();
                SimpleDateFormat inputFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                SimpleDateFormat outputFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                String formattedEndTime;
                String formattedStartTime;
                try {
                    formattedEndTime = outputFormat.format(inputFormat.parse(timeSlotData.getEndTime()));
                    formattedStartTime = outputFormat.format(inputFormat.parse(timeSlotData.getStartTime()));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                selectedSlotTiming = formattedStartTime + " : " + formattedEndTime;
                // Time slot selected
//                Log.d("RoomBookingActivity", "Selected Time Slot: " + timeSlotData.getSlotsID());
            } else {
                selectedSlotId = null;
                selectedSlotTiming = null;
                // Time slot deselected
                Log.d("RoomBookingActivity", "Time Slot Deselected");
            }
        });
        binding.rvTimeSlot.setAdapter(adapter);

        // Back button click listener
        binding.bookingHeader.imgBack.setOnClickListener(v -> finish());

        // View toggle button click listener (week view vs month view)
        binding.llCalendarView.setOnClickListener(v -> {
            if (isFullMonthView) {
                binding.tvCalendarType.setText("Week View");
                adjustScrollViewHeight(TWO_WEEK_HEIGHT);
            } else {
                binding.tvCalendarType.setText("Month View");
                adjustScrollViewHeight(FULL_MONTH_HEIGHT);
            }
            isFullMonthView = !isFullMonthView; // Toggle between full month and two-week view
        });

        // Initially set to two-week view height
        adjustScrollViewHeight(TWO_WEEK_HEIGHT);

        // Add a listener to monitor scroll changes
        binding.scrollView.getViewTreeObserver().addOnScrollChangedListener(this::updateMonthHeaderOnScroll);

        // By default, select the current date
        selectTodayAsDefault();

        binding.rlNext.setOnClickListener(v -> {
            if (selectedStartDate == null) {
                // If no date is selected, show a toast
                Toast.makeText(RoomBookingActivity.this, "Please select a date.", Toast.LENGTH_LONG).show();
//            }
//            else if (selectedSlotId == null) {
//                Toast.makeText(RoomBookingActivity.this, "Please select a Slot.", Toast.LENGTH_LONG).show();
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String startDateStr = sdf.format(selectedStartDate.getTime());

                // If selectedEndDate is null, it means we have only a single date selected
                String endDateStr = selectedEndDate == null ? startDateStr : sdf.format(selectedEndDate.getTime());
//                Toast.makeText(RoomBookingActivity.this, "Selected Date Range: " + startDateStr + " to " + endDateStr, Toast.LENGTH_LONG).show();
                int selectedCount = 0;
                if (path != null) {
                    if (bookingData.getStartDate() != null) {
                        selectedCount = getDateCount(startDateStr, endDateStr);
                    }
                    if (roomType.equals("0")) {
                        if (selectedSlotId == null) {
                            Toast.makeText(RoomBookingActivity.this, "Please select a Slot.", Toast.LENGTH_LONG).show();
                        } else if (selectedDaysFromReSchedule > selectedCount) {
                            reSchedulingError();
                        } else {
//                        Toast.makeText(RoomBookingActivity.this, "Selected Date Range: " + startDateStr + " to " + endDateStr, Toast.LENGTH_LONG).show();
                            updateRoom(selectedSlotId, startDateStr, endDateStr);
                        }
                    } else {
                        if (selectedDaysFromReSchedule > selectedCount) {
                            reSchedulingError();
                        } else {
                            // Format the selected start and end dates
                            // Display the selected date or date range
//                Toast.makeText(RoomBookingActivity.this, "Selected Date Range: " + startDateStr + " to " + endDateStr, Toast.LENGTH_LONG).show();
                            updateRoom(selectedSlotId, startDateStr, endDateStr);
                        }

                    }
                } else {
                    if (roomType.equals("0")) {
                        if (selectedSlotId == null) {
                            Toast.makeText(RoomBookingActivity.this, "Please select a Slot.", Toast.LENGTH_LONG).show();
                        } else {
//                        Toast.makeText(RoomBookingActivity.this, "Selected Date Range: " + startDateStr + " to " + endDateStr, Toast.LENGTH_LONG).show();
                            getRoomWithSlotDetails(roomId, selectedSlotId, startDateStr, endDateStr);
//                            getRoomDetails(roomId, selectedSlotId, startDateStr, endDateStr);
                        }
                    } else {
                        // Format the selected start and end dates
                        // Display the selected date or date range
//                Toast.makeText(RoomBookingActivity.this, "Selected Date Range: " + startDateStr + " to " + endDateStr, Toast.LENGTH_LONG).show();
//                        getRoomDetails(roomId, "", startDate, endDateStr);
                        getRoomWithOutSlotDetails(roomId, startDate, endDateStr);
                    }

                }
            }
        });
    }

    private void getdata() {
        if (getIntent().getStringExtra(Constant.NAVIGATION_PATH) != null) {
            path = getIntent().getStringExtra(Constant.NAVIGATION_PATH);
        }
        if (getIntent().getSerializableExtra(Constant.BOOKING_DETAIL_DATA) != null) {
            bookingData = (BookingSlotDetailData) getIntent().getSerializableExtra(Constant.BOOKING_DETAIL_DATA);
        }
        roomId = getIntent().getStringExtra(Constant.ROOM_ID);
        startDate = getIntent().getStringExtra(Constant.ROOM_START_DATE);
        endDate = getIntent().getStringExtra(Constant.ROOM_END_DATE);
        roomTitle = getIntent().getStringExtra(Constant.ROOM_TITLE);
        if (path != null) {
            selectedDaysFromReSchedule = getDateCount(bookingData.getStartDate(), bookingData.getEndDate());
        }
        if (getIntent().getStringExtra(Constant.ROOM_TYPE) != null && !getIntent().getStringExtra(Constant.ROOM_TYPE).isEmpty()) {
            roomType = getIntent().getStringExtra(Constant.ROOM_TYPE);
        } else {
            roomType = "0";
        }
        binding.bookingHeader.tvHeader.setText(roomTitle);
        binding.tvDateRange.setText(convertDateFormatForBooking(startDate) + " - " + convertDateFormatForBooking(endDate));

        // Parse start and end dates
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            startDateCalendar.setTime(sdf.parse(startDate));
            endDateCalendar.setTime(sdf.parse(endDate));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (roomType.equals("0")) {
            binding.llSlot.setVisibility(View.VISIBLE);
        } else {
            binding.llSlot.setVisibility(View.GONE);
        }
    }


    private void loadDayTitles() {
        binding.layoutDayTitles.removeAllViews();
        String[] dayTitles = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (String dayTitle : dayTitles) {
            TextView dayTitleTextView = new TextView(this);
            dayTitleTextView.setText(dayTitle);
            dayTitleTextView.setGravity(android.view.Gravity.CENTER);
            dayTitleTextView.setTextSize(17);
            dayTitleTextView.setTypeface(null, Typeface.BOLD);
            dayTitleTextView.setTextColor(ContextCompat.getColor(this, R.color.black));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            dayTitleTextView.setLayoutParams(params);
            binding.layoutDayTitles.addView(dayTitleTextView);
        }
    }

    private void loadCurrentMonthCalendar() {
        binding.layoutCalendar.removeAllViews();

        Calendar tempCalendar = Calendar.getInstance();
        tempCalendar.set(Calendar.YEAR, startDateCalendar.get(Calendar.YEAR));
        tempCalendar.set(Calendar.MONTH, startDateCalendar.get(Calendar.MONTH));
        tempCalendar.set(Calendar.DAY_OF_MONTH, 1);

        // Get today's date
        Calendar today = Calendar.getInstance();
        boolean foundCurrentWeek = false;
        boolean reachedEndDateWeek = false;

        // Loop through the calendar weeks
        while (tempCalendar.before(endDateCalendar) || tempCalendar.equals(endDateCalendar)) {
            // Adjust to the first Sunday of the current month
            Calendar firstSundayCalendar = (Calendar) tempCalendar.clone();
            int firstDayOfWeek = firstSundayCalendar.get(Calendar.DAY_OF_WEEK);
            firstSundayCalendar.add(Calendar.DAY_OF_MONTH, -(firstDayOfWeek - Calendar.SUNDAY));

            Calendar endLoopCalendar = (Calendar) tempCalendar.clone();
            endLoopCalendar.set(Calendar.DAY_OF_MONTH, tempCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));

            // Loop through weeks
            while (firstSundayCalendar.before(endLoopCalendar) || firstSundayCalendar.equals(endLoopCalendar)) {
                LinearLayout weekLayout = new LinearLayout(this);
                weekLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));
                weekLayout.setOrientation(LinearLayout.HORIZONTAL);

                boolean isCurrentWeek = false;

                // Loop through days in the week
                for (int day = 0; day < 7; day++) {
                    TextView dayTextView = new TextView(this);
                    dayTextView.setGravity(android.view.Gravity.CENTER);
                    dayTextView.setTextSize(17);

                    if (firstSundayCalendar.get(Calendar.MONTH) == tempCalendar.get(Calendar.MONTH) ||
                            firstSundayCalendar.equals(endDateCalendar)) {

                        int dayOfMonth = firstSundayCalendar.get(Calendar.DAY_OF_MONTH);
                        dayTextView.setText(String.valueOf(dayOfMonth));

                        // Tag the TextView with its corresponding date
                        dayTextView.setTag(firstSundayCalendar.clone());

                        // Highlight today's date and mark the week
                        if (firstSundayCalendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                                firstSundayCalendar.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
                                firstSundayCalendar.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH)) {
                            isCurrentWeek = true; // Mark this as the current week
                        }

                        // Highlight the week containing the end date and stop adding weeks
                        if (firstSundayCalendar.equals(endDateCalendar)) {
                            reachedEndDateWeek = true;
                        }

                        // Set the available/unavailable date color
                        if (firstSundayCalendar.before(startDateCalendar) || firstSundayCalendar.after(endDateCalendar)) {
                            dayTextView.setBackgroundResource(R.drawable.bg_unavailable_date);
                            dayTextView.setTextColor(ContextCompat.getColor(this, R.color.textunAvilableDate));
                        } else {
                            dayTextView.setBackgroundResource(R.drawable.bg_available_date);
                            dayTextView.setTextColor(ContextCompat.getColor(this, R.color.white));
                        }

                        // Set click listener for date selection
                        dayTextView.setOnClickListener(v -> handleDateSelection((Calendar) dayTextView.getTag(), dayTextView));
                    } else {
                        dayTextView.setText("");
                    }

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            0, 150, 1f);
                    params.setMargins(8, 16, 8, 16);
                    dayTextView.setLayoutParams(params);

                    weekLayout.addView(dayTextView);
                    firstSundayCalendar.add(Calendar.DAY_OF_MONTH, 1);
                }

                // Add the week only if it is within the valid range
                if ((isCurrentWeek || foundCurrentWeek) && !reachedEndDateWeek) {
                    binding.layoutCalendar.addView(weekLayout);
                    foundCurrentWeek = true; // Start adding from the current week onwards
                }

                // Stop adding weeks after the week containing the end date is added
                if (reachedEndDateWeek) {
                    break;
                }
            }

            // Stop if the end date's week is reached
            if (reachedEndDateWeek) {
                break;
            }

            tempCalendar.add(Calendar.MONTH, 1);
        }

        // Set the initial header to the first visible week
        updateInitialMonthHeader();
    }


//    private void loadCurrentMonthCalendar() {
//        binding.layoutCalendar.removeAllViews();
//
//        Calendar tempCalendar = Calendar.getInstance();
//        tempCalendar.set(Calendar.YEAR, startDateCalendar.get(Calendar.YEAR));
//        tempCalendar.set(Calendar.MONTH, startDateCalendar.get(Calendar.MONTH));
//        tempCalendar.set(Calendar.DAY_OF_MONTH, 1);
//
//        while (tempCalendar.before(endDateCalendar) || tempCalendar.equals(endDateCalendar)) {
//            // Adjust to the first Sunday of the current month
//            Calendar firstSundayCalendar = (Calendar) tempCalendar.clone();
//            int firstDayOfWeek = firstSundayCalendar.get(Calendar.DAY_OF_WEEK);
//            firstSundayCalendar.add(Calendar.DAY_OF_MONTH, -(firstDayOfWeek - Calendar.SUNDAY));
//
//            Calendar endLoopCalendar = (Calendar) tempCalendar.clone();
//            endLoopCalendar.set(Calendar.DAY_OF_MONTH, tempCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
//
//            while (firstSundayCalendar.before(endLoopCalendar) || firstSundayCalendar.equals(endLoopCalendar)) {
//                LinearLayout weekLayout = new LinearLayout(this);
//                weekLayout.setLayoutParams(new LinearLayout.LayoutParams(
//                        LinearLayout.LayoutParams.MATCH_PARENT,
//                        LinearLayout.LayoutParams.WRAP_CONTENT
//                ));
//                weekLayout.setOrientation(LinearLayout.HORIZONTAL);
//
//                for (int day = 0; day < 7; day++) {
//                    TextView dayTextView = new TextView(this);
//                    dayTextView.setGravity(android.view.Gravity.CENTER);
//                    dayTextView.setTextSize(17);
//
//                    if (firstSundayCalendar.get(Calendar.MONTH) == tempCalendar.get(Calendar.MONTH)) {
//                        int dayOfMonth = firstSundayCalendar.get(Calendar.DAY_OF_MONTH);
//                        dayTextView.setText(String.valueOf(dayOfMonth));
//
//                        // Tag the TextView with its corresponding date for easier identification
//                        dayTextView.setTag(firstSundayCalendar.clone());
//
//                        if (firstSundayCalendar.before(startDateCalendar) || firstSundayCalendar.after(endDateCalendar)) {
//                            dayTextView.setBackgroundResource(R.drawable.bg_unavailable_date);
//                            dayTextView.setTextColor(ContextCompat.getColor(this, R.color.textunAvilableDate));
//                        } else {
//                            dayTextView.setBackgroundResource(R.drawable.bg_available_date);
//                            dayTextView.setTextColor(ContextCompat.getColor(this, R.color.white));
//                        }
//
//                        // Set click listener for date selection
//                        dayTextView.setOnClickListener(v -> handleDateSelection((Calendar) dayTextView.getTag(), dayTextView));
//                    } else {
//                        dayTextView.setText("");
//                    }
//
//                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                            0, 150, 1f);
//                    params.setMargins(8, 16, 8, 16);
//                    dayTextView.setLayoutParams(params);
//
//                    weekLayout.addView(dayTextView);
//                    firstSundayCalendar.add(Calendar.DAY_OF_MONTH, 1);
//                }
//
//                binding.layoutCalendar.addView(weekLayout);
//            }
//            tempCalendar.add(Calendar.MONTH, 1);
//        }
//
//        // Set the initial header to the first month loaded
//        updateInitialMonthHeader();
//    }

    private void selectTodayAsDefault() {
        Calendar today = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String todayString = sdf.format(today.getTime());

        for (int i = 0; i < binding.layoutCalendar.getChildCount(); i++) {
            LinearLayout weekLayout = (LinearLayout) binding.layoutCalendar.getChildAt(i);
            for (int j = 0; j < weekLayout.getChildCount(); j++) {
                TextView dayTextView = (TextView) weekLayout.getChildAt(j);
                Calendar currentCalendar = (Calendar) dayTextView.getTag();

                if (currentCalendar != null && sdf.format(currentCalendar.getTime()).equals(todayString)) {
                    selectedStartDate = (Calendar) currentCalendar.clone();
                    dayTextView.setBackgroundResource(R.drawable.bg_selected_date);
                    updateDateRangeDisplay();
                    TimeSloatBasedOnDate(todayString, todayString);
                    return;
                }
            }
        }
    }

    private void handleDateSelection(Calendar selectedDate, TextView dayTextView) {
        // Check if the selected date is within the valid range
        if (selectedDate.before(startDateCalendar) || selectedDate.after(endDateCalendar)) {
            Toast.makeText(this, "Selected date is out of the allowed range.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedStartDate == null) {
            selectedStartDate = (Calendar) selectedDate.clone();
            lastSelectedDateView = dayTextView;
            dayTextView.setBackgroundResource(R.drawable.bg_selected_date);
        } else if (selectedEndDate == null) {
            if (selectedDate.equals(selectedStartDate)) {
                clearPreviousSelection();
            } else {
                selectedEndDate = (Calendar) selectedDate.clone();
                if (selectedEndDate.before(selectedStartDate)) {
                    Calendar temp = selectedStartDate;
                    selectedStartDate = selectedEndDate;
                    selectedEndDate = temp;
                }
                highlightRange(selectedStartDate, selectedEndDate);
            }
        } else {
            if (selectedDate.equals(selectedStartDate) || selectedDate.equals(selectedEndDate)) {
                clearPreviousSelection();
            } else if (selectedDate.after(selectedStartDate) && selectedDate.before(selectedEndDate)) {
                selectedStartDate = (Calendar) selectedDate.clone();
                highlightRange(selectedStartDate, selectedEndDate);
            } else if (selectedDate.before(selectedStartDate)) {
                selectedStartDate = (Calendar) selectedDate.clone();
                highlightRange(selectedStartDate, selectedEndDate);
            } else if (selectedDate.after(selectedEndDate)) {
                selectedEndDate = (Calendar) selectedDate.clone();
                highlightRange(selectedStartDate, selectedEndDate);
            }
        }
        // Update time slots based on the selected date range
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        if (selectedStartDate != null) {
            String startDateStr = sdf.format(selectedStartDate.getTime());
            String endDateStr = selectedEndDate == null ? startDateStr : sdf.format(selectedEndDate.getTime());
            TimeSloatBasedOnDate(startDateStr, endDateStr);
        } else {
            adapter.removeTimes();
        }
//        if (startDateStr != null && endDateStr != null) {
//
//        }else {
//            adapter.removeTimes();
//        }
        updateDateRangeDisplay();
    }

    private void highlightRange(Calendar start, Calendar end) {
        for (int i = 0; i < binding.layoutCalendar.getChildCount(); i++) {
            LinearLayout weekLayout = (LinearLayout) binding.layoutCalendar.getChildAt(i);
            for (int j = 0; j < weekLayout.getChildCount(); j++) {
                TextView dayTextView = (TextView) weekLayout.getChildAt(j);
                if (!dayTextView.getText().toString().isEmpty()) {
                    Calendar currentCalendar = (Calendar) dayTextView.getTag();

                    if (currentCalendar != null &&
                            (currentCalendar.after(start) || currentCalendar.equals(start)) &&
                            (currentCalendar.before(end) || currentCalendar.equals(end))) {
                        if (currentCalendar.before(startDateCalendar) || currentCalendar.after(endDateCalendar)) {
                            continue; // Skip unavailable dates
                        }
                        dayTextView.setBackgroundResource(R.drawable.bg_selected_date);
                    } else if (currentCalendar != null &&
                            !currentCalendar.before(startDateCalendar) &&
                            !currentCalendar.after(endDateCalendar)) {
                        dayTextView.setBackgroundResource(R.drawable.bg_available_date);
                    }
                }
            }
        }
    }

    private void updateDateRangeDisplay() {
        SimpleDateFormat displayFormat = new SimpleDateFormat("d MMM, yyyy", Locale.getDefault());

        if (selectedStartDate == null) {
            binding.tvDateRange.setText("");
        } else if (selectedEndDate == null) {
            String startDateStr = displayFormat.format(selectedStartDate.getTime());
            binding.tvDateRange.setText(startDateStr);
        } else {
            String startDateStr = displayFormat.format(selectedStartDate.getTime());
            String endDateStr = displayFormat.format(selectedEndDate.getTime());
            binding.tvDateRange.setText(startDateStr + " - " + endDateStr);
        }
    }

    private void updateMonthHeaderOnScroll() {
        int scrollY = binding.scrollView.getScrollY();
        int totalHeight = 0;

        for (int i = 0; i < binding.layoutCalendar.getChildCount(); i++) {
            View weekView = binding.layoutCalendar.getChildAt(i);
            totalHeight += weekView.getHeight();

            if (scrollY < totalHeight) {
                Calendar visibleCalendar = (Calendar) startDateCalendar.clone();
                visibleCalendar.add(Calendar.WEEK_OF_YEAR, i);

                SimpleDateFormat monthYearFormat = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
                binding.tvMonthYear.setText(monthYearFormat.format(visibleCalendar.getTime()));
                break;
            }
        }
    }

    private void updateInitialMonthHeader() {
        Calendar initialCalendar = (Calendar) startDateCalendar.clone();
        SimpleDateFormat monthYearFormat = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        binding.tvMonthYear.setText(monthYearFormat.format(initialCalendar.getTime()));
    }

    private void adjustScrollViewHeight(int height) {
        ViewGroup.LayoutParams params = binding.scrollView.getLayoutParams();
        params.height = height;
        binding.scrollView.setLayoutParams(params);
    }

    private void clearPreviousSelection() {
        selectedStartDate = null;
        selectedEndDate = null;
        lastSelectedDateView = null;
        loadCurrentMonthCalendar();
    }

    void TimeSloatBasedOnDate(String startDate, String endDate) {
        Log.e("RoomDate", startDate);
        Log.e("RoomDate", endDate);
        Util.checkInternet(RoomBookingActivity.this, new Util.NetworkCheckCallback() {
            @Override
            public void onNetworkCheckComplete(boolean isAvailable) {
                if (isAvailable) {
                    apiServiceProvider.getTimeSlot(roomId, startDate, endDate, new RetrofitListener<TimeSlotModel>() {
                        @Override
                        public void onResponseSuccess(TimeSlotModel sucessRespnse, String apiFlag) {
                            if (sucessRespnse.getStatusCode() == 200 && sucessRespnse.getTimeslots() != null) {
                                // Set the new list of time slots to the adapter
                                if (roomType.equals("0")) {
                                    adapter.setTimeSlotList(sucessRespnse.getTimeslots(), startDate);
                                } else {
                                    if (sucessRespnse.getTimeslots().size() != 0) {
                                        unAvilableDate();
                                    }
                                }

                            } else {
                                Toast.makeText(RoomBookingActivity.this, "No available time slots", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                            try {
                                Toast.makeText(RoomBookingActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Toast.makeText(RoomBookingActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    hideLoader();
                }
            }
        });
    }

    void getRoomWithSlotDetails(String roomId, String slotId, String startDate, String endDate) {
        Util.checkInternet(this, new Util.NetworkCheckCallback() {
            @Override
            public void onNetworkCheckComplete(boolean isAvailable) {
                if (isAvailable) {
                    showLoader(RoomBookingActivity.this);
                    apiServiceProvider.getBookingWithSlotDetails(roomId, slotId, startDate, endDate, new RetrofitListener<BookingDetailsModel>() {
                        @Override
                        public void onResponseSuccess(BookingDetailsModel sucessRespnse, String apiFlag) {
                            hideLoader();
                            if (sucessRespnse.getStatusCode() == 200) {

                                Intent intent = new Intent(RoomBookingActivity.this, BookingTermsAndConditionActivity.class);
                                intent.putExtra(Constant.BOOKING_DETAILS, sucessRespnse);
                                intent.putExtra(Constant.SELECTED_TIME_SLOT, selectedSlotTiming);
                                intent.putExtra(Constant.TIME_SLOT_ID, selectedSlotId);
                                intent.putExtra(Constant.ROOM_START_DATE, startDate);
                                intent.putExtra(Constant.ROOM_END_DATE, endDate);
                                if (sucessRespnse.getData().getRoomImage() != null) {
                                    intent.putExtra(Constant.ROOM_IMAGE, sucessRespnse.getData().getRoomImage());
                                }
                                startActivity(intent);
//                                finish();
                            } else {
                                Toast.makeText(RoomBookingActivity.this, sucessRespnse.getMsg(), Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                            hideLoader();
                            try {
                                Toast.makeText(RoomBookingActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Toast.makeText(RoomBookingActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    hideLoader();
                }
            }
        });
    }

    void getRoomWithOutSlotDetails(String roomId, String startDate, String endDate) {
        Util.checkInternet(this, new Util.NetworkCheckCallback() {
            @Override
            public void onNetworkCheckComplete(boolean isAvailable) {
                if (isAvailable) {
                    showLoader(RoomBookingActivity.this);
                    apiServiceProvider.getBookingWithoutSlotDetails(roomId, startDate, endDate, new RetrofitListener<BookingDetailsModel>() {
                        @Override
                        public void onResponseSuccess(BookingDetailsModel sucessRespnse, String apiFlag) {
                            hideLoader();
                            if (sucessRespnse.getStatusCode() == 200) {

                                Intent intent = new Intent(RoomBookingActivity.this, BookingTermsAndConditionActivity.class);
                                intent.putExtra(Constant.BOOKING_DETAILS, sucessRespnse);
                                intent.putExtra(Constant.SELECTED_TIME_SLOT, selectedSlotTiming);
                                intent.putExtra(Constant.TIME_SLOT_ID, selectedSlotId);
                                intent.putExtra(Constant.ROOM_START_DATE, startDate);
                                intent.putExtra(Constant.ROOM_END_DATE, endDate);
                                if (sucessRespnse.getData().getRoomImage() != null) {
                                    intent.putExtra(Constant.ROOM_IMAGE, sucessRespnse.getData().getRoomImage());
                                }
                                startActivity(intent);
//                                finish();
                            } else {
                                Toast.makeText(RoomBookingActivity.this, sucessRespnse.getMsg(), Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                            hideLoader();
                            try {
                                Toast.makeText(RoomBookingActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Toast.makeText(RoomBookingActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    hideLoader();
                }
            }
        });
    }

    void updateRoom(String slotId, String startDate, String endDate) {
        UpdateBookingDatesRequest request;
        if (slotId != null) {
            request = new UpdateBookingDatesRequest(Integer.parseInt(bookingData.getBookingID()), startDate, endDate, slotId);
        } else {
            request = new UpdateBookingDatesRequest(Integer.parseInt(bookingData.getBookingID()), startDate, endDate);
        }
        Util.checkInternet(this, new Util.NetworkCheckCallback() {
            @Override
            public void onNetworkCheckComplete(boolean isAvailable) {
                if (isAvailable) {
                    showLoader(RoomBookingActivity.this);
                    apiServiceProvider.updateBooking(request, new RetrofitListener<UpdateBookingResponse>() {
                        @Override
                        public void onResponseSuccess(UpdateBookingResponse sucessRespnse, String apiFlag) {
                            hideLoader();
                            if (sucessRespnse.getStatusCode() == 200) {
                                if (sucessRespnse.getData().getPendingAmount() == 0) {
                                    confirmBooking(sucessRespnse.getData().getBookingID());
                                } else {
                                    Intent intent = new Intent(RoomBookingActivity.this, PaymentActivity.class);
                                    intent.putExtra(Constant.BOOKING_DETAILS, bookingData);
                                    intent.putExtra(Constant.SELECTED_TIME_SLOT, selectedSlotTiming);
                                    intent.putExtra(Constant.ROOM_START_DATE, startDate);
                                    intent.putExtra(Constant.ROOM_END_DATE, endDate);
                                    intent.putExtra(Constant.TIME_SLOT_ID, slotId);
                                    intent.putExtra(Constant.NAVIGATION_PATH, RESCHEDUL);
                                    intent.putExtra(Constant.BOOKING_ID, sucessRespnse.getData().getBookingID());
                                    intent.putExtra(Constant.OUTSTANDING_AMOUNT, sucessRespnse.getData().getPendingAmount());
                                    startActivity(intent);
                                }
//
                            } else {
                                Toast.makeText(RoomBookingActivity.this, sucessRespnse.getMsg(), Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                            hideLoader();
                            try {
                                Toast.makeText(RoomBookingActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Toast.makeText(RoomBookingActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    hideLoader();
                }
            }
        });
    }

    private void reSchedulingError() {
        // Create a new dialog
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.alert_dialog_comman);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        // Find the buttons from the custom layout
        TextView btnDone = dialog.findViewById(R.id.btn_done);

        // Set click listeners for the buttons
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss(); // Dismiss the dialog when "Not Now" is clicked
            }
        });
        // Show the dialog
        dialog.show();
    }

    void confirmBooking(int bookingID) {
        Log.e("Naimish", "BookingID" + bookingID);

//        ConfirmBookingRequest request;
//            if (bookingDetails.getData().getAdvanceDeposit() !=null || bookingDetails.getData().getAdvanceDeposit() != 0) {
        ConfirmBookingRequest request = new ConfirmBookingRequest(bookingData.getPaymentID(), bookingID);
        Util.checkInternet(this, new Util.NetworkCheckCallback() {
            @Override
            public void onNetworkCheckComplete(boolean isAvailable) {
                if (isAvailable) {
                    apiServiceProvider.confirmBooking(request, new RetrofitListener<ConfirmBookingResponse>() {
                        @Override
                        public void onResponseSuccess(ConfirmBookingResponse sucessRespnse, String apiFlag) {
                            hideLoader();
                            if (sucessRespnse.getStatusCode() == 200) {
                                bookingSuccessDialog(bookingID);
                            }
                        }

                        @Override
                        public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                            hideLoader();
                            try {
                                Toast.makeText(RoomBookingActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Toast.makeText(RoomBookingActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    hideLoader();
                }
            }
        });

    }


    private void bookingSuccessDialog(int bookingID) {
        // Create a new dialog
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.booking_dialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
        // Find the buttons from the custom layout
        RelativeLayout rlOk = dialog.findViewById(R.id.rl_ok);
        TextView message = dialog.findViewById(R.id.tv_message);
        message.setText("Your booking has been successfully rescheduled!. Your new Booking ID is" + bookingID + "." + "\nWe look forward to your visit.");
        rlOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constant.FROM_BOOKING = true;
                dialog.dismiss();
                Intent intent = new Intent(RoomBookingActivity.this, BookingActivity.class);
                startActivity(intent);
                finish();
            }
        });
        // Show the dialog
        dialog.show();
    }

    private void unAvilableDate() {
        // Create a new dialog
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.alert_dialog_comman);
        TextView title = dialog.findViewById(R.id.title);
        TextView message = dialog.findViewById(R.id.message);

        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        title.setText("Alert!");
        message.setText("The room is not available on the selected date. Please choose a different date or check for available time slots.");
        // Find the buttons from the custom layout
        TextView btnDone = dialog.findViewById(R.id.btn_done);

        btnDone.setText("OK");
        // Set click listeners for the buttons
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss(); // Dismiss the dialog when "Not Now" is clicked
            }
        });
        // Show the dialog
        dialog.show();
    }
}
