package com.iotsmartaliv.dialog_box;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

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
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
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
