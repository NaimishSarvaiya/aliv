package com.iotsmartaliv.activity.booking;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.iotsmartaliv.R;
import com.iotsmartaliv.activity.ViewPager.ViewPagerAdapter;
import com.iotsmartaliv.databinding.ActivityBookingBinding;
import com.iotsmartaliv.fragments.booking.ActiveBookingFragment;
import com.iotsmartaliv.fragments.booking.BookFragment;
import com.iotsmartaliv.fragments.booking.BookingHistoryFragment;
import com.iotsmartaliv.fragments.booking.BookingTransactionFragment;

public class BookingActivity extends AppCompatActivity {
    ActivityBookingBinding binding;
    ViewPagerAdapter adapter;
    private int[] tabIcons = {
            R.drawable.booking,
            R.drawable.activities,
            R.drawable.transaction,
            R.drawable.inprogress_feedback
    };
    BookFragment bookingFragment;
    ActiveBookingFragment activeBookingFragment;
    BookingHistoryFragment bookingHistoryFragment;
    BookingTransactionFragment bookingTransactionFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
    }

    void init() {
        setupViewPagerTablayout();
//        rl_createFeedBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(FeedBackActivity.this, CreateFeedbackActivity.class);
//                startActivityForResult(intent, 10);
//            }
//        });

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
//                showCustomDialog("Your booking was successful! We look forward to your visit.");
//                showCustomDialog("Booking Failed!Unfortunately, your reservation couldn't be completed.",1);
//                showCustomDialog("Payment Failed! \nyour payment could not be processed. Please try again.",1);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 10) {
//            sentFeedbackFragment.loadFeed(1);
        }
    }

    private void setupViewPagerTablayout() {

        binding.vpBooking.setOffscreenPageLimit(3);

        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        bookingFragment = new BookFragment();
        activeBookingFragment = new ActiveBookingFragment();
        bookingTransactionFragment = new BookingTransactionFragment();
        bookingHistoryFragment = new BookingHistoryFragment();

        adapter.addFragment(bookingFragment, "Book");
        adapter.addFragment(activeBookingFragment, "Active");
        adapter.addFragment(bookingTransactionFragment, "Transactions");
        adapter.addFragment(bookingHistoryFragment, "History");

        binding.vpBooking.setAdapter(adapter);

        binding.tablayoutBooking.setupWithViewPager(binding.vpBooking);
        // Apply the custom ColorStateList for the tab icons
        binding.tablayoutBooking.setTabIconTintResource(R.color.tab_icon_color_booking);
        setupTabIcons();

    }

    private void setupTabIcons() {
        binding.tablayoutBooking.getTabAt(0).setIcon(tabIcons[0]);
        binding.tablayoutBooking.getTabAt(1).setIcon(tabIcons[1]);
        binding.tablayoutBooking.getTabAt(2).setIcon(tabIcons[2]);
        binding.tablayoutBooking.getTabAt(3).setIcon(tabIcons[3]);
    }

    private void showCustomDialog(String message, int status) {
        // Create an AlertDialog builder
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        // Inflate the custom layout
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.booking_dialog, null);
        dialogBuilder.setView(dialogView);
        // Get references to the TextView and Button in the custom layout
        TextView tvMessage = dialogView.findViewById(R.id.tv_message);
        TextView tv_title = dialogView.findViewById(R.id.tv_title);
        RelativeLayout btnOk = dialogView.findViewById(R.id.rl_ok);
        ImageView imgIcon = dialogView.findViewById(R.id.img_icon);
        if (status == 1) {
            imgIcon.setImageResource(R.mipmap.ic_error);
            imgIcon.setColorFilter(ContextCompat.getColor(this, R.color.Red), android.graphics.PorterDuff.Mode.MULTIPLY);
            tv_title.setText("Payment Failed!");
        }
        // Set the dialog message
        tvMessage.setText(message);
        // Set button click listener\
        // Create the dialog
        AlertDialog dialog = dialogBuilder.create();
        // Set the dialog background to be transparent, so the rounded corners are visible
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
        btnOk.setOnClickListener(v -> {
            // Dismiss the dialog on button click
            dialog.dismiss();
        });
        // Show the dialog
        dialog.show();
    }
}
