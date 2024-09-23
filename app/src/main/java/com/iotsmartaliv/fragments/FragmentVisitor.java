package com.iotsmartaliv.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.iotsmartaliv.adapter.FragmentVisitorAdapter;
import com.iotsmartaliv.adapter.GroupMultiSelectDialogAdapter;
import com.iotsmartaliv.adapter.ViewGroupListAdapter;
import com.iotsmartaliv.apiAndSocket.listeners.RetrofitListener;
import com.iotsmartaliv.apiAndSocket.models.CountryDataResponse;
import com.iotsmartaliv.apiAndSocket.models.ErrorObject;
import com.iotsmartaliv.apiAndSocket.models.GroupResponse;
import com.iotsmartaliv.apiAndSocket.retrofit.ApiServiceProvider;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.databinding.FragmentVsitorsBinding;
import com.iotsmartaliv.dialog_box.AddVisitorDialog;
import com.iotsmartaliv.dialog_box.AssignGroupDialog;
import com.iotsmartaliv.dialog_box.DeleteConfirmDialog;
import com.iotsmartaliv.dialog_box.EditVisitorDialog;
import com.iotsmartaliv.dialog_box.ViewAssignGroupDialog;
import com.iotsmartaliv.model.VisitorData;
import com.iotsmartaliv.model.VisitorsListDataResponse;
import com.iotsmartaliv.utils.Util;

import java.util.ArrayList;
import java.util.TimeZone;



import static com.iotsmartaliv.constants.Constant.LOGIN_DETAIL;

/**
 * This class is used as
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 30/7/19 :July : 2019 on 18 : 22.
 */
public class FragmentVisitor extends Fragment implements AddVisitorDialog.VisitorAddedRefresh, RetrofitListener<VisitorsListDataResponse> {
//    @BindView(R.id.recyclerView)
//    RecyclerView recyclerView;
//    Unbinder unbinder;
    ApiServiceProvider apiServiceProvider;
    FragmentVisitorAdapter fragmentVisitorAdapter;
//    @BindView(R.id.item_not_found_tv)
    FragmentVsitorsBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
binding = FragmentVsitorsBinding.inflate(inflater,container,false);
        //        View view = inflater.inflate(R.layout.fragment_vsitors, container, false);

