package com.iotsmartaliv.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AlertDialog;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.iotsmartaliv.R;
import com.iotsmartaliv.activity.EnrollmentActivity;
import com.iotsmartaliv.activity.MainActivity;
import com.iotsmartaliv.activity.NewPasswordActivity;
import com.iotsmartaliv.apiCalling.listeners.RetrofitListener;
import com.iotsmartaliv.apiCalling.models.ErrorObject;
import com.iotsmartaliv.apiCalling.models.SuccessResponse;
import com.iotsmartaliv.apiCalling.retrofit.ApiServiceProvider;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.utils.CommanUtils;
import com.iotsmartaliv.utils.SharePreference;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.iotsmartaliv.activity.MainActivity.drawerLayout;
import static com.iotsmartaliv.constants.Constant.LOGIN_DETAIL;
import static com.iotsmartaliv.constants.Constant.LOGIN_PREFRENCE;

/**
 * This fragment class is used for my account fragment.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2018-10-24
 */
public class MyAccountFragment extends Fragment implements RetrofitListener<SuccessResponse> {

    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.edt_user_name)
    TextView edtUserName;
    @BindView(R.id.edt_email_log_in)
    TextView edtEmailLogIn;
    Unbinder unbinder;
    @BindView(R.id.rel_face_registration)
    RelativeLayout relFaceRegistration;
    @BindView(R.id.tv_payment_help)
    TextView tvPaymentHelp;
    ApiServiceProvider apiServiceProvider;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.my_account_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        apiServiceProvider = ApiServiceProvider.getInstance(getContext());
        setDataUI();
        return view;
    }

    public void setDataUI() {
        tvName.setText(LOGIN_DETAIL.getUserFullName());
        edtUserName.setText(LOGIN_DETAIL.getUsername());
        edtEmailLogIn.setText(LOGIN_DETAIL.getUserEmail());
    }

    /**
     * This method is used for load the fragments.
     *
     * @param fragment
     */
    private void loadFragments(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.img_change_name, R.id.img_change_uname, R.id.edt_face_registration, R.id.tv_payment_help, R.id.rel_password})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_change_name:
                showPopForTakeInput("Enter your name.", tvName.getText().toString(), 1);
                break;

     case R.id.img_change_uname:
                showPopForTakeInput("Enter your username.", edtUserName.getText().toString(), 2);
                break;
            case R.id.edt_face_registration:
                Intent intentEnrollment = new Intent(getActivity(), EnrollmentActivity.class);
                startActivity(intentEnrollment);
                break;
            case R.id.rel_password:
                showPopForTakeInput("Enter your current password.", "Current password.", 3);
                break;
            case R.id.tv_payment_help:
                /*    Intent intentPayment = new Intent(getActivity(), PaymentSetupActivity.class);
                startActivity(intentPayment);*/
                drawerLayout.closeDrawer(Gravity.START);
                Fragment fragmentPayment = new PaymentFragment();
                ((MainActivity) getActivity()).tvHeader.setText(getResources().getString(R.string.payment));
                ((MainActivity) getActivity()).imgDraweHeader.setVisibility(View.GONE);
                loadFragments(fragmentPayment);
                break;
        }
    }

    /**
     * @param title
     * @param hint
     * @param isDialogType this param is use for which type of dialog you want to show
     *                     1= change name dialog.
     *                     2= change username dialog.
     *                     3= change password dialog for confirm old password.
     */
    public void showPopForTakeInput(String title, String hint, int isDialogType) {
        final AlertDialog dialogBuilder = new AlertDialog.Builder(getContext()).create();
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog_community, null);
        final EditText editText = dialogView.findViewById(R.id.edt_comment);
        final TextView tvTitle = dialogView.findViewById(R.id.textView);
        Button buttonSubmit = dialogView.findViewById(R.id.buttonSubmit);
        Button buttonCancel = dialogView.findViewById(R.id.buttonCancel);
        buttonSubmit.setText("Update");
        buttonCancel.setText("Cancel");
        tvTitle.setText(title);
        editText.setText(hint);
        if (isDialogType == 3) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            buttonSubmit.setText("Verify");

        }

        buttonCancel.setOnClickListener(view -> {
            dialogBuilder.dismiss();
        });
        buttonSubmit.setOnClickListener(view -> {
            if (editText.getText().toString().trim().length() > 0) {
                String value = editText.getText().toString();
                if (isDialogType != 3) {
                    dialogBuilder.dismiss();
                    apiServiceProvider.updateUserProile(LOGIN_DETAIL.getAppuserID(), (isDialogType == 2 ? value : ""), (isDialogType == 1 ? value : ""), "", this);
                } else {

                    MessageDigest md = null;
                    try {
                        md = MessageDigest.getInstance("SHA-256");
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                    byte[] hashInBytes = new byte[0];
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                        hashInBytes = md.digest(value.getBytes(StandardCharsets.UTF_8));
                    }
                    // bytes to hex
                    StringBuilder sb = new StringBuilder();
                    for (byte b : hashInBytes) {
                        sb.append(String.format("%02x", b));
                    }

                    if (LOGIN_DETAIL.getPassword().equalsIgnoreCase(sb.toString())) {
                        dialogBuilder.dismiss();
                        Intent intentEnrollment = new Intent(getActivity(), NewPasswordActivity.class);
                        intentEnrollment.putExtra("isFromAccount", true);
                        startActivity(intentEnrollment);
                    } else {
                        editText.setError("Password Not Match.");
                    }
                }
            } else {
                if (isDialogType != 3) {
                    editText.setError(isDialogType == 1 ? "Enter Name" : "Enter Username");
                } else {
                    editText.setError("Enter Your Current Password");
                }
                editText.requestFocus();
            }
        });


        dialogBuilder.setView(dialogView);
        dialogBuilder.show();

    }


    @Override
    public void onResponseSuccess(SuccessResponse sucessRespnse, String apiFlag) {
        switch (apiFlag) {
            case Constant.UrlPath.UPDATE_PROFILE_API:
                if (sucessRespnse.getStatus().equalsIgnoreCase("OK")) {
                    SharePreference.getInstance(getContext()).putString(LOGIN_PREFRENCE, new Gson().toJson(sucessRespnse.getData()));
                    Constant.LOGIN_DETAIL = sucessRespnse.getData();
                    CommanUtils.showSucessDialog(getContext(), "Profile Updated Successfully", "");
                    setDataUI();
                } else {
                    Toast.makeText(getContext(), sucessRespnse.getMsg(), Toast.LENGTH_LONG).show();
                }

                break;
        }
    }

    @Override
    public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
        switch (apiFlag) {
            case Constant.UrlPath.UPDATE_PROFILE_API:
                try {
                    Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                }
                break;

        }
    }

}