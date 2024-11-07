package com.iotsmartaliv.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.iotsmartaliv.adapter.FragmentGroupAdapter;
import com.iotsmartaliv.adapter.ViewVisitorListAdapter;
import com.iotsmartaliv.adapter.VisitorMultiSelectDialogAdapter;
import com.iotsmartaliv.apiAndSocket.listeners.RetrofitListener;
import com.iotsmartaliv.apiAndSocket.models.ErrorObject;
import com.iotsmartaliv.apiAndSocket.models.GroupData;
import com.iotsmartaliv.apiAndSocket.models.GroupResponse;
import com.iotsmartaliv.apiAndSocket.retrofit.ApiServiceProvider;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.databinding.FragmentGroupBinding;
import com.iotsmartaliv.dialog_box.AddGroupDialog;
import com.iotsmartaliv.dialog_box.AssignVisitorDialog;
import com.iotsmartaliv.dialog_box.DeleteConfirmDialog;
import com.iotsmartaliv.dialog_box.EditGroupDialog;
import com.iotsmartaliv.dialog_box.ViewAssignedVisitorDialog;
import com.iotsmartaliv.model.VisitorsListDataResponse;
import com.iotsmartaliv.utils.Util;

import java.util.ArrayList;

import static com.iotsmartaliv.constants.Constant.LOGIN_DETAIL;

/**
 * This class is used as .
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 30/7/19 :July : 2019 on 18 : 23.
 */
public class FragmentGroup extends Fragment implements RetrofitListener<GroupResponse>, AddGroupDialog.GroupAddedRefresh {
//    @BindView(R.id.recyclerView)
//    RecyclerView recyclerView;
//    Unbinder unbinder;

    ApiServiceProvider apiServiceProvider;
    FragmentGroupAdapter fragmentGroupAdapter;
//    @BindView(R.id.item_not_found_tv)
//    TextView itemNotFoundTv;

    FragmentGroupBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentGroupBinding.inflate(inflater,container,false);
//        View view = inflater.inflate(R.layout.fragment_group, container, false);
        apiServiceProvider = ApiServiceProvider.getInstance(getActivity(),false);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        binding.recyclerView.setLayoutManager(mLayoutManager);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());

        fragmentGroupAdapter = new FragmentGroupAdapter(getContext(), new FragmentGroupAdapter.GroupUpdateClick() {
            @Override
            public void groupEditNotify(GroupData groupData) {
                new EditGroupDialog(getActivity(), groupData, FragmentGroup.this).show();
            }

            @Override
            public void groupDeleteNotify(GroupData groupData) {
                new DeleteConfirmDialog(getActivity(), () ->

                        callForGroupUpdate(groupData)
                ).show();
            }

            @Override
            public void groupAssignVisitorNotify(GroupData groupData) {
                    Util.checkInternet(requireActivity(), new Util.NetworkCheckCallback() {
                        @Override
                        public void onNetworkCheckComplete(boolean isAvailable) {
                            if (isAvailable){
                                apiServiceProvider.callForListOfVisitor(LOGIN_DETAIL.getAppuserID(), new RetrofitListener<VisitorsListDataResponse>() {
                                    @Override
                                    public void onResponseSuccess(VisitorsListDataResponse groupResponse, String apiFlag) {
                                        if (groupResponse.getStatus().equalsIgnoreCase("OK")) {
                                            if (groupResponse.getData().size() > 0) {
                                                new AssignVisitorDialog(getActivity(), new VisitorMultiSelectDialogAdapter(groupResponse.getData()), groupResponse.getData(), groupData).show();
                                            } else {
                                                Toast.makeText(getContext(), "Visitor Not Added!", Toast.LENGTH_LONG).show();
                                            }
                                        } else {
                                            Toast.makeText(getContext(), groupResponse.getMsg(), Toast.LENGTH_LONG).show();
                                        }
                                    }

                                    @Override
                                    public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                                        Util.firebaseEvent(Constant.APIERROR, requireActivity(),Constant.UrlPath.SERVER_URL+apiFlag, LOGIN_DETAIL.getUsername(),LOGIN_DETAIL.getAppuserID(),errorObject.getStatus());

                                        Toast.makeText(getContext(), errorObject.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        }
                    });
                }

            @Override
            public void viewVisitorNotify(GroupData groupData) {

                    Util.checkInternet(requireActivity(), new Util.NetworkCheckCallback() {
                        @Override
                        public void onNetworkCheckComplete(boolean isAvailable) {
                            if (isAvailable){
                    apiServiceProvider.getAssignVisitorsInGroup(LOGIN_DETAIL.getAppuserID(), groupData.getVgroupID(), new RetrofitListener<VisitorsListDataResponse>() {
                        @Override
                        public void onResponseSuccess(VisitorsListDataResponse sucessRespnse, String apiFlag) {
                            if (sucessRespnse.getStatus().equalsIgnoreCase("OK")) {
                                if (sucessRespnse.getData().size() > 0) {
                                    new ViewAssignedVisitorDialog(getActivity(), new ViewVisitorListAdapter(sucessRespnse.getData()), sucessRespnse.getData(), groupData).show();
                                } else {
                                    Toast.makeText(getContext(), "Visitor Not Assign.", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(getContext(), sucessRespnse.getMsg(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                            Util.firebaseEvent(Constant.APIERROR, requireActivity(),Constant.UrlPath.SERVER_URL+apiFlag, LOGIN_DETAIL.getUsername(),LOGIN_DETAIL.getAppuserID(),errorObject.getStatus());

                            Toast.makeText(getContext(), errorObject.getDeveloperMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                            }
                        }
                    });
                }


        });
        binding.recyclerView.setAdapter(fragmentGroupAdapter);
        apiServiceProvider.callForListOfGroup(LOGIN_DETAIL.getAppuserID(), this);
        binding.floatingActionButton.setOnClickListener(v-> onViewClicked());
        return binding.getRoot();
    }

    public void callForGroupUpdate(GroupData groupData){
            Util.checkInternet(requireActivity(), new Util.NetworkCheckCallback() {
                @Override
                public void onNetworkCheckComplete(boolean isAvailable) {
                    if (isAvailable){
                        apiServiceProvider.callForGroupUpdateAndDelete("delete", groupData.getVgroupID(), groupData.getGroupName(), new RetrofitListener<GroupResponse>() {
                            @Override
                            public void onResponseSuccess(GroupResponse groupResponse, String apiFlag) {
                                if (groupResponse.getStatus().equalsIgnoreCase("Group deleted!")) {
                                    Toast.makeText(getContext(), "Group deleted.", Toast.LENGTH_SHORT).show();
                                    groupAddedRefreshNotify();
                                } else {
                                    Toast.makeText(getContext(), groupResponse.getMsg(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                                Util.firebaseEvent(Constant.APIERROR, requireActivity(),Constant.UrlPath.SERVER_URL+apiFlag, LOGIN_DETAIL.getUsername(),LOGIN_DETAIL.getAppuserID(),errorObject.getStatus());

                                Toast.makeText(getContext(), "Something Went Wrong.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //unbinder.unbind();
    }


    public void onViewClicked() {
        new AddGroupDialog(getActivity(), this).show();
    }

    @Override
    public void onResponseSuccess(GroupResponse groupResponse, String apiFlag) {
        switch (apiFlag) {
            case Constant.UrlPath.GET_GROUP_LIST:
                if (groupResponse.getStatus().equalsIgnoreCase("OK")) {
                    if (groupResponse.getData().size() > 0) {
                        fragmentGroupAdapter.addItem(groupResponse.getData());
                        binding.itemNotFoundTv.setVisibility(View.GONE);
                    } else {
                        fragmentGroupAdapter.addItem(new ArrayList<>());
                        binding.itemNotFoundTv.setVisibility(View.VISIBLE);
                        //Toast.makeText(getContext(), "Sub Community Not found", Toast.LENGTH_LONG).show();
                    }

                } else {
                    fragmentGroupAdapter.addItem(new ArrayList<>());
                    binding.itemNotFoundTv.setVisibility(View.VISIBLE);
                    binding.itemNotFoundTv.setText(groupResponse.getMsg());
                    Toast.makeText(getContext(), groupResponse.getMsg(), Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
        switch (apiFlag) {
            case Constant.UrlPath.GET_GROUP_LIST:
                Util.firebaseEvent(Constant.APIERROR, requireActivity(),Constant.UrlPath.SERVER_URL+apiFlag, LOGIN_DETAIL.getUsername(),LOGIN_DETAIL.getAppuserID(),errorObject.getStatus());

                try {
                    Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                }
                break;


        }
    }

    @Override
    public void groupAddedRefreshNotify() {
            Util.checkInternet(requireActivity(), new Util.NetworkCheckCallback() {
                @Override
                public void onNetworkCheckComplete(boolean isAvailable) {
                    apiServiceProvider.callForListOfGroup(LOGIN_DETAIL.getAppuserID(), FragmentGroup.this);
                }
            });
    }
}
