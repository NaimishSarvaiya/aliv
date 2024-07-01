package com.iotsmartaliv.fragments.community;

import android.app.Activity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.iotsmartaliv.R;
import com.iotsmartaliv.apiCalling.listeners.RetrofitListener;
import com.iotsmartaliv.apiCalling.models.ErrorObject;
import com.iotsmartaliv.apiCalling.models.SuccessResponse;
import com.iotsmartaliv.apiCalling.retrofit.ApiServiceProvider;
import com.iotsmartaliv.constants.Constant;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.iotsmartaliv.constants.Constant.LOGIN_DETAIL;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link CommunityJoinFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommunityJoinFragment extends Fragment implements RetrofitListener<SuccessResponse> {
    @BindView(R.id.edt_invitation_code)
    EditText edtInvitationCode;
    Unbinder unbinder;
    ApiServiceProvider apiServiceProvider;
    private OnJoinCommunityFragmentInListener mListener;

    public CommunityJoinFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CommunityJoinFragment.
     */
    public static CommunityJoinFragment newInstance() {
        return new CommunityJoinFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_community_join, container, false);
        unbinder = ButterKnife.bind(this, view);
        apiServiceProvider = ApiServiceProvider.getInstance(getContext());
        setupUI(view);
        return view;
    }


    public void OnFragmentInteractionListener(OnJoinCommunityFragmentInListener onFragmentInteractionListener) {
        mListener = onFragmentInteractionListener;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.submit_btn)
    public void onViewClicked() {
        if (edtInvitationCode.getText().toString().trim().length() > 0) {
            apiServiceProvider.callForJoinCommunity(LOGIN_DETAIL.getAppuserID(), edtInvitationCode.getText().toString().trim(), this);
        } else {
            edtInvitationCode.setError("Enter Invitation Code.");
            edtInvitationCode.requestFocus();
        }
    }

    @Override
    public void onResponseSuccess(SuccessResponse sucessRespnse, String apiFlag) {
        switch (apiFlag) {
            case Constant.UrlPath.JOIN_COMMUNITY_API:
                if (sucessRespnse.getStatus().equalsIgnoreCase("OK")) {
                    edtInvitationCode.setText("");
                    Toast.makeText(getContext(), sucessRespnse.getMsg(), Toast.LENGTH_LONG).show();
                    showSucessDialog();
                   /* if (mListener != null) {
                        mListener.onChangeJoinCommunityFaragment();
                    }*/
                } else {
                    Toast.makeText(getContext(), sucessRespnse.getMsg(), Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void showSucessDialog() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.my_sucess_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(dialogView);
        Button buttonOk = dialogView.findViewById(R.id.buttonOk);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
        buttonOk.setOnClickListener(v -> alertDialog.dismiss());
    }

    @Override
    public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
        switch (apiFlag) {
            case Constant.UrlPath.JOIN_COMMUNITY_API:
                try {
                    Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    public void setupUI(View view) {

        //Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener((v, event) -> {
                hideSoftKeyboard();
                return false;
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    public void hideSoftKeyboard() {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnJoinCommunityFragmentInListener {
        void onChangeJoinCommunityFaragment();
    }
}
