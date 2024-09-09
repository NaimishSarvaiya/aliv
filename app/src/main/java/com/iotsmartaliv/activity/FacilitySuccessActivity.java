package com.iotsmartaliv.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.iotsmartaliv.R;
import com.iotsmartaliv.databinding.ActivityFaceEnrollCameraBinding;
import com.iotsmartaliv.databinding.ActivityFacilitySuccessBinding;



/**
 * This activity class is used for Facility Success.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2018-10-23
 */
public class FacilitySuccessActivity extends AppCompatActivity {
    ActivityFacilitySuccessBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFacilitySuccessBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_facility_success);
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
