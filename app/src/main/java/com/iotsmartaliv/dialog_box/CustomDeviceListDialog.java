package com.iotsmartaliv.dialog_box;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.iotsmartaliv.R;
import com.iotsmartaliv.adapter.DeviceMultiSelectDialogAdapter;
import com.iotsmartaliv.apiCalling.models.DeviceObject;

import java.util.List;


public class CustomDeviceListDialog extends Dialog implements View.OnClickListener {
    public Activity activity;
    public Dialog dialog;
    public Button done_btn, cancel_btn;
    TextView title;
    EditText search_ed;
    RecyclerView recyclerView;
    DeviceMultiSelectDialogAdapter adapter;
    SelectItemListener selectItemListener;
    private RecyclerView.LayoutManager mLayoutManager;

    public CustomDeviceListDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public CustomDeviceListDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public CustomDeviceListDialog(Activity a, DeviceMultiSelectDialogAdapter adapter, SelectItemListener selectItemListener) {
        super(a);
        this.activity = a;
        this.adapter = adapter;
        this.selectItemListener = selectItemListener;
        setupLayout();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_device_dialog_layout);
        Window window = getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        cancel_btn = findViewById(R.id.cancel_btn);
        done_btn = findViewById(R.id.done_btn);
        title = findViewById(R.id.title);
        search_ed = findViewById(R.id.search_ed);
        recyclerView = findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
        cancel_btn.setOnClickListener(this);
        done_btn.setOnClickListener(this);

    }

    private void setupLayout() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_btn:
                dismiss();
                break;
            case R.id.done_btn:
                selectItemListener.getSelectedItem(adapter.getSelectItem());
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }


    public interface SelectItemListener {
        void getSelectedItem(List<DeviceObject> mDataset);
    }
}