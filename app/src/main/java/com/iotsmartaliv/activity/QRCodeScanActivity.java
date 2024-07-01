package com.iotsmartaliv.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.iotsmartaliv.R;

/**
 * This activity class is used for QR-Code scanning.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2018-10-23
 */
public class QRCodeScanActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private RelativeLayout relSubmit;
    private ImageView imgBackEnrollment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        initView();
        initListener();
    }

    /**
     * Initialize views.
     */
    private void initView() {
        relSubmit = findViewById(R.id.rel_submit);
        imgBackEnrollment = findViewById(R.id.img_back_enrollment);
    }

    /**
     * Initialize listener.
     */
    private void initListener() {
        relSubmit.setOnClickListener(this);
        imgBackEnrollment.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rel_submit:
                finish();
                break;
            case R.id.img_back_enrollment:
                finish();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}