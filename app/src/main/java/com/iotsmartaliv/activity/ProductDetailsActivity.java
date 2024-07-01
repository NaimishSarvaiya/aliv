package com.iotsmartaliv.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.iotsmartaliv.R;
import com.iotsmartaliv.adapter.ProductDetailsPagerAdapter;
import com.iotsmartaliv.model.Images;

import java.util.ArrayList;
import java.util.List;

/**
 * This activity class is used for product details.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2018-10-23
 */
public class ProductDetailsActivity extends AppCompatActivity {
    public List<Images> images;
    ViewPager viewPager;
    ImageView back;
    ProductDetailsPagerAdapter pagerAdapt;
    int[] img = {R.mipmap.ic_cctv, R.mipmap.ic_cctv, R.mipmap.ic_cctv};
    int currentPosition = 0;
    ProductDetailsPagerAdapter myCustomPagerAdapter;
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
                dots[i].setImageResource(R.drawable.selecteditem_dot);
            }
            dots[position].setImageResource(R.drawable.unselected_dot);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    };
    private Button btnAddToCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        viewPager = findViewById(R.id.viewpager);
        btnAddToCart = findViewById(R.id.AddToCart);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProductDetailsActivity.this, MyCartActivity.class));

            }
        });
        dotsLayout = null;
        dotsLayout = findViewById(R.id.viewPagerCountDots);

        for (int anImg : img) {
            Images images1 = new Images();
            images1.setImages(anImg);
            homeDetails.add(images1);
        }

        myCustomPagerAdapter = new ProductDetailsPagerAdapter(this, homeDetails);
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
