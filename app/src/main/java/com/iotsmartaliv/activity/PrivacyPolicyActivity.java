package com.iotsmartaliv.activity;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.iotsmartaliv.R;
import com.iotsmartaliv.databinding.ActivityPrivacyPolicyBinding;

public class PrivacyPolicyActivity extends AppCompatActivity {

    ActivityPrivacyPolicyBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPrivacyPolicyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.pdfView.fromAsset("Term_and_Conditions.pdf")
                .defaultPage(0)
                .onPageChange(null)
                .enableAnnotationRendering(true)
                .onLoad(nbPages -> binding.imgCross.setVisibility(View.VISIBLE))
                .scrollHandle(new DefaultScrollHandle(this))
                .spacing(0)
                .load();
        binding.imgCross.setOnClickListener(v -> {
            onBackPressed();
        });
    }
}