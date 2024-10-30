package com.iotsmartaliv.fragments.booking;

import static com.iotsmartaliv.constants.Constant.LOGIN_DETAIL;
import static com.iotsmartaliv.constants.Constant.hideLoader;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.iotsmartaliv.R;
import com.iotsmartaliv.adapter.booking.ActiveBookingAdapter;
import com.iotsmartaliv.adapter.booking.BookListAdapter;
import com.iotsmartaliv.apiAndSocket.listeners.RetrofitListener;
import com.iotsmartaliv.apiAndSocket.models.ErrorObject;
import com.iotsmartaliv.apiAndSocket.retrofit.ApiServiceProvider;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.databinding.FragmentActiveBookingBinding;
import com.iotsmartaliv.model.booking.ActiveBookingData;
import com.iotsmartaliv.model.booking.ActiveBookingModel;
import com.iotsmartaliv.utils.Util;

import java.util.ArrayList;
import java.util.List;

public class ActiveBookingFragment extends Fragment implements RetrofitListener<ActiveBookingModel> {

    FragmentActiveBookingBinding binding;
    ActiveBookingAdapter adapter;
    List<ActiveBookingData> bookingData = new ArrayList<>();
    private ApiServiceProvider apiServiceProvider;
    private boolean isRefreshing = false; // Flag to check if pull-to-refresh is active


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentActiveBookingBinding.inflate(inflater, container, false);
        init();
        return binding.getRoot();
    }

    private void init() {
        apiServiceProvider = ApiServiceProvider.getInstance(getActivity(), true);
        binding.layoutNoActiveBooking.tvTitle.setText(R.string.no_active_bookings);
        binding.layoutNoActiveBooking.tvDetail.setText(R.string.you_currently_have_no_active_bookings_please_check_back_later_or_add_a_new_booking);
        binding.layoutNoActiveBooking.llNoBooking.setVisibility(View.VISIBLE);
        binding.rvActiveBooking.setVisibility(View.GONE);
        binding.rvActiveBooking.setLayoutManager(new LinearLayoutManager(requireActivity()));
        adapter = new ActiveBookingAdapter(requireActivity(), bookingData,Constant.FROM_ACTIVE_BOOKING);
        binding.rvActiveBooking.setAdapter(adapter);
        getActiveBooking();
        binding.pullToRefresh.setOnRefreshListener(() -> {
            isRefreshing = true; // Set flag to true when pull-to-refresh starts
            getActiveBooking();
        });
    }

    void getActiveBooking() {
        Util.checkInternet(requireActivity(), new Util.NetworkCheckCallback() {
            @Override
            public void onNetworkCheckComplete(boolean isAvailable) {
                if (isAvailable) {
                    apiServiceProvider.getActiveBooking(LOGIN_DETAIL.getAppuserID(), ActiveBookingFragment.this);
                } else {
                    hideLoader();
                }
            }
        });
    }
    @Override
    public void onResponseSuccess(ActiveBookingModel sucessRespnse, String apiFlag) {
        if (sucessRespnse.getStatusCode() == 200) {
            adapter.add(sucessRespnse.getData());
            if (sucessRespnse.getData().size() != 0) {
                binding.layoutNoActiveBooking.llNoBooking.setVisibility(View.GONE);
                binding.rvActiveBooking.setVisibility(View.VISIBLE);
            } else {
                binding.layoutNoActiveBooking.llNoBooking.setVisibility(View.VISIBLE);
                binding.rvActiveBooking.setVisibility(View.GONE);
            }
        }else {
            Toast.makeText(requireActivity(), sucessRespnse.getMsg(), Toast.LENGTH_LONG).show();
        }
        isRefreshing = false;
        binding.pullToRefresh.setRefreshing(false);
    }

    @Override
    public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
        try {
            Toast.makeText(requireActivity(), throwable.getMessage(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(requireActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
        }
    }
}