        apiServiceProvider = ApiServiceProvider.getInstance(getActivity());
//        if (Util.checkInternet(requireActivity())) {
        Util.checkInternet(requireActivity(), new Util.NetworkCheckCallback() {
            @Override
            public void onNetworkCheckComplete(boolean isAvailable) {
                if (isAvailable) {
                    apiServiceProvider.callForListOfVisitor(LOGIN_DETAIL.getAppuserID(),FragmentVisitor.this);

                }
            }
        });
//        }
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        fragmentVisitorAdapter = new FragmentVisitorAdapter(getContext(), new FragmentVisitorAdapter.VisitorUpdateClick() {
            @Override
            public void visitorEditNotify(VisitorData visitorData) {
                new EditVisitorDialog(getActivity(), visitorData, FragmentVisitor.this).show();
            }

            @Override
            public void visitorDeleteNotify(VisitorData visitorData) {
                new DeleteConfirmDialog(getActivity(), () ->
                        getAssignGroups(visitorData)
                ).show();

            }

            @Override
            public void assignGroupVisitorNotify(VisitorData visitorData) {
//               if (Util.checkInternet(requireActivity())) {
                Util.checkInternet(requireActivity(), new Util.NetworkCheckCallback() {
                    @Override
                    public void onNetworkCheckComplete(boolean isAvailable) {
                        if (isAvailable) {
                            apiServiceProvider.callForListOfGroup(LOGIN_DETAIL.getAppuserID(), new RetrofitListener<GroupResponse>() {
                                @Override
                                public void onResponseSuccess(GroupResponse groupResponse, String apiFlag) {
                                    if (groupResponse.getStatus().equalsIgnoreCase("OK")) {
                                        if (groupResponse.getData().size() > 0) {
                                            new AssignGroupDialog(getActivity(), new GroupMultiSelectDialogAdapter(groupResponse.getData()), groupResponse.getData(), visitorData).show();
                                        } else {
                                            Toast.makeText(getContext(), "Group Not found.", Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        Toast.makeText(getContext(), groupResponse.getMsg(), Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                                    Util.firebaseEvent(Constant.APIERROR, requireActivity(), Constant.UrlPath.SERVER_URL + apiFlag, LOGIN_DETAIL.getUsername(), LOGIN_DETAIL.getAppuserID(), errorObject.getStatus());
                                    Toast.makeText(getContext(), errorObject.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
//                }
            }

            @Override
            public void viewGroupVisitorNotify(VisitorData visitorData) {
//                if (Util.checkInternet(requireActivity())) {
                Util.checkInternet(requireActivity(), new Util.NetworkCheckCallback() {
                    @Override
                    public void onNetworkCheckComplete(boolean isAvailable) {
                        if (isAvailable) {
                            apiServiceProvider.getAssignGroupsOfVisitor(LOGIN_DETAIL.getAppuserID(), visitorData.getVisitorID(), new RetrofitListener<GroupResponse>() {
                                @Override
                                public void onResponseSuccess(GroupResponse sucessRespnse, String apiFlag) {
                                    if (sucessRespnse.getStatus().equalsIgnoreCase("OK")) {
                                        if (sucessRespnse.getData().size() > 0) {
                                            new ViewAssignGroupDialog(getActivity(), new ViewGroupListAdapter(sucessRespnse.getData()), sucessRespnse.getData(), visitorData).show();
                                        } else {
                                            Toast.makeText(getContext(), "Visitor Not Assign.", Toast.LENGTH_SHORT).show();
                                        }

                                    } else {
                                        Toast.makeText(getContext(), sucessRespnse.getMsg(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                                    Util.firebaseEvent(Constant.APIERROR, requireActivity(), Constant.UrlPath.SERVER_URL + apiFlag, LOGIN_DETAIL.getUsername(), LOGIN_DETAIL.getAppuserID(), errorObject.getStatus());
                                    Toast.makeText(getContext(), errorObject.getDeveloperMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }
                });
//                }
            }
        });
        binding.recyclerView.setAdapter(fragmentVisitorAdapter);
binding.floatingActionButton.setOnClickListener(v -> onViewClicked());

        return binding.getRoot();
    }

    public void getAssignGroups(VisitorData visitorData) {
//        if (Util.checkInternet(requireActivity())) {
        Util.checkInternet(requireActivity(), new Util.NetworkCheckCallback() {
            @Override
            public void onNetworkCheckComplete(boolean isAvailable) {
                if (isAvailable) {
                    apiServiceProvider.visitorUpdateAndDelete("delete", visitorData.getVisitID(), LOGIN_DETAIL.getAppuserID(), visitorData.getUvisitorName(), new RetrofitListener<VisitorsListDataResponse>() {
                        @Override
                        public void onResponseSuccess(VisitorsListDataResponse sucessResponse, String apiFlag1) {
                            if (sucessResponse.getStatus().equalsIgnoreCase("Visitor deleted!")) {
                                Toast.makeText(getContext(), "Visitor deleted.", Toast.LENGTH_SHORT).show();
                                visitorAddedRefreshNotify();
                            } else {
                                Toast.makeText(getContext(), sucessResponse.getMsg(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag1) {
                            Util.firebaseEvent(Constant.APIERROR, requireActivity(), Constant.UrlPath.SERVER_URL + apiFlag1, LOGIN_DETAIL.getUsername(), LOGIN_DETAIL.getAppuserID(), errorObject.getStatus());
                            Toast.makeText(getContext(), "Unable to Delete.", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });
//        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //unbinder.unbind();
    }


    public void onViewClicked() {

        TimeZone tz = TimeZone.getDefault();
//        if (Util.checkInternet(requireActivity())) {
        Util.checkInternet(requireActivity(), new Util.NetworkCheckCallback() {
            @Override
            public void onNetworkCheckComplete(boolean isAvailable) {
                apiServiceProvider.callForCountryCodes(tz.getID(), new RetrofitListener<CountryDataResponse>() {
                    @Override
                    public void onResponseSuccess(CountryDataResponse sucessResponse, String apiFlag) {
                        new AddVisitorDialog(getActivity(), FragmentVisitor.this, sucessResponse.getCountryArrayData()).show();
                    }

                    @Override
                    public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                        Util.firebaseEvent(Constant.APIERROR, requireActivity(), Constant.UrlPath.SERVER_URL + apiFlag, LOGIN_DETAIL.getUsername(), LOGIN_DETAIL.getAppuserID(), errorObject.getStatus());
                        Toast.makeText(getContext(), errorObject.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

//        }

    }

    @Override
    public void visitorAddedRefreshNotify() {
//        if (Util.checkInternet(requireActivity())) {
        Util.checkInternet(requireActivity(), new Util.NetworkCheckCallback() {
            @Override
            public void onNetworkCheckComplete(boolean isAvailable) {
                if (isAvailable) {
                    apiServiceProvider.callForListOfVisitor(LOGIN_DETAIL.getAppuserID(), FragmentVisitor.this);
                }
            }
        });


//        }

    }

    @Override
    public void onResponseSuccess(VisitorsListDataResponse visitorsListDataResponse, String apiFlag) {
        switch (apiFlag) {
            case Constant.UrlPath.GET_USER_VISITORS:
                if (visitorsListDataResponse.getStatus().equalsIgnoreCase("OK")) {
                    if (visitorsListDataResponse.getData().size() > 0) {
                        fragmentVisitorAdapter.addItem(visitorsListDataResponse.getData());
                        binding.itemNotFoundTv.setVisibility(View.GONE);
                    } else {
                        fragmentVisitorAdapter.addItem(new ArrayList<>());
                        binding.itemNotFoundTv.setVisibility(View.VISIBLE);
                        //  Toast.makeText(getContext(), "No Visitors found.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    binding.itemNotFoundTv.setVisibility(View.VISIBLE);
                    binding.itemNotFoundTv.setText(visitorsListDataResponse.getMsg());
                    Toast.makeText(getContext(), visitorsListDataResponse.getMsg(), Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
        switch (apiFlag) {
            case Constant.UrlPath.GET_USER_VISITORS:
                Util.firebaseEvent(Constant.APIERROR, requireActivity(), Constant.UrlPath.SERVER_URL + apiFlag, LOGIN_DETAIL.getUsername(), LOGIN_DETAIL.getAppuserID(), errorObject.getStatus());
                try {
                    Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
