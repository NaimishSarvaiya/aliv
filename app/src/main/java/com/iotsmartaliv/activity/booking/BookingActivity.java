package com.iotsmartaliv.activity.booking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
        binding.tablayoutBooking.setTabIconTintResource(R.color.tab_icon_color);
        setupTabIcons();

    }

    private void setupTabIcons() {
        binding.tablayoutBooking.getTabAt(0).setIcon(tabIcons[0]);
        binding.tablayoutBooking.getTabAt(1).setIcon(tabIcons[1]);
        binding.tablayoutBooking.getTabAt(2).setIcon(tabIcons[2]);
        binding.tablayoutBooking.getTabAt(3).setIcon(tabIcons[3]);
    }
}
