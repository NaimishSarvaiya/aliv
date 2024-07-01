package com.iotsmartaliv.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSeekBar;

import com.iotsmartaliv.R;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.services.ShakeOpenService;
import com.iotsmartaliv.utils.SharePreference;

import static com.iotsmartaliv.constants.Constant.BACKGROUND_SHAKE_ENABLE;
import static com.iotsmartaliv.constants.Constant.SHAKE_DISTANCE;
import static com.iotsmartaliv.constants.Constant.SHAKE_ENABLE;
import static com.iotsmartaliv.utils.CommanUtils.isServiceRunning;

/**
 * This activity class is used for Shake Setting.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2018-10-23
 */

public class ShakeActivity extends AppCompatActivity {


    private static String TAG = "Act_Shake";


    public Switch shake_open;
    public Switch shake_background;
    public AppCompatSeekBar sk_distance;

    public Button btn_select_device;

    private int progress;
    private Intent shakeService;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake);
        initView();
        setupView();
    }

    /**
     * init view
     */
    private void initView() {
        shake_background = findViewById(R.id.background_shake);
        shake_open = findViewById(R.id.lock_info_shake_open);
        sk_distance = findViewById(R.id.sk_distance);
        btn_select_device = findViewById(R.id.btn_select_device);
        shakeService = new Intent(this, ShakeOpenService.class);

        shake_open.setChecked(SharePreference.getInstance(ShakeActivity.this).getBoolean(SHAKE_ENABLE));

        shake_background.setChecked(SharePreference.getInstance(ShakeActivity.this).getBoolean(BACKGROUND_SHAKE_ENABLE));

        sk_distance.setMax(Constant.SHAKE_MAX_PROGRESS);

        sk_distance.setProgress(SharePreference.getInstance(ShakeActivity.this).getIntForShake(SHAKE_DISTANCE));

        sk_distance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                progress = seekBar.getProgress();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                progress = seekBar.getProgress();
                SharePreference.getInstance(ShakeActivity.this).putInt(SHAKE_DISTANCE, progress);
            }
        });
    }

    private void setupView() {
        //  isShake = userData.getShakeOpen(username);
        shake_open.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
            /*    if (deviceLIST == null || deviceLIST.size() == 0) {
                    Toast.makeText(this, "You need to add to device fist", Toast.LENGTH_SHORT).show();
                    return;
                }*/
                if (!isServiceRunning(ShakeActivity.this, "ShakeOpenService")) {

                    if (shakeService == null) {
                        shakeService = new Intent(ShakeActivity.this, ShakeOpenService.class);
                    }
                    //   shakeService.putExtra("distance", shakeProgress);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startForegroundService(shakeService);
                    } else {
                        startService(shakeService);

                    }
                } else {
                    Toast.makeText(ShakeActivity.this, "Already Running", Toast.LENGTH_SHORT).show();
                }
            } else {
                if (shakeService == null) {
                    shakeService = new Intent(ShakeActivity.this, ShakeOpenService.class);
                }
                stopService(shakeService);
            }
            SharePreference.getInstance(ShakeActivity.this).putBoolean(SHAKE_ENABLE, isChecked);

        });
        shake_background.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked)
                    onBackgroundShakeDialog1();
                else
                    SharePreference.getInstance(ShakeActivity.this).putBoolean(BACKGROUND_SHAKE_ENABLE, isChecked);

            }
        });
    }

    private void onBackgroundShakeDialog1() {


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ShakeActivity.this);
        alertDialogBuilder.setTitle("Shake Setting");
        alertDialogBuilder.setMessage("Enjoy contactless and hassle free Access by shaking " +
                "your phone! Do you want to “Shake” to open the door?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> {

                    onBackgroundShakeDialog2();

                })
                .setNegativeButton("No", (dialog, id) -> {

                    shake_background.setChecked(false);

                    dialog.dismiss();
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    private void onBackgroundShakeDialog2() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ShakeActivity.this);
        alertDialogBuilder.setTitle("Shake Setting");
        alertDialogBuilder.setMessage("We recommend enabling “Shake” to Open in the background " +
                "only if you frequently use it. Enabling “Shake” to Open in the background " +
                "requires ALiv app to run in the background while you are not using it. " +
                "Do you want to enable “Shake” to Open in the background?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> {

                    SharePreference.getInstance(ShakeActivity.this).putBoolean(BACKGROUND_SHAKE_ENABLE, true);

                })
                .setNegativeButton("No", (dialog, id) -> {
                    shake_background.setChecked(false);
                    dialog.dismiss();
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }


    public void goBack(View view) {
        onBackPressed();
    }

    public void selectDeviceClick(View view) {

    }
}
