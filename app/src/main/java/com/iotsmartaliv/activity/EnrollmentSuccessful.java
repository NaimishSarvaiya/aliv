package com.iotsmartaliv.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.iotsmartaliv.R;

/**
 * This activity class is used for Enrollment successfully.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2018-10-23
 */
public class EnrollmentSuccessful extends AppCompatActivity {

    private ImageView imgFacePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enrollment_success_activity);
        initViews();
        new Handler().postDelayed(() -> openActivity(), 3000);
    }

    /**
     * This is for opening new activity.
     */
    public void openActivity() {
        Intent intent = new Intent(EnrollmentSuccessful.this, MainActivity.class);
        startActivity(intent);
        finishAffinity();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    /**
     * Initialzing views
     */
    private void initViews() {
        imgFacePicture = findViewById(R.id.face_image);
        try {
            Intent intent = getIntent();
            String path = intent.getStringExtra("path");
            Glide.with(EnrollmentSuccessful.this)
                    .load(path)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .apply(RequestOptions.circleCropTransform())
                    .into(imgFacePicture);
            //  imgFacePicture.setImageBitmap(BitmapFactory.decodeFile(path));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
