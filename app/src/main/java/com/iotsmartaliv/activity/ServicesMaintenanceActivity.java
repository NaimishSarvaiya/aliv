package com.iotsmartaliv.activity;

import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.iotsmartaliv.R;
import com.iotsmartaliv.adapter.TabAdapter;
import com.iotsmartaliv.databinding.ActivitySerivcesMaintainenceBinding;
import com.iotsmartaliv.fragments.MaintenenceFragment;
import com.iotsmartaliv.fragments.ServiceFragment;


/**
 * This activity class is used for service maintenance.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2018-10-23
 */
public class ServicesMaintenanceActivity extends AppCompatActivity {

    TabAdapter adapter;
    ActivitySerivcesMaintainenceBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySerivcesMaintainenceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        ButterKnife.bind(this);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

//        binding.viewPager = findViewById(R.id.viewPager);
//        tabLayout = findViewById(R.id.tabLayout);

        adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(new ServiceFragment(), "Service");
        adapter.addFragment(new MaintenenceFragment(), "Maintenance");

        binding.viewPager.setAdapter(adapter);
        binding.tabLayout.setupWithViewPager(binding.viewPager);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}