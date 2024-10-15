package com.iotsmartaliv.activity.booking;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.iotsmartaliv.R;
import com.iotsmartaliv.activity.AddCardActivity;
import com.iotsmartaliv.adapter.booking.AddCardForBookingActivity;
import com.iotsmartaliv.adapter.booking.BookListAdapter;
import com.iotsmartaliv.adapter.booking.CardAdapter;
import com.iotsmartaliv.databinding.ActivityCardBinding;

public class CardActivity extends AppCompatActivity {
    ActivityCardBinding binding;
    CardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
    }

    void init() {
//        binding.layoutNoData.tvTitle.setText(R.string.we_couldn_t_find_any_result);
//        binding.layoutNoData.tvDetail.setText(R.string.we_couldn_t_locate_any_relevant_results_in_your_communities_right_now_please_try_again_later);
//       binding.layoutNoData.llNoFeedback.setVisibility(View.VISIBLE);
//       binding.rvBooking.setVisibility(View.GONE);
        binding.rvCard.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CardAdapter(this);
        binding.rvCard.setAdapter(adapter);
        binding.llToolbar.tvHeader.setText("Card");
        binding.fabAddCard.setOnClickListener(v -> {
            Intent intent = new Intent(CardActivity.this, AddCardForBookingActivity.class);
            startActivity(intent);
        });
        binding.llToolbar.imgBack.setOnClickListener(v -> {
            finish();
        });
    }
}