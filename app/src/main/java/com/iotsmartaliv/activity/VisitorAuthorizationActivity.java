package com.iotsmartaliv.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iotsmartaliv.R;
import com.iotsmartaliv.adapter.CommunityDialogAdapter;
import com.iotsmartaliv.adapter.CountryCodeDialogAdapter;
import com.iotsmartaliv.adapter.DeviceMultiSelectDialogAdapter;
import com.iotsmartaliv.adapter.SelectDeviceListAdapter;
import com.iotsmartaliv.adapter.VisitorListAdapter;
import com.iotsmartaliv.apiCalling.listeners.RetrofitListener;
import com.iotsmartaliv.apiCalling.models.Country;
import com.iotsmartaliv.apiCalling.models.ErrorObject;
import com.iotsmartaliv.apiCalling.models.ResponseData;
import com.iotsmartaliv.apiCalling.models.SuccessArrayResponse;
import com.iotsmartaliv.apiCalling.models.SuccessResponse;
import com.iotsmartaliv.apiCalling.retrofit.ApiServiceProvider;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.dialog_box.CustomCommunityDialog;
import com.iotsmartaliv.dialog_box.CustomCountryCodeDialog;
import com.iotsmartaliv.dialog_box.CustomDeviceListDialog;
import com.iotsmartaliv.model.AddVisitor;
import com.iotsmartaliv.utils.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.iotsmartaliv.constants.Constant.LOGIN_DETAIL;

/**
 * This activity class is used for visitor authorization.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2019-01-16
 */
