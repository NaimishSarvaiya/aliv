package com.iotsmartaliv.fragments.instructor;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iotsmartaliv.R;
import com.iotsmartaliv.activity.InstructorActivity;
import com.iotsmartaliv.adapter.CommunityDialogAdapter;
import com.iotsmartaliv.adapter.CountryCodeDialogAdapter;
import com.iotsmartaliv.adapter.DeviceMultiSelectDialogAdapter;
import com.iotsmartaliv.apiAndSocket.listeners.RetrofitListener;
import com.iotsmartaliv.apiAndSocket.models.Country;
import com.iotsmartaliv.apiAndSocket.models.ErrorObject;
import com.iotsmartaliv.apiAndSocket.models.InstructorInfoResponse;
import com.iotsmartaliv.apiAndSocket.models.ResponseData;
import com.iotsmartaliv.apiAndSocket.models.SuccessArrayResponse;
import com.iotsmartaliv.apiAndSocket.models.SuccessResponse;
import com.iotsmartaliv.apiAndSocket.retrofit.ApiServiceProvider;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.databinding.AddNewFragmentBinding;
import com.iotsmartaliv.dialog_box.CustomCommunityDialog;
import com.iotsmartaliv.dialog_box.CustomCountryCodeDialog;
import com.iotsmartaliv.dialog_box.CustomTimePicker;
import com.iotsmartaliv.utils.CommanUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;


//import butterknife.Unbinder;

import static com.iotsmartaliv.constants.Constant.LOGIN_DETAIL;
import static com.iotsmartaliv.constants.Constant.hideKeyBoard;

/**
 * This class is used as .
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 22/7/19 :July.
 */
public class AddNewInstructorFragment extends Fragment implements View.OnClickListener, RetrofitListener<SuccessResponse> {
    CustomCommunityDialog customDialog;
    CustomCountryCodeDialog customCountryCodeDialog;
    RelativeLayout rl_submitInstructor;
    TextView tvStartDate, tvEndDate, tvStartTime, tvEndTime;
    DateFormat outputFormat = new SimpleDateFormat("dd.MM.yyyy");
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
    String communityName;
    DeviceMultiSelectDialogAdapter deviceMultiSelectDialogAdapter;
    TextView tv_community, tv_countryCode;
    String activity, company, instructorName, instructorMobileNumber, countryCode, startDate, endDate;
    ResponseData responseDataDevice;
    String communityID = null;
    ApiServiceProvider apiServiceProvider;
    RelativeLayout rlSelectCommunity;
    Country country = null;
    CheckBox checkboxSunday, checkboxMonday, checkboxTuesday, checkboxWednesday, checkboxThursday, checkboxFriday, checkboxSaturday;
    EditText edInstructorMobileNumber, edNameOfInstructor, edActivity, edCompany;
    ArrayList<Integer> selectedDaysList = new ArrayList<>();
    SimpleDateFormat dateFormatValidation = new SimpleDateFormat("hh:mm a");
    private String TAG = "AddNewInstructorFragment";

    AddNewFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = AddNewFragmentBinding.inflate(inflater,container,false);
        View view = binding.getRoot();

