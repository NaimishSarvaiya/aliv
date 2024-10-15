package com.iotsmartaliv.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceLandmark;
import com.iotsmartaliv.R;
import com.iotsmartaliv.databinding.ActivityFaceEnrollCameraBinding;
import com.iotsmartaliv.utils.faceenroll.FaceCenterCrop;
import com.iotsmartaliv.utils.faceenroll.FaceDetectionProcessor;
import com.iotsmartaliv.utils.faceenroll.FaceDetectionResultListener;
import com.iotsmartaliv.utils.faceenroll.Imageutils;
import com.iotsmartaliv.utils.faceenroll.common.CameraSource;
import com.iotsmartaliv.utils.faceenroll.common.CameraSourcePreview;
import com.iotsmartaliv.utils.faceenroll.common.FrameMetadata;
import com.iotsmartaliv.utils.faceenroll.common.GraphicOverlay;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


import static com.iotsmartaliv.utils.faceenroll.FaceDetectionScanner.Constants.KEY_CAMERA_PERMISSION_GRANTED;
import static com.iotsmartaliv.utils.faceenroll.FaceDetectionScanner.Constants.PERMISSION_REQUEST_CAMERA;

public class FaceEnrollCameraActivity extends AppCompatActivity {

    String TAG = "ScannerActivity";
    FaceDetectionProcessor faceDetectionProcessor;
    FaceDetectionResultListener faceDetectionResultListener = null;
    Bitmap bmpCapturedImage;
    List<FirebaseVisionFace> capturedFaces;
    FaceCenterCrop faceCenterCrop;
    FaceCenterCrop.FaceCenterCropListener faceCenterCropListener;
    boolean isProcessing;
    CountDownTimer countDownTimer;
    boolean isEnable;
    int faceId = -1;
    private CameraSource mCameraSource = null;

