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
import com.iotsmartaliv.apiCalling.listeners.RetrofitListener;
import com.iotsmartaliv.apiCalling.models.ErrorObject;
import com.iotsmartaliv.apiCalling.retrofit.ApiServiceProvider;
import com.iotsmartaliv.model.VisitorData;
import com.iotsmartaliv.model.VisitorsListDataResponse;

import static com.iotsmartaliv.constants.Constant.LOGIN_DETAIL;

/**
 * This class is used as .
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 7/8/19 :August.
 */
public class EditVisitorDialog extends Dialog implements View.OnClickListener {
    Activity activity;
    Button buttonUpdate;
    EditText edName, edContactNumber, edLinsencePlate;
    TextView tvCountryCode;
    ApiServiceProvider apiServiceProvider;
    AddVisitorDialog.VisitorAddedRefresh visitorAddedRefresh;
    VisitorData visitorData;

    public EditVisitorDialog(Context context) {
        super(context);
    }

    public EditVisitorDialog(Activity activity, VisitorData visitorData, AddVisitorDialog.VisitorAddedRefresh visitorAddedRefresh) {
        super(activity);
        this.activity = activity;
        apiServiceProvider = ApiServiceProvider.getInstance(activity);
        this.visitorAddedRefresh = visitorAddedRefresh;
        this.visitorData = visitorData;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_edit_visitor);
        Window window = getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        buttonUpdate = findViewById(R.id.buttonUpdate);
        buttonUpdate.setOnClickListener(this);
        edName = findViewById(R.id.edt_name);
        edName.setText(visitorData.getUvisitorName());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonUpdate:
                if (edName.getText().toString().trim().length() > 0) {
                    if (!edName.getText().toString().trim().equalsIgnoreCase(visitorData.getUvisitorName())) {
                        apiServiceProvider.visitorUpdateAndDelete("put", visitorData.getVisitID(), LOGIN_DETAIL.getAppuserID(), edName.getText().toString().trim(), new RetrofitListener<VisitorsListDataResponse>() {
                            @Override
                            public void onResponseSuccess(VisitorsListDataResponse sucessResponse, String apiFlag) {
                                if (sucessResponse.getStatus().equalsIgnoreCase("Visitor updated")) {
                                    Toast.makeText(activity, "Visitor Updated Successfully.", Toast.LENGTH_SHORT).show();
                                    visitorAddedRefresh.visitorAddedRefreshNotify();
                                    dismiss();
                                } else {
                                    Toast.makeText(activity, sucessResponse.getMsg(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {

                            }
                        });
                    } else {
                        edName.setError("Change Visitor Name.");
                        edName.requestFocus();
                        Toast.makeText(activity, "Change Visitor Name.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    edName.setError("Enter Visitor Name.");
                    edName.requestFocus();
                    Toast.makeText(activity, "Enter Visitor Name.", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                dismiss();
                break;
        }
    }
}
