package com.iotsmartaliv.dialog_box;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.iotsmartaliv.R;
import com.iotsmartaliv.apiAndSocket.listeners.RetrofitListener;
import com.iotsmartaliv.apiAndSocket.models.ErrorObject;
import com.iotsmartaliv.apiAndSocket.retrofit.ApiServiceProvider;
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
        apiServiceProvider = ApiServiceProvider.getInstance(activity,false);
        this.visitorAddedRefresh = visitorAddedRefresh;
        this.visitorData = visitorData;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_edit_visitor);
        Window window = getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);

        // Get screen width using the context
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        int screenWidth = metrics.widthPixels;

        // Adjust window layout parameters
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = (int) (screenWidth * 0.9); // Set width to 90% of screen width
        params.height = WindowManager.LayoutParams.WRAP_CONTENT; // Set height to wrap content
        window.setAttributes(params);

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
