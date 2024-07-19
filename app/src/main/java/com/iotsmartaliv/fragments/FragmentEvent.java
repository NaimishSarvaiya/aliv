package com.iotsmartaliv.fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iotsmartaliv.R;
import com.iotsmartaliv.activity.VisitorActivity;
import com.iotsmartaliv.adapter.AddNewVisitorAdapterUncheck;
import com.iotsmartaliv.adapter.CommunityDialogAdapter;
import com.iotsmartaliv.adapter.CountryCodeDialogAdapter;
import com.iotsmartaliv.adapter.DeviceMultiSelectDialogAdapter;
import com.iotsmartaliv.adapter.GroupListAdapter;
import com.iotsmartaliv.adapter.MultiSelectVisitorAdapter;
import com.iotsmartaliv.adapter.RadioSingleAddNewVisitorAdapter;
import com.iotsmartaliv.adapter.SelectDeviceListAdapter;
import com.iotsmartaliv.adapter.SelectedVisitorListAdapter;
import com.iotsmartaliv.adapter.SingleSelectVisitorAdapter;
import com.iotsmartaliv.adapter.SingleSelectedVisitorAdapter;
import com.iotsmartaliv.adapter.VisitorListAdapter;
import com.iotsmartaliv.apiCalling.listeners.RetrofitListener;
import com.iotsmartaliv.apiCalling.models.Country;
import com.iotsmartaliv.apiCalling.models.ErrorObject;
import com.iotsmartaliv.apiCalling.models.GroupData;
import com.iotsmartaliv.apiCalling.models.GroupResponse;
import com.iotsmartaliv.apiCalling.models.ResponseData;
import com.iotsmartaliv.apiCalling.models.SuccessArrayResponse;
import com.iotsmartaliv.apiCalling.models.SuccessResponse;
import com.iotsmartaliv.apiCalling.retrofit.ApiServiceProvider;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.dialog_box.CustomCommunityDialog;
import com.iotsmartaliv.dialog_box.CustomCountryCodeDialog;
import com.iotsmartaliv.dialog_box.CustomDeviceListDialog;
import com.iotsmartaliv.dialog_box.CustomTimePicker;
import com.iotsmartaliv.dialog_box.GroupListDialog;
import com.iotsmartaliv.dialog_box.MultiSelectVisitorDailog;
import com.iotsmartaliv.dialog_box.SingleSelectVisitorDialog;
import com.iotsmartaliv.model.AddVisitor;
import com.iotsmartaliv.model.VisitorData;
import com.iotsmartaliv.model.VisitorsListDataResponse;
import com.iotsmartaliv.utils.CommanUtils;
import com.iotsmartaliv.utils.Util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.iotsmartaliv.constants.Constant.LOGIN_DETAIL;

/**
 * This class is used as .
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 30/7/19 :July : 2019 on 18 : 22.
 */
public class FragmentEvent extends Fragment implements View.OnClickListener, RetrofitListener<SuccessResponse> {

    @BindView(R.id.rl_select_community)
    RelativeLayout rlSelectCommunity;
    @BindView(R.id.cbEnableEnhance)
    CheckBox cbEnableEnhance;
    @BindView(R.id.rl_checkbox)
    RelativeLayout rlCheckbox;
    @BindView(R.id.rl_select_device)
    RelativeLayout rlSelectDevice;
    @BindView(R.id.ed_event_title)
    EditText edEventTitle;
    @BindView(R.id.ed_purpose)
    EditText edPurpose;
    @BindView(R.id.ed_event_location)
    EditText edEventLocation;
    @BindView(R.id.ed_pin_usage_limit)
    EditText edPinUsageLimit;
    @BindView(R.id.tv_limit)
    TextView tvLimit;
    /*@BindView(R.id.ed_scan_time)
    EditText edScanTime;*/
    @BindView(R.id.tvCommunity)
    TextView tvCommunity;
    @BindView(R.id.rl_upper)
    RelativeLayout rlUpper;
    @BindView(R.id.tv_start_date)
    TextView tvStartDate;
    @BindView(R.id.tv_end_date)
    TextView tvEndDate;
    @BindView(R.id.ll_select_date)
    LinearLayout llSelectDate;
    @BindView(R.id.tv_start_time)
    TextView tvStartTime;
    @BindView(R.id.tv_end_time)
    TextView tvEndTime;
    @BindView(R.id.ll_select_time)
    LinearLayout llSelectTime;
    @BindView(R.id.rl_date_time)
    RelativeLayout rlDateTime;
    @BindView(R.id.radioSingleVisitor)
    RadioButton radioSingleVisitor;
    @BindView(R.id.radioGroupOfVisitor)
    RadioButton radioGroupOfVisitor;
    @BindView(R.id.radioVisitor)
    RadioGroup radioVisitor;
    @BindView(R.id.rl_select_group)
    RelativeLayout rlSelectGroup;
    @BindView(R.id.rl_select_visitor)
    RelativeLayout rlSelectVisitor;
    @BindView(R.id.iv_addVisitor)
    ImageView ivAddVisitor;
    @BindView(R.id.rl_add_visitor)
    RelativeLayout rlAddVisitor;
    @BindView(R.id.tvGroupName)
    TextView tvGroupName;
    @BindView(R.id.recyclerView_selectedMultipleVisitor)
    RecyclerView recyclerViewSelectedMultipleVisitor;
    VisitorListAdapter visitorListAdapter;
    Unbinder unbinder;
    @BindView(R.id.visitor_list)
    RecyclerView visitorList;
    @BindView(R.id.rl_device)
    RelativeLayout rlDevice;
    DeviceMultiSelectDialogAdapter deviceDialogAdapter;
    @BindView(R.id.device_list)
    RecyclerView deviceList;
    @BindView(R.id.ll_singleVisitor)
    LinearLayout ll_singleVisitor;
    @BindView(R.id.ll_groupsOfVisitors)
    LinearLayout ll_groupsOfVisitors;
    @BindView(R.id.rl_event)
    RelativeLayout rlEvent;
    Country country;
    @BindView(R.id.tvVisitorName)
    TextView tvVisitorName;
    @BindView(R.id.tvVisitorName_single)
    TextView tvVisitorNameSingle;
    /*@BindView(R.id.ed_pinUsage_limit)
    EditText edPinUsageLimitEvent;*/
    @BindView(R.id.rlSubmitEvent)
    RelativeLayout rlSubmitEvent;
    @BindView(R.id.rl_add_visitor_uncheck)
    RelativeLayout rlAddVisitorUncheck;
    @BindView(R.id.rl_select_visitorSingle)
    RelativeLayout rl_selectVisitorSingle;
    @BindView(R.id.recyclerView_selectedSingleVisitor)
    RecyclerView recyclerViewSelectedSingleVisitor;
    @BindView(R.id.visitor_list_single)
    RecyclerView addNewvisitorListSingle;
    @BindView(R.id.rl_add_visitor_single)
    RelativeLayout rlAddNewVisitorSingle;
    @BindView(R.id.visitor_list_uncheck)
    RecyclerView addNewVisitorListUncheck;

