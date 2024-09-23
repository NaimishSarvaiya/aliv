package com.iotsmartaliv.dialog_box;

import android.app.Activity;
import android.app.Dialog;
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
import android.widget.Toast;

import com.iotsmartaliv.R;
import com.iotsmartaliv.adapter.GroupMultiSelectDialogAdapter;
import com.iotsmartaliv.apiAndSocket.listeners.RetrofitListener;
import com.iotsmartaliv.apiAndSocket.models.ErrorObject;
import com.iotsmartaliv.apiAndSocket.models.GroupData;
import com.iotsmartaliv.apiAndSocket.models.SuccessResponse;
import com.iotsmartaliv.apiAndSocket.retrofit.ApiServiceProvider;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.model.VisitorData;
import com.iotsmartaliv.utils.Util;

import java.util.ArrayList;
import java.util.List;

import static com.iotsmartaliv.constants.Constant.LOGIN_DETAIL;


public class AssignGroupDialog extends Dialog implements View.OnClickListener {
    public Activity activity;
    public Dialog dialog;
    public Button cancel_btn, done_btn;
    TextView title;
    EditText search_ed;
    RecyclerView recyclerView;
    GroupMultiSelectDialogAdapter adapter;
    List<GroupData> mainDataList;
    ApiServiceProvider apiServiceProvider;
    VisitorData visitorData;
    private RecyclerView.LayoutManager mLayoutManager;

    public AssignGroupDialog(Activity a, GroupMultiSelectDialogAdapter adapter, List<GroupData> mDataset, VisitorData visitorData) {
        super(a);
        this.activity = a;
        this.adapter = adapter;
        this.mainDataList = mDataset;
        this.visitorData = visitorData;
        apiServiceProvider = ApiServiceProvider.getInstance(activity);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog_for_group_layout);
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
        ArrayList<GroupData> temp = new ArrayList<>();
        for (GroupData d : mainDataList) {
            if (d.getGroupName().contains(text)) {
                temp.add(d);
            }
        }
        adapter.updateList(temp);
    }

    public ArrayList<GroupData> getSelectItem() {
        ArrayList<GroupData> list = new ArrayList<>();
        for (GroupData deviceObject : mainDataList) {
            if (deviceObject.isSelected()) {
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
                        Util.checkInternet(activity, new Util.NetworkCheckCallback() {
                            @Override
                            public void onNetworkCheckComplete(boolean isAvailable) {
                                if (isAvailable) {
                                    apiServiceProvider.callForAssignOrUnssignVisitorsToGroups(LOGIN_DETAIL.getAppuserID(),
                                            "add",
                                            new ArrayList<VisitorData>() {{
                                                add(visitorData);
                                            }},
                                            getSelectItem(),
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
                                                    Util.firebaseEvent(Constant.APIERROR, activity, Constant.UrlPath.SERVER_URL + apiFlag, LOGIN_DETAIL.getUsername(), LOGIN_DETAIL.getAppuserID(), errorObject.getStatus());

                                                    Toast.makeText(getContext(), "Something Went Wrong.", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            }
                        });

                } else {
                    Toast.makeText(activity, "Please Select Group.", Toast.LENGTH_SHORT).show();
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