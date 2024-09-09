package com.iotsmartaliv.activity.booking;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.widget.TextView;

import com.iotsmartaliv.R;
import com.iotsmartaliv.adapter.TabAdapter;
import com.iotsmartaliv.databinding.ActivityBookingFacilityBinding;
import com.iotsmartaliv.fragments.booking.SearchRoomFragment;
import com.iotsmartaliv.fragments.booking.ShowBookedRoomFragment;


public class BookingFacilityActivity extends AppCompatActivity {

    ActivityBookingFacilityBinding binding;

//    @BindView(R.id.toolbar_title)
//    TextView toolbarTitle;
//    @BindView(R.id.toolbar)
//    Toolbar toolbar;
//    @BindView(R.id.tabLayout)
//    TabLayout tabLayout;
//    @BindView(R.id.viewPager)
//    ViewPager viewPager;
    TabAdapter adapter;
    ShowBookedRoomFragment showBookedRoomFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookingFacilityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        ButterKnife.bind(this);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(new SearchRoomFragment(), "Search Room");
        showBookedRoomFragment = new ShowBookedRoomFragment();
        adapter.addFragment(showBookedRoomFragment, "My Bookings");
        binding.viewPager.setAdapter(adapter);
        binding.tabLayout.setupWithViewPager(binding.viewPager);
        binding.viewPager.setOffscreenPageLimit(2);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 121) {
            if (resultCode == Activity.RESULT_OK) {
                showBookedRoomFragment.callAPI();
            }

        }
    }

}