    ActivityFaceEnrollCameraBinding binding;
    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG, "handleMessage: ");
            if (binding.preview != null)
                createCameraSource();
        }
    };
    private final Runnable mMessageSender = () -> {
        Log.d(TAG, "mMessageSender: ");
        Message msg = mHandler.obtainMessage();
        Bundle bundle = new Bundle();
        bundle.putBoolean(KEY_CAMERA_PERMISSION_GRANTED, false);
        msg.setData(bundle);
        mHandler.sendMessage(msg);
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getWindow() != null) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            Log.e(TAG, "Barcode scanner could not go into fullscreen mode!");
        }
        super.onCreate(savedInstanceState);
        binding = ActivityFaceEnrollCameraBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Log.e("FACEENROLL", "TRUE");

        faceCenterCrop = new FaceCenterCrop(this, 300, 300, 1);
        countDownTimer = new CountDownTimer(2500, 1000) {
            public void onTick(long millissUntilFinished) {
            }

            public void onFinish() {
                faceCenterCrop.transform(bmpCapturedImage, faceCenterCrop.getCenterPoint(capturedFaces), getFaceCropResult());
//                Toast.makeText(FaceEnrollCameraActivity.this, "Face Capture.", Toast.LENGTH_SHORT).show();
            }
        };
        if (binding.preview != null)
            if (binding.preview.isPermissionGranted(true, mMessageSender))
                new Thread(mMessageSender).start();

    }

    private void createCameraSource() {
        // To initialise the detector
        FirebaseVisionFaceDetectorOptions options =
                new FirebaseVisionFaceDetectorOptions.Builder()
                        .setClassificationMode(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
                        .setLandmarkMode(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
                        .enableTracking()
                        .build();

        FirebaseVisionFaceDetector detector = FirebaseVision.getInstance().getVisionFaceDetector(options);
        // To connect the camera resource with the detector
        mCameraSource = new CameraSource(this, binding.barcodeOverlay);
        mCameraSource.setFacing(CameraSource.CAMERA_FACING_FRONT);
        // FaceContourDetectorProcessor faceDetectionProcessor = new FaceContourDetectorProcessor(detector);
        faceDetectionProcessor = new FaceDetectionProcessor(detector);
        faceDetectionProcessor.setFaceDetectionResultListener(getFaceDetectionListener());
        mCameraSource.setMachineLearningFrameProcessor(faceDetectionProcessor);
        startCameraSource();
    }

    private FaceDetectionResultListener getFaceDetectionListener() {
        if (faceDetectionResultListener == null)
            faceDetectionResultListener = new FaceDetectionResultListener() {
                @Override
                public void onSuccess(@Nullable Bitmap originalCameraImage, @NonNull List<FirebaseVisionFace> faces, @NonNull FrameMetadata frameMetadata, @NonNull GraphicOverlay graphicOverlay) {
                    isEnable = faces.size() == 1;
                    if (isEnable) {
                        FirebaseVisionFace face = faces.get(0);
                        Log.d(TAG, "Face bounds : " + face.getBoundingBox());
                        // To get this, we have to set the ClassificationMode attribute as ALL_CLASSIFICATIONS
                        Log.d(TAG, "Left eye open probability : " + face.getLeftEyeOpenProbability());
                        Log.d(TAG, "Right eye open probability : " + face.getRightEyeOpenProbability());
                        Log.d(TAG, "Smiling probability : " + face.getSmilingProbability());

                        FirebaseVisionFaceLandmark LEFT_EAR = face.getLandmark(FirebaseVisionFaceLandmark.LEFT_EAR);
                        FirebaseVisionFaceLandmark RIGHT_EAR = face.getLandmark(FirebaseVisionFaceLandmark.RIGHT_EAR);
                        FirebaseVisionFaceLandmark LEFT_CHEEK = face.getLandmark(FirebaseVisionFaceLandmark.LEFT_CHEEK);
                        FirebaseVisionFaceLandmark RIGHT_CHEEK = face.getLandmark(FirebaseVisionFaceLandmark.RIGHT_CHEEK);
                        FirebaseVisionFaceLandmark LEFT_EYE = face.getLandmark(FirebaseVisionFaceLandmark.LEFT_EYE);
                        FirebaseVisionFaceLandmark RIGHT_EYE = face.getLandmark(FirebaseVisionFaceLandmark.RIGHT_EYE);
                        FirebaseVisionFaceLandmark MOUTH_LEFT = face.getLandmark(FirebaseVisionFaceLandmark.MOUTH_LEFT);
                        FirebaseVisionFaceLandmark MOUTH_RIGHT = face.getLandmark(FirebaseVisionFaceLandmark.MOUTH_RIGHT);
                        FirebaseVisionFaceLandmark NOSE_BASE = face.getLandmark(FirebaseVisionFaceLandmark.NOSE_BASE);
                        FirebaseVisionFaceLandmark MOUTH_BOTTOM = face.getLandmark(FirebaseVisionFaceLandmark.MOUTH_BOTTOM);
                        if (NOSE_BASE == null) {
                            Log.d(TAG, "onSuccess: NOSE_BASE Not Found.");
                            return;
                        }
                        Log.d(TAG, "onSuccess: " + NOSE_BASE.toString());

                        if (LEFT_EYE == null) {
                            Log.d(TAG, "onSuccess: LEFT_EYE Not Found.");
                            return;
                        }
                        if (RIGHT_EYE == null) {
                            Log.d(TAG, "onSuccess: RIGHT_EYE Not Found.");
                            return;
                        }
                        if (MOUTH_LEFT == null) {
                            Log.d(TAG, "onSuccess: MOUTH_LEFT Not Found.");
                            return;
                        }
                        if (MOUTH_RIGHT == null) {
                            Log.d(TAG, "onSuccess: MOUTH_RIGHT Not Found.");
                            return;
                        }
                        if (LEFT_EAR == null) {
                            Log.d(TAG, "onSuccess: LEFT_EAR Not Found.");
                            return;
                        }
                        if (RIGHT_EAR == null) {
                            Log.d(TAG, "onSuccess:RIGHT_EAR Not Found.");
                            return;
                        }
                        if (LEFT_CHEEK == null) {
                            Log.d(TAG, "onSuccess: LEFT_CHEEK Not Found.");
                            return;
                        }
                        if (RIGHT_CHEEK == null) {
                            Log.d(TAG, "onSuccess:RIGHT_CHEEK Not Found.");
                            return;
                        }

                        if (MOUTH_BOTTOM == null) {
                            Log.d(TAG, "onSuccess: MOUTH_BOTTOM Not Found.");
                            return;
                        }

                        // To get this, we have to enableTracking

                        Log.d(TAG, "Face ID : " + face.getTrackingId());

                        //  if (face.getHeadEulerAngleY())


                        runOnUiThread(() -> {
                            Log.d(TAG, "button enable true ");
                            bmpCapturedImage = originalCameraImage;
                            capturedFaces = faces;

                            if (faceCenterCrop != null) {
                                if (faceId == face.getTrackingId()) {
                                    countDownTimer.start();
                                    faceDetectionProcessor.setFaceDetectionResultListener(null);
                                    binding.progressBar.setVisibility(View.VISIBLE);
                                    //  faceCenterCrop.transform(bmpCapturedImage, faceCenterCrop.getCenterPoint(capturedFaces), getFaceCropResult());
                                    //    Toast.makeText(ScannerActivity.this, "Found Face Start", Toast.LENGTH_SHORT).show();

                                } else {
                                    faceId = face.getTrackingId();
                                    faceDetectionProcessor.setFaceDetectionResultListener(null);

                                    new Timer().schedule(new TimerTask() {
                                        @Override
                                        public void run() {
                                            faceDetectionProcessor.setFaceDetectionResultListener(getFaceDetectionListener());

                                            // this code will be executed after 2 seconds
                                        }
                                    }, 2000);
                                    binding.progressBar.setVisibility(View.GONE);

                                    countDownTimer.cancel();
                                }


                            }


                        });

                    } else {

                        //countDownTimer.cancel();
                    }
                }

                @Override
                public void onFailure(@NonNull Exception e) {

                }
            };

        return faceDetectionResultListener;
    }

    private void startCameraSource() {

        // check that the device has play services available.
        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
                getApplicationContext());

        Log.d(TAG, "startCameraSource: " + code);

        if (code != ConnectionResult.SUCCESS) {
            Dialog dlg =
                    GoogleApiAvailability.getInstance().getErrorDialog(this, code, PERMISSION_REQUEST_CAMERA);
            dlg.show();
        }

        if (mCameraSource != null && binding.preview != null && binding.barcodeOverlay != null) {
            try {
                Log.d(TAG, "startCameraSource: ");
                binding.preview.start(mCameraSource, binding.barcodeOverlay);
            } catch (IOException e) {
                Log.d(TAG, "Unable to start camera source.", e);
                mCameraSource.release();
                mCameraSource = null;
            }
        } else
            Log.d(TAG, "startCameraSource: not started");

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult: " + requestCode);
        binding.preview.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * Restarts the camera.
     */
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        startCameraSource();
    }

    /**
     * Stops the camera.
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (binding.preview != null)
            binding.preview.stop();
    }

    /**
     * Releases the resources associated with the camera source, the associated detector, and the
     * rest of the processing pipeline.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (mCameraSource != null) {
//            mCameraSource.release();
//        }
    }

    private FaceCenterCrop.FaceCenterCropListener getFaceCropResult() {
        if (faceCenterCropListener == null)
            faceCenterCropListener = new FaceCenterCrop.FaceCenterCropListener() {
                @Override
                public void onTransform(Bitmap updatedBitmap) {

                    Log.d(TAG, "onTransform: ");

                    try {
                        File capturedFile = new File(getFilesDir(), "newImage.jpg");

                        Imageutils imageutils = new Imageutils(FaceEnrollCameraActivity.this);
                        imageutils.store_image(capturedFile, updatedBitmap);

                        Intent currentIntent = getIntent();
                        currentIntent.putExtra("image", capturedFile.getAbsolutePath());
                        setResult(RESULT_OK, currentIntent);
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }

                @Override
                public void onFailure() {
                    Toast.makeText(FaceEnrollCameraActivity.this, "No face found", Toast.LENGTH_SHORT).show();
                }
            };

        return faceCenterCropListener;
    }
}
