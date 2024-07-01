package com.iotsmartaliv.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.iotsmartaliv.R;

/**
 * This activity class is used for Live Video.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2018-10-23
 */
public class LiveVideoActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout relCancel;
    private ImageView imageView_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.live_video_activity);
        initViews();
        initListeners();
    }

    /**
     * Initializing listeners
     */
    private void initListeners() {
        relCancel.setOnClickListener(this);
        imageView_back.setOnClickListener(this);
    }

    /**
     * Initializing views
     */
    private void initViews() {
        relCancel = findViewById(R.id.rel_cancel);
        imageView_back = findViewById(R.id.img_back_add_card);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rel_cancel:
                finish();
                break;

            case R.id.img_back_add_card:
                finish();
                break;
        }
    }
}

