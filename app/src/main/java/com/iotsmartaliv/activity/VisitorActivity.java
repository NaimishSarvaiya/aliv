package com.iotsmartaliv.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.iotsmartaliv.R;
import com.iotsmartaliv.adapter.TabAdapter;
import com.iotsmartaliv.apiCalling.retrofit.ApiServiceProvider;
import com.iotsmartaliv.fragments.FragmentEvent;
import com.iotsmartaliv.fragments.FragmentGroup;
import com.iotsmartaliv.fragments.FragmentVisitor;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * This class is used as .
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 30/7/19 :July : 2019 on 15 : 06.
 */
public class VisitorActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    TabAdapter adapter;

    ApiServiceProvider apiServiceProvider;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitor);
        apiServiceProvider = ApiServiceProvider.getInstance(this);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentEvent(), "Event");
        adapter.addFragment(new FragmentGroup(), "Group");
        adapter.addFragment(new FragmentVisitor(), "Visitor");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(3);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
