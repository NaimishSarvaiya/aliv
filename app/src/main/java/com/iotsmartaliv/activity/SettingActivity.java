package com.iotsmartaliv.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.iotsmartaliv.R;

/**
 * This class is usages as activity fot app setting.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2018-10-23
 */
public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }

    public void goBack(View view) {
        onBackPressed();
    }

    public void deviceShake(View view) {
        Intent shake_intent = new Intent(SettingActivity.this, ShakeActivity.class);
        startActivity(shake_intent);
    }
}
