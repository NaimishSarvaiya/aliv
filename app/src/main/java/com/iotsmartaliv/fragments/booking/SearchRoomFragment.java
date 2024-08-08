package com.iotsmartaliv.fragments.booking;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iotsmartaliv.R;
import com.iotsmartaliv.activity.booking.SelectRoomActivity;
import com.iotsmartaliv.adapter.CommunityDialogAdapter;
import com.iotsmartaliv.apiCalling.listeners.RetrofitListener;
import com.iotsmartaliv.apiCalling.models.ErrorObject;
import com.iotsmartaliv.apiCalling.models.ResArrayObjectData;
import com.iotsmartaliv.apiCalling.models.SearchBookingResponse;
import com.iotsmartaliv.apiCalling.models.SuccessArrayResponse;
import com.iotsmartaliv.apiCalling.retrofit.ApiServiceProvider;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.databinding.FragmentSearchRoomBinding;
import com.iotsmartaliv.dialog_box.CustomCommunityDialog;
import com.iotsmartaliv.dialog_box.CustomTimePicker;
import com.iotsmartaliv.utils.CommanUtils;
import com.iotsmartaliv.utils.Util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;



import static com.iotsmartaliv.constants.Constant.LOGIN_DETAIL;


public class SearchRoomFragment extends Fragment {
    public static SearchBookingResponse SEARCH_ROOMS;
    DateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
    ApiServiceProvider apiServiceProvider;
    String communityID;
    CustomCommunityDialog customCommunityDialog;
    FragmentSearchRoomBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSearchRoomBinding.inflate(inflater,container,false);
        View view = inflater.inflate(R.layout.fragment_search_room, container, false);

        apiServiceProvider = ApiServiceProvider.getInstance(getActivity());

