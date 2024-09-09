package com.iotsmartaliv.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.iotsmartaliv.R;
import com.iotsmartaliv.databinding.ActivityBookServiceBinding;
import com.iotsmartaliv.databinding.ActivityBookingFacilityBinding;

/**
 * This activity class is used for book services.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2018-10-23
 */
public class BookServiceActivity extends AppCompatActivity {
    ActivityBookServiceBinding binding;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookServiceBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_book_service);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

//    @OnClick(R.id.book_service)
//    public void onViewClicked() {
//    }
}
