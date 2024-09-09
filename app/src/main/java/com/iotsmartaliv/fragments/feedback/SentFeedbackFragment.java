package com.iotsmartaliv.fragments.feedback;

import static com.iotsmartaliv.constants.Constant.LOGIN_DETAIL;
import static com.iotsmartaliv.constants.Constant.hideLoader;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.iotsmartaliv.R;
import com.iotsmartaliv.adapter.FeedbackListAdapter;
import com.iotsmartaliv.apiCalling.listeners.RetrofitListener;
import com.iotsmartaliv.apiCalling.models.ErrorObject;
import com.iotsmartaliv.apiCalling.retrofit.ApiServiceProvider;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.model.feedback.FeedbackData;
import com.iotsmartaliv.model.feedback.FeedbackModel;
import com.iotsmartaliv.utils.Util;

import java.util.ArrayList;

public class SentFeedbackFragment extends Fragment implements RetrofitListener<FeedbackModel> {

    RecyclerView rv_sendedFeed;
    LinearLayout noFeed;
    FeedbackListAdapter feedbackListAdapter;
    private ApiServiceProvider apiServiceProvider;
    ArrayList<FeedbackData> feedbackList = new ArrayList<>(); // Initialize the list here
    int page = 1;
    int totalRecords = 0;
    SwipeRefreshLayout pullToRefresh;
    private boolean isRefreshing = false; // Flag to check if pull-to-refresh is active

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sent_feedback, container, false);
        init(view);
        return view;
    }

    void init(View view) {
        rv_sendedFeed = view.findViewById(R.id.rv_sentFeedback);
        noFeed = view.findViewById(R.id.ll_noSentFeedback);
        pullToRefresh = view.findViewById(R.id.pullToRefresh);
        noFeed.setVisibility(View.VISIBLE);
        rv_sendedFeed.setVisibility(View.GONE);
        rv_sendedFeed.setLayoutManager(new LinearLayoutManager(requireActivity()));
        feedbackListAdapter = new FeedbackListAdapter(requireActivity(), Constant.SENT_FEED, feedbackList);
        rv_sendedFeed.setAdapter(feedbackListAdapter);
        apiServiceProvider = ApiServiceProvider.getInstance(getActivity());
        loadFeed(page);

        // Handle scroll for pagination
        rv_sendedFeed.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // Check if not loading, not refreshing, and reached the bottom of the list
                if (!isRefreshing && !recyclerView.canScrollVertically(1)) {
                    // Check if there are more records to load
                    if (feedbackList.size() < totalRecords) {
                        page++; // Increase page number
                        loadFeed(page); // Load next page
                    } else {
                        // Show message if maximum records are loaded
//                        Toast.makeText(requireActivity(), "All feeds loaded.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // Handle pull-to-refresh
        pullToRefresh.setOnRefreshListener(() -> {
            isRefreshing = true; // Set flag to true when pull-to-refresh starts
            page = 1; // Reset page number for refresh
            feedbackList.clear(); // Clear current list
            feedbackListAdapter.refreshItems(new ArrayList<>()); // Clear adapter data
            loadFeed(page); // Load the first page
        });
    }

    void loadFeed(int page) {
        Util.checkInternet(requireActivity(), new Util.NetworkCheckCallback() {
            @Override
            public void onNetworkCheckComplete(boolean isAvailable) {
                if (isAvailable) {
                    apiServiceProvider.FeedbackList(LOGIN_DETAIL.getAppuserID(), Constant.SENT_FEED_STATUS, Constant.FEED_PAGE_LIMIT, String.valueOf(page), SentFeedbackFragment.this);
                } else {
                    hideLoader();
                }
            }
        });
    }

    @Override
    public void onResponseSuccess(FeedbackModel successResponse, String apiFlag) {
        if (successResponse.statusCode == 200) {
            totalRecords = Integer.parseInt(successResponse.getTotalRecords());
            ArrayList<FeedbackData> newData = successResponse.getData();

            if (newData != null && newData.size() > 0) {
                noFeed.setVisibility(View.GONE);
                rv_sendedFeed.setVisibility(View.VISIBLE);

                if (page == 1) {
                    // Clear the existing data before adding new items for the first page
                    feedbackList.clear();
                    feedbackListAdapter.refreshItems(newData);
                } else {
                    // Add new items for pagination
                    feedbackListAdapter.addItems(newData);
                }
            } else {
                if (page == 1) {
                    noFeed.setVisibility(View.VISIBLE);
                    rv_sendedFeed.setVisibility(View.GONE);
                }
            }
        } else {
            Toast.makeText(requireActivity(), successResponse.msg, Toast.LENGTH_LONG).show();
        }

        // Reset the refreshing flag after the response is handled
        isRefreshing = false;
        pullToRefresh.setRefreshing(false);
    }

    @Override
    public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
        try {
            Toast.makeText(requireActivity(), throwable.getMessage(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(requireActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
        }

        // Reset the refreshing flag in case of an error
        isRefreshing = false;
        pullToRefresh.setRefreshing(false);
    }
}
