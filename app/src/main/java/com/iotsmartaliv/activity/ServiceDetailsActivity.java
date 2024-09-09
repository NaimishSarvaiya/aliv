package com.iotsmartaliv.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.widget.Button;
import android.widget.TextView;

import com.iotsmartaliv.R;
import com.iotsmartaliv.databinding.ActivityServiceDetailsBinding;

/**
 * This activity class is used for service details  .
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2018-10-23
 */
public class ServiceDetailsActivity extends AppCompatActivity {
    ActivityServiceDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityServiceDetailsBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_service_details);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
