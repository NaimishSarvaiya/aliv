package com.iotsmartaliv.activity;

import static com.iotsmartaliv.constants.Constant.LOGIN_DETAIL;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeSuccessDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.iotsmartaliv.R;
import com.iotsmartaliv.apiCalling.listeners.RetrofitListener;
import com.iotsmartaliv.apiCalling.models.ErrorObject;
import com.iotsmartaliv.apiCalling.models.SuccessResponse;
import com.iotsmartaliv.apiCalling.retrofit.ApiServiceProvider;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.utils.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * This class is usages as activity class.
 * This activity will be  call when user want to forgot password.
 */
public class ForgotActivity extends AppCompatActivity implements RetrofitListener<SuccessResponse> {

    @BindView(R.id.edt_email_log_in)
    EditText edtEmailLogIn;
    ApiServiceProvider apiServiceProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        ButterKnife.bind(this);
        apiServiceProvider = ApiServiceProvider.getInstance(this);
    }

    public void backPress(View view) {
        onBackPressed();
    }

    @Override
    public void onResponseSuccess(SuccessResponse sucessRespnse, String apiFlag) {
        switch (apiFlag) {
            case Constant.UrlPath.FORGOT_PASSWORD_API:
                if (sucessRespnse.getStatus().equalsIgnoreCase("OK")) {
                    new AwesomeSuccessDialog(this)
                            .setTitle("Forgot Password")
                            .setMessage("Mail has been sent successfully")
                            .setColoredCircle(R.color.colorPrimary)
                            .setDialogIconAndColor(R.drawable.ic_success, R.color.Orangebtn)
                            .setCancelable(false)
                            .setDoneButtonClick(new Closure() {
                                @Override
                                public void exec() {
                                    onBackPressed();
                                }
                            })
                            .setDoneButtonText(getString(R.string.ok))
                            .setDoneButtonbackgroundColor(R.color.Orangebtn)
                            .setDoneButtonTextColor(R.color.white)
                            .show();
                } else {
                    Toast.makeText(this, sucessRespnse.getMsg(), Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
        switch (apiFlag) {
            case Constant.UrlPath.FORGOT_PASSWORD_API:
                Util.firebaseEvent(Constant.APIERROR, ForgotActivity.this, Constant.UrlPath.SERVER_URL + apiFlag, LOGIN_DETAIL.getUsername(), LOGIN_DETAIL.getAppuserID(), errorObject.getStatus());

                try {
                    Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
                }
                break;

        }
    }

    @OnClick(R.id.btn_send_pwd)
    public void onViewClicked() {
        if (edtEmailLogIn.getText().toString().trim().length() > 0) {
            Util.checkInternet(this, new Util.NetworkCheckCallback() {
                @Override
                public void onNetworkCheckComplete(boolean isAvailable) {
                    if (isAvailable) {
                        apiServiceProvider.callForForgotPassword(edtEmailLogIn.getText().toString().trim(), ForgotActivity.this);

                    }
                }
            });
        } else {
            edtEmailLogIn.setError("Please Enter Username.");
        }
    }
}