public class VisitorAuthorizationActivity extends AppCompatActivity implements RetrofitListener<SuccessResponse> {
    CustomCommunityDialog customDialog;
    CustomCountryCodeDialog customCountryCodeDialog;
    CustomDeviceListDialog customDeviceListDialog;
    ApiServiceProvider apiServiceProvider;
    @BindView(R.id.community_lay)
    RelativeLayout communityLay;
    @BindView(R.id.device_lay)
    RelativeLayout deviceLay;
    String communityID = null;
    @BindView(R.id.community_name)
    TextView communityName;
    @BindView(R.id.visitor_list)
    RecyclerView visitorList;
    VisitorListAdapter visitorListAdapter;
    ResponseData responseDataDevice;
    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat dateFormatValidation = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm:ss");
    @BindView(R.id.edt_pin_use_limit)
    EditText edtPinUseLimit;
    @BindView(R.id.edt_purpose)
    EditText edtPurpose;
    @BindView(R.id.tvStartDate)
    TextView tvStartDate;
    @BindView(R.id.tvStartTime)
    TextView tvStartTime;
    @BindView(R.id.tvEndDate)
    TextView tvEndDate;
    @BindView(R.id.tvEndTime)
    TextView tvEndTime;
    DeviceMultiSelectDialogAdapter deviceMultiSelectDialogAdapter;
    @BindView(R.id.device_list)
    RecyclerView deviceList;
    SelectDeviceListAdapter selectDeviceListAdapter;
    Country country = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitor_authorization);
        ButterKnife.bind(this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        visitorList.setLayoutManager(layoutManager);
        visitorListAdapter = new VisitorListAdapter(new ArrayList<>());
        visitorList.setAdapter(visitorListAdapter);
        visitorList.addItemDecoration(new DividerItemDecoration(VisitorAuthorizationActivity.this, DividerItemDecoration.VERTICAL));

        selectDeviceListAdapter = new SelectDeviceListAdapter(new ArrayList<>(), () -> deviceMultiSelectDialogAdapter.notifyDataSetChanged());
        deviceList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        deviceList.addItemDecoration(new DividerItemDecoration(VisitorAuthorizationActivity.this, DividerItemDecoration.VERTICAL));
        deviceList.setAdapter(selectDeviceListAdapter);

        apiServiceProvider = ApiServiceProvider.getInstance(this);
        Util.checkInternet(this, new Util.NetworkCheckCallback() {
            @Override
            public void onNetworkCheckComplete(boolean isAvailable) {
                if (isAvailable) {
                    apiServiceProvider.callForListOfCommunity(LOGIN_DETAIL.getAppuserID(), new RetrofitListener<SuccessArrayResponse>() {
                        @Override
                        public void onResponseSuccess(SuccessArrayResponse successArrayResponse, String apiFlag) {
                            if (successArrayResponse.getStatus().equalsIgnoreCase("OK")) {
                                if (successArrayResponse.getData().size() > 0) {
                                    if (successArrayResponse.getData().size() == 1) {
                                        communityLay.setVisibility(View.GONE);
                                        communityID = successArrayResponse.getData().get(0).getCommunityID();
                                        Util.checkInternet(VisitorAuthorizationActivity.this, new Util.NetworkCheckCallback() {
                                            @Override
                                            public void onNetworkCheckComplete(boolean isAvailable) {
                                                if (isAvailable) {
                                                    apiServiceProvider.callForCommunityDeviceList(LOGIN_DETAIL.getAppuserID(), communityID, VisitorAuthorizationActivity.this);

                                                }
                                            }
                                        });
                                    } else {
                                        CommunityDialogAdapter dataAdapter = new CommunityDialogAdapter(successArrayResponse.getData(), data -> {
                                            communityID = data.getCommunityID();
                                            communityName.setText(data.getCommunityName());
                                            Util.checkInternet(VisitorAuthorizationActivity.this, new Util.NetworkCheckCallback() {
                                                @Override
                                                public void onNetworkCheckComplete(boolean isAvailable) {
                                                    if (isAvailable) {
                                                        apiServiceProvider.callForCommunityDeviceList(LOGIN_DETAIL.getAppuserID(), communityID, VisitorAuthorizationActivity.this);
                                                    }
                                                }
                                            });
                                            customDialog.dismiss();
                                        });
                                        customDialog = new CustomCommunityDialog(VisitorAuthorizationActivity.this, dataAdapter, successArrayResponse.getData());
                                        customDialog.setCanceledOnTouchOutside(false);
                                    }
                                } else {
                                    finish();
                                    Toast.makeText(VisitorAuthorizationActivity.this, "Please join the Community.", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(VisitorAuthorizationActivity.this, successArrayResponse.getMsg(), Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                            switch (apiFlag) {
                                case Constant.UrlPath.COMMUNITY_LIST_API:
                                    Util.firebaseEvent(Constant.APIERROR, VisitorAuthorizationActivity.this, Constant.UrlPath.SERVER_URL + apiFlag, LOGIN_DETAIL.getUsername(), LOGIN_DETAIL.getAppuserID(), errorObject.getStatus());

                                    try {
                                        Toast.makeText(VisitorAuthorizationActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
                                    } catch (Exception e) {
                                        Toast.makeText(VisitorAuthorizationActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                                    }
                                    break;
                            }
                        }
                    });
                }
            }
        });
    }

    @OnClick({R.id.community_lay, R.id.device_lay, R.id.rl_visitor_add, R.id.img_back_visitor})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.community_lay:
                customDialog.show();
                break;

            case R.id.device_lay:
                if (customDeviceListDialog != null) {
                    customDeviceListDialog.show();
                } else {
                    Toast.makeText(this, "No Device List.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.img_back_visitor:
                onBackPressed();
                break;
            case R.id.rl_visitor_add:
                if (responseDataDevice != null) {
                    addVisitor();
                } else {
                    Toast.makeText(this, "No data found , Please select community that have device.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void addVisitor() {
        country = null;
        final AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog_add_visitor, null);
        final EditText edt_name = dialogView.findViewById(R.id.edt_name);
        final EditText edt_contact_number = dialogView.findViewById(R.id.edt_contact_number);
        final TextView tv_country_code = dialogView.findViewById(R.id.tv_country_code);
        final TextView edt_license_plate = dialogView.findViewById(R.id.edt_linsence_plate);
        tv_country_code.setText(responseDataDevice.getDefaultCountry());
        for (Country countrya : responseDataDevice.getCountry()) {
            if (countrya.getPhonecode().equalsIgnoreCase(responseDataDevice.getDefaultCountry())) {
                country = countrya;
                break;
            }
        }
        Button buttonSubmit = dialogView.findViewById(R.id.buttonSubmit);
        Button buttonCancel = dialogView.findViewById(R.id.buttonCancel);
        tv_country_code.setOnClickListener(v -> {
            CountryCodeDialogAdapter countryCodeDialogAdapter = new CountryCodeDialogAdapter(responseDataDevice.getCountry(), data -> {
                tv_country_code.setText(data.getPhonecode());
                country = data;
                customCountryCodeDialog.dismiss();
            });
            customCountryCodeDialog = new CustomCountryCodeDialog(VisitorAuthorizationActivity.this, countryCodeDialogAdapter, responseDataDevice.getCountry());
            customCountryCodeDialog.setCanceledOnTouchOutside(false);
            customCountryCodeDialog.show();
        });

        buttonCancel.setOnClickListener(view -> dialogBuilder.dismiss());
        buttonSubmit.setOnClickListener(view -> {
            if (edt_name.getText().toString().trim().isEmpty() || edt_name.getText().toString().equalsIgnoreCase("") ){
                edt_name.setError("Enter Visitor Name.");
                edt_name.requestFocus();
            }else if (edt_contact_number.getText().toString().trim().isEmpty() || edt_contact_number.getText().toString().equalsIgnoreCase("")){
                edt_contact_number.setError("Enter Contact Number.");
                edt_contact_number.requestFocus();
            }else if (edt_license_plate.getText().toString().trim().isEmpty() || edt_license_plate.getText().toString().equalsIgnoreCase("")){
                edt_license_plate.setError("Enter Licence plat");
                edt_license_plate.requestFocus();
            }else {
                visitorListAdapter.addItem(new AddVisitor(edt_name.getText().toString().trim(), tv_country_code.getText().toString().trim(), edt_contact_number.getText().toString().trim(), country.getId(), edt_license_plate.getText().toString().isEmpty() ? "" : edt_license_plate.getText().toString()));
                dialogBuilder.dismiss();
            }

//            if (edt_name.getText().toString().trim().length() > 0) {
//                if (edt_contact_number.getText().toString().trim().length() > 0) {
//                    visitorListAdapter.addItem(new AddVisitor(edt_name.getText().toString().trim(), tv_country_code.getText().toString().trim(), edt_contact_number.getText().toString().trim(), country.getId(), edt_license_plate.getText().toString().isEmpty() ? "" : edt_license_plate.getText().toString()));
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

    @Override
    public void onResponseSuccess(SuccessResponse successDeviceListResponse, String apiFlag) {
        switch (apiFlag) {
            case Constant.UrlPath.COMMUNITY_DEVICE_LIST_API:
                if (successDeviceListResponse.getStatus().equalsIgnoreCase("OK")) {
                    responseDataDevice = successDeviceListResponse.getData();
                    if (successDeviceListResponse.getData().getDevices().size() > 0) {
                        deviceMultiSelectDialogAdapter = new DeviceMultiSelectDialogAdapter(successDeviceListResponse.getData().getDevices());
                        customDeviceListDialog = new CustomDeviceListDialog(VisitorAuthorizationActivity.this, deviceMultiSelectDialogAdapter, mDataset -> {
                            selectDeviceListAdapter.updateItem(mDataset);
                            customDeviceListDialog.setCanceledOnTouchOutside(false);
                        });
                    } else {
                        customDeviceListDialog = null;
                        responseDataDevice = null;
                        selectDeviceListAdapter.updateItem(new ArrayList<>());
                        Toast.makeText(this, "No Device", Toast.LENGTH_LONG).show();
                    }
                } else {
                    customDeviceListDialog = null;
                    responseDataDevice = null;
                    selectDeviceListAdapter.updateItem(new ArrayList<>());
                    Toast.makeText(this, successDeviceListResponse.getMsg(), Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
        switch (apiFlag) {
            case Constant.UrlPath.COMMUNITY_DEVICE_LIST_API:
                Util.firebaseEvent(Constant.APIERROR, VisitorAuthorizationActivity.this, Constant.UrlPath.SERVER_URL + apiFlag, LOGIN_DETAIL.getUsername(), LOGIN_DETAIL.getAppuserID(), errorObject.getStatus());

                try {
                    Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
                }
                customDeviceListDialog = null;
                selectDeviceListAdapter.updateItem(new ArrayList<>());
                break;
        }
    }

    public void clickStartDate(View view) {
        showDatePicker(view);
    }

    public void clickEndDate(View view) {
        showDatePicker(view);
    }

    public void clickStartTime(View view) {
        showTimePicker(view);
    }

    public void clickEndTime(View view) {
        showTimePicker(view);
    }

    public void clickOnSubmitBtn(View view) {
        callApiForSubmitDetail();
    }

    public void showDatePicker(View views) {
        Calendar myCalendar = Calendar.getInstance(TimeZone.getDefault());
        DatePickerDialog datePicker = new DatePickerDialog(this, R.style.TimePickerTheme, (mDatePicker, year, month, dayOfMonth) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth);
            String strDate = format.format(calendar.getTime());
            ((TextView) views).setText(strDate);
        }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        datePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePicker.show();
    }

    public void showTimePicker(View views) {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(this, R.style.TimePickerTheme, (timePicker, selectedHour, selectedMinute) -> {
            mcurrentTime.set(Calendar.HOUR_OF_DAY, selectedHour);
            mcurrentTime.set(Calendar.MINUTE, selectedMinute);
            String formatted = formatTime.format(mcurrentTime.getTime());
            ((TextView) views).setText(formatted);
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    private void callApiForSubmitDetail() {
        if (communityID == null) {
            Toast.makeText(this, "Select community ID.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (deviceMultiSelectDialogAdapter.getSelectItem().size() < 1) {
            Toast.makeText(this, "Please select device.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (edtPurpose.getText().toString().trim().length() < 1) {
            edtPurpose.setError("Please enter purpose.");
            edtPurpose.requestFocus();
            Toast.makeText(this, "Please enter purpose.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (edtPinUseLimit.getText().toString().trim().length() < 1) {
            edtPinUseLimit.setError("Please enter  pin usage limit.");
            edtPinUseLimit.requestFocus();
            Toast.makeText(this, "Please enter pin usage limit.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (tvStartDate.getText().toString().trim().length() < 1) {
            Toast.makeText(this, "Please select start date.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (tvEndDate.getText().toString().trim().length() < 1) {
            Toast.makeText(this, "Please select end date.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (tvStartTime.getText().toString().trim().length() < 1) {
            Toast.makeText(this, "Please select start time.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (tvEndTime.getText().toString().trim().length() < 1) {
            Toast.makeText(this, "Please select end time.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (visitorListAdapter.getVisitors().size() < 1) {
            Toast.makeText(this, "Please add visitor.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (visitorListAdapter.getVisitors().size() < 1) {
            Toast.makeText(this, "Please add visitor.", Toast.LENGTH_SHORT).show();
            return;
        }
        String startTime = tvStartDate.getText().toString() + " " + tvStartTime.getText().toString();
        String endTime = tvEndDate.getText().toString() + " " + tvEndTime.getText().toString();
        try {
            if (dateFormatValidation.parse(endTime).before(dateFormatValidation.parse(startTime))) {
                Toast.makeText(this, "End time should not be less than to start time.", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(this, "Something is wrong to parse the date.", Toast.LENGTH_SHORT).show();
            return;
        }

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("appuser_ID", LOGIN_DETAIL.getAppuserID());
        hashMap.put("pincode_usage_limit", edtPinUseLimit.getText().toString());
        hashMap.put("purpose", edtPurpose.getText().toString());
        hashMap.put("event_start_date", startTime);
        hashMap.put("event_end_date", endTime);
        hashMap.put("community_ID", communityID);
        for (int i = 0; i < visitorListAdapter.getVisitors().size(); i++) {
            AddVisitor addVisitor = visitorListAdapter.getVisitors().get(i);
            hashMap.put("visitors[" + i + "][name]", addVisitor.getVisitorName());
            hashMap.put("visitors[" + i + "][country_code]", addVisitor.getCountryID());
            hashMap.put("visitors[" + i + "][contact]", addVisitor.getCountryCode() + addVisitor.getContactNumber());
        }
        for (int i = 0; i < deviceMultiSelectDialogAdapter.getSelectItem().size(); i++) {
            hashMap.put("device_SNs[" + i + "]", deviceMultiSelectDialogAdapter.getSelectItem().get(i).getDeviceSno());
        }

        Util.checkInternet(VisitorAuthorizationActivity.this, new Util.NetworkCheckCallback() {
            @Override
            public void onNetworkCheckComplete(boolean isAvailable) {
                if (isAvailable) {
                    apiServiceProvider.submitVisitorEvent(hashMap, new RetrofitListener<SuccessResponse>() {
                        @Override
                        public void onResponseSuccess(SuccessResponse sucessRespnse, String apiFlag) {
                            if (sucessRespnse.getStatus().equalsIgnoreCase("OK")) {
                                Toast.makeText(VisitorAuthorizationActivity.this, sucessRespnse.getMsg(), Toast.LENGTH_LONG).show();
                                View dialogView = LayoutInflater.from(VisitorAuthorizationActivity.this).inflate(R.layout.sucess_dialog_layout, null);
                                AlertDialog.Builder builder = new AlertDialog.Builder(VisitorAuthorizationActivity.this);
                                builder.setView(dialogView);
                                Button buttonOk = dialogView.findViewById(R.id.buttonOk);
                                AlertDialog alertDialog = builder.create();
                                alertDialog.setCancelable(false);
                                alertDialog.show();
                                buttonOk.setOnClickListener(v -> {
                                    alertDialog.dismiss();
                                    finish();
                                });
                            } else {
                                Toast.makeText(VisitorAuthorizationActivity.this, sucessRespnse.getMsg(), Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                            Util.firebaseEvent(Constant.APIERROR, VisitorAuthorizationActivity.this, Constant.UrlPath.SERVER_URL + apiFlag, LOGIN_DETAIL.getUsername(), LOGIN_DETAIL.getAppuserID(), errorObject.getStatus());
                            Toast.makeText(VisitorAuthorizationActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();

                        }
                    });
                }
            }
        });
    }
}
