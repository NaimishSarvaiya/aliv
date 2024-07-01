package com.iotsmartaliv.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.iotsmartaliv.R;
import com.iotsmartaliv.fragments.CameraSelectorDialogFragment;
import com.iotsmartaliv.fragments.FormatSelectorDialogFragment;
import com.iotsmartaliv.fragments.MessageDialogFragment;

import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * The class is used as qr code scanner screen of the app
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2018-10-26
 */
public class QRCodeScannerActivity extends AppCompatActivity implements MessageDialogFragment.MessageDialogListener,
        ZXingScannerView.ResultHandler, FormatSelectorDialogFragment.FormatSelectorDialogListener,
        CameraSelectorDialogFragment.CameraSelectorDialogListener, View.OnClickListener {

    private static final String FLASH_STATE = "FLASH_STATE";
    private static final String AUTO_FOCUS_STATE = "AUTO_FOCUS_STATE";
    private static final String SELECTED_FORMATS = "SELECTED_FORMATS";
    private static final String CAMERA_ID = "CAMERA_ID";
    private static final int ACCESS_CAMERA_PERMISSION = 1001, ACCESS_LOCATION_PERMISSION = 1002, ACCESS_EXTERNAL_STORAGE_PERMISSION = 1003;
    public static ZXingScannerView mScannerView;
    public static boolean isSetting = false;
    private final String TAG = QRCodeScannerActivity.class.getSimpleName();
    private boolean mFlash, mAutoFocus;
    private ArrayList<Integer> mSelectedIndices;
    private int mCameraId = 1, isFrontCamera = 1, flag1 = 0;
    private ImageView ivFlipcamera, ivFlash;
    private ViewGroup contentFrame;

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        checkState(state);
        setContentView(R.layout.activity_qrcode_scanner);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
        }
        initView();
        mScannerView = new ZXingScannerView(this);
        setupFormats();
        contentFrame.addView(mScannerView);
        mScannerView.setResultHandler(this);
    }

    /**
     * This method is used to check activity state
     */
    public void checkState(Bundle state) {
        if (state != null) {
            mFlash = state.getBoolean(FLASH_STATE, false);
            mAutoFocus = state.getBoolean(AUTO_FOCUS_STATE, true);
            mSelectedIndices = state.getIntegerArrayList(SELECTED_FORMATS);
            mCameraId = state.getInt(CAMERA_ID, 1);
        } else {
            mFlash = false;
            mAutoFocus = true;
            mSelectedIndices = null;
            mCameraId = 1;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        checkAndRequestPermissions();
        isSetting = false;
        if (flag1 == 2 || flag1 == 1) {
            isFrontCamera = 1;
            mCameraId = 1;          //get front-facing
        } else if (flag1 == 0) {
            isFrontCamera = 0;      //back-facing
            mCameraId = 0;
        }
        mScannerView.startCamera(mCameraId);
        mScannerView.setAutoFocus(mAutoFocus);

        try {
            mScannerView.setFlash(mFlash);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(FLASH_STATE, mFlash);
        outState.putBoolean(AUTO_FOCUS_STATE, mAutoFocus);
        outState.putIntegerArrayList(SELECTED_FORMATS, mSelectedIndices);
        outState.putInt(CAMERA_ID, mCameraId);
    }

    /**
     * This method is used to initialize the views
     */
    private void initView() {
        ivFlipcamera = findViewById(R.id.iv_flip_camera);
        ivFlash = findViewById(R.id.iv_flash);
        contentFrame = findViewById(R.id.content_frame);
        ivFlipcamera.setOnClickListener(this);
        ivFlash.setOnClickListener(this);
    }

    @Override
    public void handleResult(Result rawResult) {
        String s = rawResult.getText();
        Log.d("Scanner_Result", s);

        if (s.equalsIgnoreCase("")) {
            Toast.makeText(this, "No data available in this QR code", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "QR code scanned successfully ", Toast.LENGTH_SHORT).show();

            //Toast.makeText(this, "Data available : " + s, Toast.LENGTH_SHORT).show();
            finish();

        }
    }

    /**
     * This method is used to close message dialog
     */
    public void closeMessageDialog() {
        closeDialog("scan_results");
    }

    /**
     * This method is used to close format dialog
     */
    public void closeFormatsDialog() {
        closeDialog("format_selector");
    }

    /**
     * This method is used to close dialog
     */
    public void closeDialog(String dialogName) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        DialogFragment fragment = (DialogFragment) fragmentManager.findFragmentByTag(dialogName);
        if (fragment != null) {
            fragment.dismiss();
        }
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        // Resume the camera
        mScannerView.resumeCameraPreview(this);
    }

    @Override
    public void onFormatsSaved(ArrayList<Integer> selectedIndices) {
        mSelectedIndices = selectedIndices;
        setupFormats();
    }

    @Override
    public void onCameraSelected(int cameraId) {
        mCameraId = cameraId;
        mScannerView.startCamera(mCameraId);
        mScannerView.setFlash(mFlash);
        mScannerView.setAutoFocus(mAutoFocus);
    }

    /**
     * This method is used as setup format
     */
    public void setupFormats() {
        List<BarcodeFormat> formats = new ArrayList<BarcodeFormat>();
        if (mSelectedIndices == null || mSelectedIndices.isEmpty()) {
            mSelectedIndices = new ArrayList<Integer>();
            for (int i = 0; i < ZXingScannerView.ALL_FORMATS.size(); i++) {
                mSelectedIndices.add(i);
            }
        }

        for (int index : mSelectedIndices) {
            formats.add(ZXingScannerView.ALL_FORMATS.get(index));
        }
        if (mScannerView != null) {
            mScannerView.setFormats(formats);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
        closeMessageDialog();
        closeFormatsDialog();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_flip_camera:
                flipCamera();
                break;
            case R.id.iv_flash:
                mFlash = !mFlash;
                mScannerView.setFlash(mFlash);
                break;
        }
    }

    /**
     * This method is used as flip camera
     */
    private void flipCamera() {
        mScannerView.setResultHandler(this);
        int numberOfCameras = Camera.getNumberOfCameras();
        final String[] cameraNames = new String[numberOfCameras];
        int checkedIndex = 0;
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                cameraNames[i] = "Front Facing";
            } else if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                cameraNames[i] = "Rear Facing";
            } else {
                cameraNames[i] = "Camera ID: " + i;
            }
        }
        switchCamera();
    }

    /**
     * This method is used to switch the camera front/rear.
     */
    public void switchCamera() {
        int camNum = 0;
        camNum = Camera.getNumberOfCameras();
        if (mScannerView != null) {
            if (camNum > 1) {
                mScannerView.stopCamera();
                if (isFrontCamera == 0) {
                    isFrontCamera = 1;
                    mScannerView.startCamera(1);
                    mScannerView.setFlash(mFlash);
                    mScannerView.setAutoFocus(mAutoFocus);
                } else {
                    mScannerView.startCamera(0);
                    mScannerView.setFlash(mFlash);
                    mScannerView.setAutoFocus(mAutoFocus);
                    isFrontCamera = 0;
                }
            }
        }
    }

    /**
     * This method is used for checking runtime permission.
     */
    private boolean checkAndRequestPermissions() {
        try {
            if (!isAccessCameraAllowed()) {
                //Requesting camera permission
                requestAccessCameraPermission();
            } else if (!isWriteExternalAllowed()) {
                //Requesting external storage permission
                requestWriteExternalPermission();
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
        int result = ContextCompat.checkSelfPermission(QRCodeScannerActivity.this, Manifest.permission.CAMERA);
        //If permission is granted returning true
        return result == PackageManager.PERMISSION_GRANTED;
        //If permission is not granted returning false
    }

    /**
     * This method is used for request camera permission.
     */
    private void requestAccessCameraPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(QRCodeScannerActivity.this, Manifest.permission.CAMERA)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(QRCodeScannerActivity.this, new String[]{Manifest.permission.CAMERA}, ACCESS_CAMERA_PERMISSION);
    }

    /**
     * This method is used for checking the write external storage permission whether allow or not.
     */
    private boolean isWriteExternalAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(QRCodeScannerActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        //If permission is granted returning true
        return result == PackageManager.PERMISSION_GRANTED;
        //If permission is not granted returning false
    }

    /**
     * This method is used for request write external storage permission.
     */
    private void requestWriteExternalPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(QRCodeScannerActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(QRCodeScannerActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, ACCESS_EXTERNAL_STORAGE_PERMISSION);
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
                    if (ActivityCompat.shouldShowRequestPermissionRationale(QRCodeScannerActivity.this, Manifest.permission.CAMERA)) {
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
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkAndRequestPermissions();
                } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(QRCodeScannerActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        checkAndRequestPermissions();
                    } else {
                        //Never ask again and handle your app without permission.
                        if (!isSetting) {
                            //openPopUpForPermission(getResources().getString(R.string.allow_storage_permission));
                            Toast.makeText(this, "Please allow storage permission form app setting", Toast.LENGTH_SHORT).show();

                        }
                    }
                }
                break;
        }
    }

    /**
     * This method is used for goto setting for allow the permission.
     */
    private void goToSettings() {
        Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + QRCodeScannerActivity.this.getPackageName()));
        myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
        myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        QRCodeScannerActivity.this.startActivity(myAppSettings);
    }

}