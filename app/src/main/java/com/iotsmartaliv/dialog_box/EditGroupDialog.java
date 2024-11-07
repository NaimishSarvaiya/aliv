package com.iotsmartaliv.dialog_box;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.iotsmartaliv.R;
import com.iotsmartaliv.apiAndSocket.listeners.RetrofitListener;
import com.iotsmartaliv.apiAndSocket.models.ErrorObject;
import com.iotsmartaliv.apiAndSocket.models.GroupData;
import com.iotsmartaliv.apiAndSocket.models.GroupResponse;
import com.iotsmartaliv.apiAndSocket.retrofit.ApiServiceProvider;


public class EditGroupDialog extends Dialog implements View.OnClickListener {
    public Activity activity;
    public Dialog dialog;
    public Button add_btn;
    EditText group_name_ed;
    TextView title;
    AddGroupDialog.GroupAddedRefresh groupAddedRefresh;
    ApiServiceProvider apiServiceProvider;
    GroupData groupData;

    public EditGroupDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public EditGroupDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public EditGroupDialog(Activity activity, GroupData groupData, AddGroupDialog.GroupAddedRefresh groupAddedRefresh) {
        super(activity);
        this.activity = activity;
        apiServiceProvider = ApiServiceProvider.getInstance(activity,false);
        this.groupAddedRefresh = groupAddedRefresh;
        this.groupData = groupData;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog_edit_group_layout);
        Window window = getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        add_btn = findViewById(R.id.add_btn);
        title = findViewById(R.id.title);
        group_name_ed = findViewById(R.id.group_name_ed);
        add_btn.setOnClickListener(this);
        group_name_ed.setText(groupData.getGroupName());

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_btn:
                if (group_name_ed.getText().toString().trim().length() > 0) {
                    if (!group_name_ed.getText().toString().equals(groupData.getGroupName())) {
                        apiServiceProvider.callForGroupUpdateAndDelete("put", groupData.getVgroupID(), group_name_ed.getText().toString(), new RetrofitListener<GroupResponse>() {
                            @Override
                            public void onResponseSuccess(GroupResponse groupResponse, String apiFlag) {
                                if (groupResponse.getStatus().equalsIgnoreCase("Group updated")) {
                                    Toast.makeText(activity, "Group Updated Successfully.", Toast.LENGTH_SHORT).show();
                                    groupAddedRefresh.groupAddedRefreshNotify();
                                    dismiss();
                                } else {
                                    Toast.makeText(activity, groupResponse.getMsg(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {

                            }
                        });
                    } else {
                        group_name_ed.setError("Change Group Name.");
                        group_name_ed.requestFocus();
                        Toast.makeText(activity, "Change Group Name.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    group_name_ed.setError("Enter Group Name.");
                    group_name_ed.requestFocus();
                    Toast.makeText(activity, "Enter Group Name.", Toast.LENGTH_SHORT).show();
                }
                //dismiss();
                break;
            default:
                dismiss();
                break;
        }
    }


}