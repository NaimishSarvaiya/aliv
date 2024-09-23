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
import com.iotsmartaliv.apiAndSocket.models.SuccessResponse;
import com.iotsmartaliv.apiAndSocket.retrofit.ApiServiceProvider;

import static com.iotsmartaliv.constants.Constant.LOGIN_DETAIL;


public class AddGroupDialog extends Dialog implements View.OnClickListener {
    public Activity activity;
    public Dialog dialog;
    public Button add_btn;
    EditText group_name_ed;
    TextView title;
    GroupAddedRefresh groupAddedRefresh;
    ApiServiceProvider apiServiceProvider;

    public AddGroupDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public AddGroupDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public AddGroupDialog(Activity activity, GroupAddedRefresh groupAddedRefresh) {
        super(activity);
        this.activity = activity;
        apiServiceProvider = ApiServiceProvider.getInstance(activity);
        this.groupAddedRefresh = groupAddedRefresh;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog_add_group_layout);
        Window window = getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        add_btn = findViewById(R.id.add_btn);
        title = findViewById(R.id.title);
        group_name_ed = findViewById(R.id.group_name_ed);
        add_btn.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_btn:
                if (group_name_ed.getText().toString().trim().length() > 0) {
                    apiServiceProvider.callForAddGroup(LOGIN_DETAIL.getAppuserID(), group_name_ed.getText().toString(), new RetrofitListener<SuccessResponse>() {
                        @Override
                        public void onResponseSuccess(SuccessResponse sucessRespnse, String apiFlag) {
                            if (sucessRespnse.getStatus().equalsIgnoreCase("Group added")) {
                                Toast.makeText(activity, "Group Added Successfully.", Toast.LENGTH_SHORT).show();
                                groupAddedRefresh.groupAddedRefreshNotify();
                                dismiss();
                            } else {
                                Toast.makeText(activity, sucessRespnse.getMsg(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {

                        }
                    });
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

    public interface GroupAddedRefresh {
        void groupAddedRefreshNotify();
    }

}