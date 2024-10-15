package com.iotsmartaliv.activity.booking;

import static com.iotsmartaliv.utils.Util.convertDateFormatForBooking;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.databinding.ActivityPreviewBookingBinding;
import com.iotsmartaliv.model.booking.BookingDetailsModel;

public class PreviewBookingActivity extends AppCompatActivity {
    ActivityPreviewBookingBinding binding;
    String startDate, endDate, timeSlot;
    BookingDetailsModel bookingDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPreviewBookingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.llToolbar.tvHeader.setText("Meeting Room");
        binding.rlConfirmBooking.setOnClickListener(v -> {
            Intent intent = new Intent(this, PaymentActivity.class);
            intent.putExtra(Constant.BOOKING_DETAILS, bookingDetails);
            intent.putExtra(Constant.SELECTED_TIME_SLOT, timeSlot);
            intent.putExtra(Constant.ROOM_START_DATE, startDate);
            intent.putExtra(Constant.ROOM_END_DATE, endDate);
            startActivity(intent);
        });

        binding.llToolbar.imgBack.setOnClickListener(v -> {
            finish();
        });
        setData();
    }

    private void setData() {
        Intent intent = getIntent();
        if (intent.getSerializableExtra(Constant.BOOKING_DETAILS) != null) {
            bookingDetails = (BookingDetailsModel) intent.getSerializableExtra(Constant.BOOKING_DETAILS);
        }
        if (intent.getStringExtra(Constant.ROOM_START_DATE) != null) {
            startDate = intent.getStringExtra(Constant.ROOM_START_DATE);
        } else {
            startDate = "";
        }
        if (intent.getStringExtra(Constant.ROOM_END_DATE) != null) {
            endDate = intent.getStringExtra(Constant.ROOM_END_DATE);
        } else {
            endDate = "";
        }
        if (intent.getStringExtra(Constant.SELECTED_TIME_SLOT) != null) {
            timeSlot = intent.getStringExtra(Constant.SELECTED_TIME_SLOT);
        } else {
            timeSlot = "";
        }

        if (bookingDetails != null) {
            Spanned cancelPolicyText;
            Spanned reSchedulePolicyText;
            if (bookingDetails.getData().getRoomName() != null) {
                binding.tvRoomName.setText(bookingDetails.getData().getRoomName());
                binding.llToolbar.tvHeader.setText(bookingDetails.getData().getRoomName());

            } else {
                binding.tvRoomName.setText("");
                binding.llToolbar.tvHeader.setText("");
            }
            if (bookingDetails.getData().getCommunityName() != null) {
                binding.tvCommunity.setText(bookingDetails.getData().getCommunityName());
            } else {
                binding.tvCommunity.setText("");
            }
            binding.tvBookingSlot.setText(timeSlot);
            if (startDate.equals(endDate)) {
                binding.tvBookingDate.setText(convertDateFormatForBooking(startDate));
            } else {
                binding.tvBookingDate.setText(convertDateFormatForBooking(startDate) + " - " + convertDateFormatForBooking(endDate));
            }
            if (bookingDetails.getData().getReschedulingPolicy() != null && !bookingDetails.getData().getReschedulingPolicy().isEmpty()) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    cancelPolicyText = Html.fromHtml(bookingDetails.getData().getReschedulingPolicy(), Html.FROM_HTML_MODE_LEGACY);
                } else {
                    cancelPolicyText = Html.fromHtml(bookingDetails.getData().getReschedulingPolicy());
                }
                binding.tvHeaderReschedule.setVisibility(View.VISIBLE);
                binding.llReschedule.setVisibility(View.VISIBLE);
                binding.tvReschedulePolicy.setText(cancelPolicyText);
            } else {
                binding.tvHeaderReschedule.setText("");
                binding.llReschedule.setVisibility(View.GONE);
                binding.tvReschedulePolicy.setVisibility(View.GONE);
            }

            // cancel Policy
            if (bookingDetails.getData().getCancellationPolicy() != null && !bookingDetails.getData().getCancellationPolicy().isEmpty()) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    cancelPolicyText = Html.fromHtml(bookingDetails.getData().getCancellationPolicy(), Html.FROM_HTML_MODE_LEGACY);
                } else {
                    cancelPolicyText = Html.fromHtml(bookingDetails.getData().getCancellationPolicy());
                }
                binding.tvHeaderCancelPolicy.setVisibility(View.VISIBLE);
                binding.llCancelPolicy.setVisibility(View.VISIBLE);
                binding.tvCancelPolicy.setText(cancelPolicyText);
            } else {
                binding.tvCancelPolicy.setText("");
                binding.tvHeaderCancelPolicy.setVisibility(View.GONE);
                binding.llCancelPolicy.setVisibility(View.GONE);
            }
            // Use the booking details object
        }
    }
}