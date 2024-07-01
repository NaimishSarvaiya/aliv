package com.iotsmartaliv.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.iotsmartaliv.R;

/**
 * This activity class is used for create guest activity.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2018-10-23
 */
public class CreateGuestActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private TextView tvCreate;
    private ImageView imgBackAddGuest;
    private EditText edtGuestName, edtDeviceName;
    private String guestName = "", deviceName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crate_guest_user);
        initView();
        initListener();
    }

    /**
     * Initialize views.
     */
    private void initView() {
        tvCreate = findViewById(R.id.tv_create);
        imgBackAddGuest = findViewById(R.id.img_back_add_guest);
        edtGuestName = findViewById(R.id.edt_guest_name);
        edtDeviceName = findViewById(R.id.edt_contact_number);
    }

    /**
     * Initialize listener.
     */
    private void initListener() {
        imgBackAddGuest.setOnClickListener(this);
        tvCreate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_create:
                guestName = edtDeviceName.getText().toString().trim();
                deviceName = edtDeviceName.getText().toString().trim();
                if (!guestName.equalsIgnoreCase("") &&
                        !deviceName.equalsIgnoreCase("")) {
                    Intent intent = new Intent();
                    intent.putExtra("guest_name", guestName);
                    intent.putExtra("device_name", deviceName);
                    setResult(RESULT_OK, intent);

                }
                finish();
                break;
            case R.id.img_back_add_guest:
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
