package com.iotsmartaliv.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.iotsmartaliv.R;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.onboarding.OnBoardingPermissionActivity;
import com.iotsmartaliv.utils.SharePreference;

import java.util.ArrayList;
import java.util.List;

import static com.iotsmartaliv.constants.Constant.LOGIN_DETAIL;
import static com.iotsmartaliv.constants.Constant.LOGIN_PREFRENCE;

/**
 * This activity class is used for splash screen.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2018-10-23
 */
public class SplashActivity extends AppCompatActivity {

    public static boolean checkFromOnResume = false;
    private boolean isUserLogin;
    private Intent onboardingPermission;
    boolean hasOnBoardingShown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        hasOnBoardingShown = SharePreference.getInstance(SplashActivity.this).getBoolean(Constant.HAS_ON_BOARDING_SHOWN);
        onboardingPermission = new Intent(this, OnBoardingPermissionActivity.class);

        moveToNxtScreen();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (checkFromOnResume) {
            if (checkAndRequestPermissions()) {
                // All permissions granted
                moveToNxtScreen();
            } else {
                // Permissions required
            }
        }
    }

    /**
     * This method is used for opening login activity.
     */
    private void openActivityLogin() {
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        intent.putExtra("showAlert", false);
        startActivity(intent);
        finish();
    }

    /**
     * This method is used for opening main activity.
     */
    private void openActivityMain() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean checkAndRequestPermissions() {
        int camerapermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int location = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int recordaudio = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        int permissionLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int bluetoothPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (camerapermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (location != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (recordaudio != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECORD_AUDIO);
        }

        if (bluetoothPermission != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                listPermissionsNeeded.add(Manifest.permission.BLUETOOTH_CONNECT);
            }
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R && permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            listPermissionsNeeded.add(Manifest.permission.READ_MEDIA_VIDEO);
            listPermissionsNeeded.add(Manifest.permission.READ_MEDIA_IMAGES);
        }

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_DENIED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                ActivityCompat.requestPermissions(SplashActivity.this, new String[]{Manifest.permission.BLUETOOTH_SCAN}, 2);
                listPermissionsNeeded.add(Manifest.permission.BLUETOOTH_SCAN);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            onboardingPermission.putExtra(Constant.PERMISSION_ARRAY, listPermissionsNeeded.toArray(new String[0]));
            return false;
        }

        return true;
    }

    private void moveToNxtScreen() {
        isUserLogin = SharePreference.getInstance(SplashActivity.this).getBoolean(Constant.IS_LOGIN);
        new Handler().postDelayed(() -> {
            if (isUserLogin) {
                // Move JSON parsing to a background thread
                new Thread(() -> {
                    LOGIN_DETAIL = new Gson().fromJson(
                            SharePreference.getInstance(SplashActivity.this).getString(LOGIN_PREFRENCE),
                            com.iotsmartaliv.apiAndSocket.models.ResponseData.class
                    );

                    // Switch back to main thread for UI updates
                    runOnUiThread(() -> {
                        if (LOGIN_DETAIL != null && LOGIN_DETAIL.getLoginStatus().equalsIgnoreCase("1")) {
                            openActivityMain();
                        } else {
                            Intent intent = new Intent(SplashActivity.this, NewPasswordActivity.class);
                            intent.putExtra("IS_FOR_NEW_PASSWORD", true);
                            startActivity(intent);
                            finish();
                        }
                    });
                }).start(); // Start the background thread
            } else if (!hasOnBoardingShown) {
                checkAndRequestPermissions();
                startActivity(onboardingPermission);
                finish();
            } else {
                openActivityLogin();
            }
        }, 2000);
    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }
}
