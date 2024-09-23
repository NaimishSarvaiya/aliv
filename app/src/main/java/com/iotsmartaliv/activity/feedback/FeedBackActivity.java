package com.iotsmartaliv.activity.feedback;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.iotsmartaliv.R;
import com.iotsmartaliv.activity.ViewPager.ViewPagerAdapter;
import com.iotsmartaliv.fragments.feedback.FeedbackHistoryFragment;
import com.iotsmartaliv.fragments.feedback.InProgressFeedbackFragment;
import com.iotsmartaliv.fragments.feedback.SentFeedbackFragment;

public class FeedBackActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageView imgBack;
    private RelativeLayout rlHeader, rl_createFeedBack;
    ViewPagerAdapter adapter;
    SentFeedbackFragment sentFeedbackFragment;
    InProgressFeedbackFragment inProgressFeedbackFragment;
    FeedbackHistoryFragment feedbackHistoryFragment;

    private int[] tabIcons = {
            R.drawable.sent_feedback,
            R.drawable.inprogress_feedback,
            R.drawable.history_feedback
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        init();
    }

    void init() {
        viewPager = findViewById(R.id.vp_feedback);
//        viewPager.setOffscreenPageLimit(1);
        tabLayout = findViewById(R.id.tablayout_feedback);
        imgBack = findViewById(R.id.img_back);
        rlHeader = findViewById(R.id.header_layout);
        rl_createFeedBack = findViewById(R.id.rl_createFeedBack);
        setupViewPagerTablayout();
        rl_createFeedBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FeedBackActivity.this, CreateFeedbackActivity.class);
                startActivityForResult(intent, 10);
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
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
            sentFeedbackFragment.loadFeed(1);
        }
    }

    private void setupViewPagerTablayout() {

        viewPager.setOffscreenPageLimit(3);

        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        sentFeedbackFragment = new SentFeedbackFragment();
        inProgressFeedbackFragment = new InProgressFeedbackFragment();
        feedbackHistoryFragment = new FeedbackHistoryFragment();

        adapter.addFragment(sentFeedbackFragment, "Sent");
        adapter.addFragment(inProgressFeedbackFragment, "In Progress");
        adapter.addFragment(feedbackHistoryFragment, "History");

        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);
        // Apply the custom ColorStateList for the tab icons
        tabLayout.setTabIconTintResource(R.color.tab_icon_color);
        setupTabIcons();

    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }

}