package com.iotsmartaliv.activity.booking;

import static com.iotsmartaliv.constants.Constant.BOOKING_ID;
import static com.iotsmartaliv.constants.Constant.hideLoader;
import static com.iotsmartaliv.constants.Constant.showLoader;
import static com.iotsmartaliv.utils.Util.convertDateFormatForBooking;
import static com.iotsmartaliv.utils.Util.getBookingStatusDescription;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.iotsmartaliv.R;
import com.iotsmartaliv.adapter.booking.RoomImageViewPagerAdapter;
import com.iotsmartaliv.apiAndSocket.listeners.RetrofitListener;
import com.iotsmartaliv.apiAndSocket.models.ErrorObject;
import com.iotsmartaliv.apiAndSocket.retrofit.ApiServiceProvider;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.databinding.ActivityBookingDetailsBinding;
import com.iotsmartaliv.model.booking.BookingSlotDetailData;
import com.iotsmartaliv.model.booking.BookingSlotDetailResponse;
import com.iotsmartaliv.model.booking.CancelBookingRequest;
import com.iotsmartaliv.model.booking.CancelBookingResponse;
import com.iotsmartaliv.utils.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class BookingDetailsActivity extends AppCompatActivity implements RetrofitListener<BookingSlotDetailResponse> {
    ActivityBookingDetailsBinding binding;
    RoomImageViewPagerAdapter adapter;
    private List<String> roomImageList = new ArrayList<>();
    private ImageView[] dots;
    private ApiServiceProvider apiServiceProvider;
    String bookingID;
    String cancelPolicy, reSchedulePolicy;
    BookingSlotDetailData bookingData;
    private String startDate = "";
    private String endDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookingDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
    }

    void init() {
        if (getIntent().getStringExtra(Constant.PATH).equalsIgnoreCase(Constant.FROM_HISTORY_BOOKING)) {
            binding.llButtons.setVisibility(View.GONE);
        } else {
            binding.llButtons.setVisibility(View.VISIBLE);
        }
        if (getIntent().getStringExtra(BOOKING_ID) != null && !getIntent().getStringExtra(BOOKING_ID).isEmpty()) {
            bookingID = getIntent().getStringExtra(BOOKING_ID);
        }
        apiServiceProvider = ApiServiceProvider.getInstance(this, true);
        getBookedSlotDetail();
        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                addDotsIndicator(position);
            }
        });
        binding.imgBack.setOnClickListener(v -> {
            onBackPressed();
        });
        binding.rlCancel.setOnClickListener(v -> {
            showCancelandReshceduleDialog(0);
        });
        binding.rlReSchedule.setOnClickListener(v -> {
            showCancelandReshceduleDialog(1);
        });
    }

    private void addDotsIndicator(int currentPage) {
        dots = new ImageView[roomImageList.size()];
        binding.dotsLayout.removeAllViews(); // Clear previous dots

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                    R.drawable.inactive_dot)); // Default inactive dot

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    18, 18);
            params.setMargins(8, 0, 8, 0);
            binding.dotsLayout.addView(dots[i], params);
        }

        // Highlight the current dot
        if (dots.length > 0) {
            dots[currentPage].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                    R.drawable.active_dot));
        }
    }

    void getBookedSlotDetail() {
        Util.checkInternet(this, new Util.NetworkCheckCallback() {
            @Override
            public void onNetworkCheckComplete(boolean isAvailable) {
                if (isAvailable) {
                    apiServiceProvider.getBookingSlotDetail(bookingID, BookingDetailsActivity.this);
                } else {
                    hideLoader();
                }
            }
        });

    }

    @Override
    public void onResponseSuccess(BookingSlotDetailResponse sucessRespnse, String apiFlag) {
        if (sucessRespnse.getStatusCode() == 200) {
            if (!sucessRespnse.getData().isEmpty()) {
                setDate(sucessRespnse.getData().get(0));
                bookingData = sucessRespnse.getData().get(0);
            } else {
                Toast.makeText(this, sucessRespnse.getMsg(), Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(this, sucessRespnse.getMsg(), Toast.LENGTH_LONG).show();
        }
    }

    private void setDate(BookingSlotDetailData data) {
        if (data.getRoomBookLimit() != null) {
            dayCount(Integer.parseInt(data.getRoomBookLimit()));
        } else {
            dayCount(10);
        }

        cancelPolicy = data.getCancellationPolicy();
        reSchedulePolicy = data.getReschedulingPolicy();
        if (data.getRoomImage() != null) {
            roomImageList.addAll(data.getRoomImage());
            binding.ccRoomImage.setVisibility(View.GONE);
            binding.viewPager.setVisibility(View.VISIBLE);
        } else {
            binding.imgRoom.setImageResource(R.mipmap.ic_room);
            binding.viewPager.setVisibility(View.GONE);
            binding.ccRoomImage.setVisibility(View.VISIBLE);
        }
        adapter = new RoomImageViewPagerAdapter(this, roomImageList);
        binding.viewPager.setAdapter(adapter);
        binding.viewPager.post(() -> addDotsIndicator(0));
        binding.tvBookingID.setText("#" + data.getBookingID());
        binding.tvRoomName.setText(data.getRoomName());
        binding.tvHeader.setText(data.getRoomName());
        if (data.getStartDate().equalsIgnoreCase(data.getEndDate())) {
            binding.tvBookingDate.setText(convertDateFormatForBooking(data.getStartDate()));
        } else {
            binding.tvBookingDate.setText(convertDateFormatForBooking(data.getStartDate()) + " - " + convertDateFormatForBooking(data.getEndDate()));
        }

        if (data.getStartTime() != null) {
            SimpleDateFormat inputFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            String formattedEndTime;
            String formattedStartTime;
            try {
                formattedEndTime = outputFormat.format(inputFormat.parse(data.getEndTime()));
                formattedStartTime = outputFormat.format(inputFormat.parse(data.getStartTime()));

            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            binding.tvBookingSlot.setText(formattedStartTime + " : " + formattedEndTime);
        } else {
            binding.tvBookingSlot.setVisibility(View.GONE);
        }
        if (data.getFees() != null) {
            binding.tvBookingFees.setText("$" + data.getFees());
        }
        if (data.getDeposit() != null && !data.getDeposit().isEmpty() && !data.getDeposit().equalsIgnoreCase("0")) {
            binding.tvDepositAmt.setText("$" + data.getDeposit());
            binding.tvDepostPaymentID.setText(data.getDepositPaymentID());
            binding.llDeposit.setVisibility(View.VISIBLE);
            binding.tvDepReturnStatus.setText(data.getDepositStatus());
        } else {
            binding.llDeposit.setVisibility(View.GONE);
        }
        if (data.getPaymentID() != null) {
            binding.tvBookigPaymnetID.setText(data.getPaymentID());
        }
        binding.tvCommunity.setText(data.getCommunityName());
        String bookingStatus = getBookingStatusDescription(data.getBookingStatus());
        binding.tvBookingStatus.setText(bookingStatus);

// Reschedule Tag
        if (data.getRescheduled().equals("1")) {
            binding.tvReScheduled.setVisibility(View.VISIBLE);
            binding.rlReSchedule.setVisibility(View.GONE);
        } else {
            if (data.getReschedulingPolicy() != null &&
                    !data.getReschedulingPolicy().equals("")
                    && Integer.parseInt(data.getRescheduleLimit()) > 0
                    && data.getRescheduled().equals("0")) {

                binding.tvReScheduled.setVisibility(View.GONE);
                binding.rlReSchedule.setVisibility(View.VISIBLE);
            } else {
                    binding.tvReScheduled.setVisibility(View.GONE);
                    binding.rlReSchedule.setVisibility(View.GONE);
            }
        }
//        Deposit return detail
//        if (data.getIsRefunded() != null && data.getIsRefunded().equals("1")) {
//            binding.llDepositReturn.setVisibility(View.VISIBLE);
//            if (data.getDepositRefunded().equals("0")) {
//                binding.tvDepositReturnAmt.setText(data.getDepositRefunded());
//            } else {
//                binding.tvDepositReturnAmt.setText("-");
//            }
//            binding.tvDepReturnStatus.setText(Util.getDepostiReturnStatusDescription(data.getDepositStatus()));
//        }

        if (data.getDepositRefunded() != null && !data.getDepositRefunded().equals("0")) {
            binding.llDepositReturn.setVisibility(View.VISIBLE);
            if (data.getDepositRefunded().equals("0")) {
                binding.tvDepositReturnAmt.setText(data.getDepositRefunded());
            } else {
                binding.tvDepositReturnAmt.setText("-");
            }
            binding.tvDepReturnStatus.setText(Util.getDepostiReturnStatusDescription(data.getDepositStatus()));

        } else {
            if (data.getIsRefunded() != null && data.getIsRefunded().equals("1")) {
                binding.llDepositReturn.setVisibility(View.VISIBLE);
//                if (data.getDepositRefunded().equals("0")) {
                binding.tvDepositReturnAmt.setText(data.getDeposit());
//                } else {
//                    binding.tvDepositReturnAmt.setText("-");
//                }
                binding.tvDepReturnStatus.setText("Payment refunded. It may take a few days for the money to reach the customer's bank account.");
            }
        }

//        Penelty Details
        if ( data.getPenaltyAmount() != null && data.getPenaltyAmount().equals("0") ) {
            binding.llPenelty.setVisibility(View.GONE);
        } else {
            binding.llPenelty.setVisibility(View.VISIBLE);
            binding.tvPenltyAmt.setText(data.getPenaltyAmount());
            binding.tvPenaltyResoan.setText(data.getPenaltyFor());
        }

    }

    @Override
    public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
        try {
            Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }
    }

    // 0 = cancel
    // 2 = reSchedule
//    private void showCancelandReshceduleDialog(int type) {
//        Spanned formattedText;
//        final Dialog dialog = new Dialog(this);
//        dialog.setContentView(R.layout.confirmation_dialog_for_booking);
//        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//        // Get the screen width
//        DisplayMetrics metrics = new DisplayMetrics();
//        this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
//        int screenWidth = metrics.widthPixels;
//
//        // Set dialog width to 80% of screen width
//        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
//        params.width = (int) (screenWidth * 0.9);
//        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        dialog.getWindow().setAttributes(params);
//        // Find the buttons from the custom layout
//        TextView btnNotNow = dialog.findViewById(R.id.btn_not_now);
//        TextView btnConfirm = dialog.findViewById(R.id.btn_confirm);
//        TextView message = dialog.findViewById(R.id.message);
//        ScrollView scrollview = dialog.findViewById(R.id.scrollView);
//
//            btnNotNow.setText("Not Now");
//            btnConfirm.setText("Confirm");
//
//            if (type == 0) {
//                if (cancelPolicy != null) {
//                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
//                        formattedText = Html.fromHtml(cancelPolicy, Html.FROM_HTML_MODE_LEGACY);
//                    } else {
//                        formattedText = Html.fromHtml(cancelPolicy);
//                    }
//                    message.setText(formattedText);
//                } else {
//                    message.setText("Are you sure you want to cancel \n this booking?");
//                }
//            }else {
//                if (reSchedulePolicy !=null){
//                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
//                        formattedText = Html.fromHtml(reSchedulePolicy, Html.FROM_HTML_MODE_LEGACY);
//                    } else {
//                        formattedText = Html.fromHtml(reSchedulePolicy);
//                    }
//                    message.setText(formattedText);
//                }else {
//                    message.setText("Are you sure you want to reschedule \n this booking?");
//
//                }
//            }
//        btnNotNow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss(); // Dismiss the dialog when "Not Now" is clicked
//            }
//        });
//      btnConfirm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//                if (type==0) {
//                    cancelBooking();
//                }else {
//                    Intent intent = new Intent(BookingDetailsActivity.this,RoomBookingActivity.class);
//                    startActivity(intent);
//                }
//            }
//        });
//
//        dialog.show();
//    }

    private void showCancelandReshceduleDialog(int type) {
        Spanned formattedText;
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.confirmation_dialog_for_booking);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        // Get the screen width and height
        DisplayMetrics metrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenWidth = metrics.widthPixels;
        int screenHeight = metrics.heightPixels;

        // Set dialog width to 80% of screen width
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = (int) (screenWidth * 0.9);
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(params);

        // Find the buttons and message TextView from the custom layout
        TextView btnNotNow = dialog.findViewById(R.id.btn_not_now);
        TextView btnConfirm = dialog.findViewById(R.id.btn_confirm);
        TextView message = dialog.findViewById(R.id.message);
        TextView title = dialog.findViewById(R.id.title);
        ScrollView scrollview = dialog.findViewById(R.id.scrollView);

        btnNotNow.setText("Not Now");
        btnConfirm.setText("Confirm");

        // Get the formatted text based on the type (0 for cancel, 1 for reschedule)
        if (type == 0) {
            if (cancelPolicy != null) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    formattedText = Html.fromHtml(cancelPolicy, Html.FROM_HTML_MODE_LEGACY);
                } else {
                    formattedText = Html.fromHtml(cancelPolicy);
                }
                message.setText(formattedText);
            } else {
                formattedText = Html.fromHtml("Are you sure you want to cancel \n this booking?");
                message.setText(formattedText);
            }
        } else {
            title.setText("Confirm Reschedule");
            if (reSchedulePolicy != null) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    formattedText = Html.fromHtml(reSchedulePolicy, Html.FROM_HTML_MODE_LEGACY);
                } else {
                    formattedText = Html.fromHtml(reSchedulePolicy);
                }
                message.setText(formattedText);
            } else {
                formattedText = Html.fromHtml("Are you sure you want to reschedule \n this booking?");
                message.setText(formattedText);
            }
        }

        // Get the text length of formattedText
        int textLength = formattedText.length();

        // Adjust the ScrollView height based on the text length
        ViewGroup.LayoutParams scrollViewParams = scrollview.getLayoutParams();
        if (textLength <= 360) {
            // Set ScrollView height to wrap_content if text length is 360 or less
            scrollViewParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        } else {
            // Set ScrollView height to 60% of the screen height if text length is greater than 360
            scrollViewParams.height = (int) (screenHeight * 0.6);
        }
        scrollview.setLayoutParams(scrollViewParams);

        // Set up button click listeners
        btnNotNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss(); // Dismiss the dialog when "Not Now" is clicked
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (type == 0) {
                    cancelBooking();
                } else {
                    Intent intent = new Intent(BookingDetailsActivity.this, RoomBookingActivity.class);
                    intent.putExtra(Constant.ROOM_ID, bookingData.getRoomID());
                    intent.putExtra(Constant.ROOM_START_DATE, startDate);
                    intent.putExtra(Constant.ROOM_END_DATE, endDate);
                    intent.putExtra(Constant.ROOM_TITLE, bookingData.getRoomName());
                    intent.putExtra(Constant.ROOM_TYPE, bookingData.getRoomType());
                    intent.putExtra(Constant.NAVIGATION_PATH, Constant.RESCHEDUL);
                    intent.putExtra(Constant.BOOKING_DETAIL_DATA, bookingData);
                    startActivity(intent);
                }
            }
        });

        dialog.show();
    }


    void cancelBooking() {
        CancelBookingRequest request = new CancelBookingRequest(Integer.parseInt(bookingID));
        Util.checkInternet(this, new Util.NetworkCheckCallback() {
            @Override
            public void onNetworkCheckComplete(boolean isAvailable) {
                if (isAvailable) {
                    showLoader(BookingDetailsActivity.this);
                    apiServiceProvider.cancelBooking(request, new RetrofitListener<CancelBookingResponse>() {
                        @Override
                        public void onResponseSuccess(CancelBookingResponse sucessRespnse, String apiFlag) {
                            if (sucessRespnse.getStatusCode() == 200) {
                                Constant.FROM_BOOKING = true;
                                Intent intent = new Intent(BookingDetailsActivity.this, BookingActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }

                        @Override
                        public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                            hideLoader();
                            try {
                                Toast.makeText(BookingDetailsActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Toast.makeText(BookingDetailsActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    hideLoader();
                }
            }
        });
    }

    void dayCount(int count) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        // Get today's date
        Calendar calendar = Calendar.getInstance();
        startDate = dateFormat.format(calendar.getTime());

        // Calculate the end date by adding count - 1 days to today's date
        calendar.add(Calendar.DAY_OF_YEAR, count - 1);
        endDate = dateFormat.format(calendar.getTime());
        Log.e("count", String.valueOf(count));
        Log.e("startDate", startDate);
        Log.e("endDate", endDate);
    }
}
