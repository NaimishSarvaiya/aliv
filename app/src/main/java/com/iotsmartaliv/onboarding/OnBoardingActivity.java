package com.iotsmartaliv.onboarding;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.iotsmartaliv.R;
import com.iotsmartaliv.activity.LoginActivity;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.utils.SharePreference;
import com.ncorti.slidetoact.SlideToActView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static com.iotsmartaliv.activity.MainActivity.POWERMANAGER_INTENTS;

public class OnBoardingActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView tvSkip;
    private ImageView imgNext;
    private OnBoardingPagerAdapter adapter;
    private String manufacturer = "";
    private Button btnNext;
    private SlideToActView slideToActView;
    private TextView tvTapHere;
    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 5000; // time in milliseconds between successive task executions.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);
        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.light_blue));
        manufacturer = Build.MANUFACTURER;
        initView();
        btnNext.getBackground().setColorFilter(getResources().getColor(R.color.orange_text_heading), PorterDuff.Mode.SRC_IN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            viewPager.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    switch (viewPager.getCurrentItem()) {

                        case 0: {
                            btnNext.getBackground().setColorFilter(getResources().getColor(R.color.orange_text_heading), PorterDuff.Mode.SRC_IN);
                            btnNext.setText(getResources().getString(R.string.next));
                            tvTapHere.setVisibility(View.VISIBLE);
                            break;
                        }
                        case 1: {
                            btnNext.getBackground().setColorFilter(getResources().getColor(R.color.orange_text_heading), PorterDuff.Mode.SRC_IN);
                            btnNext.setText(getResources().getString(R.string.next));
                            tvTapHere.setVisibility(View.VISIBLE);
                            break;
                        }
                        case 2: {
                            btnNext.getBackground().setColorFilter(getResources().getColor(R.color.orange_text_heading), PorterDuff.Mode.SRC_IN);
                            btnNext.setText(getResources().getString(R.string.next));
                            tvTapHere.setVisibility(View.VISIBLE);
                            break;
                        }
                        case 3: {
                            btnNext.getBackground().setColorFilter(getResources().getColor(R.color.getstarted_button), PorterDuff.Mode.SRC_IN);
                            btnNext.setText(getResources().getString(R.string.lets_get_started));
                            tvTapHere.setVisibility(View.GONE);
                        }
                    }
                }
            });
        }
        tvTapHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (viewPager.getCurrentItem()) {
                    case 0: {
                        if (!isBluetoothEnabled()) {
                            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED)
                            {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                                {
                                    ActivityCompat.requestPermissions(OnBoardingActivity.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 2);
                                    return;
                                }
                            }

                            Intent eintent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                            startActivityForResult(eintent, 1);
                        } else
                            Toast.makeText(OnBoardingActivity.this, "Already Enabled", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case 1: {
                        if (!isGPSEnabled()) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivityForResult(intent, 2);
                        } else
                            Toast.makeText(OnBoardingActivity.this, "Already Enabled", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case 2: {
                        if (!isBackgroundModeEnabled())
                            showSettingDialog();
                        else
                            Toast.makeText(OnBoardingActivity.this, "Already Enabled", Toast.LENGTH_SHORT).show();
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
                        break;
                    }
                    case 3: {
                        openActivityLogin();
                    }
                }
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (viewPager.getCurrentItem()) {
                    case 0: {
                        if (!isBluetoothEnabled())
                            Toast.makeText(OnBoardingActivity.this, "Enable bluetooth", Toast.LENGTH_SHORT).show();
                        else
                            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
                        tvTapHere.setVisibility(View.VISIBLE);
                        break;
                    }
                    case 1: {
                        if (!isGPSEnabled())
                            Toast.makeText(OnBoardingActivity.this, "Enable GPS", Toast.LENGTH_SHORT).show();
                        else viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
                        tvTapHere.setVisibility(View.VISIBLE);
                        break;
                    }
                    case 2: {
                        if (!isBackgroundModeEnabled())
                            showSettingDialog();
                        else
                            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
                        tvTapHere.setVisibility(View.GONE);
                        break;
                    }
                    case 3: {
                        openActivityLogin();
                    }
                }
                currentPage = viewPager.getCurrentItem();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
                break;
            case 2:
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
                break;
            case 3:
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
                break;
        }
    }

    private void initView() {
        tvTapHere = findViewById(R.id.tv_tap_here);
        btnNext = findViewById(R.id.btn_start);
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tab_layout);
        /* tvSkip = findViewById(R.id.tvSkip);*/
        /*imgNext = findViewById(R.id.imgNext);*/
        ArrayList<Fragment> pages = new ArrayList<>();
        pages.add(getPage(R.string.onboarding_page2_title, R.string.onboarding_page2_desc, R.mipmap.iv_bluetooth_permission));
        pages.add(getPage(R.string.onboarding_page3_title, R.string.onboarding_page3_desc, R.mipmap.iv_location));
        pages.add(getPage(R.string.onboarding_page1_title, R.string.onboarding_page1_desc, R.mipmap.iv_alert_setting));
        pages.add(new LastPageFragment());
        adapter = new OnBoardingPagerAdapter(getSupportFragmentManager(), pages);
        viewPager.setAdapter(adapter);
