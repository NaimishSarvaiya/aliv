package com.iotsmartaliv.activity.digitalForm;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.iotsmartaliv.R;
import com.iotsmartaliv.activity.ViewPager.ViewPagerAdapter;
import com.iotsmartaliv.databinding.ActivityDigitalFormBinding;
import com.iotsmartaliv.databinding.FragmentApprovedFormBinding;
import com.iotsmartaliv.fragments.booking.ActiveBookingFragment;
import com.iotsmartaliv.fragments.booking.BookFragment;
import com.iotsmartaliv.fragments.booking.BookingHistoryFragment;
import com.iotsmartaliv.fragments.booking.BookingTransactionFragment;
import com.iotsmartaliv.fragments.form.ApplyFormFragment;
import com.iotsmartaliv.fragments.form.ApprovedFormFragment;
import com.iotsmartaliv.fragments.form.NotApprovedFormFragment;
import com.iotsmartaliv.fragments.form.SentFormFragment;

public class DigitalFormActivity extends AppCompatActivity {
    ActivityDigitalFormBinding binding;
    ViewPagerAdapter adapter;
    private int[] tabIcons = {
            R.drawable.apply_form,
            R.drawable.sent_form,
            R.drawable.apply_form,
            R.drawable.not_approved
    };
    ApplyFormFragment applyFormFragment;
    SentFormFragment sentFormFragment;
    ApprovedFormFragment approvedFormFragment;
    NotApprovedFormFragment notApprovedFormFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDigitalFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
    }

    void init() {
        binding.llToolbar.tvHeader.setText(R.string.online_form);
        binding.llToolbar.imgBack.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
        setupViewPagerTablayout();

    }

    private void setupViewPagerTablayout() {


        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        applyFormFragment = new ApplyFormFragment();
        sentFormFragment = new SentFormFragment();
        approvedFormFragment = new ApprovedFormFragment();
        notApprovedFormFragment = new NotApprovedFormFragment();

        adapter.addFragment(applyFormFragment, "Apply");
        adapter.addFragment(sentFormFragment, "Sent");
        adapter.addFragment(approvedFormFragment, "Approved");
        adapter.addFragment(notApprovedFormFragment, "Not Approved");

        binding.vpForm.setAdapter(adapter);
        binding.vpForm.setOffscreenPageLimit(1);

        binding.tablayoutForm.setupWithViewPager(binding.vpForm);
        // Apply the custom ColorStateList for the tab icons
        binding.tablayoutForm.setTabIconTintResource(R.color.tab_icon_color_booking);
        setupTabIcons();
    }

    private void setupTabIcons() {
        binding.tablayoutForm.getTabAt(0).setIcon(tabIcons[0]);
        binding.tablayoutForm.getTabAt(1).setIcon(tabIcons[1]);
        binding.tablayoutForm.getTabAt(2).setIcon(tabIcons[2]);
        binding.tablayoutForm.getTabAt(3).setIcon(tabIcons[3]);
    }

}