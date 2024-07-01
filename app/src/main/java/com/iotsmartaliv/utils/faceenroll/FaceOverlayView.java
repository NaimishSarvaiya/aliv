package com.iotsmartaliv.utils.faceenroll;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;

import com.google.android.gms.vision.face.Face;

public class FaceOverlayView {
    private Bitmap mBitmap;
    private SparseArray<Face> mFaces;

    public FaceOverlayView(Context context) {
    }


/*

    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
        FaceDetector detector = new FaceDetector.Builder(getContext())
                .setTrackingEnabled(true)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .setMode(FaceDetector.ACCURATE_MODE)
                .build();

        if (!detector.isOperational()) {
            //Handle contingency        } else {
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            mFaces = detector.detect(frame);
            detector.release();
        }

    }



    private void drawBitmap(Canvas ) {
        double viewWidth = canvas.getWidth();
        double viewHeight = canvas.getHeight();
        double imageWidth = mBitmap.getWidth();
        double imageHeight = mBitmap.getHeight();
        int size = 200;
        float left = 0;
        float top = 0;
        float right = 0;
        float bottom = 0;
        for (int i = 0; i < mFaces.size(); i++) {
            Face face = mFaces.valueAt(i);
            left = (float) (face.getPosition().x);
            top = (float) (face.getPosition().y);
            right = (float) (face.getPosition().x + face.getWidth());
            bottom = (float) (face.getPosition().y + face.getHeight());
        }
        Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
        Rect dst = new Rect(0, 0, size, size);
        canvas.drawBitmap(mBitmap, src, dst, null);
    }
*/

}
