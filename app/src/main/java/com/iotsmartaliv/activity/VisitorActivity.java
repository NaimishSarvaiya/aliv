package com.iotsmartaliv.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.iotsmartaliv.adapter.TabAdapter;
import com.iotsmartaliv.apiAndSocket.retrofit.ApiServiceProvider;
import com.iotsmartaliv.databinding.ActivityVisitorBinding;
import com.iotsmartaliv.fragments.FragmentEvent;
import com.iotsmartaliv.fragments.FragmentGroup;
import com.iotsmartaliv.fragments.FragmentVisitor;


/**
 * This class is used as .
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 30/7/19 :July : 2019 on 15 : 06.
 */
public class VisitorActivity extends AppCompatActivity {
    TabAdapter adapter;

    ApiServiceProvider apiServiceProvider;
    ActivityVisitorBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVisitorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        apiServiceProvider = ApiServiceProvider.getInstance(this,false);
//        ButterKnife.bind(this);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentEvent(), "Event");
        adapter.addFragment(new FragmentGroup(), "Group");
        adapter.addFragment(new FragmentVisitor(), "Visitor");
        binding.viewPager.setAdapter(adapter);
        binding.tabLayout.setupWithViewPager(binding.viewPager);
        binding.viewPager.setOffscreenPageLimit(3);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
