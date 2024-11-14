package com.iotsmartaliv.fragments.booking;

import static com.iotsmartaliv.constants.Constant.FROM_HISTORY_BOOKING;
import static com.iotsmartaliv.constants.Constant.LOGIN_DETAIL;
import static com.iotsmartaliv.constants.Constant.hideLoader;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.iotsmartaliv.R;
import com.iotsmartaliv.adapter.booking.ActiveBookingAdapter;
import com.iotsmartaliv.apiAndSocket.listeners.RetrofitListener;
import com.iotsmartaliv.apiAndSocket.models.ErrorObject;
import com.iotsmartaliv.apiAndSocket.retrofit.ApiServiceProvider;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.databinding.FragmentBookingHistoryBinding;
import com.iotsmartaliv.model.booking.ActiveBookingData;
import com.iotsmartaliv.model.booking.ActiveBookingModel;
import com.iotsmartaliv.model.booking.RoomData;
import com.iotsmartaliv.utils.Util;

import java.util.ArrayList;
import java.util.List;

public class BookingHistoryFragment extends Fragment implements RetrofitListener<ActiveBookingModel> {

    FragmentBookingHistoryBinding binding;
    ActiveBookingAdapter adapter;
    List<ActiveBookingData> bookingData = new ArrayList<>();
    private ApiServiceProvider apiServiceProvider;
    int page = 1;
    int totalRecords = 0;
    boolean hasMoredate = true;
    private boolean isRefreshing = false; // Flag to check if pull-to-refresh is active

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentBookingHistoryBinding.inflate(getLayoutInflater(), container, false);
        init();
        return binding.getRoot();
    }

    private void init() {
        apiServiceProvider = ApiServiceProvider.getInstance(getActivity(), true);
        binding.layoutNoHistoryBooking.tvTitle.setText(R.string.no_active_bookings);
        binding.layoutNoHistoryBooking.tvDetail.setText(R.string.you_currently_have_no_active_bookings_please_check_back_later_or_add_a_new_booking);
        binding.layoutNoHistoryBooking.llNoBooking.setVisibility(View.VISIBLE);
        binding.rvHistoryBooking.setVisibility(View.GONE);
        binding.rvHistoryBooking.setLayoutManager(new LinearLayoutManager(requireActivity()));
        adapter = new ActiveBookingAdapter(requireActivity(), bookingData,FROM_HISTORY_BOOKING);
        binding.rvHistoryBooking.setAdapter(adapter);
        getBooking(page);
        binding.rvHistoryBooking.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // Check if not loading, not refreshing, and reached the bottom of the list
                if (!isRefreshing && !recyclerView.canScrollVertically(1)) {
                    // Check if there are more records to load
                    if (page <= totalRecords) {
                        page++; // Increase page number
                        getBooking(page); // Load next page
                    } else {
                        // Show message if maximum records are loaded
//                        Toast.makeText(requireActivity(), "All feeds loaded.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        binding.pullToRefresh.setOnRefreshListener(() -> {
            isRefreshing = true; // Set flag to true when pull-to-refresh starts
            page = 1; // Reset page number for refresh
            bookingData.clear(); // Clear current list
            adapter.refreshItems(new ArrayList<>()); // Clear adapter data
            getBooking(page); // Load the first page
        });
    }

    void getBooking(int page) {
        Util.checkInternet(requireActivity(), new Util.NetworkCheckCallback() {
            @Override
            public void onNetworkCheckComplete(boolean isAvailable) {
                if (isAvailable) {
                    apiServiceProvider.getHistoryBooking(LOGIN_DETAIL.getAppuserID(), Constant.HiSTORY_PAGE_LIMIT, String.valueOf(page), BookingHistoryFragment.this);
                } else {
                    hideLoader();
                }
            }
        });
    }

    @Override
    public void onResponseSuccess(ActiveBookingModel sucessRespnse, String apiFlag) {

        if (sucessRespnse.getStatusCode() == 200) {
            ArrayList<ActiveBookingData> newData = (ArrayList<ActiveBookingData>) sucessRespnse.getData();
            if (sucessRespnse.getData().size() == 10) {
                hasMoredate = true;
            } else {
                hasMoredate = false;
            }
            if (newData != null && newData.size() > 0) {
                binding.layoutNoHistoryBooking.llNoBooking.setVisibility(View.GONE);
                binding.rvHistoryBooking.setVisibility(View.VISIBLE);

                if (page == 1) {
                    // Clear the existing data before adding new items for the first page
                    bookingData.clear();
                    adapter.refreshItems(newData);
                } else {
                    // Add new items for pagination
                    adapter.addItems(newData);
                }
            } else {
                if (page == 1) {
                    binding.layoutNoHistoryBooking.llNoBooking.setVisibility(View.VISIBLE);
                    binding.rvHistoryBooking.setVisibility(View.GONE);
                }
            }
        } else {
            Toast.makeText(requireActivity(), sucessRespnse.getMsg(), Toast.LENGTH_LONG).show();
        }

        // Reset the refreshing flag after the response is handled
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
        isRefreshing = false;
        binding.pullToRefresh.setRefreshing(false);
    }
}