//        viewPager.beginFakeDrag();
        tabLayout.setupWithViewPager(viewPager, true);
        /*After setting the adapter use the timer */
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == 4) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
                if (viewPager.getCurrentItem() == 3) {
                    tvTapHere.setVisibility(View.GONE);
                } else {
                    tvTapHere.setVisibility(View.VISIBLE);
                }
            }
        };
        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);
        LinearLayout tabStrip = ((LinearLayout) tabLayout.getChildAt(0));
        for (int i = 0; i < tabStrip.getChildCount(); i++) {
            tabStrip.getChildAt(i).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
        }
       /* tvSkip.setOnClickListener(onClickListener);
        imgNext.setOnClickListener(onClickListener);*/
    }

    private Page1To3Fragment getPage(int title, int desc, int imgSrc) {
        Page1To3Fragment page = new Page1To3Fragment();
        Bundle pageData = new Bundle();

        pageData.putString(Constant.ONBOARDING_PAGE_TITLE, getString(title));
        pageData.putString(Constant.ONBOARDING_PAGE_DESC, getString(desc));
        pageData.putInt(Constant.ONBOARDING_PAGE_IMG, imgSrc);

        page.setArguments(pageData);

        return page;
    }

   /* private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tvSkip: {
                    if (viewPager.getCurrentItem() == 3)
                        openActivityLogin();
                    else
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
                    break;
                }
                case R.id.imgNext: {
                    switch (viewPager.getCurrentItem()) {
                        case 0: {
                            if (!isBluetoothEnabled())
                                Toast.makeText(OnBoardingActivity.this, "Enable bluetooth or skip for now", Toast.LENGTH_SHORT).show();
                            else
                                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
                            break;
                        }
                        case 1: {
                            if (!isGPSEnabled())
                                Toast.makeText(OnBoardingActivity.this, "Enable GPS or skip for now", Toast.LENGTH_SHORT).show();
                            else viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
                            break;
                        }
                        case 2: {
                            if (!isBackgroundModeEnabled())
                                showSettingDialog();
                            else
                                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
                            break;
                        }
                        case 3: {
                            openActivityLogin();
                            break;
                        }
                    }
                }
            }
        }
    };*/

    private void showSettingDialog() {
      /*  AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(R.string.alert);
        alert.setMessage(R.string.app_need_to_enable_background_mode);
        alert.setPositiveButton("OPEN SETTING", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dlg, int sumthin) {
                startActivity(new Intent(OnBoardingActivity.this, SettingActivity.class));
            }
        });
        alert.show();*/

        boolean isPowerSettingShown = false;

        for (int i = 0; i < POWERMANAGER_INTENTS.length; i++/*Intent intent : POWERMANAGER_INTENTS*/) {

            int j = i;
//            if (!manufacturer.equalsIgnoreCase("Realme")) {
            if (getPackageManager().resolveActivity(POWERMANAGER_INTENTS[i], PackageManager.MATCH_DEFAULT_ONLY) != null) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(OnBoardingActivity.this);
                alertDialogBuilder.setTitle("Alert!");
                alertDialogBuilder.setMessage("App need to enable background mode. If app is disable in background mode, some functionalities will not be working. Go on settings and enable background mode.")
                        .setCancelable(false)
                        .setPositiveButton("Open Setting", (dialog, id) -> {
                            try {
                                startActivity(POWERMANAGER_INTENTS[j]);
                                SharePreference.getInstance(OnBoardingActivity.this).putBoolean(Constant.SKIP_PROTECTIONAPPCHECK, true);
                            } catch (Exception e) {
//                                    Toast.makeText(this, "Exception:- " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.setCancelable(false);
                alertDialog.show();
                isPowerSettingShown = true;
            } else if (!isPowerSettingShown && POWERMANAGER_INTENTS.length - 1 == i) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
            }
            /*} else {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(OnBoardingActivity.this);
                alertDialogBuilder.setTitle("Alert!");
                alertDialogBuilder.setMessage("App need to enable background mode. if app is disable background mode some functionalities will not be work. Please click on Enable.")
                        .setCancelable(false)
                        .setPositiveButton("Enable", (dialog, id) -> {
                            try {
                                SharePreference.getInstance(OnBoardingActivity.this).putBoolean(Constant.SKIP_PROTECTIONAPPCHECK, true);
                                startActivity(POWERMANAGER_INTENTS[j]);
                            } catch (Exception e) {
//                                            Toast.makeText(this, "Exception:- " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.setCancelable(false);
                alertDialog.show();
                break;
            }
*/
        }
    }

    public boolean isGPSEnabled() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public boolean isBluetoothEnabled() {
        return BluetoothAdapter.getDefaultAdapter().isEnabled();
    }

    public boolean isBackgroundModeEnabled() {
        return SharePreference.getInstance(this).getBoolean(Constant.SKIP_PROTECTIONAPPCHECK);
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() > 0) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1, true);
            currentPage = viewPager.getCurrentItem();
        } else super.onBackPressed();
    }

    /**
     * This method is used for opening login activity.
     */
    private void openActivityLogin() {
//        SharePreference.getInstance(OnBoardingActivity.this).putBoolean(Constant.HAS_ON_BOARDING_SHOWN, true);
        if (getIntent().getBooleanExtra(Constant.FROM_DRAWER, false)) {
            finish();
        } else {
            Intent intent = new Intent(OnBoardingActivity.this, LoginActivity.class);
            intent.putExtra("showAlert", true);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finishAffinity();
        }
    }
}
