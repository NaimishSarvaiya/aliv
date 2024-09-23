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
import com.iotsmartaliv.adapter.GroupListAdapter;
import com.iotsmartaliv.apiAndSocket.models.GroupData;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used as .
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 9/8/19 :August
 */
public class GroupListDialog extends Dialog implements View.OnClickListener {
    TextView title;
    Activity activity;
    EditText search_ed;
    RecyclerView recyclerView;
    Button cancel_btn;
    List<GroupData> mainDataList;

    GroupListAdapter groupListAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public GroupListDialog(Context context) {
        super(context);
    }

    public GroupListDialog(Activity a, GroupListAdapter adapter, List<GroupData> mDataset) {
        super(a);
        this.activity = a;
        this.groupListAdapter = adapter;
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
        title.setText("Select Group");
        search_ed = findViewById(R.id.search_ed);
        search_ed.setHint("Search Group");
        recyclerView = findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(groupListAdapter);
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
        ArrayList<GroupData> temp = new ArrayList<>();

        for (GroupData d : mainDataList) {
            if (d.getGroupName().contains(text)) {
                temp.add(d);
            }
        }
        groupListAdapter.updateList(temp);
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
