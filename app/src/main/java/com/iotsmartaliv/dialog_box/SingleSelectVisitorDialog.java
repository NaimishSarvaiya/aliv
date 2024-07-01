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
import com.iotsmartaliv.adapter.SingleSelectVisitorAdapter;
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
public class SingleSelectVisitorDialog extends Dialog implements View.OnClickListener {

    public Activity activity;
    public Dialog dialog;
    public Button done_btn, cancel_btn;
    TextView title;
    EditText search_ed;
    RecyclerView recyclerView;
    SingleSelectVisitorAdapter singleSelectVisitorAdapter;
    List<VisitorData> mainDataList;
    private RecyclerView.LayoutManager mLayoutManager;

    public SingleSelectVisitorDialog(Context context) {
        super(context);
    }

    public SingleSelectVisitorDialog(Activity a, SingleSelectVisitorAdapter adapter, List<VisitorData> mDataset) {
        super(a);
        this.activity = a;
        this.singleSelectVisitorAdapter = adapter;
        this.mainDataList = mDataset;

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
        title.setText("Select Visitor");
        search_ed = findViewById(R.id.search_ed);
        search_ed.setHint("Search Visitor");
        recyclerView = findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(singleSelectVisitorAdapter);
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

    private void filter(String text) {
        ArrayList<VisitorData> temp = new ArrayList<>();

        for (VisitorData d : mainDataList) {
            if (d.getUvisitorName().contains(text)) {
                temp.add(d);
            }
        }
        singleSelectVisitorAdapter.updateList(temp);
    }

}
