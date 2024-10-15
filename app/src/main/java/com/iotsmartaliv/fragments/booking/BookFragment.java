package com.iotsmartaliv.fragments.booking;

import static com.iotsmartaliv.constants.Constant.LOGIN_DETAIL;
import static com.iotsmartaliv.constants.Constant.hideLoader;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iotsmartaliv.R;
import com.iotsmartaliv.adapter.booking.BookListAdapter;
import com.iotsmartaliv.apiAndSocket.listeners.RetrofitListener;
import com.iotsmartaliv.apiAndSocket.models.ErrorObject;
import com.iotsmartaliv.apiAndSocket.retrofit.ApiServiceProvider;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.databinding.FragmentBookBinding;
import com.iotsmartaliv.model.booking.RoomData;
import com.iotsmartaliv.model.booking.RoomModel;
import com.iotsmartaliv.model.feedback.FeedbackData;
import com.iotsmartaliv.utils.Util;

import java.util.ArrayList;

public class BookFragment extends Fragment implements RetrofitListener<RoomModel> {

    FragmentBookBinding binding;
    BookListAdapter adapter;
    private ApiServiceProvider apiServiceProvider;
    ArrayList<RoomData> roomList = new ArrayList<>();
    int page = 1;
    int totalRecords = 0;
    private boolean isRefreshing = false; // Flag to check if pull-to-refresh is active

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentBookBinding.inflate(inflater, container, false);
        init();
        return binding.getRoot();
    }

    private void init() {
        apiServiceProvider = ApiServiceProvider.getInstance(getActivity(),true);
        binding.layoutNoData.tvTitle.setText(R.string.we_couldn_t_find_any_result);
        binding.layoutNoData.tvDetail.setText(R.string.we_couldn_t_locate_any_relevant_results_in_your_communities_right_now_please_try_again_later);
        binding.layoutNoData.llNoFeedback.setVisibility(View.VISIBLE);
        binding.rvBooking.setVisibility(View.GONE);
        binding.rvBooking.setLayoutManager(new LinearLayoutManager(requireActivity()));
        adapter = new BookListAdapter(requireActivity(), roomList);
        binding.rvBooking.setAdapter(adapter);
        binding.rvBooking.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // Check if not loading, not refreshing, and reached the bottom of the list
                if (!isRefreshing && !recyclerView.canScrollVertically(1)) {
                    // Check if there are more records to load
                    if (page <= totalRecords) {
                        page++; // Increase page number
                        loadFeed(page); // Load next page
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
            roomList.clear(); // Clear current list
            adapter.refreshItems(new ArrayList<>()); // Clear adapter data
            loadFeed(page); // Load the first page
        });
    }

    void loadFeed(int page) {
        Util.checkInternet(requireActivity(), new Util.NetworkCheckCallback() {
            @Override
            public void onNetworkCheckComplete(boolean isAvailable) {
                if (isAvailable) {
                    apiServiceProvider.getRoomList(LOGIN_DETAIL.getAppuserID(), Constant.ROOM_PAGE_LIMIT, String.valueOf(page), BookFragment.this);
                } else {
                    hideLoader();
                }
            }
        });
    }

    @Override
    public void onResponseSuccess(RoomModel successResponse, String apiFlag) {
        if (successResponse.getStatusCode() == 200) {
            totalRecords = successResponse.getPagination().getTotalPages();
            ArrayList<RoomData> newData = (ArrayList<RoomData>) successResponse.getData();

            if (newData != null && newData.size() > 0) {
                binding.layoutNoData.llNoFeedback.setVisibility(View.GONE);
                binding.rvBooking.setVisibility(View.VISIBLE);

                if (page == 1) {
                    // Clear the existing data before adding new items for the first page
                    roomList.clear();
                    adapter.refreshItems(newData);
                } else {
                    // Add new items for pagination
                    adapter.addItems(newData);
                }
            } else {
                if (page == 1) {
                    binding.layoutNoData.llNoFeedback.setVisibility(View.VISIBLE);
                    binding.rvBooking.setVisibility(View.GONE);
                }
            }
        } else {
            Toast.makeText(requireActivity(), successResponse.getMsg(), Toast.LENGTH_LONG).show();
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
    }

    @Override
    public void onResume() {
        super.onResume();
        loadFeed(page);
    }
}