package com.iotsmartaliv.activity;

import android.os.Bundle;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

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
        // Wait until the layout is rendered
        binding.llBottom.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Remove the listener to prevent it from being called multiple times
                binding.llBottom.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                // Calculate the width of each child in LinearLayout with weight 1
                int totalWidth = binding.llBottom.getWidth();
                int totalheight = binding.llBottom.getWidth();
                int childWidth = (totalWidth / 5);
                childWidth = (int) (childWidth*1.4);
                // Since weightSum is 5, and we want width of 2// Since weightSum is 5, and we want width of 2

                // Set the width of the center ImageView
                ViewGroup.LayoutParams params = binding.rlKey.getLayoutParams();
                params.width = childWidth;
                binding.rlKey.setLayoutParams(params); // Update the layout with the new width
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.rvNoticeBoard.setLayoutManager(layoutManager);
        adapter = new NoticeBoardAdapter(this);
        binding.rvNoticeBoard.setAdapter(adapter);

    }
}