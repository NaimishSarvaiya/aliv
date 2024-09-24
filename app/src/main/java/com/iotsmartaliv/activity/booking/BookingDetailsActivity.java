package com.iotsmartaliv.activity.booking;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.iotsmartaliv.R;
import com.iotsmartaliv.adapter.booking.RoomImageViewPagerAdapter;
import com.iotsmartaliv.databinding.ActivityBookingDetailsBinding;

public class BookingDetailsActivity extends AppCompatActivity {
    ActivityBookingDetailsBinding binding;
    RoomImageViewPagerAdapter adapter;
    private int[] imageList = {
            R.mipmap.ic_room,
            R.mipmap.ic_room,
            R.mipmap.ic_room,
            R.mipmap.ic_room,
    };
    private ImageView[] dots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookingDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
    }

    void init() {
        adapter = new RoomImageViewPagerAdapter(this, imageList);
        binding.viewPager.setAdapter(adapter);

        binding.viewPager.post(() -> addDotsIndicator(0));
        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                addDotsIndicator(position);
            }
        });
    }
    private void addDotsIndicator(int currentPage) {
        dots = new ImageView[imageList.length];
        binding.dotsLayout.removeAllViews(); // Clear previous dots

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                    R.drawable.inactive_dot)); // Default inactive dot

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    18, 18);
            params.setMargins(8, 0, 8, 0);
            binding.dotsLayout.addView(dots[i], params);
        }

        // Highlight the current dot
        if (dots.length > 0) {
            dots[currentPage].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                    R.drawable.active_dot));
        }
    }

}