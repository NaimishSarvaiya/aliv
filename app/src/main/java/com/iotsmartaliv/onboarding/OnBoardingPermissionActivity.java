package com.iotsmartaliv.onboarding;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.iotsmartaliv.R;
import com.iotsmartaliv.activity.LoginActivity;
import com.iotsmartaliv.activity.MainActivity;
import com.iotsmartaliv.activity.SplashActivity;
import com.iotsmartaliv.adapter.AutoSlideViewPager;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.fragments.TermsOfFragment;
import com.ncorti.slidetoact.SlideToActView;
import com.rd.PageIndicatorView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class OnBoardingPermissionActivity extends AppCompatActivity {


    int page = 0;
    private LinearLayout linPage2, linPage1;
    private String[] permissions;
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1101;
    private ImageButton mBack;
    private AutoSlideViewPager adapter;
    private ViewPager pager;
    private ArrayList<Integer> dataItems;
    private Button btnStart;
    private SlideToActView slideToActView;
    private PageIndicatorView indicatorView;
    private TabLayout tabLayout;
    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 2000; // time in milliseconds between successive task executions.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_on_boarding_permission);
        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.light_blue));
        tabLayout = findViewById(R.id.tab_layout);
//        indicatorView = findViewById(R.id.indicator);
        slideToActView = findViewById(R.id.slide_button);
        btnStart = findViewById(R.id.btn_start);
        pager = findViewById(R.id.viewpager);
        linPage1 = findViewById(R.id.linPage1);
        linPage2 = findViewById(R.id.linPage2);




        permissions = getIntent().getStringArrayExtra(Constant.PERMISSION_ARRAY);
        mBack = findViewById(R.id.imgBack);
        mBack.setOnClickListener(v -> onBackPressed());
        prepareList();
        setAutoSlideAdapter();
       /* pager.setIndicatorPageChangeListener(new LoopingViewPager.IndicatorPageChangeListener() {
            @Override
            public void onIndicatorProgress(int selectingPosition, float progress) {
//                indicatorView.setProgress(selectingPosition, progress);
            }

            @Override
            public void onIndicatorPageChange(int newIndicatorPosition) {
                indicatorView.setSelection(newIndicatorPosition);
            }
        });*/
        slideToActView.setOnSlideToActAnimationEventListener(new SlideToActView.OnSlideToActAnimationEventListener() {
            @Override
            public void onSlideCompleteAnimationStarted(SlideToActView slideToActView, float v) {

            }

            @Override
            public void onSlideCompleteAnimationEnded(SlideToActView slideToActView) {


//                if (page == 1) {
                if (permissions != null && permissions.length > 0)
                    ActivityCompat.requestPermissions(OnBoardingPermissionActivity.this, permissions, REQUEST_ID_MULTIPLE_PERMISSIONS);
                else
                    moveToNxtScreen();
//                } else {
//                    changeView();
//                    page++;
//                }
            }

            @Override
            public void onSlideResetAnimationStarted(SlideToActView slideToActView) {

            }

            @Override
            public void onSlideResetAnimationEnded(SlideToActView slideToActView) {

            }
        });
    }

    private void prepareList() {
        dataItems = new ArrayList<>();
        dataItems.add(R.mipmap.iv_permission_one);
        dataItems.add(R.mipmap.iv_permission_second);
        dataItems.add(R.mipmap.iv_permission_third);
        dataItems.add(R.mipmap.iv_permission_fourth);
        dataItems.add(R.mipmap.iv_permission_fifth);
    }

    private void setAutoSlideAdapter() {
        adapter = new AutoSlideViewPager(this, dataItems, true);
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager, true);
        /*After setting the adapter use the timer */
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == 6 - 1) {
                    currentPage = 0;
                }
                pager.setCurrentItem(currentPage++, true);
            }
        };
        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);
    }


    public void onNextButtonClick(View view) {
        if (page == 1) {
            if (permissions != null && permissions.length > 0)
                ActivityCompat.requestPermissions(this, permissions, REQUEST_ID_MULTIPLE_PERMISSIONS);
            else
                moveToNxtScreen();
        } else {
            changeView();
            page++;
        }

    }

    @Override
    public void onBackPressed() {
        if (page == 1) {
            changeView();
            page--;
        } else super.onBackPressed();
    }

    /* */

    /**
     * This method is used for opening login activity.
     *//*
    private void openActivityLogin() {
        SharePreference.getInstance(OnBoardingPermissionActivity.this).putBoolean(Constant.HAS_ON_BOARDING_SHOWN, true);
        Intent intent = new Intent(OnBoardingPermissionActivity.this, LoginActivity.class);
        startActivity(intent);
        finishAffinity();
    }
*/
    void changeView() {
        if (page == 0) {
            linPage1.setVisibility(View.GONE);
            linPage2.setVisibility(View.VISIBLE);
        } else {
            linPage1.setVisibility(View.VISIBLE);
            linPage2.setVisibility(View.GONE);
        }
    }

    private /*boolean*/ void checkAndRequestPermissions() {
        int camerapermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int phonestate = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        int location = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int recordaudio = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        int permissionLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int bluetothpermission = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            bluetothpermission = ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT);
        }

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (camerapermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (phonestate != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (location != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (recordaudio != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECORD_AUDIO);
        }

        if (ContextCompat.checkSelfPermission(OnBoardingPermissionActivity.this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            {
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 2);
                listPermissionsNeeded.add(Manifest.permission.BLUETOOTH_CONNECT);
                Log.e("BL P", "TRUE");
            }
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (bluetothpermission != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.BLUETOOTH_CONNECT);
            }
        }

        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
            if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
        }


        if (!listPermissionsNeeded.isEmpty()) {
            /*ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;*/
            Intent onboardingPermission = new Intent(this, OnBoardingPermissionActivity.class);
            onboardingPermission.putExtra(Constant.PERMISSION_ARRAY, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]));
            startActivity(onboardingPermission);
        }
        /*return true;*/
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<>();

                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_PHONE_STATE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.RECORD_AUDIO, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    perms.put(Manifest.permission.BLUETOOTH_CONNECT, PackageManager.PERMISSION_GRANTED);
                }

                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED

                    ) {
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && perms.get(Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                            checkAndRequestPermissions();
                            Toast.makeText(this, "Please allow required permissions to continue", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) != null && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {


                            Log.d("SplashScreen", "Some permissions are not granted ask again ");
                            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                showDialogOK("Some Permissions are required for Open doors",
                                        (dialog, which) -> {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    checkAndRequestPermissions();
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    // proceed with logic by disabling the related features or quit the app.
                                                    dialog.dismiss();
                                                    finish();
                                                    break;
                                            }
                                        });
                                return;
                            }

                        }

                        Log.d("SplashScreen", "camera & location services permission granted");
                        // here you can do your logic all Permission Success Call
                        moveToNxtScreen();

                    }  else {
                        Log.d("SplashScreen", "Some permissions are not granted ask again ");
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            showDialogOK("Some Permissions are required for Open doors",
                                    (dialog, which) -> {
                                        switch (which) {
                                            case DialogInterface.BUTTON_POSITIVE:
                                                checkAndRequestPermissions();
                                                break;
                                            case DialogInterface.BUTTON_NEGATIVE:
                                                // proceed with logic by disabling the related features or quit the app.
                                                dialog.dismiss();
                                                finish();
                                                break;
                                        }
                                    });
                        } else {
                            explain("You need to give some mandatory permissions to continue. Do you want to go to app settings?");
                        }
                    }
                }
            }
        }

    }


    private void moveToNxtScreen() {
        /*boolean isUserLogin = SharePreference.getInstance(OnBoardingPermissionActivity.this).getBoolean(Constant.IS_LOGIN);
        boolean hasOnBoardingShown = SharePreference.getInstance(OnBoardingPermissionActivity.this).getBoolean(Constant.HAS_ON_BOARDING_SHOWN);
        if (isUserLogin) {
            LOGIN_DETAIL = new Gson().fromJson(SharePreference.getInstance(OnBoardingPermissionActivity.this).getString(LOGIN_PREFRENCE), com.iotsmart.apiCalling.models.ResponseData.class);
            if (LOGIN_DETAIL.getLoginStatus().equalsIgnoreCase("1")) {
                openActivityMain();
            } else {
                Intent intent = new Intent(OnBoardingPermissionActivity.this, NewPasswordActivity.class);
                intent.putExtra("IS_FOR_NEW_PASSWORD", true);
                startActivity(intent);
                finish();
            }
        } else {
            if (!hasOnBoardingShown) {*/
        Intent intent = new Intent(OnBoardingPermissionActivity.this, OnBoardingActivity.class);
        intent.putExtra(Constant.FROM_DRAWER, false);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
               /* finish();
            } else
                openActivityLogin();
        }*/
    }

    /**
     * This method is used for opening login activity.
     */
    private void openActivityLogin() {
        Intent intent = new Intent(OnBoardingPermissionActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * This method is used for opening main activity.
     */
    private void openActivityMain() {
        Intent intent = new Intent(OnBoardingPermissionActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        slideToActView.resetSlider();
    }

    private void explain(String msg) {
        SplashActivity.checkFromOnResume = false;
        final androidx.appcompat.app.AlertDialog.Builder dialog = new androidx.appcompat.app.AlertDialog.Builder(this);
        dialog.setMessage(msg)
                .setPositiveButton("Yes", (paramDialogInterface, paramInt) -> {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.parse("package:" + getPackageName()));
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    SplashActivity.checkFromOnResume = true;
                })
                .setNegativeButton("Cancel", (paramDialogInterface, paramInt) -> {
                    dialog.create().dismiss();
                    finish();
                });
        dialog.show();
    }

    public void onPrivacyPolicyClick(View view) {
        getSupportFragmentManager().beginTransaction().add(android.R.id.content, new TermsOfFragment()).addToBackStack("tag").commit();
    }
}