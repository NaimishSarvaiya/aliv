package com.iotsmartaliv.dialog_box;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.iotsmartaliv.R;
import com.iotsmartaliv.adapter.DeviceMultiSelectDialogAdapter;
import com.iotsmartaliv.apiAndSocket.models.DeviceObject;
import com.iotsmartaliv.apiAndSocket.models.ResArrayObjectData;

import java.util.ArrayList;
import java.util.List;


public class CustomDeviceListDialog extends Dialog implements View.OnClickListener {
    public Activity activity;
    public Dialog dialog;
    public Button done_btn, cancel_btn;
    TextView title;
    EditText search_device;
    RecyclerView recyclerView;
    DeviceMultiSelectDialogAdapter adapter;
    SelectItemListener selectItemListener;
    private RecyclerView.LayoutManager mLayoutManager;
    List<DeviceObject> devices;

    public CustomDeviceListDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public CustomDeviceListDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public CustomDeviceListDialog(Activity a, DeviceMultiSelectDialogAdapter adapter, List<DeviceObject> devices, SelectItemListener selectItemListener) {
        super(a);
        this.activity = a;
        this.adapter = adapter;
        this.selectItemListener = selectItemListener;
        this.devices = devices;
        setupLayout();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_device_dialog_layout);
        Window window = getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        window.setBackgroundDrawableResource(android.R.color.transparent);
        cancel_btn = findViewById(R.id.cancel_btn);
        done_btn = findViewById(R.id.done_btn);
        title = findViewById(R.id.title);
        search_device = findViewById(R.id.search_device);
        recyclerView = findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
        cancel_btn.setOnClickListener(this);
        done_btn.setOnClickListener(this);

        search_device.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

    }

    private void filter(String text) {
        ArrayList<DeviceObject> temp = new ArrayList<>();
        for (DeviceObject d : devices) {
            if (!d.getCdeviceName().isEmpty()) {
                if (d.getCdeviceName().toLowerCase().contains(text.toLowerCase())) {
                    temp.add(d);
                }
            } else {
                if (d.getDeviceName().toLowerCase().contains(text.toLowerCase())) {
                    temp.add(d);
                }
            }
        }
        adapter.updateList(temp);
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