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
import com.iotsmartaliv.adapter.CommunityDialogAdapter;
import com.iotsmartaliv.apiCalling.models.ResArrayObjectData;

import java.util.ArrayList;
import java.util.List;


public class CustomCommunityDialog extends Dialog implements View.OnClickListener {
    public Activity activity;
    public Dialog dialog;
    public Button cancel_btn;
    TextView title;
    EditText search_ed;
    RecyclerView recyclerView;
    CommunityDialogAdapter adapter;
    List<ResArrayObjectData> mainDataList;
    private RecyclerView.LayoutManager mLayoutManager;

    public CustomCommunityDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public CustomCommunityDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public CustomCommunityDialog(Activity a, CommunityDialogAdapter adapter, List<ResArrayObjectData> mDataset) {
        super(a);
        this.activity = a;
        this.adapter = adapter;
        this.mainDataList = mDataset;
        setupLayout();
    }

    private void setupLayout() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog_layout);
        Window window = getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        cancel_btn = findViewById(R.id.cancel_btn);
        title = findViewById(R.id.title);
        search_ed = findViewById(R.id.search_ed);
        recyclerView = findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
        cancel_btn.setOnClickListener(this);
        search_ed.addTextChangedListener(new TextWatcher() {
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
        ArrayList<ResArrayObjectData> temp = new ArrayList<>();
        for (ResArrayObjectData d : mainDataList) {
            if (d.getCommunityName().contains(text)) {
                temp.add(d);
            }
        }
        adapter.updateList(temp);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_btn:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }
}