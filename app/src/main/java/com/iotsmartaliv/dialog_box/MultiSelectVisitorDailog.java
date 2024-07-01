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
import com.iotsmartaliv.adapter.MultiSelectVisitorAdapter;
import com.iotsmartaliv.model.VisitorData;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used as .
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 10/8/19 :August.
 */
public class MultiSelectVisitorDailog extends Dialog implements View.OnClickListener {

    public Activity activity;
    public Dialog dialog;
    public Button done_btn, cancel_btn;
    TextView title;
    EditText search_ed;
    RecyclerView recyclerView;
    MultiSelectVisitorAdapter adapter;
    SelectItemListener selectItemListener;
    List<VisitorData> mainDataList;

    private RecyclerView.LayoutManager mLayoutManager;

    public MultiSelectVisitorDailog(Context context) {
        super(context);
    }

    public MultiSelectVisitorDailog(Activity a, MultiSelectVisitorAdapter multiSelectVisitorAdapter, List<VisitorData> mDataset, SelectItemListener selectItemListener) {
        super(a);
        this.activity = a;
        this.adapter = multiSelectVisitorAdapter;
        this.selectItemListener = selectItemListener;
        this.mainDataList = mDataset;


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
        ArrayList<VisitorData> temp = new ArrayList<>();

        for (VisitorData d : mainDataList) {
            if (d.getUvisitorName().contains(text)) {
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
            case R.id.done_btn:
                List<VisitorData> list = new ArrayList<>();
                for (int i = 0; i < mainDataList.size(); i++) {
                    if (mainDataList.get(i).isCheck()) {
                        list.add(mainDataList.get(i));
                    }
                }
                selectItemListener.getSelectedItem(list);
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }

    public interface SelectItemListener {
        void getSelectedItem(List<VisitorData> mDataset);
    }
}
