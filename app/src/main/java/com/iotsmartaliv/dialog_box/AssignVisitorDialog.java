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
import com.iotsmartaliv.adapter.VisitorMultiSelectDialogAdapter;
import com.iotsmartaliv.apiCalling.listeners.RetrofitListener;
import com.iotsmartaliv.apiCalling.models.ErrorObject;
import com.iotsmartaliv.apiCalling.models.GroupData;
import com.iotsmartaliv.apiCalling.models.SuccessResponse;
import com.iotsmartaliv.apiCalling.retrofit.ApiServiceProvider;
import com.iotsmartaliv.model.VisitorData;

import java.util.ArrayList;
import java.util.List;

import static com.iotsmartaliv.constants.Constant.LOGIN_DETAIL;


public class AssignVisitorDialog extends Dialog implements View.OnClickListener {
    public Activity activity;
    public Dialog dialog;
    public Button cancel_btn, done_btn;
    TextView title;
    EditText search_ed;
    RecyclerView recyclerView;
    VisitorMultiSelectDialogAdapter adapter;
    List<VisitorData> mainDataList;
    ApiServiceProvider apiServiceProvider;
    GroupData groupData;
    private RecyclerView.LayoutManager mLayoutManager;

    public AssignVisitorDialog(Activity a, VisitorMultiSelectDialogAdapter adapter, List<VisitorData> mDataset, GroupData groupData) {
        super(a);
        this.activity = a;
        this.adapter = adapter;
        this.mainDataList = mDataset;
        this.groupData = groupData;
        apiServiceProvider = ApiServiceProvider.getInstance(activity);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog_for_visitor_layout);
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
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.HORIZONTAL));
        done_btn.setOnClickListener(this);
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

    public ArrayList<VisitorData> getSelectItem() {
        ArrayList<VisitorData> list = new ArrayList<>();
        for (VisitorData deviceObject : mainDataList) {
            if (deviceObject.isCheck()) {
                list.add(deviceObject);
            }
        }
        return list;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.done_btn:
                if (getSelectItem().size() > 0) {
                    apiServiceProvider.callForAssignOrUnssignVisitorsToGroups(LOGIN_DETAIL.getAppuserID(),
                            "add",
                            getSelectItem(),
                            new ArrayList<GroupData>() {{
                                add(groupData);
                            }},

                            new RetrofitListener<SuccessResponse>() {
                                @Override
                                public void onResponseSuccess(SuccessResponse sucessRespnse, String apiFlag) {
                                    if (sucessRespnse.getStatus().equalsIgnoreCase("OK")) {
                                        Toast.makeText(getContext(), sucessRespnse.getMsg(), Toast.LENGTH_SHORT).show();
                                        dismiss();
                                    } else {
                                        Toast.makeText(getContext(), sucessRespnse.getMsg(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                                    Toast.makeText(getContext(), "Something Went Wrong.", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(activity, "Please Select Visitor.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.cancel_btn:
                dismiss();
                break;
            default:
                break;
        }
    }
}