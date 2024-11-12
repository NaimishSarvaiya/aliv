package com.iotsmartaliv.activity;

import static com.iotsmartaliv.constants.Constant.API_AUTH;
import static com.iotsmartaliv.constants.Constant.LOGIN_DETAIL;
import static com.iotsmartaliv.constants.Constant.STRIPE_CUSTOMER_ID;
import static com.iotsmartaliv.constants.Constant.VO_IP;
import static com.iotsmartaliv.constants.Constant.VO_PORT;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.doormaster.vphone.inter.DMVPhoneModel;
import com.google.firebase.messaging.FirebaseMessaging;
import com.iotsmartaliv.R;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.databinding.ActivitySettingBinding;
import com.iotsmartaliv.model.booking.PaymentMethodModel;
import com.iotsmartaliv.services.ShakeOpenService;
import com.iotsmartaliv.utils.SharePreference;

/**
 * This class is usages as activity fot app setting.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2018-10-23
 */
public class SettingActivity extends AppCompatActivity {

    ActivitySettingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.llShake.setOnClickListener(v -> {
            Intent shake_intent = new Intent(SettingActivity.this, ShakeActivity.class);
            startActivity(shake_intent);
        });

        binding.llAboutUs.setOnClickListener(v -> {

        });

        binding.llPrivacyPolicy.setOnClickListener(v -> {
            Intent privacyPolicyIntent = new Intent(this, PrivacyPolicyActivity.class);
            startActivity(privacyPolicyIntent);
        });

        binding.llLogout.setOnClickListener(v -> {
            logOutDialog();
        });
    }

    public void goBack(View view) {
        onBackPressed();
    }



    private void logOutDialog() {
        // Create a new dialog
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.confirmation_dialog_for_booking);

        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        // Get the screen width
        DisplayMetrics metrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenWidth = metrics.widthPixels;

        // Set dialog width to 80% of screen width
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = (int) (screenWidth * 0.9);
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(params);

        // Find the buttons from the custom layout
        TextView btnNotNow = dialog.findViewById(R.id.btn_not_now);
        TextView btnConfirm = dialog.findViewById(R.id.btn_confirm);
        TextView title = dialog.findViewById(R.id.title);
        TextView details = dialog.findViewById(R.id.message);
        ImageView icon = dialog.findViewById(R.id.img_icon);
        icon.setVisibility(View.VISIBLE);

        title.setText("Logout!");
        details.setText("Are you sure? \n Dou you want to logout");
        // Set click listeners for the buttons
        btnNotNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss(); // Dismiss the dialog when "Not Now" is clicked
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
                logout();
                // Handle the confirmation action here
                // Dismiss the dialog after confirming
            }
        });

        // Show the dialog
        dialog.show();
    }

    void  logout(){
        LOGIN_DETAIL = null;
        DMVPhoneModel.exit();
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    FirebaseMessaging.getInstance().deleteToken();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("FCMTOKEN", "doInBackground: " + e.getLocalizedMessage());
                }
                return null;
            }
            @Override
            protected void onPostExecute(Void result) {
                Log.d("FCMTOKEN", "doInBackground: Done");
            }
        }.execute();
        stopService(new Intent(SettingActivity.this, ShakeOpenService.class));
        SharePreference.getInstance(SettingActivity.this).delete(API_AUTH);
        SharePreference.getInstance(SettingActivity.this).delete(STRIPE_CUSTOMER_ID);
        SharePreference.getInstance(SettingActivity.this).delete(VO_IP);
        SharePreference.getInstance(SettingActivity.this).delete(VO_PORT);
        SharePreference.getInstance(SettingActivity.this).clearPref();
        SharePreference.getInstance(SettingActivity.this).putBoolean(Constant.HAS_ON_BOARDING_SHOWN, true);
        // startActivity(new Intent(getContext(), SplashActivity.class));

        // login in issue-----------
        Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
        //--------------------
    }
}
