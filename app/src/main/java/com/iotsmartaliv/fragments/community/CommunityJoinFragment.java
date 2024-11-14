package com.iotsmartaliv.fragments.community;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iotsmartaliv.R;
import com.iotsmartaliv.apiAndSocket.listeners.RetrofitListener;
import com.iotsmartaliv.apiAndSocket.models.ErrorObject;
import com.iotsmartaliv.apiAndSocket.models.SuccessResponse;
import com.iotsmartaliv.apiAndSocket.retrofit.ApiServiceProvider;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.databinding.FragmentCommunityJoinBinding;


//import butterknife.OnClick;
//import butterknife.Unbinder;

import static com.iotsmartaliv.constants.Constant.LOGIN_DETAIL;

public class CommunityJoinFragment extends AppCompatActivity implements RetrofitListener<SuccessResponse> {
//    @BindView(R.id.edt_invitation_code)
//    EditText edtInvitationCode;
//    Unbinder unbinder;
    ApiServiceProvider apiServiceProvider;
    private OnJoinCommunityFragmentInListener mListener;
    private FragmentCommunityJoinBinding binding;


    public CommunityJoinFragment() {
        // Required empty public constructor
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =  FragmentCommunityJoinBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        apiServiceProvider = ApiServiceProvider.getInstance(this,false);
        binding.submitBtn.setOnClickListener( v-> onViewClicked() );
        binding.llHeader.imgBack.setOnClickListener(v -> onBackPressed());
        binding.llHeader.tvHeader.setText("Join Community");
    }



    public void OnFragmentInteractionListener(OnJoinCommunityFragmentInListener onFragmentInteractionListener) {
        mListener = onFragmentInteractionListener;
    }


//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
////        unbinder.unbind();
//    }

    public void onViewClicked() {
        if (binding.edtInvitationCode.getText().toString().trim().length() > 0) {
            apiServiceProvider.callForJoinCommunity(LOGIN_DETAIL.getAppuserID(), binding.edtInvitationCode.getText().toString().trim(), this);
        } else {
            binding.edtInvitationCode.setError("Enter Invitation Code.");
            binding.edtInvitationCode.requestFocus();
        }
    }

    @Override
    public void onResponseSuccess(SuccessResponse sucessRespnse, String apiFlag) {
        switch (apiFlag) {
            case Constant.UrlPath.JOIN_COMMUNITY_API:
                if (sucessRespnse.getStatus().equalsIgnoreCase("OK")) {
                    binding.edtInvitationCode.setText("");
                    Toast.makeText(CommunityJoinFragment.this, sucessRespnse.getMsg(), Toast.LENGTH_LONG).show();
                    showSuccessfullDailog();
                   /* if (mListener != null) {
                        mListener.onChangeJoinCommunityFaragment();
                    }*/
                } else {
                    Toast.makeText(CommunityJoinFragment.this, sucessRespnse.getMsg(), Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    public void showSuccessfullDailog() {
        final AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
        dialogBuilder.setCancelable(false);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_successful_booked, null);
        dialogBuilder.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        // Get the screen width
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenWidth = metrics.widthPixels;

        // Set dialog width to 80% of screen width
        WindowManager.LayoutParams params = dialogBuilder.getWindow().getAttributes();
        params.width = (int) (screenWidth * 0.9);
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogBuilder.getWindow().setAttributes(params);

        RelativeLayout rlOk = dialogView.findViewById(R.id.rl_ok);
        TextView tvMessage = dialogView.findViewById(R.id.tv_message);
        tvMessage.setText("Thank you for joining the community, please wait for request to be approved by Community Admin");
        rlOk.setOnClickListener(v -> {
            dialogBuilder.dismiss();
        });
        dialogBuilder.setView(dialogView);
        Window window = dialogBuilder.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialogBuilder.show();
    }

    @Override
    public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
        switch (apiFlag) {
            case Constant.UrlPath.JOIN_COMMUNITY_API:
                try {
                    Toast.makeText(CommunityJoinFragment.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(CommunityJoinFragment.this, "Something went wrong", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

//    public void setupUI(View view) {
//
//        //Set up touch listener for non-text box views to hide keyboard.
//        if (!(view instanceof EditText)) {
//            view.setOnTouchListener((v, event) -> {
//                hideSoftKeyboard();
//                return false;
//            });
//        }
//
//        //If a layout container, iterate over children and seed recursion.
//        if (view instanceof ViewGroup) {
//            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
//                View innerView = ((ViewGroup) view).getChildAt(i);
//                setupUI(innerView);
//            }
//        }
//    }
//
//    public void hideSoftKeyboard() {
//        try {
//            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
//            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public interface OnJoinCommunityFragmentInListener {
        void onChangeJoinCommunityFaragment();
    }
}
