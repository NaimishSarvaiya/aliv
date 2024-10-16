package com.iotsmartaliv.utils.faceenroll;

import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.iotsmartaliv.utils.faceenroll.common.FrameMetadata;
import com.iotsmartaliv.utils.faceenroll.common.GraphicOverlay;

import java.util.List;

/**
 * Created by Jaison.
 */
public interface FaceDetectionResultListener {
    void onSuccess(
            @Nullable Bitmap originalCameraImage,
            @NonNull List<FirebaseVisionFace> faces,
            @NonNull FrameMetadata frameMetadata,
            @NonNull GraphicOverlay graphicOverlay);

    void onFailure(@NonNull Exception e);
}
