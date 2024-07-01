package com.iotsmartaliv.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.widget.TextView;

import com.iotsmartaliv.R;
import com.iotsmartaliv.adapter.TabAdapter;
import com.iotsmartaliv.fragments.instructor.AddNewInstructorFragment;
import com.iotsmartaliv.fragments.instructor.InstructorInductionFragment;
import com.iotsmartaliv.fragments.instructor.ListOfInstructorFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.iotsmartaliv.constants.Constant.LOGIN_DETAIL;

/**
 * This class is used as .
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 22/7/19 :July.
 */
public class InstructorActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    TabAdapter adapter;
    TextView toolbarTitle;
    InstructorInductionFragment instructorInductionFragment;
    ListOfInstructorFragment listOfInstructorFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        toolbarTitle = findViewById(R.id.toolbar_title);
        adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(new AddNewInstructorFragment(), "Add new Instructor");
        listOfInstructorFragment = new ListOfInstructorFragment();
        adapter.addFragment(listOfInstructorFragment, "List of instructors");
        for (String rolid : LOGIN_DETAIL.getRoleIDs()) {
            if (rolid.equalsIgnoreCase("2")) {
                instructorInductionFragment = new InstructorInductionFragment();
                adapter.addFragment(instructorInductionFragment, "Instructor induction");
            }
        }
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(3);

    }

    public void refreshPage() {
        try {
            instructorInductionFragment.initiate();
            listOfInstructorFragment.initiate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
