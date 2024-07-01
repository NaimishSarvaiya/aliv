package com.iotsmartaliv.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.TextureView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.iotsmartaliv.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This activity class is used for open Camera.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2018-10-23
 */
public class CameraActivity extends AppCompatActivity implements TextureView.SurfaceTextureListener {

    private ImageView btnCapture;
    private Camera mCamera;
    private TextureView mTextureView;
    private int finalRotation = 0;

    /**
     * This method is used for rotate bitmap.
     *
     * @param source
     * @param angle
     * @return
     */
    public static Bitmap RotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_camera);
        btnCapture = findViewById(R.id.bt_click);
        mTextureView = findViewById(R.id.texture);
        mTextureView.setSurfaceTextureListener(this);
        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    if (ContextCompat.checkSelfPermission(CameraActivity.this,
                            Manifest.permission.CAMERA)
                            == PackageManager.PERMISSION_GRANTED) {
                        takePicture(v);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        int currentApiVersion = Build.VERSION.SDK_INT;
        if (currentApiVersion >= Build.VERSION_CODES.LOLLIPOP) {
            getPermission();
        } else {
            //crashAnalytics();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPermission();
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        try {
            Log.d("wh", width + "," + height);
            int cameraId = 0;
            Camera.CameraInfo info = new Camera.CameraInfo();
            for (cameraId = 0; cameraId < Camera.getNumberOfCameras(); cameraId++) {
                Camera.getCameraInfo(1, info);
                if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    finalRotation = info.orientation;
                } else {
                    Toast.makeText(getApplicationContext(), "Your device does not have front Camera.", Toast.LENGTH_LONG).show();
                }
            }
            Display display = getWindowManager().getDefaultDisplay();
            Point size1 = new Point();
            display.getSize(size1);
            int width1 = size1.x;
            int height1 = size1.y;

            mCamera = Camera.open(getFrontCameraId());
            //  mCamera.getParameters().setPreviewSize(1000, 1000);
            mCamera.setDisplayOrientation(90);
            mTextureView.setLayoutParams(new FrameLayout.LayoutParams(
                    900, 1200, Gravity.CENTER_HORIZONTAL));
//                transform.setScale(-1, 1, previewSize.height / 2, 0);

            try {
                mCamera.setPreviewTexture(surface);
            } catch (IOException t) {
            }

            Camera.Parameters parameters = mCamera.getParameters();
            List<Camera.Size> allSizes = parameters.getSupportedPictureSizes();
            Camera.Size size = allSizes.get(0); // get top size
            for (int i = 0; i < allSizes.size(); i++) {
                if (allSizes.get(i).width > size.width)
                    size = allSizes.get(i);
            }
            //parameters.setRotation(270); //set rotation to save the picture

            //mCamera.setDisplayOrientation(result); //set the rotation for preview camera

            mCamera.setParameters(parameters);
            mCamera.startPreview();
            //mCamera.setDisplayOrientation(0);
            mTextureView.setRotation(0.0f);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {

        try {
            mCamera.stopPreview();
            mCamera.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    /**
     * This method is used for get the front camera id.
     *
     * @return
     */
    private int getFrontCameraId() {
        int camId = -1;
        int numberOfCameras = Camera.getNumberOfCameras();
        Camera.CameraInfo ci = new Camera.CameraInfo();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.getCameraInfo(i, ci);
            if (ci.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                camId = i;
            }
        }
        return camId;
    }

    /**
     * This method is used for take picture.
     *
     * @param view
     */
    public void takePicture(View view) {

        mCamera.takePicture(new Camera.ShutterCallback() {

            @Override
            public void onShutter() {

            }

        }, new Camera.PictureCallback() {

            @Override
            public void onPictureTaken(byte[] data, Camera camera) {

            }

        }, new Camera.PictureCallback() {

            @Override
            public void onPictureTaken(byte[] data, Camera camera) {

                File file1 = new File(Environment.getExternalStorageDirectory(), "attendance");
                if (!file1.exists()) {
                    file1.mkdir();
                }

                File photo = new File(file1, ((int) (Math.random() * 56456466)) + "photo.jpg");//+  +
                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inSampleSize = 2;
                Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length, opts);
                Bitmap bm1 = RotateBitmap(bmp, -90);
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(photo);
                    bm1.compress(Bitmap.CompressFormat.JPEG, 80, fos);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Uri contentUri = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    final Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    contentUri = Uri.fromFile(photo);
                    scanIntent.setData(contentUri);
                    sendBroadcast(scanIntent);
                } else {
                    final Intent intent = new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory()));
                    sendBroadcast(intent);
                }

                String strPath = photo.getPath();
                Intent intent = new Intent();
                intent.putExtra("RESULT_STRING", strPath);
                assert contentUri != null;
                intent.putExtra("RESULT_STRING_BASE64", contentUri.toString());
                setResult(RESULT_OK, intent);
                finish();
               /* Intent intent = new Intent(CameraActivity.this,EnrollmentSuccessful.class);
                intent.putExtra("RESULT_STRING",strPath);
                startActivity(intent);*/
            }
        });
    }

    /*
    Checking and enabling run time permissions
     */
    private void getPermission() {
        int cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int storageWritePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int storageReadPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (storageWritePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (storageReadPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 10);
        }
    }

    /**
     * Handling result of the permission requested
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        /* Make sure it's our original READ_CONTACTS request*/
        if (requestCode == 10) {
            if (grantResults.length == 7) {

            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}