    AddNewVisitorAdapterUncheck addNewVisitorAdapterUncheck;
    RadioSingleAddNewVisitorAdapter radioSingleAddNewVisitorAdapter;
    GroupListDialog groupListDialog;
    MultiSelectVisitorDailog multiSelectVisitorDailog;
    SingleSelectVisitorDialog singleSelectVisitorDialog;
    CustomCountryCodeDialog customCountryCodeDialog;
    CustomDeviceListDialog customDeviceListDialog;
    CustomCommunityDialog customCommunityDialog;
    ApiServiceProvider apiServiceProvider;
    SelectDeviceListAdapter selectDeviceListAdapter;
    SelectedVisitorListAdapter selectedVisitorListAdapter;
    SingleSelectedVisitorAdapter singleSelectedVisitorAdapter;
    ResponseData responseDataDevice;
    GroupData selectedGroup;
    String string, startDate, endDate, communityID;
    DateFormat outputFormat = new SimpleDateFormat("dd.MM.yyyy");
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
    SimpleDateFormat dateFormatValidation = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    String addLicensePlate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event, container, false);
        unbinder = ButterKnife.bind(this, view);
        clickListener();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        visitorList.setLayoutManager(layoutManager);
        visitorListAdapter = new VisitorListAdapter(new ArrayList<>());
        visitorList.setAdapter(visitorListAdapter);
        visitorList.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(getContext());
        addNewvisitorListSingle.setLayoutManager(layoutManager1);
        radioSingleAddNewVisitorAdapter = new RadioSingleAddNewVisitorAdapter(new ArrayList<>());
        addNewvisitorListSingle.setAdapter(radioSingleAddNewVisitorAdapter);
        addNewvisitorListSingle.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getContext());
        addNewVisitorListUncheck.setLayoutManager(layoutManager2);
        addNewVisitorAdapterUncheck = new AddNewVisitorAdapterUncheck(new ArrayList<>());
        addNewVisitorListUncheck.setAdapter(addNewVisitorAdapterUncheck);
        addNewVisitorListUncheck.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        selectedVisitorListAdapter = new SelectedVisitorListAdapter(new ArrayList<>(), null);
        singleSelectedVisitorAdapter = new SingleSelectedVisitorAdapter(new ArrayList<>(), () -> singleSelectedVisitorAdapter.notifyDataSetChanged());
        recyclerViewSelectedSingleVisitor.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewSelectedSingleVisitor.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerViewSelectedSingleVisitor.setAdapter(singleSelectedVisitorAdapter);

        selectDeviceListAdapter = new SelectDeviceListAdapter(new ArrayList<>(), () -> {
            deviceDialogAdapter.notifyDataSetChanged();
            deviceList.setVisibility(View.GONE);
        });
        deviceList.setLayoutManager(new LinearLayoutManager(getContext()));
        deviceList.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        deviceList.setAdapter(selectDeviceListAdapter);


        apiServiceProvider = ApiServiceProvider.getInstance(getContext());
        Util.checkInternet(requireActivity(), new Util.NetworkCheckCallback() {
            @Override
            public void onNetworkCheckComplete(boolean isAvailable) {
                if (isAvailable) {
                    apiServiceProvider.callForListOfCommunity(LOGIN_DETAIL.getAppuserID(), new RetrofitListener<SuccessArrayResponse>() {
                        @Override
                        public void onResponseSuccess(SuccessArrayResponse successArrayResponse, String apiFlag) {
                            if (successArrayResponse.getStatus().equalsIgnoreCase("OK")) {
                                if (successArrayResponse.getData().size() > 0) {
                                    if (successArrayResponse.getData().size() == 1) {
                                        rlSelectCommunity.setVisibility(View.GONE);
                                        communityID = successArrayResponse.getData().get(0).getCommunityID();

                                        Util.checkInternet(requireActivity(), new Util.NetworkCheckCallback() {
                                            @Override
                                            public void onNetworkCheckComplete(boolean isAvailable) {
                                                if (isAvailable) {
                                                    apiServiceProvider.callForCommunityDeviceList(LOGIN_DETAIL.getAppuserID(), communityID, FragmentEvent.this);

                                                }
                                            }
                                        });
                                        if (successArrayResponse.getData().get(0).getVisitorManagement().equalsIgnoreCase("1")) {
                                            rlCheckbox.setVisibility(View.VISIBLE);
                                        } else {
                                            rlCheckbox.setVisibility(View.GONE);
                                        }
                                    } else {
                                        CommunityDialogAdapter dataAdapter = new CommunityDialogAdapter(successArrayResponse.getData(), data -> {
                                            communityID = data.getCommunityID();
                                            tvCommunity.setText(data.getCommunityName());
                                            addLicensePlate = data.getAddLicensePlate();
                                            cbEnableEnhance.setChecked(false);
                                            Util.checkInternet(requireActivity(), new Util.NetworkCheckCallback() {
                                                @Override
                                                public void onNetworkCheckComplete(boolean isAvailable) {
                                                    if (isAvailable) {
                                                        apiServiceProvider.callForCommunityDeviceList(LOGIN_DETAIL.getAppuserID(), communityID, FragmentEvent.this);

                                                    }
                                                }
                                            });
                                            if (data.getVisitorManagement().equalsIgnoreCase("1")) {
                                                rlCheckbox.setVisibility(View.VISIBLE);
                                            } else {
                                                rlCheckbox.setVisibility(View.GONE);
                                            }
                                            customCommunityDialog.dismiss();
                                        });
                                        if (getActivity() != null) {
                                            customCommunityDialog = new CustomCommunityDialog(getActivity(), dataAdapter, successArrayResponse.getData());
                                        }
                                        customCommunityDialog.setCanceledOnTouchOutside(false);
                                    }
                                } else {
                                    getActivity().finish();
                                    Toast.makeText(getContext(), "Please join the Community.", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(getContext(), successArrayResponse.getMsg(), Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                            switch (apiFlag) {
                                case Constant.UrlPath.COMMUNITY_LIST_API:
                                    Util.firebaseEvent(Constant.APIERROR, getActivity(), Constant.UrlPath.SERVER_URL + apiFlag, LOGIN_DETAIL.getUsername(), LOGIN_DETAIL.getAppuserID(), errorObject.getStatus());
                                    try {
                                        Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                                    } catch (Exception e) {
                                        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                                    }
                                    break;
                            }
                        }
                    });
                } else {
                    getActivity().finish();
                }
            }
        });


        cbEnableEnhance.setOnCheckedChangeListener((buttonView, isChecked) -> {
            rlEvent.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            radioVisitor.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            ll_singleVisitor.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            ll_groupsOfVisitors.setVisibility(isChecked ? View.VISIBLE : View.GONE);

            rlDevice.setVisibility(isChecked ? View.GONE : View.VISIBLE);
            rlAddVisitorUncheck.setVisibility(isChecked ? View.GONE : View.VISIBLE);
            rlAddVisitorUncheck.setVisibility(isChecked ? View.GONE : View.VISIBLE);

            if (isChecked) {
                radioVisitor.clearCheck();
                radioSingleVisitor.setChecked(true);
            }

            if (addNewVisitorAdapterUncheck != null) {
                addNewVisitorAdapterUncheck.clearAll();
            }
            if (radioSingleAddNewVisitorAdapter != null) {
                radioSingleAddNewVisitorAdapter.clearAll();
            }
            if (visitorListAdapter != null) {
                visitorListAdapter.clearAll();
            }
        });


        radioVisitor.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.radioSingleVisitor:
                    rlSelectVisitor.setVisibility(View.VISIBLE);
                    rlSelectGroup.setVisibility(View.GONE);
                    ll_singleVisitor.setVisibility(View.VISIBLE);
                    ll_groupsOfVisitors.setVisibility(View.GONE);
                    break;
                case R.id.radioGroupOfVisitor:
                    ll_singleVisitor.setVisibility(View.GONE);
                    ll_groupsOfVisitors.setVisibility(View.VISIBLE);
                    rlSelectVisitor.setVisibility(View.VISIBLE);
                    tvVisitorName.setText("Select Visitor");
                    tvVisitorName.setTextColor(getResources().getColor(R.color.gray));
                    recyclerViewSelectedMultipleVisitor.setVisibility(View.VISIBLE);
                    rlSelectGroup.setVisibility(View.VISIBLE);
                    break;
            }
        });

        return view;
    }

    private void clickListener() {
        tvStartDate.setOnClickListener(this);
        tvStartTime.setOnClickListener(this);
        tvEndDate.setOnClickListener(this);
        tvEndTime.setOnClickListener(this);
        rlSelectCommunity.setOnClickListener(this);
        rlSelectDevice.setOnClickListener(this);
        rlAddVisitor.setOnClickListener(this);
        rlSubmitEvent.setOnClickListener(this);
        rlSelectGroup.setOnClickListener(this);
        rlSelectVisitor.setOnClickListener(this);
        rl_selectVisitorSingle.setOnClickListener(this);
        rlAddNewVisitorSingle.setOnClickListener(this);
        rlAddVisitorUncheck.setOnClickListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_start_date:
                string = "start date";
                showDatePicker();
                break;
            case R.id.tv_end_date:
                string = "end date";
                showDatePicker();
                break;
            case R.id.tv_start_time:
                string = "start time";
                showCustomTimePicker(true);
                break;
            case R.id.tv_end_time:
                string = "end time";
                showCustomTimePicker(false);
                break;
            case R.id.rl_select_community:
                if (customCommunityDialog != null) {
                    customCommunityDialog.show();
                }
                break;

            case R.id.rl_select_device:
                if (communityID != null) {
                    if (customDeviceListDialog != null) {
                        customDeviceListDialog.show();
                    } else {
                        Toast.makeText(getContext(), "No Device List.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Select the community.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.rl_add_visitor:
                if (responseDataDevice != null) {
                    addVisitor();
                } else {
                    Toast.makeText(getContext(), "No data found , Please select community that have device.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.rlSubmitEvent:
                if (cbEnableEnhance.isChecked()) {
                    callApiForSubmitEventEnhanced();
                } else {
                    callApiForSubmitBasicEvent();
                }

                break;
            case R.id.rl_select_group:
                Util.checkInternet(requireActivity(), new Util.NetworkCheckCallback() {
                    @Override
                    public void onNetworkCheckComplete(boolean isAvailable) {
                        if (isAvailable) {
                            apiServiceProvider.callForListOfGroup(LOGIN_DETAIL.getAppuserID(), new RetrofitListener<GroupResponse>() {
                                @Override
                                public void onResponseSuccess(GroupResponse sucessRespnse, String apiFlag) {
                                    GroupListAdapter groupListAdapter = new GroupListAdapter(sucessRespnse.getData(), data -> {
                                        tvGroupName.setText(data.getGroupName());
                                        selectedGroup = data;
                                        if (selectedVisitorListAdapter != null) {
                                            selectedVisitorListAdapter.updateItem(new ArrayList<>());
                                        }
                                        groupListDialog.dismiss();

                                    });
                                    groupListAdapter.addItem(sucessRespnse.getData());
                                    groupListDialog = new GroupListDialog(getActivity(), groupListAdapter, sucessRespnse.getData());
                                    groupListDialog.show();
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

                break;
            case R.id.rl_select_visitor:
                if (selectedGroup != null) {
                    Util.checkInternet(requireActivity(), new Util.NetworkCheckCallback() {
                        @Override
                        public void onNetworkCheckComplete(boolean isAvailable) {
                            if (isAvailable) {
                                apiServiceProvider.getAssignVisitorsInGroup(LOGIN_DETAIL.getAppuserID(), selectedGroup.getVgroupID(), new RetrofitListener<VisitorsListDataResponse>() {
                                    @Override
                                    public void onResponseSuccess(VisitorsListDataResponse sucessRespnse, String apiFlag) {
                                        if (sucessRespnse.getData().size() > 0) {
                                            multiSelectVisitorDailog = new MultiSelectVisitorDailog(getActivity(), new MultiSelectVisitorAdapter(sucessRespnse.getData(), selectedVisitorListAdapter.getVisitors()), sucessRespnse.getData(), new MultiSelectVisitorDailog.SelectItemListener() {
                                                @Override
                                                public void getSelectedItem(List<VisitorData> mDataset) {
                                                    recyclerViewSelectedMultipleVisitor.setVisibility(View.VISIBLE);
                                                    selectedVisitorListAdapter = new SelectedVisitorListAdapter(new ArrayList<>(), () -> selectedVisitorListAdapter.notifyDataSetChanged());
                                                    recyclerViewSelectedMultipleVisitor.setLayoutManager(new LinearLayoutManager(getContext()));
                                                    recyclerViewSelectedMultipleVisitor.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
                                                    recyclerViewSelectedMultipleVisitor.setAdapter(selectedVisitorListAdapter);
                                                    selectedVisitorListAdapter.updateItem(mDataset);
                                                    multiSelectVisitorDailog.setCanceledOnTouchOutside(false);
                                                }
                                            });
                                            multiSelectVisitorDailog.show();
                                        } else {
                                            Toast.makeText(getContext(), "No Visitors assign to Group", Toast.LENGTH_LONG).show();
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

                } else {
                    Toast.makeText(getContext(), "Please Select Group.", Toast.LENGTH_SHORT).show();
                }
                //}
                break;
            case R.id.rl_select_visitorSingle:
                int count = radioSingleAddNewVisitorAdapter.getItemCount() + singleSelectedVisitorAdapter.getItemCount();
                if (count < 1) {
                    Util.checkInternet(requireActivity(), new Util.NetworkCheckCallback() {
                        @Override
                        public void onNetworkCheckComplete(boolean isAvailable) {
                            if (isAvailable) {
                                apiServiceProvider.callForListOfVisitor(LOGIN_DETAIL.getAppuserID(), new RetrofitListener<VisitorsListDataResponse>() {
                                    @Override
                                    public void onResponseSuccess(VisitorsListDataResponse sucessRespnse, String apiFlag) {
                                        SingleSelectVisitorAdapter selectVisitorAdapter = new SingleSelectVisitorAdapter(sucessRespnse.getData(), data -> {
                                            List<VisitorData> visitorData = new ArrayList<>();
                                            visitorData.add(data);
                                            singleSelectedVisitorAdapter.updateItem(visitorData);
                                            singleSelectVisitorDialog.dismiss();
                                        });
                                        selectVisitorAdapter.addItem(sucessRespnse.getData());
                                        singleSelectVisitorDialog = new SingleSelectVisitorDialog(getActivity(), selectVisitorAdapter, sucessRespnse.getData());
                                        singleSelectVisitorDialog.show();
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
                } else {
                    Toast.makeText(getContext(), "You can not add more then one Visitor.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.rl_add_visitor_single:
                if (responseDataDevice != null) {
                    if ((radioSingleAddNewVisitorAdapter.getItemCount() + singleSelectedVisitorAdapter.getItemCount()) < 1) {
                        addNewVisitorSingle();
                    } else {
                        Toast.makeText(getContext(), "You can not add more then one Visitor.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Select Community.", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.rl_add_visitor_uncheck:
                if (responseDataDevice != null) {
                    if (responseDataDevice.getDevices().size() > 0) {
                        addNewVisitorUncheck();
                    } else {
                        Toast.makeText(getContext(), "Community don't have an device.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Select Community.", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

    private void addNewVisitorUncheck() {
        country = null;
        final AlertDialog dialogBuilder = new AlertDialog.Builder(getContext()).create();
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog_add_visitor, null);
        final EditText edt_name = dialogView.findViewById(R.id.edt_name);
        final EditText edt_contact_number = dialogView.findViewById(R.id.edt_contact_number);
        final TextView tv_country_code = dialogView.findViewById(R.id.tv_country_code);
        final TextView edt_license_plate = dialogView.findViewById(R.id.edt_linsence_plate);
        for (Country countrya : responseDataDevice.getCountry()) {
            if (countrya.getId().equalsIgnoreCase(responseDataDevice.getDefaultCountry())) {
                country = countrya;
                tv_country_code.setText(country.getPhonecode());
                break;
            }
        }
        Button buttonSubmit = dialogView.findViewById(R.id.buttonSubmit);
        Button buttonCancel = dialogView.findViewById(R.id.buttonCancel);
        if (addLicensePlate.endsWith("1")) {
            edt_license_plate.setVisibility(View.VISIBLE);
        } else {
            edt_license_plate.setVisibility(View.GONE);
        }
        tv_country_code.setOnClickListener(v -> {
            CountryCodeDialogAdapter countryCodeDialogAdapter = new CountryCodeDialogAdapter(responseDataDevice.getCountry(), data -> {
                tv_country_code.setText(data.getPhonecode());
                country = data;
                customCountryCodeDialog.dismiss();
            });
            customCountryCodeDialog = new CustomCountryCodeDialog(getActivity(), countryCodeDialogAdapter, responseDataDevice.getCountry());
            customCountryCodeDialog.setCanceledOnTouchOutside(false);
            customCountryCodeDialog.show();
        });


        buttonCancel.setOnClickListener(view -> dialogBuilder.dismiss());
        buttonSubmit.setOnClickListener(view -> {

            if (addLicensePlate.endsWith("1")) {

                if (edt_name.getText().toString().trim().isEmpty() || edt_name.getText().toString().equalsIgnoreCase("")) {
                    edt_name.setError("Enter Visitor Name.");
                    edt_name.requestFocus();
                } else if (edt_contact_number.getText().toString().trim().isEmpty() || edt_contact_number.getText().toString().equalsIgnoreCase("")) {
                    edt_contact_number.setError("Enter Contact Number.");
                    edt_contact_number.requestFocus();
                } else if (edt_license_plate.getText().toString().trim().isEmpty() || edt_license_plate.getText().toString().equalsIgnoreCase("")) {
                    edt_license_plate.setError("Enter Licence plat");
                    edt_license_plate.requestFocus();
                } else {
                    addNewVisitorAdapterUncheck.addItem(new AddVisitor(edt_name.getText().toString().trim(), tv_country_code.getText().toString().trim(), edt_contact_number.getText().toString().trim(), country.getId(), edt_license_plate.getText().toString().isEmpty() ? "" : edt_license_plate.getText().toString()));
                    dialogBuilder.dismiss();
                }
            } else {
                if (edt_name.getText().toString().trim().isEmpty() || edt_name.getText().toString().equalsIgnoreCase("")) {
                    edt_name.setError("Enter Visitor Name.");
                    edt_name.requestFocus();
                } else if (edt_contact_number.getText().toString().trim().isEmpty() || edt_contact_number.getText().toString().equalsIgnoreCase("")) {
                    edt_contact_number.setError("Enter Contact Number.");
                    edt_contact_number.requestFocus();
                } else {
                    addNewVisitorAdapterUncheck.addItem(new AddVisitor(edt_name.getText().toString().trim(), tv_country_code.getText().toString().trim(), edt_contact_number.getText().toString().trim(), country.getId(), edt_license_plate.getText().toString().isEmpty() ? "" : edt_license_plate.getText().toString()));
                    dialogBuilder.dismiss();
                }
            }
//            if (edt_name.getText().toString().trim().length() > 0) {
//                if (edt_contact_number.getText().toString().trim().length() > 0) {
//                    addNewVisitorAdapterUncheck.addItem(new AddVisitor(edt_name.getText().toString().trim(), tv_country_code.getText().toString().trim(), edt_contact_number.getText().toString().trim(), country.getId(), edt_license_plate.getText().toString().isEmpty() ? "" : edt_license_plate.getText().toString()));
//                    dialogBuilder.dismiss();
//                } else {
//                    edt_contact_number.setError("Enter Contact Number.");
//                    edt_contact_number.requestFocus();
//                }
//            } else {
//                edt_name.setError("Enter Visitor Name.");
//                edt_name.requestFocus();
//            }
        });
        dialogBuilder.setView(dialogView);
        Window window = dialogBuilder.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialogBuilder.show();
    }

    private void addNewVisitorSingle() {
        country = null;
        final AlertDialog dialogBuilder = new AlertDialog.Builder(getContext()).create();
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog_add_visitor, null);
        final EditText edt_name = dialogView.findViewById(R.id.edt_name);
        final EditText edt_contact_number = dialogView.findViewById(R.id.edt_contact_number);
        final TextView tv_country_code = dialogView.findViewById(R.id.tv_country_code);
        final TextView edt_license_plate = dialogView.findViewById(R.id.edt_linsence_plate);
        for (Country countrya : responseDataDevice.getCountry()) {
            if (countrya.getId().equalsIgnoreCase(responseDataDevice.getDefaultCountry())) {
                country = countrya;
                tv_country_code.setText(country.getPhonecode());
                break;
            }
        }

        Button buttonSubmit = dialogView.findViewById(R.id.buttonSubmit);
        Button buttonCancel = dialogView.findViewById(R.id.buttonCancel);

        if (addLicensePlate.endsWith("1")){
            edt_license_plate.setVisibility(View.VISIBLE);
        }else {
            edt_license_plate.setVisibility(View.GONE);
        }
        tv_country_code.setOnClickListener(v -> {
            CountryCodeDialogAdapter countryCodeDialogAdapter = new CountryCodeDialogAdapter(responseDataDevice.getCountry(), data -> {
                tv_country_code.setText(data.getPhonecode());
                country = data;
                customCountryCodeDialog.dismiss();
            });
            customCountryCodeDialog = new CustomCountryCodeDialog(getActivity(), countryCodeDialogAdapter, responseDataDevice.getCountry());
            customCountryCodeDialog.setCanceledOnTouchOutside(false);
            customCountryCodeDialog.show();
        });

        buttonCancel.setOnClickListener(view -> dialogBuilder.dismiss());
        buttonSubmit.setOnClickListener(view -> {
//            if (edt_name.getText().toString().trim().length() > 0) {
//                if (edt_contact_number.getText().toString().trim().length() > 0) {
//                    radioSingleAddNewVisitorAdapter.addItem(new AddVisitor(edt_name.getText().toString(), tv_country_code.getText().toString(), edt_contact_number.getText().toString(), country.getId(),edt_linsence_plate.getText().toString().isEmpty() ? "" : edt_linsence_plate.getText().toString()));
//                    dialogBuilder.dismiss();
//                } else {
//                    edt_contact_number.setError("Enter Contact Number.");
//                    edt_contact_number.requestFocus();
//                }
//            } else {
//                edt_name.setError("Enter Visitor Name.");
//                edt_name.requestFocus();
//            }
            if (addLicensePlate.endsWith("1")) {
                if (edt_name.getText().toString().trim().isEmpty() || edt_name.getText().toString().equalsIgnoreCase("")) {
                    edt_name.setError("Enter Visitor Name.");
                    edt_name.requestFocus();
                } else if (edt_contact_number.getText().toString().trim().isEmpty() || edt_contact_number.getText().toString().equalsIgnoreCase("")) {
                    edt_contact_number.setError("Enter Contact Number.");
                    edt_contact_number.requestFocus();
                } else if (edt_license_plate.getText().toString().trim().isEmpty() || edt_license_plate.getText().toString().equalsIgnoreCase("")) {
                    edt_license_plate.setError("Enter Licence plat");
                    edt_license_plate.requestFocus();
                } else {
                    radioSingleAddNewVisitorAdapter.addItem(new AddVisitor(edt_name.getText().toString(), tv_country_code.getText().toString(), edt_contact_number.getText().toString(), country.getId(), edt_license_plate.getText().toString().isEmpty() ? "" : edt_license_plate.getText().toString()));
                    dialogBuilder.dismiss();
                }
            }else {
                if (edt_name.getText().toString().trim().isEmpty() || edt_name.getText().toString().equalsIgnoreCase("")) {
                    edt_name.setError("Enter Visitor Name.");
                    edt_name.requestFocus();
                } else if (edt_contact_number.getText().toString().trim().isEmpty() || edt_contact_number.getText().toString().equalsIgnoreCase("")) {
                    edt_contact_number.setError("Enter Contact Number.");
                    edt_contact_number.requestFocus();
                } else {
                    radioSingleAddNewVisitorAdapter.addItem(new AddVisitor(edt_name.getText().toString(), tv_country_code.getText().toString(), edt_contact_number.getText().toString(), country.getId(), edt_license_plate.getText().toString().isEmpty() ? "" : edt_license_plate.getText().toString()));
                    dialogBuilder.dismiss();
                }
            }

        });
        dialogBuilder.setView(dialogView);
        Window window = dialogBuilder.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialogBuilder.show();
    }

    private void callApiForSubmitEventEnhanced() {
        if (communityID == null) {
            Toast.makeText(getContext(), "Select Community ID.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (edEventTitle.getText().toString().trim().length() < 1) {
            edEventTitle.setError("Please enter event title.");
            edEventTitle.requestFocus();
            Toast.makeText(getContext(), "Please enter event title.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (edEventLocation.getText().toString().trim().length() < 1) {
            edEventLocation.setError("Please event location.");
            edEventLocation.requestFocus();
            Toast.makeText(getContext(), "Please enter event location.", Toast.LENGTH_SHORT).show();
            return;
        }
     /*   if (edPinUsageLimitEvent.getText().toString().trim().length() < 1) {
            edPinUsageLimitEvent.setError("Please enter pin usage limit.");
            edPinUsageLimitEvent.requestFocus();
            Toast.makeText(getContext(), "Please enter pin usage limit.", Toast.LENGTH_SHORT).show();
            return;
        }*/
       /* if (edScanTime.getText().toString().trim().length() < 1) {
            edScanTime.setError("Please enter scan time.");
            edScanTime.requestFocus();
            Toast.makeText(getContext(), "Please enter scan time.", Toast.LENGTH_SHORT).show();
            return;
        }*/
        if (tvStartDate.getText().toString().trim().length() < 1) {
            Toast.makeText(getContext(), "Please select start date.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (tvEndDate.getText().toString().trim().length() < 1) {
            Toast.makeText(getContext(), "Please select end date.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (tvStartTime.getText().toString().trim().length() < 1) {
            Toast.makeText(getContext(), "Please select start time.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (tvEndTime.getText().toString().trim().length() < 1) {
            Toast.makeText(getContext(), "Please select end time.", Toast.LENGTH_SHORT).show();
            return;
        }

        String startTime1 = startDate + " " + CommanUtils.convert12To24Time(tvStartTime.getText().toString());
        String endTime1 = endDate + " " + CommanUtils.convert12To24Time(tvEndTime.getText().toString());
        try {
            if (dateFormatValidation.parse(endTime1).before(dateFormatValidation.parse(startTime1))) {
                Toast.makeText(getContext(), "End time should not be less than to start time.", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Something is wrong to parse the date.", Toast.LENGTH_SHORT).show();
            return;
        }
        boolean isSingleVisitor = radioSingleVisitor.isChecked();

        if (isSingleVisitor) {
            int count = radioSingleAddNewVisitorAdapter.getItemCount() + singleSelectedVisitorAdapter.getItemCount();
            if (count != 1) {
                Toast.makeText(getContext(), "Please select or add visitor.", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            int count = selectedVisitorListAdapter.getVisitors().size() + visitorListAdapter.getVisitors().size();
            if (count < 1) {
                Toast.makeText(getContext(), "Please select or add visitor.", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("appuser_ID", LOGIN_DETAIL.getAppuserID());
        hashMap.put("community_ID", communityID);
        hashMap.put("event_title", edEventTitle.getText().toString());
        hashMap.put("event_location", edEventLocation.getText().toString());
        //   hashMap.put("pincode_use_limit", edPinUsageLimitEvent.getText().toString());
        //   hashMap.put("scan_time", edScanTime.getText().toString());
        hashMap.put("event_start_date", startTime1);
        hashMap.put("event_end_date", endTime1);


        hashMap.put("visitorOption", isSingleVisitor ? "'0'" : "1");
        if (isSingleVisitor) {
        /*    ArrayList<String> visitorDataList = new ArrayList<>();
            for (VisitorData visitorData : singleSelectedVisitorAdapter.getVisitors()) {
                visitorDataList.add(visitorData.getVisitorID());
            }*/

            for (int i = 0; i < singleSelectedVisitorAdapter.getVisitors().size(); i++) {
                VisitorData visitorData = singleSelectedVisitorAdapter.getVisitors().get(i);
                hashMap.put("visitorsids[" + i + "]", visitorData.getVisitorID());
            }

            for (int i = 0; i < radioSingleAddNewVisitorAdapter.getVisitors().size(); i++) {
                AddVisitor addVisitor = radioSingleAddNewVisitorAdapter.getVisitors().get(i);
                hashMap.put("visitors[" + i + "][name]", addVisitor.getVisitorName());
                hashMap.put("visitors[" + i + "][country_code]", addVisitor.getCountryID());
                hashMap.put("visitors[" + i + "][contact]", addVisitor.getCountryCode() + addVisitor.getContactNumber());
                hashMap.put("visitors[" + i + "][license_plate]", addVisitor.getLicensePlate());
            }
        } else {
            if (selectedGroup != null) {
                hashMap.put("group_ID", selectedGroup.getVgroupID());
            }
         /*   ArrayList<String> visitorDataList = new ArrayList<>();
            for (VisitorData visitorData : selectedVisitorListAdapter.getVisitors()) {
                visitorDataList.add(visitorData.getVisitorID());
            }
            hashMap.put("visitorsids", visitorDataList);
*/
            for (int i = 0; i < selectedVisitorListAdapter.getVisitors().size(); i++) {
                VisitorData visitorData = selectedVisitorListAdapter.getVisitors().get(i);
                hashMap.put("visitorsids[" + i + "]", visitorData.getVisitorID());
            }

            for (int i = 0; i < visitorListAdapter.getVisitors().size(); i++) {
                AddVisitor addVisitor = visitorListAdapter.getVisitors().get(i);
                hashMap.put("visitors[" + i + "][name]", addVisitor.getVisitorName());
                hashMap.put("visitors[" + i + "][country_code]", addVisitor.getCountryID());
                hashMap.put("visitors[" + i + "][contact]", addVisitor.getCountryCode() + addVisitor.getContactNumber());
                hashMap.put("visitors[" + i + "][license_plate]", addVisitor.getLicensePlate());
            }

        }

        Util.checkInternet(requireActivity(), new Util.NetworkCheckCallback() {
            @Override
            public void onNetworkCheckComplete(boolean isAvailable) {
                if (isAvailable) {
                    apiServiceProvider.submitVisitorEnhanceEvent(hashMap, new RetrofitListener<SuccessResponse>() {
                        @Override
                        public void onResponseSuccess(SuccessResponse sucessRespnse, String apiFlag) {
                            if (sucessRespnse.getStatus().equalsIgnoreCase("OK")) {
//                                    Toast.makeText(getContext(), sucessRespnse.getMsg(), Toast.LENGTH_LONG).show();
                                View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.sucess_dialog_layout, null);
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setView(dialogView);
                                Button buttonOk = dialogView.findViewById(R.id.buttonOk);
                                AlertDialog alertDialog = builder.create();
                                alertDialog.setCancelable(false);
                                alertDialog.show();
                                buttonOk.setOnClickListener(v -> {
                                    alertDialog.dismiss();
                                    getActivity().finish();
                                    startActivity(new Intent(getContext(), VisitorActivity.class));
                                });
                            } else {
                                Toast.makeText(getContext(), sucessRespnse.getMsg(), Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                            Util.firebaseEvent(Constant.APIERROR, requireActivity(), Constant.UrlPath.SERVER_URL + apiFlag, LOGIN_DETAIL.getUsername(), LOGIN_DETAIL.getAppuserID(), errorObject.getStatus());

                            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();

                        }
                    });

                }
            }
        });
    }

    private void callApiForSubmitBasicEvent() {
        if (communityID == null) {
            Toast.makeText(getContext(), "Select community ID.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (deviceDialogAdapter != null && deviceDialogAdapter.getSelectItem() != null) {
            if (deviceDialogAdapter.getSelectItem().isEmpty()) {
                Toast.makeText(getContext(), "Please select device.", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if (edPurpose.getText().toString().trim().length() < 1) {
            edPurpose.setError("Please enter purpose.");
            edPurpose.requestFocus();
            Toast.makeText(getContext(), "Please enter purpose.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (edPinUsageLimit.getText().toString().trim().length() < 1) {
            edPinUsageLimit.setError("Please enter  pin usage limit.");
            edPinUsageLimit.requestFocus();
            Toast.makeText(getContext(), "Please enter pin usage limit.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (tvStartDate.getText().toString().trim().length() < 1) {
            Toast.makeText(getContext(), "Please select start date.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (tvEndDate.getText().toString().trim().length() < 1) {
            Toast.makeText(getContext(), "Please select end date.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (tvStartTime.getText().toString().trim().length() < 1) {
            Toast.makeText(getContext(), "Please select start time.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (tvEndTime.getText().toString().trim().length() < 1) {
            Toast.makeText(getContext(), "Please select end time.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (addNewVisitorAdapterUncheck.getVisitors().size() < 1) {
            Toast.makeText(getContext(), "Please add visitor.", Toast.LENGTH_SHORT).show();
            return;
        }

        String startTime1 = startDate + " " + CommanUtils.convert12To24Time(tvStartTime.getText().toString());
        String endTime1 = endDate + " " + CommanUtils.convert12To24Time(tvEndTime.getText().toString());
        try {
            if (dateFormatValidation.parse(endTime1).before(dateFormatValidation.parse(startTime1))) {
                Toast.makeText(getContext(), "End time should not be less than to start time.", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Something is wrong to parse the date.", Toast.LENGTH_SHORT).show();
            return;
        }


        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("appuser_ID", LOGIN_DETAIL.getAppuserID());
        hashMap.put("pincode_usage_limit", edPinUsageLimit.getText().toString());
        hashMap.put("purpose", edPurpose.getText().toString());
        hashMap.put("event_start_date", startTime1);
        hashMap.put("event_end_date", endTime1);
        hashMap.put("community_ID", communityID);
        for (int i = 0; i < addNewVisitorAdapterUncheck.getVisitors().size(); i++) {
            AddVisitor addVisitor = addNewVisitorAdapterUncheck.getVisitors().get(i);
            hashMap.put("visitors[" + i + "][name]", addVisitor.getVisitorName());
            hashMap.put("visitors[" + i + "][country_code]", addVisitor.getCountryID());
            hashMap.put("visitors[" + i + "][contact]", addVisitor.getCountryCode() + addVisitor.getContactNumber());
            hashMap.put("visitors[" + i + "][contact]", addVisitor.getCountryCode() + addVisitor.getContactNumber());
            hashMap.put("visitors[" + i + "][license_plate]", addVisitor.getLicensePlate());
        }
        for (int i = 0; i < deviceDialogAdapter.getSelectItem().size(); i++) {
            hashMap.put("device_SNs[" + i + "]", deviceDialogAdapter.getSelectItem().get(i).getDeviceSno());
        }
        Util.checkInternet(requireActivity(), new Util.NetworkCheckCallback() {
            @Override
            public void onNetworkCheckComplete(boolean isAvailable) {
                apiServiceProvider.submitVisitorEvent(hashMap, new RetrofitListener<SuccessResponse>() {
                    @Override
                    public void onResponseSuccess(SuccessResponse successResponse, String apiFlag) {
                        if (successResponse.getStatus().equalsIgnoreCase("OK")) {
//                            Toast.makeText(getContext(), successResponse.getMsg(), Toast.LENGTH_LONG).show();
                            View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.sucess_dialog_layout, null);
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setView(dialogView);
                            Button buttonOk = dialogView.findViewById(R.id.buttonOk);
                            AlertDialog alertDialog = builder.create();
                            alertDialog.setCancelable(false);
                            alertDialog.show();
                            buttonOk.setOnClickListener(v -> {
                                alertDialog.dismiss();
                                getActivity().finish();
                                startActivity(new Intent(getContext(), VisitorActivity.class));
                            });
                        } else {
                            Toast.makeText(getContext(), successResponse.getMsg(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                        Util.firebaseEvent(Constant.APIERROR, requireActivity(), Constant.UrlPath.SERVER_URL + apiFlag, LOGIN_DETAIL.getUsername(), LOGIN_DETAIL.getAppuserID(), errorObject.getStatus());

                        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();

                    }
                });

            }
        });
    }

    private void addVisitor() {
        country = null;
        final AlertDialog dialogBuilder = new AlertDialog.Builder(getContext()).create();
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog_add_visitor, null);
        final EditText edt_name = dialogView.findViewById(R.id.edt_name);
        final EditText edt_contact_number = dialogView.findViewById(R.id.edt_contact_number);
        final TextView tv_country_code = dialogView.findViewById(R.id.tv_country_code);
        final TextView edt_license_plate = dialogView.findViewById(R.id.edt_linsence_plate);
        for (Country countrya : responseDataDevice.getCountry()) {
            if (countrya.getId().equalsIgnoreCase(responseDataDevice.getDefaultCountry())) {
                country = countrya;
                tv_country_code.setText(country.getPhonecode());
                break;
            }
        }
        Button buttonSubmit = dialogView.findViewById(R.id.buttonSubmit);
        Button buttonCancel = dialogView.findViewById(R.id.buttonCancel);
        if (addLicensePlate.endsWith("1")){
            edt_license_plate.setVisibility(View.VISIBLE);
        }else {
            edt_license_plate.setVisibility(View.GONE);
        }
        tv_country_code.setOnClickListener(v -> {
            CountryCodeDialogAdapter countryCodeDialogAdapter = new CountryCodeDialogAdapter(responseDataDevice.getCountry(), data -> {
                tv_country_code.setText(data.getPhonecode());
                country = data;
                customCountryCodeDialog.dismiss();
            });
            customCountryCodeDialog = new CustomCountryCodeDialog(getActivity(), countryCodeDialogAdapter, responseDataDevice.getCountry());
            customCountryCodeDialog.setCanceledOnTouchOutside(false);
            customCountryCodeDialog.show();
        });

        buttonCancel.setOnClickListener(view -> dialogBuilder.dismiss());
        buttonSubmit.setOnClickListener(view -> {
//            if (edt_name.getText().toString().trim().length() > 0) {
//                if (edt_contact_number.getText().toString().trim().length() > 0) {
//                    visitorListAdapter.addItem(new AddVisitor(edt_name.getText().toString(), tv_country_code.getText().toString(), edt_contact_number.getText().toString(), country.getId(), edt_linsence_plate.getText().toString().isEmpty() ? "" : edt_linsence_plate.getText().toString()));
//                    dialogBuilder.dismiss();
//                } else {
//                    edt_contact_number.setError("Enter Contact Number.");
//                    edt_contact_number.requestFocus();
//                }
//            } else {
//                edt_name.setError("Enter Visitor Name.");
//                edt_name.requestFocus();
//            }

            if (addLicensePlate.endsWith("1")) {
                if (edt_name.getText().toString().trim().isEmpty() || edt_name.getText().toString().equalsIgnoreCase("")) {
                    edt_name.setError("Enter Visitor Name.");
                    edt_name.requestFocus();
                } else if (edt_contact_number.getText().toString().trim().isEmpty() || edt_contact_number.getText().toString().equalsIgnoreCase("")) {
                    edt_contact_number.setError("Enter Contact Number.");
                    edt_contact_number.requestFocus();
                } else if (edt_license_plate.getText().toString().trim().isEmpty() || edt_license_plate.getText().toString().equalsIgnoreCase("")) {
                    edt_license_plate.setError("Enter Licence plat");
                    edt_license_plate.requestFocus();
                } else {
                    visitorListAdapter.addItem(new AddVisitor(edt_name.getText().toString(), tv_country_code.getText().toString(), edt_contact_number.getText().toString(), country.getId(), edt_license_plate.getText().toString().isEmpty() ? "" : edt_license_plate.getText().toString()));
                    dialogBuilder.dismiss();
                }
            }else {
                if (edt_name.getText().toString().trim().isEmpty() || edt_name.getText().toString().equalsIgnoreCase("")) {
                    edt_name.setError("Enter Visitor Name.");
                    edt_name.requestFocus();
                } else if (edt_contact_number.getText().toString().trim().isEmpty() || edt_contact_number.getText().toString().equalsIgnoreCase("")) {
                    edt_contact_number.setError("Enter Contact Number.");
                    edt_contact_number.requestFocus();
                } else {
                    visitorListAdapter.addItem(new AddVisitor(edt_name.getText().toString(), tv_country_code.getText().toString(), edt_contact_number.getText().toString(), country.getId(), edt_license_plate.getText().toString().isEmpty() ? "" : edt_license_plate.getText().toString()));
                    dialogBuilder.dismiss();
                }
            }
        });
        dialogBuilder.setView(dialogView);
        Window window = dialogBuilder.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialogBuilder.show();
    }

    public void showDatePicker() {
        Calendar myCalendar = Calendar.getInstance(TimeZone.getDefault());
        DatePickerDialog datePicker = new DatePickerDialog(getContext(), R.style.TimePickerTheme, (mDatePicker, year, month, dayOfMonth) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth);
            String strDate = outputFormat.format(calendar.getTime());
            if (string.equalsIgnoreCase("start date")) {
                startDate = dateFormat.format(calendar.getTime());
                tvStartDate.setText(strDate);
            } else if (string.equalsIgnoreCase("end date")) {
                endDate = dateFormat.format(calendar.getTime());
                tvEndDate.setText(strDate);
            }

        }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        datePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePicker.show();
    }

    private void showCustomTimePicker(boolean isStart) {
        new CustomTimePicker(getActivity(), time -> {
            if (isStart) {
                tvStartTime.setText(time);
                showCustomTimePicker(false);
            } else {
                tvEndTime.setText(time);
            }

        }, isStart ? "Start Time" : "End Time").show();

    }


    @Override
    public void onResponseSuccess(SuccessResponse successDeviceListResponse, String apiFlag) {
        switch (apiFlag) {
            case Constant.UrlPath.COMMUNITY_DEVICE_LIST_API:
                if (successDeviceListResponse.getStatus().equalsIgnoreCase("OK")) {

                    responseDataDevice = successDeviceListResponse.getData();
                    if (successDeviceListResponse.getData().getDevices().size() > 0) {
                        deviceDialogAdapter = new DeviceMultiSelectDialogAdapter(successDeviceListResponse.getData().getDevices());
                        customDeviceListDialog = new CustomDeviceListDialog(getActivity(), deviceDialogAdapter, mDataset -> {
                            selectDeviceListAdapter.updateItem(mDataset);
                            if (mDataset.size() > 0) {
                                deviceList.setVisibility(View.VISIBLE);
                            } else deviceList.setVisibility(View.GONE);
                            customDeviceListDialog.setCanceledOnTouchOutside(false);
                        });
                    } else {
                        customDeviceListDialog = null;
                        //        responseDataDevice = null;
                        deviceList.setVisibility(View.GONE);
                        selectDeviceListAdapter.updateItem(new ArrayList<>());
                        Toast.makeText(getContext(), "No Device", Toast.LENGTH_LONG).show();
                    }
                } else {
                    customDeviceListDialog = null;
                    responseDataDevice = null;
                    selectDeviceListAdapter.updateItem(new ArrayList<>());
                    Toast.makeText(getContext(), successDeviceListResponse.getMsg(), Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
        switch (apiFlag) {
            case Constant.UrlPath.COMMUNITY_DEVICE_LIST_API:
                Util.firebaseEvent(Constant.APIERROR, requireActivity(), Constant.UrlPath.SERVER_URL + apiFlag, LOGIN_DETAIL.getUsername(), LOGIN_DETAIL.getAppuserID(), errorObject.getStatus());

                try {
                    Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                }
                customDeviceListDialog = null;
                selectDeviceListAdapter.updateItem(new ArrayList<>());
                break;
        }
    }
}
