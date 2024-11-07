package com.iotsmartaliv.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iotsmartaliv.R;
import com.iotsmartaliv.adapter.booking.NoticeBoardAdapter;
import com.iotsmartaliv.databinding.ActivityNewMainBinding;

public class NewMainActivity extends AppCompatActivity {
    ActivityNewMainBinding binding;
    NoticeBoardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.rvNoticeBoard.setLayoutManager(layoutManager);
        adapter = new NoticeBoardAdapter(this);
        binding.rvNoticeBoard.setAdapter(adapter);

    }
}