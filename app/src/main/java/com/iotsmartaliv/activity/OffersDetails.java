package com.iotsmartaliv.activity;

import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.iotsmartaliv.R;
import com.iotsmartaliv.adapter.OffersDetailsPagerAdapter;
import com.iotsmartaliv.model.Images;

import java.util.ArrayList;
import java.util.List;

/**
 * This activity class is used for offers details.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2018-10-23
 */
public class OffersDetails extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private List<Images> images;
    private ViewPager viewPager;
    private RelativeLayout relOfferSubmit;
    private int[] img = {R.mipmap.ic_background, R.mipmap.ic_background, R.mipmap.ic_background};
    private int currentPosition = 0;
    private OffersDetailsPagerAdapter myCustomPagerAdapter;
    private ArrayList<Images> homeDetails = new ArrayList<>();
    private LinearLayout dotsLayout;
    private int dotsCount;
    private ImageView[] dots;
    /**
     * View Pagers page changed listener.
     */
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            currentPosition = position;
            for (int i = 0; i < dotsCount; i++) {
                dots[i].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
            }
            dots[position].setImageDrawable(getResources().getDrawable(R.drawable.unselected_dot));
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    };
    private ImageView imgBackOffer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers_details);
        initView();
        initListener();
    }

    /**
     * Initialize views.
     */
    private void initView() {
        viewPager = findViewById(R.id.viewpager);
        relOfferSubmit = findViewById(R.id.rel_submit);
        imgBackOffer = findViewById(R.id.img_back_offer);

        dotsLayout = null;
        dotsLayout = findViewById(R.id.viewPagerCountDots);

        for (int anImg : img) {
            Images images1 = new Images();
            images1.setImages(anImg);
            homeDetails.add(images1);
        }

        myCustomPagerAdapter = new OffersDetailsPagerAdapter(this, homeDetails);
        viewPager.setAdapter(myCustomPagerAdapter);
        viewPager.setCurrentItem(0);
        viewPager.setOnPageChangeListener(viewPagerPageChangeListener);
        viewPager.setHorizontalFadingEdgeEnabled(true);
        setUiPageViewController();
        try {
            myCustomPagerAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialize listener.
     */
    private void initListener() {
        relOfferSubmit.setOnClickListener(this);
        imgBackOffer.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rel_submit:
                finish();
                break;
            case R.id.img_back_offer:
                finish();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    /**
     * Setting suitable pages on tutorial
     */
    private void setUiPageViewController() {
        try {
            dotsCount = myCustomPagerAdapter.getCount();
            dots = null;
            dots = new ImageView[dotsCount];
            dotsLayout.removeAllViews();
            for (int i = 0; i < dotsCount; i++) {
                dots[i] = new ImageView(this);
                dots[i].setImageResource(R.drawable.selecteditem_dot);
                dots[i].setPadding(5, 5, 5, 5);
                dotsLayout.addView(dots[i]);
            }
            dots[0].setImageResource(R.drawable.unselected_dot);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

