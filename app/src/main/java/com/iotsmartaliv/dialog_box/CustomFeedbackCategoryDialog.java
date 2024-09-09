package com.iotsmartaliv.dialog_box;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iotsmartaliv.R;
import com.iotsmartaliv.adapter.FeedbackCategoryDialogAdapter;
import com.iotsmartaliv.model.feedback.FeedbackCategoryData;

import java.util.ArrayList;
import java.util.List;


public class CustomFeedbackCategoryDialog extends Dialog implements View.OnClickListener {
    public Activity activity;
    public Dialog dialog;
    public Button cancel_btn;
    TextView title;
    EditText search_ed;
    RecyclerView recyclerView;
    FeedbackCategoryDialogAdapter adapter;
    List<FeedbackCategoryData> mainDataList;
    private RecyclerView.LayoutManager mLayoutManager;

    public CustomFeedbackCategoryDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public CustomFeedbackCategoryDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public CustomFeedbackCategoryDialog(Activity a, FeedbackCategoryDialogAdapter adapter, List<FeedbackCategoryData> mDataset) {
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
        ArrayList<FeedbackCategoryData> temp = new ArrayList<>();
        for (FeedbackCategoryData d : mainDataList) {
            if (d.getCatName().contains(text)) {
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