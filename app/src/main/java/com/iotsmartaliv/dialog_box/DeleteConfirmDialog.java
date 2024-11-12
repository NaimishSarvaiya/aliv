package com.iotsmartaliv.dialog_box;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.iotsmartaliv.R;

/**
 * This class is used as .
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 6/8/19 :August.
 */
public class DeleteConfirmDialog extends Dialog implements View.OnClickListener {
    public Activity activity;
    ConfirmListner confirmListner;

    public DeleteConfirmDialog(Activity activity, ConfirmListner confirmListner) {
        super(activity);
        this.activity = activity;
        this.confirmListner = confirmListner;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_delete_confirm);
        Window window = getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);

        // Get screen width using the context
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        int screenWidth = metrics.widthPixels;

        // Adjust window layout parameters
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = (int) (screenWidth * 0.9); // Set width to 90% of screen width
        params.height = WindowManager.LayoutParams.WRAP_CONTENT; // Set height to wrap content
        window.setAttributes(params);

        findViewById(R.id.buttonAdd).setOnClickListener(this);
        findViewById(R.id.buttonNo).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonAdd:
                confirmListner.deleteItem();
                break;
            case R.id.buttonNo:
                break;
        }
        dismiss();
    }

    public interface ConfirmListner {
        void deleteItem();
    }
}
