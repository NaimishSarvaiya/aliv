package com.iotsmartaliv.fragments.form;

import static com.iotsmartaliv.constants.Constant.LOGIN_DETAIL;
import static com.iotsmartaliv.constants.Constant.hideLoader;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.iotsmartaliv.R;
import com.iotsmartaliv.activity.feedback.CreateFeedbackActivity;
import com.iotsmartaliv.adapter.CommunityDialogAdapter;
import com.iotsmartaliv.apiAndSocket.listeners.RetrofitListener;
import com.iotsmartaliv.apiAndSocket.models.ErrorObject;
import com.iotsmartaliv.apiAndSocket.models.ResArrayObjectData;
import com.iotsmartaliv.apiAndSocket.models.SuccessArrayResponse;
import com.iotsmartaliv.apiAndSocket.retrofit.ApiServiceProvider;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.databinding.FragmentApplyFormBinding;
import com.iotsmartaliv.dialog_box.CustomCommunityDialog;
import com.iotsmartaliv.fragments.FragmentEvent;
import com.iotsmartaliv.utils.Util;
import com.iotsmartaliv.viewModel.ApplyFormViewModel;

import java.util.ArrayList;
import java.util.List;

import io.sentry.protocol.App;

public class ApplyFormFragment extends Fragment {

    FragmentApplyFormBinding binding;
    private ApplyFormViewModel viewModel;
    CustomCommunityDialog customCommunityDialog;
    ApiServiceProvider apiServiceProvider;
    private List<ResArrayObjectData> mDataset = new ArrayList<>();
    String communityID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentApplyFormBinding.inflate(inflater, container, false);
        init();
        observeViewModel();
        return binding.getRoot();
    }

    void init() {
        apiServiceProvider = ApiServiceProvider.getInstance(getContext(), false);
        viewModel = new ViewModelProvider(requireActivity()).get(ApplyFormViewModel.class);
        binding.layoutNoData.tvTitle.setText("We Couldn't Find Any Results");
        binding.layoutNoData.tvDetail.setText("All online forms for the selected community will be displayed in this section. Currently, you do not have any online forms available. For further assistance, please contact management through the feedback module.");
        binding.rlSelectCommunity.setOnClickListener(v -> {
           viewModel.fetchCommunity(LOGIN_DETAIL.getAppuserID(),apiServiceProvider);
        });
    }

    private void observeViewModel() {
        viewModel.getDataset().observe(requireActivity(), new Observer<List<ResArrayObjectData>>() {
            @Override
            public void onChanged(List<ResArrayObjectData> resArrayObjectData) {
                if (resArrayObjectData.size() == 1) {
                    binding.rlSelectCommunity.setVisibility(View.GONE);
                    communityID = resArrayObjectData.get(0).getCommunityID();
                    binding.tvCommunity.setText(resArrayObjectData.get(0).getCommunityName());
                } else {
                    mDataset.clear();
                    mDataset.addAll(resArrayObjectData);
                    CommunityDialogAdapter communityDialogAdapter = new CommunityDialogAdapter(mDataset, data -> {
                        communityID = data.getCommunityID();
                        binding.tvCommunity.setText(data.getCommunityName());
                        customCommunityDialog.dismiss();
                    });
                    customCommunityDialog = new CustomCommunityDialog(requireActivity(), communityDialogAdapter, mDataset);
                    customCommunityDialog.setCanceledOnTouchOutside(false);
                    customCommunityDialog.show();
                }
            }
        });
    }
}
//    void getCommunity() {
//        viewModel.fetchCommunity();
//        Util.checkInternet(requireActivity(), new Util.NetworkCheckCallback() {
//            @Override
//            public void onNetworkCheckComplete(boolean isAvailable) {
//                if (isAvailable) {
//                    apiServiceProvider.callForListOfCommunity(LOGIN_DETAIL.getAppuserID(), new RetrofitListener<SuccessArrayResponse>() {
//                        @Override
//                        public void onResponseSuccess(SuccessArrayResponse successArrayResponse, String apiFlag) {
//                            if (successArrayResponse.getStatus().equalsIgnoreCase("OK")) {
//                                if (successArrayResponse.getData().size() > 0) {
//                                    if (successArrayResponse.getData().size() == 1) {
//                                        binding.rlSelectCommunity.setVisibility(View.GONE);
//                                        communityID = successArrayResponse.getData().get(0).getCommunityID();
//                                        binding.tvCommunity.setText(successArrayResponse.getData().get(0).getCommunityName());
//                                    } else {
//                                        CommunityDialogAdapter dataAdapter = new CommunityDialogAdapter(successArrayResponse.getData(), data -> {
//                                            communityID = data.getCommunityID();
//                                            binding.tvCommunity.setText(data.getCommunityName());
//                                            customCommunityDialog.dismiss();
//                                        });
//                                        if (requireActivity() != null) {
//                                            customCommunityDialog = new CustomCommunityDialog(requireActivity(), dataAdapter, successArrayResponse.getData());
//                                        }
//                                        customCommunityDialog.setCanceledOnTouchOutside(false);
//                                    }
//                                } else {
//                                    getActivity().finish();
//                                    Toast.makeText(getContext(), "Please join the Community.", Toast.LENGTH_LONG).show();
//                                }
//                            } else {
//                                Toast.makeText(getContext(), successArrayResponse.getMsg(), Toast.LENGTH_LONG).show();
//                            }
//                        }
//
//                        @Override
//                        public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
//                            Util.firebaseEvent(Constant.APIERROR, getActivity(), Constant.UrlPath.SERVER_URL + apiFlag, LOGIN_DETAIL.getUsername(), LOGIN_DETAIL.getAppuserID(), errorObject.getStatus());
//                            try {
//                                Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
//                            } catch (Exception e) {
//                                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
//                            }
//                        }
//                    });
//                } else {
//                    hideLoader();
//                }
//            }
//        });
//    }
//}