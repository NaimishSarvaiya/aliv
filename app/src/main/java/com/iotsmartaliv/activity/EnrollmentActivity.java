package com.iotsmartaliv.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.iotsmartaliv.R;
import com.iotsmartaliv.apiCalling.listeners.RetrofitListener;
import com.iotsmartaliv.apiCalling.models.ErrorObject;
import com.iotsmartaliv.apiCalling.models.SuccessResponse;
import com.iotsmartaliv.apiCalling.retrofit.ApiServiceProvider;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.utils.CommanUtils;
import com.iotsmartaliv.utils.SharePreference;
import com.iotsmartaliv.utils.Util;
import com.iotsmartaliv.utils.faceenroll.FaceCenterCrop;
import com.iotsmartaliv.utils.faceenroll.Imageutils;
import com.iotsmartaliv.utils.faceenroll.ProgressUtils;

import static com.iotsmartaliv.constants.Constant.LOGIN_DETAIL;
import static com.iotsmartaliv.constants.Constant.LOGIN_PREFRENCE;

/**
 * This activity class is used for Enrollment.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2018-10-23
 */
public class EnrollmentActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int ACCESS_CAMERA_PERMISSION = 1001, ACCESS_LOCATION_PERMISSION = 1002, ACCESS_EXTERNAL_STORAGE_PERMISSION = 1003;
    public static boolean isSetting = false;
    Imageutils.ImageAttachmentListener imageAttachmentListener;
    FaceCenterCrop.FaceCenterCropListener faceCenterCropListener;
    ProgressUtils progressUtils;
    private ImageView imgPlus, imgBackEnrollment;
    private TextView tvSubmit;
    private String path;
    RelativeLayout btn_lay;
    ApiServiceProvider apiServiceProvider;

    TextView tvSuggetion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enrollment);
        apiServiceProvider = ApiServiceProvider.getInstance(this);
        imgPlus = findViewById(R.id.plus_image);
        tvSubmit = findViewById(R.id.submit);
        tvSuggetion = findViewById(R.id.please_click_on_plus);
        btn_lay = findViewById(R.id.btn_lay);
        imgBackEnrollment = findViewById(R.id.img_back_enrollment);
        imgPlus.setOnClickListener(this);
        tvSubmit.setOnClickListener(this);
        imgBackEnrollment.setOnClickListener(this);
        progressUtils = new ProgressUtils(this);
        if (LOGIN_DETAIL.getFacialImage() != null && LOGIN_DETAIL.getFacialImage().length() > 0) {
            byte[] imageByteArray = Base64.decode(LOGIN_DETAIL.getFacialImage(), Base64.DEFAULT);
            Glide.with(EnrollmentActivity.this)
                    .load(imageByteArray)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .apply(RequestOptions.circleCropTransform())
                    .into(imgPlus);

            tvSuggetion.setText(getResources().getString(R.string.please_click_on_image));
        }
        else {
            tvSuggetion.setText(getResources().getString(R.string.please_click_on_plus));
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == Activity.RESULT_OK && !TextUtils.isEmpty(data.getStringExtra("image"))) {
                path = data.getStringExtra("image");
                Glide.with(EnrollmentActivity.this)
                        .load(path)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .apply(RequestOptions.circleCropTransform())
                        .into(imgPlus);
                btn_lay.setVisibility(View.VISIBLE);
            }
        } catch (Exception ex) {
            Toast.makeText(this, ex.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.plus_image:
                boolean isAllowed = checkAndRequestPermissions();
                if (isAllowed) {
                    Intent intentCameraActivity = new Intent(EnrollmentActivity.this, FaceEnrollCameraActivity.class);
                    startActivityForResult(intentCameraActivity, 101);
                } else {
                    checkAndRequestPermissions();
                }
                break;

            case R.id.submit:
                if (path != null && !path.equalsIgnoreCase("")) {
                    String base64String = CommanUtils.bitmapToBase64(BitmapFactory.decodeFile(path));
                    Util.checkInternet(EnrollmentActivity.this, new Util.NetworkCheckCallback() {
                        @Override
                        public void onNetworkCheckComplete(boolean isAvailable) {
                            if (isAvailable) {
                                apiServiceProvider.callAPIForUploadFaceEnrollImg(LOGIN_DETAIL.getAppuserID(), base64String, new RetrofitListener<SuccessResponse>() {
                                    @Override
                                    public void onResponseSuccess(SuccessResponse sucessRespnse, String apiFlag) {
                                        if (sucessRespnse.getStatus().equalsIgnoreCase("OK")) {
                                            Toast.makeText(EnrollmentActivity.this, sucessRespnse.getMsg(), Toast.LENGTH_SHORT).show();
                                            LOGIN_DETAIL.setFacialImage(base64String);
                                            SharePreference.getInstance(EnrollmentActivity.this).putString(LOGIN_PREFRENCE, new Gson().toJson(LOGIN_DETAIL));
                                            Intent intentCameraActivity = new Intent(EnrollmentActivity.this, EnrollmentSuccessful.class);
                                            intentCameraActivity.putExtra("path", path);
                                            startActivity(intentCameraActivity);
                                            btn_lay.setVisibility(View.GONE);


                                        } else {
                                            Toast.makeText(EnrollmentActivity.this, sucessRespnse.getMsg(), Toast.LENGTH_SHORT).show();
                                        }

                                    }

                                    @Override
                                    public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                                        if (throwable.getMessage()!=null) {
                                            Toast.makeText(EnrollmentActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                        Util.firebaseEvent(Constant.APIERROR, EnrollmentActivity.this, Constant.UrlPath.SERVER_URL + apiFlag, LOGIN_DETAIL.getUsername(), LOGIN_DETAIL.getAppuserID(), errorObject.getStatus());

                                    }
                                });
                            }
                        }
                    });


                } else {
                    Toast.makeText(this, "Please select picture", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.img_back_enrollment:
                finish();
                break;
        }
    }

 /*   @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 101:
                if (data != null) {
                    path = data.getStringExtra("RESULT_STRING");
                    SharePreference.getInstance(this).putString("image_path", path);
                    Bitmap bitmap = BitmapFactory.decodeFile(path);
                    imgPlus.setImageBitmap(bitmap);
                }
                break;
        }
    }*/

    /**
     * This method is used for checking runtime permission.
     */
    private boolean checkAndRequestPermissions() {
        try {
            if (!isAccessCameraAllowed()) {
                //Requesting camera permission
                requestAccessCameraPermission();
            } else if (!isWriteExternalAllowed()) {


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    return true;
                } else {
                    //Requesting external storage permission
                    requestWriteExternalPermission();
                }
            } else {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * This method is used for checking whether camera permission allow or not.
     */
    private boolean isAccessCameraAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(EnrollmentActivity.this, Manifest.permission.CAMERA);
        //If permission is granted returning true
        return result == PackageManager.PERMISSION_GRANTED;
        //If permission is not granted returning false
    }

    /**
     * This method is used for request camera permission.
     */
    private void requestAccessCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(EnrollmentActivity.this, Manifest.permission.CAMERA)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(EnrollmentActivity.this, new String[]{Manifest.permission.CAMERA}, ACCESS_CAMERA_PERMISSION);
    }

    /**
     * This method is used for checking the write external storage permission whether allow or not.
     */
    private boolean isWriteExternalAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(EnrollmentActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        //If permission is granted returning true
        return result == PackageManager.PERMISSION_GRANTED;
        //If permission is not granted returning false
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    /**
     * This method is used for request write external storage permission.
     */
    private void requestWriteExternalPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(EnrollmentActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(EnrollmentActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, ACCESS_EXTERNAL_STORAGE_PERMISSION);
    }

    /**
     * Handling result of the permission requested
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case ACCESS_CAMERA_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkAndRequestPermissions();
                } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(EnrollmentActivity.this, Manifest.permission.CAMERA)) {
                        checkAndRequestPermissions();
                    } else {
                        //Never ask again and handle your app without permission.
                        if (!isSetting) {
                            // openPopUpForPermission(getResources().getString(R.string.allow_camera_permission));
                            Toast.makeText(this, "Please allow camera permission form app setting", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
            case ACCESS_EXTERNAL_STORAGE_PERMISSION:
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        checkAndRequestPermissions();
                    } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(EnrollmentActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            checkAndRequestPermissions();
                        } else {
                            //Never ask again and handle your app without permission.

                            if (!isSetting) {
                                //openPopUpForPermission(getResources().getString(R.string.allow_storage_permission));
                                Toast.makeText(this, "Please allow storage permission form app setting", Toast.LENGTH_SHORT).show();

                            }
                        }
                    }
                }
                break;
        }
    }

    /* *//**
     * This method is used to show the dialog box of allow permission when user deny the permission.
     *
     * @param msg
     *//*
    private void openPopUpForPermission(String msg) {
        if (permissionsDialog != null) {
            if (permissionsDialog.isShowing()) {
                return;
            }
        }
        permissionsDialog = new Dialog(EnrollmentActivity.this);
        permissionsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        permissionsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        permissionsDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        permissionsDialog.getWindow().setGravity(Gravity.CENTER);
        permissionsDialog.setContentView(R.layout.dialog_permission);
        TextView alert_message = (TextView) permissionsDialog.findViewById(R.id.alert_message);
        alert_message.setText(msg);
        permissionsDialog.setCanceledOnTouchOutside(false);
        Button btnReturnOk = (Button) permissionsDialog.findViewById(R.id.btn_ok);
        btnReturnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permissionsDialog.dismiss();
                goToSettings();
                isSetting = true;
            }
        });
        permissionsDialog.show();
    }*/

    /**
     * This method is used for goto setting for allow the permission.
     */
    private void goToSettings() {
        Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + EnrollmentActivity.this.getPackageName()));
        myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
        myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        EnrollmentActivity.this.startActivity(myAppSettings);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkAndRequestPermissions();
        isSetting = false;
    }
}
