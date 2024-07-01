package com.iotsmartaliv.apiCalling.listeners;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.view.WindowManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This class is used as Callback of API response. with the help of this class we are showing progress bar on calling of api .
 * when we are using this in callback so we don't need to show progress bar from activity.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 27/3/19 :March : 2019 on 15 : 22.
 */

public class CallBackWithProgress<T> implements Callback<T> {

    private Context context;
    private ProgressDialog mProgressDialog;

    protected CallBackWithProgress(Context context) {
        this.context = context;
        mProgressDialog = new ProgressDialog(context);
        ((Activity) context).getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        Activity activity = ((Activity) context);
        if (activity.isDestroyed()) {
            return;
        }
        if (activity.isFinishing()) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if(activity.isActivityTransitionRunning()) {
                return;
            }
        }

        try {
            mProgressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (mProgressDialog.isShowing()) {
            ((Activity) context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            Activity activity = ((Activity) context);
            if (activity.isDestroyed()) {
                return;
            }
            if (activity.isFinishing()) {
                return;
            }
            mProgressDialog.dismiss();

        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        if (mProgressDialog.isShowing()) {
            Activity activity = ((Activity) context);
            ((Activity) context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            if (activity.isDestroyed()) {
                return;
            }
            if (activity.isFinishing()) {
                return;
            }


            mProgressDialog.dismiss();
        }
    }
}