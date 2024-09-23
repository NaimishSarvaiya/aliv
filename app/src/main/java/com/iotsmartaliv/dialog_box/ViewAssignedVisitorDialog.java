package com.iotsmartaliv.dialog_box;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import androidx.recyclerview.widget.DividerItemDecoration;
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
import android.widget.Toast;

import com.iotsmartaliv.R;
import com.iotsmartaliv.adapter.ViewVisitorListAdapter;
import com.iotsmartaliv.apiAndSocket.listeners.RetrofitListener;
import com.iotsmartaliv.apiAndSocket.models.ErrorObject;
import com.iotsmartaliv.apiAndSocket.models.GroupData;
import com.iotsmartaliv.apiAndSocket.models.SuccessResponse;
import com.iotsmartaliv.apiAndSocket.retrofit.ApiServiceProvider;
import com.iotsmartaliv.model.VisitorData;

import java.util.ArrayList;
import java.util.List;

import static com.iotsmartaliv.constants.Constant.LOGIN_DETAIL;


public class ViewAssignedVisitorDialog extends Dialog implements View.OnClickListener {
    public Activity activity;
    public Dialog dialog;
    public Button cancel_btn;
    TextView title;
    EditText search_ed;
    RecyclerView recyclerView;
    ViewVisitorListAdapter adapter;
    List<VisitorData> mainDataList;
    ApiServiceProvider apiServiceProvider;
    GroupData groupData;
    private RecyclerView.LayoutManager mLayoutManager;

    public ViewAssignedVisitorDialog(Activity a, ViewVisitorListAdapter adapter, List<VisitorData> mDataset, GroupData groupData) {
        super(a);
        this.activity = a;
        this.groupData = groupData;
        this.adapter = adapter;
        this.mainDataList = mDataset;
        apiServiceProvider = ApiServiceProvider.getInstance(activity);
        adapter.setOnUnassignedListener((data, position) -> {
            apiServiceProvider.callForAssignOrUnssignVisitorsToGroups(LOGIN_DETAIL.getAppuserID(), "delete",
                    new ArrayList<VisitorData>() {{
                        add(data);
                    }},
                    new ArrayList<GroupData>() {{
                        add(groupData);
                    }},
                    new RetrofitListener<SuccessResponse>() {
                        @Override
                        public void onResponseSuccess(SuccessResponse sucessRespnse, String apiFlag) {
                            if (sucessRespnse.getStatus().equalsIgnoreCase("OK")) {
                                Toast.makeText(getContext(), sucessRespnse.getMsg(), Toast.LENGTH_SHORT).show();
                                adapter.removeItem(position);
                            } else {
                                Toast.makeText(getContext(), sucessRespnse.getMsg(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                            Toast.makeText(getContext(), "Something Went Wrong.", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_view_asigned_visitor_layout);
        Window window = getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        cancel_btn = findViewById(R.id.cancel_btn);
        title = findViewById(R.id.title);
        search_ed = findViewById(R.id.search_ed);
        recyclerView = findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.HORIZONTAL));

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
            default:
                break;
        }
        dismiss();
    }
}