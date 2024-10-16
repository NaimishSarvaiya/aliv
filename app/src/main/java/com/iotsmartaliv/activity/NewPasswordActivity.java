package com.iotsmartaliv.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Toast;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeSuccessDialog;
import com.google.gson.Gson;
import com.iotsmartaliv.R;
import com.iotsmartaliv.apiAndSocket.listeners.RetrofitListener;
import com.iotsmartaliv.apiAndSocket.models.ErrorObject;
import com.iotsmartaliv.apiAndSocket.models.SuccessResponse;
import com.iotsmartaliv.apiAndSocket.retrofit.ApiServiceProvider;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.databinding.ActivityNewPasswordBinding;
import com.iotsmartaliv.utils.SharePreference;
import com.iotsmartaliv.utils.Util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


import static com.iotsmartaliv.constants.Constant.LOGIN_DETAIL;
import static com.iotsmartaliv.constants.Constant.LOGIN_PREFRENCE;
import static com.iotsmartaliv.constants.Constant.isValidPassword;

/**
 * This class is usages as activity class.
 * This activity will be change the password of user,when user login with temporary password this activity will be show for change new password.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2018-10-23
 */
public class NewPasswordActivity extends AppCompatActivity implements RetrofitListener<SuccessResponse> {

    ApiServiceProvider apiServiceProvider;
    boolean isFromAccount;

    ActivityNewPasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        apiServiceProvider = ApiServiceProvider.getInstance(this);
        isFromAccount = getIntent().getBooleanExtra("isFromAccount", false);
        binding.tvSignUp.setOnClickListener( v->onViewClicked() );
    }

    public void backPress(View view) {
        onBackPressed();
    }

    public void onViewClicked() {
        String strPassword =  binding.edtPassword.getText().toString().trim();
        String strConfirmPassword =  binding.edtConfirmPassword.getText().toString().trim();
        if (strPassword.equalsIgnoreCase("")) {
            binding.edtPassword.setError(getString(R.string.empty_password));
            binding.edtPassword.requestFocus();
            // Toast.makeText(this, R.string.empty_password, Toast.LENGTH_SHORT).show();
            return;
        }
        if (strPassword.length() < 8) {
            binding.edtPassword.setError(getString(R.string.valid_password));
            binding.edtPassword.requestFocus();
            //  Toast.makeText(this, R.string.valid_password, Toast.LENGTH_SHORT).show();
            return;
        } else if (!isValidPassword(strPassword)) {
            //  Toast.makeText(this, R.string.valid_password, Toast.LENGTH_SHORT).show();
            binding.edtPassword.setError(getResources().getString(R.string.error_msg));
            binding.edtPassword.requestFocus();
            return;
        }
        if (strConfirmPassword.equalsIgnoreCase("")) {
            binding.edtConfirmPassword.setError(getString(R.string.empty_confirm_password));
            binding.edtConfirmPassword.requestFocus();

            //   Toast.makeText(this, R.string.empty_confirm_password, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!strPassword.equalsIgnoreCase(strConfirmPassword)) {
            binding.edtConfirmPassword.setError(getString(R.string.password_and_confirm_password_doesnt_match));
            binding.edtConfirmPassword.requestFocus();
            // Toast.makeText(this, R.string.password_and_confirm_password_doesnt_match, Toast.LENGTH_SHORT).show();
            return;
        }
            Util.checkInternet(this, new Util.NetworkCheckCallback() {
                @Override
                public void onNetworkCheckComplete(boolean isAvailable) {
                    if (isAvailable){
                        apiServiceProvider.callForChangePassword(LOGIN_DETAIL.getAppuserID(), binding.edtPassword.getText().toString().trim(), binding.edtConfirmPassword.getText().toString().trim(), NewPasswordActivity.this);

                    }
                }
            });

    }


    @Override
    public void onResponseSuccess(SuccessResponse sucessRespnse, String apiFlag) {
        switch (apiFlag) {
            case Constant.UrlPath.CHANGE_PASSWORD_API:
                if (sucessRespnse.getStatus().equalsIgnoreCase("OK")) {
                    // showLoader(this);
                    MessageDigest md = null;
                    try {
                        md = MessageDigest.getInstance("SHA-256");
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                    byte[] hashInBytes = new byte[0];
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                        hashInBytes = md.digest(binding.edtConfirmPassword.getText().toString().getBytes(StandardCharsets.UTF_8));
                    }
                    // bytes to hex
                    StringBuilder sb = new StringBuilder();
                    for (byte b : hashInBytes) {
                        sb.append(String.format("%02x", b));
                    }
                    Constant.LOGIN_DETAIL.setPassword(sb.toString());
                    Constant.LOGIN_DETAIL.setLoginStatus("1");
                    SharePreference.getInstance(this).putString(LOGIN_PREFRENCE, new Gson().toJson(Constant.LOGIN_DETAIL));

                    new AwesomeSuccessDialog(this)
                            .setTitle("Successfully")
                            .setMessage("Password has been changed successfully.")
                            .setColoredCircle(R.color.colorPrimary)
                            .setDialogIconAndColor(R.drawable.ic_success, R.color.Orange)
                            .setCancelable(false)
                            .setDoneButtonClick(() -> {
                                if (!isFromAccount) {
                                    Intent intent1 = new Intent(NewPasswordActivity.this, MainActivity.class);
                                    startActivity(intent1);
                                    finishAffinity();
                                } else {
                                    finish();
                                }
                            })
                            .setDoneButtonText(getString(R.string.ok))
                            .setDoneButtonbackgroundColor(R.color.Orange)
                            .setDoneButtonTextColor(R.color.white)
                            .show();

                } else {
                    Toast.makeText(this, sucessRespnse.getMsg(), Toast.LENGTH_LONG).show();
                }
                break;

         /*   case Constant.UrlPath.LOGIN_API:
                if (sucessRespnse.getStatus().equalsIgnoreCase("OK")) {
                    SharePreference.getInstance(this).putString(LOGIN_PREFRENCE, new Gson().toJson(sucessRespnse.getData()));
                    Constant.LOGIN_DETAIL = sucessRespnse.getData();
                    SharePreference.getInstance(NewPasswordActivity.this).putBoolean(Constant.IS_LOGIN, true);
                    Intent service = new Intent(NewPasswordActivity.this, DeviceLogSyncService.class);
                    startService(service);
                    new AwesomeSuccessDialog(this)
                            .setTitle("Successfully")
                            .setMessage("Password has been changed successfully.")
                            .setColoredCircle(R.color.colorPrimary)
                            .setDialogIconAndColor(R.drawable.ic_success, R.color.Orange)
                            .setCancelable(false)
                            .setDoneButtonClick(() -> {
                                if (!isFromAccount) {
                                    Intent intent1 = new Intent(NewPasswordActivity.this, MainActivity.class);
                                    startActivity(intent1);
                                    finishAffinity();
                                } else {
                                    finish();
                                }
                            })
                            .setDoneButtonText(getString(R.string.ok))
                            .setDoneButtonbackgroundColor(R.color.Orange)
                            .setDoneButtonTextColor(R.color.white)
                            .show();
                } else {
                    Toast.makeText(this, sucessRespnse.getMsg(), Toast.LENGTH_LONG).show();
                }
                break;*/
        }
    }

    @Override
    public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
        switch (apiFlag) {

           /* case Constant.UrlPath.LOGIN_API:
                showLoader(this);
                FirebaseInstanceId.getInstance().getInstanceId()
                        .addOnCompleteListener(task -> {
                            if (!task.isSuccessful()) {
                                Toast.makeText(NewPasswordActivity.this, "Try Again:" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                return;
                            }
                            hideLoader();
                           // apiServiceProvider.perfromLogin(LOGIN_DETAIL.getUsername(), edtPassword.getText().toString(), "", "", task.getResult().getToken(), this);
                        });

                try {
                    Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
                }
                break;*/
            case Constant.UrlPath.CHANGE_PASSWORD_API:
                Util.firebaseEvent(Constant.APIERROR, NewPasswordActivity.this,Constant.UrlPath.SERVER_URL+apiFlag, LOGIN_DETAIL.getUsername(),LOGIN_DETAIL.getAppuserID(),errorObject.getStatus());

                try {
                    Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