                binding.tvStart.setOnClickListener(this::onViewClicked);
                binding.tvEndDate.setOnClickListener(this::onViewClicked);
                binding.tvStartTime.setOnClickListener(this::onViewClicked);
                binding.tvEndTime.setOnClickListener(this::onViewClicked);
                binding.searchBtn.setOnClickListener(this::onViewClicked);
                binding.rlSelectCommunity.setOnClickListener(this::onViewClicked);
        return binding.getRoot();
    }

    void getCommunity(){
        Util.checkInternet(requireActivity(), new Util.NetworkCheckCallback() {
            @Override
            public void onNetworkCheckComplete(boolean isAvailable) {
                if (isAvailable) {
                    apiServiceProvider.callForListOfCommunity(LOGIN_DETAIL.getAppuserID(), new RetrofitListener<SuccessArrayResponse>() {
                        @Override
                        public void onResponseSuccess(SuccessArrayResponse successArrayResponse, String apiFlag) {
                            if (successArrayResponse.getStatus().equalsIgnoreCase("OK")) {
                                if (successArrayResponse.getData().size() > 0) {
//                                    if (successArrayResponse.getData().size() == 1) {
//                                        rlSelectCommunity.setVisibility(View.GONE);
//                                        communityID = successArrayResponse.getData().get(0).getCommunityID();
//                                    } else {
                                        List<ResArrayObjectData> mDataseta = new ArrayList<>();
                                        for (int i = 0; i < successArrayResponse.getData().size(); i++) {
                                            Log.e("Booking Authority", successArrayResponse.getData().get(i).getBookingmanagement().toString());
                                            if (successArrayResponse.getData().get(i).getBookingmanagement().toString().equalsIgnoreCase("1")) {
                                                mDataseta.add(successArrayResponse.getData().get(i));
                                            }
                                        }
                                        if (mDataseta == null || mDataseta.size() == 0) {
                                            Toast.makeText(requireActivity(),"Please Contact Administrator for booking System",Toast.LENGTH_SHORT).show();
                                        } else {
                                            CommunityDialogAdapter dataAdapter = new CommunityDialogAdapter(mDataseta, data -> {
                                                communityID = data.getCommunityID();
                                                binding.communityId.setText(data.getCommunityName());
                                                customCommunityDialog.dismiss();
                                            });
                                            customCommunityDialog = new CustomCommunityDialog(getActivity(), dataAdapter, successArrayResponse.getData());
                                            customCommunityDialog.setCanceledOnTouchOutside(false);
                                            if (customCommunityDialog != null) {
                                                customCommunityDialog.show();
                                            }



//                                        }

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
                                    Util.firebaseEvent(Constant.APIERROR, requireActivity(), Constant.UrlPath.SERVER_URL + apiFlag, LOGIN_DETAIL.getUsername(), LOGIN_DETAIL.getAppuserID(), errorObject.getStatus());
                                    try {
                                        Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                                    } catch (Exception e) {
                                        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                                    }
                                    break;
                            }
                            getActivity().finish();

                        }
                    });
                } else {
                    getActivity().finish();
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        SEARCH_ROOMS = null;
    }

    public void showMyToast(final Toast toast, final int delay) {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                toast.show();
            }
        }, 0, 1000);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                toast.cancel();
                timer.cancel();
            }
        }, delay);
    }


    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.search_btn:

                if (communityID != null) {
                    if (binding.tvStart.getText().toString().trim().length() > 0 && binding.tvEndDate.getText().toString().trim().length() > 0 && binding.tvStartTime.getText().toString().trim().length() > 0 && binding.tvEndTime.getText().toString().trim().length() > 0) {
                        Util.checkInternet(requireActivity(), new Util.NetworkCheckCallback() {
                            @Override
                            public void onNetworkCheckComplete(boolean isAvailable) {
                                if (isAvailable) {
                                    apiServiceProvider.searchBookings(communityID, LOGIN_DETAIL.getAppuserID(), binding.tvStart.getText().toString(), binding.tvEndDate.getText().toString(), CommanUtils.convert12To24Time(binding.tvStartTime.getText().toString()), CommanUtils.convert12To24Time(binding.tvEndTime.getText().toString()), new RetrofitListener<SearchBookingResponse>() {
                                        @Override
                                        public void onResponseSuccess(SearchBookingResponse searchBookingResponse, String apiFlag) {
                                            if (searchBookingResponse.getStatus().equalsIgnoreCase("OK")) {
                                                if (searchBookingResponse.getData().size() > 0) {
                                                    Intent intent = new Intent(getActivity(), SelectRoomActivity.class);
                                                    SEARCH_ROOMS = searchBookingResponse;
                                                    getActivity().startActivityForResult(intent, 121);
                                                    new Handler().postDelayed(() -> {
                                                        binding.tvStart.setText("");
                                                        binding.tvEndDate.setText("");
                                                        binding.tvStart.setText("");
                                                        binding.tvEndDate.setText("");
                                                        if (communityID != null && binding.communityId.getText().toString().trim().length() > 0) {
                                                            binding.communityId.setText("");
                                                            communityID = null;
                                                        }
                                                    }, 800);
                                                } else {
                                                    Toast.makeText(getContext(), "Sorry! Room Not Available.", Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                Toast.makeText(getContext(), searchBookingResponse.getMsg(), Toast.LENGTH_SHORT).show();
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

                    } else {
                        Toast.makeText(getContext(), "Please fill all entries.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast toast = Toast.makeText(getContext(), "Please Select Community", Toast.LENGTH_SHORT);
                    showMyToast(toast, 1000);
                }
                break;
            case R.id.rl_select_community:
                getCommunity();

                break;
            case R.id.tv_start:
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

        }
    }

    private Calendar startDate = null;
    private Calendar endDate = null;

    public void showDatePicker(boolean isStartDate) {
        Calendar myCalendar = Calendar.getInstance(TimeZone.getDefault());

        DatePickerDialog datePicker = new DatePickerDialog(getContext(), R.style.TimePickerTheme, (mDatePicker, year, month, dayOfMonth) -> {
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(year, month, dayOfMonth);

            // Validate dates
            if (isStartDate) {
                if (endDate != null && selectedDate.after(endDate)) {
                    // Show error message or handle invalid date
//                    Toast.makeText(getContext(), "Start date cannot be after end date", Toast.LENGTH_SHORT).show();
                    return;
                }
                startDate = selectedDate;
                binding.tvStart.setText(outputFormat.format(selectedDate.getTime()));
            } else {
                if (startDate != null && selectedDate.before(startDate)) {
                    // Show error message or handle invalid date
//                    Toast.makeText(getContext(), "End date cannot be before start date", Toast.LENGTH_SHORT).show();
                    return;
                }
                endDate = selectedDate;
                binding.tvEndDate.setText(outputFormat.format(selectedDate.getTime()));
            }
        }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));

        // Set min date to current date
        datePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        // Set max date based on whether it's start or end date
        if (isStartDate && endDate != null) {
            datePicker.getDatePicker().setMaxDate(endDate.getTimeInMillis());
        } else if (!isStartDate && startDate != null) {
            datePicker.getDatePicker().setMinDate(startDate.getTimeInMillis());
        }

        datePicker.show();
    }



    private void showCustomTimePicker(boolean isStart) {
        new CustomTimePicker(getActivity(), time -> {
            if (isStart) {
                binding.tvStart.setText(time);
                showCustomTimePicker(false);
            } else {
                binding.tvEndDate.setText(time);
            }

        }, isStart ? "Start Time" : "End Time").show();

    }

}
