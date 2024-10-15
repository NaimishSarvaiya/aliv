package com.iotsmartaliv.activity.booking;

import static com.iotsmartaliv.utils.Util.convertDateFormatForBooking;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.iotsmartaliv.R;
import com.iotsmartaliv.adapter.booking.RoomImageViewPagerAdapter;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.databinding.ActivityBookingTermsAndConditionBinding;
import com.iotsmartaliv.model.booking.BookingDetailsModel;

public class BookingTermsAndConditionActivity extends AppCompatActivity {
    ActivityBookingTermsAndConditionBinding binding;
    RoomImageViewPagerAdapter adapter;
    BookingDetailsModel bookingDetails;
    private int[] imageList = {
            R.mipmap.ic_room,
            R.mipmap.ic_room,
            R.mipmap.ic_room,
            R.mipmap.ic_room,
    };
    private ImageView[] dots;
    String startDate, endDate, timeSlot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookingTermsAndConditionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
    }

    void init() {
        setData();
        adapter = new RoomImageViewPagerAdapter(this, imageList);
        binding.viewPager.setAdapter(adapter);

        binding.viewPager.post(() -> addDotsIndicator(0));
        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                addDotsIndicator(position);
            }
        });
        binding.rlAgree.setOnClickListener(v -> {
            Intent intent = new Intent(BookingTermsAndConditionActivity.this, PreviewBookingActivity.class);
            intent.putExtra(Constant.BOOKING_DETAILS, bookingDetails);
            intent.putExtra(Constant.SELECTED_TIME_SLOT, timeSlot);
            intent.putExtra(Constant.ROOM_START_DATE, startDate);
            intent.putExtra(Constant.ROOM_END_DATE, endDate);
            startActivity(intent);
            finish();
        });
        binding.llToolbar.imgBack.setOnClickListener(v -> {
            finish();
        });
    }

    private void setData() {
        Intent intent = getIntent();
        if (intent.getSerializableExtra(Constant.BOOKING_DETAILS)!=null) {
            bookingDetails = (BookingDetailsModel) intent.getSerializableExtra(Constant.BOOKING_DETAILS);
        }
        if (intent.getStringExtra(Constant.ROOM_START_DATE)!=null) {
            startDate = intent.getStringExtra(Constant.ROOM_START_DATE);
        }else {
            startDate = "";
        }
        if (intent.getStringExtra(Constant.ROOM_END_DATE)!=null) {
            endDate = intent.getStringExtra(Constant.ROOM_END_DATE);
        }else {
            endDate = "";
        }
        if (intent.getStringExtra(Constant.SELECTED_TIME_SLOT)!=null) {
            timeSlot = intent.getStringExtra(Constant.SELECTED_TIME_SLOT);
        }else {
            timeSlot = "";
        }

        if (bookingDetails != null) {

            binding.llToolbar.tvHeader.setText(bookingDetails.getData().getRoomName());
            Spanned formattedText;

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                formattedText = Html.fromHtml(bookingDetails.getData().getTermConditions(), Html.FROM_HTML_MODE_LEGACY);
            } else {
                formattedText = Html.fromHtml(bookingDetails.getData().getTermConditions());
            }
            if (bookingDetails.getData().getRoomName()!=null) {
                binding.llToolbar.tvHeader.setText(bookingDetails.getData().getRoomName());
            }else {
                binding.llToolbar.tvHeader.setText("");
            }
            if (bookingDetails.getData().getTermConditions()!=null) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    formattedText = Html.fromHtml(bookingDetails.getData().getTermConditions(), Html.FROM_HTML_MODE_LEGACY);
                } else {
                    formattedText = Html.fromHtml(bookingDetails.getData().getTermConditions());
                }
                binding.tvTermsAndCondition.setText(formattedText);
            }else {
                binding.tvTermsAndCondition.setText("");
            }

            binding.tvSelectedSlot.setText(timeSlot);
            if (startDate.equals(endDate)) {
                binding.tvSelectedDate.setText(convertDateFormatForBooking(startDate));
            } else {
                binding.tvSelectedDate.setText(convertDateFormatForBooking(startDate) + " - " + convertDateFormatForBooking(endDate));
            }


            // Use the booking details object
        }


    }

    private void addDotsIndicator(int currentPage) {
        dots = new ImageView[imageList.length];
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


}