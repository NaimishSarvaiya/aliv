package com.iotsmartaliv.activity.booking;

import static com.iotsmartaliv.constants.Constant.LOGIN_DETAIL;
import static com.iotsmartaliv.constants.Constant.hideLoader;
import static com.iotsmartaliv.utils.Util.convertDateFormatForBooking;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.iotsmartaliv.R;
import com.iotsmartaliv.adapter.booking.AddCardForBookingActivity;
import com.iotsmartaliv.apiAndSocket.listeners.RetrofitListener;
import com.iotsmartaliv.apiAndSocket.models.ErrorObject;
import com.iotsmartaliv.apiAndSocket.retrofit.ApiServiceProvider;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.databinding.ActivityPaymentBinding;
import com.iotsmartaliv.model.booking.BookingDetailsModel;
import com.iotsmartaliv.model.booking.CreateCustomerOnStripRequest;
import com.iotsmartaliv.model.booking.CreateCustomerResponse;
import com.iotsmartaliv.utils.Util;

public class PaymentActivity extends AppCompatActivity {
    ActivityPaymentBinding binding;
    BookingDetailsModel bookingDetails;
    String startDate, endDate, timeSlot;
    private ApiServiceProvider apiServiceProvider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        apiServiceProvider = ApiServiceProvider.getInstance(this,true);
        binding.tvChangeCard.setOnClickListener(v -> {
            Intent intent = new Intent(this, CardActivity.class);
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

            binding.llToolbar.tvHeader.setText(bookingDetails.getData().getRoomName());
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
            if (bookingDetails.getData().getRoomName() != null) {
                binding.llToolbar.tvHeader.setText(bookingDetails.getData().getRoomName());
            } else {
                binding.llToolbar.tvHeader.setText("");
            }
            binding.tvSlot.setText(timeSlot);
            if (startDate.equals(endDate)) {
                binding.tvBookingDate.setText(convertDateFormatForBooking(startDate));
            } else {
                binding.tvBookingDate.setText(convertDateFormatForBooking(startDate) + " - " + convertDateFormatForBooking(endDate));
            }
            if (bookingDetails.getData().getFees() != null) {
                binding.tvBookinFees.setText("$ "+String.valueOf(bookingDetails.getData().getFees().toString()));
                binding.tvTotal.setText("$ "+String.valueOf(bookingDetails.getData().getFees().toString()));
            } else {
                binding.tvBookinFees.setText("");
                binding.tvTotal.setText("");
            }
            if (bookingDetails.getData().getAdvanceDeposit()!=null){
                binding.tvDeposit.setText("$ "+ String.valueOf(bookingDetails.getData().getAdvanceDeposit().toString()));
            }else {
                binding.tvDeposit.setText("");
            }
            // Use the booking details object
        }
        createCustomerOnStripe();
    }

    void createCustomerOnStripe(){
        Util.checkInternet(this, new Util.NetworkCheckCallback() {
            CreateCustomerOnStripRequest request = new CreateCustomerOnStripRequest(LOGIN_DETAIL.getUserEmail(),LOGIN_DETAIL.getUserFullName());

            @Override
            public void onNetworkCheckComplete(boolean isAvailable) {
                if (isAvailable) {
                    apiServiceProvider.createCustomerOnStripe(request, new RetrofitListener<CreateCustomerResponse>() {
                        @Override
                        public void onResponseSuccess(CreateCustomerResponse sucessRespnse, String apiFlag) {
                            if (sucessRespnse.getStatusCode() ==200) {
                                Toast.makeText(PaymentActivity.this,sucessRespnse.getCustomerID(),Toast.LENGTH_LONG ).show();
                            }
                        }
                        @Override
                        public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                            try {
                                Toast.makeText(PaymentActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Toast.makeText(PaymentActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    hideLoader();
                }
            }
        });
    }
}