        initView(view);
        clickListener();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        apiServiceProvider = ApiServiceProvider.getInstance(getContext());
        apiServiceProvider.callForListOfCommunity(LOGIN_DETAIL.getAppuserID(), new RetrofitListener<SuccessArrayResponse>() {
            @Override
            public void onResponseSuccess(SuccessArrayResponse successArrayResponse, String apiFlag) {
                if (successArrayResponse.getStatus().equalsIgnoreCase("OK")) {
                    if (successArrayResponse.getData().size() > 0) {
                        if (successArrayResponse.getData().size() == 1) {
                            rlSelectCommunity.setVisibility(View.GONE);
                            communityID = successArrayResponse.getData().get(0).getCommunityID();
//                            tv_community.setText(successArrayResponse.getData().get(0).getCommunityName());
                            Log.d("AddFragment", "onResponseSuccess: " + communityID);
                            apiServiceProvider.callForCommunityDeviceList(LOGIN_DETAIL.getAppuserID(), communityID, AddNewInstructorFragment.this);
                        } else {
                            edInstructorMobileNumber.setPadding(20, 0, 0, 0);
                            tv_countryCode.setVisibility(View.GONE);
                            CommunityDialogAdapter dataAdapter = new CommunityDialogAdapter(successArrayResponse.getData(), data -> {
                                communityID = data.getCommunityID();
                                tv_community.setText(data.getCommunityName());
                                apiServiceProvider.callForCommunityDeviceList(LOGIN_DETAIL.getAppuserID(), communityID, AddNewInstructorFragment.this);
                                customDialog.dismiss();
                            });
                            customDialog = new CustomCommunityDialog(getActivity(), dataAdapter, successArrayResponse.getData());
                            customDialog.setCanceledOnTouchOutside(false);
                        }
                    } else {
                        getActivity().finish();
                        Toast.makeText(getContext(), "Please join the Community.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    getActivity().finish();
                    Toast.makeText(getContext(), successArrayResponse.getMsg(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                switch (apiFlag) {
                    case Constant.UrlPath.COMMUNITY_LIST_API:
                        try {
                            Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                        }
                        break;
                }
            }
        });


        return binding.getRoot();
    }

    private void clickListener() {
        tvStartDate.setOnClickListener(this);
        tvEndDate.setOnClickListener(this);
        tvStartTime.setOnClickListener(this);
        tvEndTime.setOnClickListener(this);
        rlSelectCommunity.setOnClickListener(this);
        rl_submitInstructor.setOnClickListener(this);
    }

    private void initView(View view) {
        tvStartDate = view.findViewById(R.id.tv_start_date);
        tvEndDate = view.findViewById(R.id.tv_end_date);
        tvStartTime = view.findViewById(R.id.tv_start_time);
        tvEndTime = view.findViewById(R.id.tv_end_time);
        edInstructorMobileNumber = view.findViewById(R.id.ed_instructor_mobile_number);
        edNameOfInstructor = view.findViewById(R.id.ed_name_of_instructor);
        edActivity = view.findViewById(R.id.ed_activity);
        edCompany = view.findViewById(R.id.ed_company);
        checkboxSunday = view.findViewById(R.id.checkbox_sunday);
        checkboxMonday = view.findViewById(R.id.checkbox_monday);
        checkboxTuesday = view.findViewById(R.id.checkbox_tuesday);
        checkboxWednesday = view.findViewById(R.id.checkbox_wednesday);
        checkboxThursday = view.findViewById(R.id.checkbox_thursday);
        checkboxFriday = view.findViewById(R.id.checkbox_friday);
        checkboxSaturday = view.findViewById(R.id.checkbox_saturday);
        rlSelectCommunity = view.findViewById(R.id.rl_select_community);
        tv_community = view.findViewById(R.id.tv_community);
        rl_submitInstructor = view.findViewById(R.id.rl_submit_instructor);
        tv_countryCode = view.findViewById(R.id.tv_countryCode);
        edInstructorMobileNumber.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                if (communityID != null) {
                    if (edInstructorMobileNumber.getText().toString().trim().length() > 0) {
                        // code to execute when EditText loses focus
                        if (country == null) {
                            return;
                        }


                        apiServiceProvider.callAPIForInstructorInfo(country.getPhonecode() + edInstructorMobileNumber.getText().toString(), communityID, new RetrofitListener<InstructorInfoResponse>() {
                            @Override
                            public void onResponseSuccess(InstructorInfoResponse sucessRespnse, String apiFlag) {
                                if (sucessRespnse.getStatus().equalsIgnoreCase("OK")) {
                                    edCompany.setText(sucessRespnse.getData().getInstructorCompany());
                                    edNameOfInstructor.setText(sucessRespnse.getData().getVisitorName());
                                } else {
                                    edCompany.setText("");
                                    edNameOfInstructor.setText("");
                                }
                            }

                            @Override
                            public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                                edCompany.setText("");
                                edNameOfInstructor.setText("");
                            }
                        });
                    }
                } else {
                    Toast.makeText(getContext(), "Select Community Id.", Toast.LENGTH_SHORT).show();
                }
            } else {
                if (communityID == null) {
                    Toast.makeText(getContext(), "Select Community Id.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_start_date:
                showDatePicker(true);
                break;
            case R.id.tv_end_date:
                showDatePicker(false);
                break;
            case R.id.tv_start_time:
                showCustomTimePicker(true);
                break;
            case R.id.tv_end_time:
                showCustomTimePicker(false);
                break;
            case R.id.rl_select_community:
                if (customDialog != null) {
                    customDialog.show();
                }
                break;
            case R.id.rl_submit_instructor:
                checkBoxInputs();
                if (validateInputs()) {
                    allInputsFromFields();
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("community_ID", communityID);
                    hashMap.put("appuser_ID", LOGIN_DETAIL.getAppuserID());
                    hashMap.put("instructor_name", instructorName);
                    hashMap.put("instructor_country_code", country.getId());
                    hashMap.put("instructor_contact", country.getPhonecode() + instructorMobileNumber);
                    hashMap.put("instructor_activity", activity);
                    hashMap.put("instructor_company", company);
                    //hashMap.put("instructor_license_plate","");
                    hashMap.put("start_date", startDate);
                    hashMap.put("end_date", endDate);
                    hashMap.put("start_time", CommanUtils.convert12To24Time(tvStartTime.getText().toString()));
                    hashMap.put("end_time", CommanUtils.convert12To24Time(tvEndTime.getText().toString()));
                    for (int i = 0; i < selectedDaysList.size(); i++) {
                        hashMap.put("recurrence_days[" + i + "]", String.valueOf(selectedDaysList.get(i)));
                    }
                    // hashMap.put("recurrence_days[0]",selectedDaysList);
                    apiServiceProvider.submitAddInstructor(hashMap, new RetrofitListener<SuccessResponse>() {
                        @Override
                        public void onResponseSuccess(SuccessResponse sucessRespnse, String apiFlag) {
                            Log.d(TAG, "onResponseSuccess: " + sucessRespnse.toString());
                            if (sucessRespnse.getStatus().equalsIgnoreCase("OK")) {
                                String message = sucessRespnse.getMsg();
                                ((InstructorActivity) getActivity()).refreshPage();
                                showDialog(true);
//                                Toast.makeText(getContext(), sucessRespnse.getMsg(), Toast.LENGTH_SHORT).show();
                            } else {
                                if (sucessRespnse.getMsg().equalsIgnoreCase("Instructor Occupied")) {
                                    showDialog(false);
                                } else {
                                    Toast.makeText(getContext(), sucessRespnse.getMsg(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                            Toast.makeText(getContext(), errorObject.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                break;
        }

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

    private void allFeildsEmpty() {
        edInstructorMobileNumber.setText("");
        edNameOfInstructor.setText("");
        edActivity.setText("");
        edCompany.setText("");
        tvStartDate.setText("");
        tvEndDate.setText("");
        tvStartTime.setText("");
        tvEndTime.setText("");
        if (communityID != null && tv_community.getText().toString().trim().length() > 0) {
            tv_community.setText("");
            communityID = null;
            tv_countryCode.setVisibility(View.GONE);
            tv_countryCode.setText("");

        }
        tv_community.requestFocus();
        binding.scrollView.scrollTo(0, 0);
        getActivity().getWindow().getDecorView().clearFocus();

        selectedDaysList.clear();
        checkboxSunday.setChecked(false);
        checkboxMonday.setChecked(false);
        checkboxTuesday.setChecked(false);
        checkboxWednesday.setChecked(false);
        checkboxThursday.setChecked(false);
        checkboxFriday.setChecked(false);
        checkboxSaturday.setChecked(false);
    }


    private void checkBoxInputs() {
        selectedDaysList.clear();
        if (checkboxSunday.isChecked()) {
            selectedDaysList.add(0);
        }
        if (checkboxMonday.isChecked()) {
            selectedDaysList.add(1);
        }
        if (checkboxTuesday.isChecked()) {
            selectedDaysList.add(2);
        }
        if (checkboxWednesday.isChecked()) {
            selectedDaysList.add(3);
        }
        if (checkboxThursday.isChecked()) {
            selectedDaysList.add(4);
        }
        if (checkboxFriday.isChecked()) {
            selectedDaysList.add(5);
        }
        if (checkboxSaturday.isChecked()) {
            selectedDaysList.add(6);
        }
    }

    private void showDialog(boolean isSuccess) {
        final AlertDialog dialogBuilder = new AlertDialog.Builder(getContext()).create();
        dialogBuilder.setCancelable(false);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_successful_booked, null);
        RelativeLayout rlOk = dialogView.findViewById(R.id.rl_ok);
        ImageView iv_greenCheck = dialogView.findViewById(R.id.iv_greenCheck);
        TextView tv_message = dialogView.findViewById(R.id.tv_message);
        TextView tv_title = dialogView.findViewById(R.id.tv_title);
        if (isSuccess) {
            tv_message.setText("Instructor registered and will be inducted by HR.\n Thank you.");
        } else {
            tv_message.setText("Instructor has been inducted and is currently occupied. Please register a different instructor.\nThank You.");
            iv_greenCheck.setImageDrawable(getResources().getDrawable(R.mipmap.ic_error));
            tv_title.setText("Unsuccessful!!");
        }

        rlOk.setOnClickListener(v -> {
            dialogBuilder.dismiss();
            allFeildsEmpty();
        });
        dialogBuilder.setView(dialogView);
        Window window = dialogBuilder.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialogBuilder.show();
    }

    private boolean validateInputs() {
        boolean isTimeGreaterThen = false;
        try {
            isTimeGreaterThen = dateFormatValidation.parse(tvStartTime.getText().toString()).before(dateFormatValidation.parse(tvEndTime.getText().toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (communityID == null) {
            Toast.makeText(getContext(), "Please Select Community", Toast.LENGTH_SHORT).show();
            return false;
        } else if (edInstructorMobileNumber.getText().toString().trim().length() == 0) {
            Toast.makeText(getContext(), "Please enter mobile number", Toast.LENGTH_SHORT).show();
            return false;
        } else if (edNameOfInstructor.getText().toString().trim().length() == 0) {
            Toast.makeText(getContext(), "Please enter name of Instructor", Toast.LENGTH_SHORT).show();
            return false;
        } else if (edActivity.getText().toString().trim().length() == 0) {
            Toast.makeText(getContext(), "Please enter activity", Toast.LENGTH_SHORT).show();
            return false;
        } else if (edCompany.getText().toString().trim().length() == 0) {
            Toast.makeText(getContext(), "Please enter Company", Toast.LENGTH_SHORT).show();
            return false;
        } else if (tvStartDate.getText().toString().trim().length() == 0) {
            Toast.makeText(getContext(), "Please enter Start date", Toast.LENGTH_SHORT).show();
            return false;
        } else if (tvEndDate.getText().toString().trim().length() == 0) {
            Toast.makeText(getContext(), "Please enter End date", Toast.LENGTH_SHORT).show();
            return false;
        } else if (tvStartTime.getText().toString().trim().length() == 0) {
            Toast.makeText(getContext(), "Please enter Start time", Toast.LENGTH_SHORT).show();
            return false;
        } else if (tvEndTime.getText().toString().trim().length() == 0) {
            Toast.makeText(getContext(), "Please enter End time", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!isTimeGreaterThen) {
            Toast.makeText(getContext(), "End Time can not be less than Start Time", Toast.LENGTH_SHORT).show();
            return false;
        } else if (selectedDaysList.size() == 0) {
            Toast.makeText(getContext(), "Please Select Recurrence Days", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void allInputsFromFields() {
        activity = edActivity.getText().toString().trim();
        company = edCompany.getText().toString().trim();
        instructorName = edNameOfInstructor.getText().toString().trim();
        countryCode = tv_countryCode.getText().toString().trim();
        instructorMobileNumber = edInstructorMobileNumber.getText().toString().trim();
        communityName = tv_community.getText().toString().trim();
    }

    public void showDatePicker(boolean isStartDate) {
        hideKeyBoard(getActivity());
        DatePickerDialog datePicker;
        Calendar myCalendar = Calendar.getInstance(TimeZone.getDefault());
        datePicker = new DatePickerDialog(getContext(), R.style.TimePickerTheme, (mDatePicker, year, month, dayOfMonth) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth);
            String strDate = outputFormat.format(calendar.getTime());
            if (isStartDate) {
                startDate = dateFormat.format(calendar.getTime());
                tvStartDate.setText(strDate);
                showDatePicker(false);
            } else {
                endDate = dateFormat.format(calendar.getTime());
                tvEndDate.setText(strDate);
            }

        }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        datePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.title_lay, null, false);
        TextView title_tv = view.findViewById(R.id.title_tv);
        title_tv.setText(isStartDate ? "Select Start Date" : "Select End Date");
        datePicker.setCustomTitle(view);

        datePicker.show();

    }

    @Override
    public void onResponseSuccess(SuccessResponse sucessRespnse, String apiFlag) {
        switch (apiFlag) {
            case Constant.UrlPath.COMMUNITY_DEVICE_LIST_API:
                if (sucessRespnse.getStatus().equalsIgnoreCase("OK")) {
                    responseDataDevice = sucessRespnse.getData();
                    country = null;
                    if (responseDataDevice != null) {
                        edInstructorMobileNumber.setPadding(0, 0, 0, 0);
                        tv_countryCode.setVisibility(View.VISIBLE);
                        for (Country countrys : responseDataDevice.getCountry()) {
                            if (countrys.getId().equalsIgnoreCase(responseDataDevice.getDefaultCountry())) {
                                country = countrys;
                                break;
                            }
                        }
                        tv_countryCode.setText(country.getPhonecode());
                        tv_countryCode.setOnClickListener(v -> {
                            CountryCodeDialogAdapter countryCodeDialogAdapter = new CountryCodeDialogAdapter(responseDataDevice.getCountry(), data -> {
                                tv_countryCode.setText(data.getPhonecode());
                                country = data;
                                customCountryCodeDialog.dismiss();
                            });
                            customCountryCodeDialog = new CustomCountryCodeDialog(getActivity(), countryCodeDialogAdapter, responseDataDevice.getCountry());
                            customCountryCodeDialog.setCanceledOnTouchOutside(false);
                            customCountryCodeDialog.show();
                        });
                    }

                 /*   if (sucessRespnse.getData().getDevices().size() > 0) {
                        // deviceMultiSelectDialogAdapter = new DeviceMultiSelectDialogAdapter(sucessRespnse.getData().getDevices());
                        // customDeviceListDialog = new CustomDeviceListDialog(getActivity(), deviceMultiSelectDialogAdapter, mDataset -> {
                        //selectDeviceListAdapter.updateItem(mDataset);
                        //  customDeviceListDialog.setCanceledOnTouchOutside(false);
                        // });
                    } else {
                        // customDeviceListDialog = null;
                        // responseDataDevice = null;
                        // selectDeviceListAdapter.updateItem(new ArrayList<>());
                        //  Toast.makeText(getContext(), "No Device", Toast.LENGTH_LONG).show();
                    }*/
                } else {
                    // customDeviceListDialog = null;
                    // responseDataDevice = null;
                    //selectDeviceListAdapter.updateItem(new ArrayList<>());
                    Toast.makeText(getContext(), sucessRespnse.getMsg(), Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
        switch (apiFlag) {
            case Constant.UrlPath.COMMUNITY_DEVICE_LIST_API:
                try {
                    Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                }
                // customDeviceListDialog = null;
                //selectDeviceListAdapter.updateItem(new ArrayList<>());
                break;
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        unbinder.unbind();
    }
}
