package com.iotsmartaliv.dialog_box;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.common.api.Status;
import com.iotsmartaliv.R;

public class GpsEnableDialog extends Dialog implements View.OnClickListener{
    LocationListener mListener;
    Status locationStatus;
    TextView btnCancel,btnOk;


    public GpsEnableDialog(Activity activity, LocationListener listener, Status locationStatus){
        super(activity);
        this.mListener = listener;
        this.locationStatus = locationStatus;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_gps_enable);
        Window window = getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        initializeView();

    }

    private void initializeView() {

        btnCancel = findViewById(R.id.btn_cancel_enable_gps);
        btnOk = findViewById(R.id.btn_ok_enable_gps);
        btnCancel.setOnClickListener(this);
        btnOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btn_ok_enable_gps:
                mListener.googleLocationEnable(locationStatus);
                dismiss();
                break;

            case R.id.btn_cancel_enable_gps:
                dismiss();
                break;

        }


    }

    public interface LocationListener {
        void googleLocationEnable(Status locationStatus);
    }
    public GpsEnableDialog(@NonNull Context context) {
        super(context);
    }

    public GpsEnableDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected GpsEnableDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

}
