package com.iotsmartaliv.activity.feedback;

import static com.iotsmartaliv.constants.Constant.LOGIN_DETAIL;
import static com.iotsmartaliv.constants.Constant.hideLoader;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.iotsmartaliv.R;
import com.iotsmartaliv.adapter.FeedbackChatAdapter;
import com.iotsmartaliv.adapter.automation.FeedbackHistoryChatAdapter;
import com.iotsmartaliv.apiAndSocket.listeners.RetrofitListener;
import com.iotsmartaliv.apiAndSocket.models.ErrorObject;
import com.iotsmartaliv.apiAndSocket.retrofit.ApiServiceProvider;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.databinding.ActivityFeedbackDetailsBinding;
import com.iotsmartaliv.model.feedback.FeedbackDetails;
import com.iotsmartaliv.model.feedback.FeedbackDetailsData;
import com.iotsmartaliv.model.feedback.MessageHistory;
import com.iotsmartaliv.model.feedback.MessageHistoryData;
import com.iotsmartaliv.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FeedbackDetailsActivity extends AppCompatActivity implements RetrofitListener<FeedbackDetails> {

    private FeedbackHistoryChatAdapter adapter;
    private List<MessageHistoryData> messageList = new ArrayList<>();
    private boolean isLoading = false;
    private int page = 1;
    private String limit = "20";
    ActivityFeedbackDetailsBinding binding;
    private ApiServiceProvider apiServiceProvider;
    FeedbackDetailsData feedbackDetailsData;
    String feedbackId;
    String TotalMessageCount = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFeedbackDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        apiServiceProvider = ApiServiceProvider.getInstance(this,false);
        if (getIntent().getExtras().getString("feedback_ID") != null) {
            feedbackId = getIntent().getStringExtra("feedback_ID");
        } else {
            String data = getIntent().getExtras().get("data").toString();
            // Convert the string to a JSONObject
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(data);
                feedbackId = jsonObject.getString("feedback_ID");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            // Retrieve the values using the keys
        }
        getFeedbackDetails();

        setupRecyclerView();
        setupScrollViewListener();

        binding.rlRetusOrChat.setOnClickListener(v -> {
            Intent intent = new Intent(FeedbackDetailsActivity.this, ChatActivity.class);
            intent.putExtra(Constant.FEEDBACK_ID, feedbackId);
            intent.putExtra("limit",TotalMessageCount);
            startActivity(intent);
        });

        binding.imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.rlDocPreview.setOnClickListener(v -> {
            if (feedbackDetailsData.getFeedbackDocument() != null) {
                Intent intent = new Intent(this, FilePreviewActivity.class);
                intent.putExtra(Constant.PATH, Constant.FROM_FEEDBACK_DETAILS);
                intent.putExtra(Constant.FILE_URI, feedbackDetailsData.getFeedbackDocument());
                startActivity(intent);
            }
        });
    }

    private void setupRecyclerView() {
        binding.rvChatHistory.setVisibility(View.VISIBLE);
        binding.rvChatHistory.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FeedbackHistoryChatAdapter(this, messageList);
        binding.rvChatHistory.setAdapter(adapter);
        binding.rvChatHistory.setNestedScrollingEnabled(false);// Disable nested scrolling
    }

    private void setupScrollViewListener() {
        binding.scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // Check if the NestedScrollView has reached the bottom
                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    if (!isLoading) {
                        loadMoreMessages();
                    }
                }
            }
        });
    }

    private void loadMoreMessages() {
        isLoading = true;
        page--;
        getChatHistory();
    }

    private void getFeedbackDetails() {
        Util.checkInternet(this, new Util.NetworkCheckCallback() {
            @Override
            public void onNetworkCheckComplete(boolean isAvailable) {
                if (isAvailable) {
                    apiServiceProvider.getFeedbackDetails(feedbackId, FeedbackDetailsActivity.this);
                } else {
                    hideLoader();
                }
            }
        });
    }

    private void updateProgressStatus(int status) {
        switch (status) {
            case 1: // Sent
                binding.circleSent.setImageResource(R.drawable.circle_drawable_selected);
                binding.lineBetweenSentInProgress.setBackgroundResource(R.drawable.line_drawable);
                binding.circleInProgress.setImageResource(R.drawable.circle_drawable);
                binding.lineBetweenInProgressClosed.setBackgroundResource(R.drawable.line_drawable);
                binding.circleClosed.setImageResource(R.drawable.circle_drawable);
                break;
            case 2: // In Progress
                binding.circleSent.setImageResource(R.drawable.circle_drawable_selected);
                binding.lineBetweenSentInProgress.setBackgroundResource(R.drawable.line_drawable_selected);
                binding.circleInProgress.setImageResource(R.drawable.circle_drawable_selected);
                binding.lineBetweenInProgressClosed.setBackgroundResource(R.drawable.line_drawable);
                binding.circleClosed.setImageResource(R.drawable.circle_drawable);
                break;
            case 3: // Closed
                binding.circleSent.setImageResource(R.drawable.circle_drawable_selected);
                binding.lineBetweenSentInProgress.setBackgroundResource(R.drawable.line_drawable_selected);
                binding.circleInProgress.setImageResource(R.drawable.circle_drawable_selected);
                binding.lineBetweenInProgressClosed.setBackgroundResource(R.drawable.line_drawable_selected);
                binding.circleClosed.setImageResource(R.drawable.circle_drawable_selected);
                binding.rlRetusOrChat.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onResponseSuccess(FeedbackDetails sucessRespnse, String apiFlag) {
        if (sucessRespnse.getStatusCode() == 200) {


            feedbackDetailsData = sucessRespnse.getData();
            feedbackId = feedbackDetailsData.getId();
            setData(feedbackDetailsData);
             TotalMessageCount = String.valueOf(sucessRespnse.getTotalRecords());
            if (feedbackDetailsData.getFeedbackStatus().equals("3")) {
                if (sucessRespnse.getTotalRecords() != 0) {
                    int totalMessageCount = sucessRespnse.getTotalRecords();
                    int recordLimit = Integer.parseInt(limit);
                    page = (int) Math.ceil((double) totalMessageCount / recordLimit);

                    getChatHistory();
                }
            }
        } else {
            Toast.makeText(this, sucessRespnse.getMsg(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
        // Handle error
    }

    void setData(FeedbackDetailsData feedbackDetailsData) {
        updateProgressStatus(Integer.parseInt(feedbackDetailsData.getFeedbackStatus()));
        binding.tvFeedbackID.setText("#" + feedbackDetailsData.getId());
        binding.tvCategoryName.setText(feedbackDetailsData.getCatName());
        binding.tvFeedbackTitle.setText(feedbackDetailsData.getFeedbackTitle());
        binding.tvFeedbackDescription.setText(feedbackDetailsData.getFeedbackDescription());
        binding.tvUserName.setText(LOGIN_DETAIL.getUsername());
        binding.tvCommunityName.setText(feedbackDetailsData.getCommunityName());

        if (feedbackDetailsData.getCreatedAt() != null) {
            binding.sentDate.setText(Util.formatFeedbackDate(feedbackDetailsData.getCreatedAt()));
        } else {
            binding.sentDate.setText("");
        }
        if (feedbackDetailsData.getProgressDate() != null) {
            binding.inProgressDate.setText(Util.formatFeedbackDate(feedbackDetailsData.getProgressDate()));
        } else {
            binding.inProgressDate.setText("");
        }

        if (feedbackDetailsData.getCloseDate() != null) {
            binding.closedDate.setText(Util.formatFeedbackDate(feedbackDetailsData.getCloseDate()));
        } else {
            binding.closedDate.setText("");
        }
        if (feedbackDetailsData.getFeedbackDocument() != null && !feedbackDetailsData.getFeedbackDocument().isEmpty()) {
            binding.rlAttachedDoc.setVisibility(View.VISIBLE);
            binding.tvNoDoc.setVisibility(View.GONE);
            Glide.with(this)
                    .load(feedbackDetailsData.getFeedbackDocument())
                    .override(800, 800) // Resize the image for preview
                    .placeholder(R.drawable.attached_file) // Placeholder while loading
                    .into(binding.imgFeedbackDoc);
        } else {
            binding.rlAttachedDoc.setVisibility(View.GONE);
            binding.tvNoDoc.setVisibility(View.VISIBLE);
        }

        if (feedbackDetailsData.getFeedbackStatus().equals("2")) {
            if (feedbackDetailsData.getCreatedAt() != null && feedbackDetailsData.getProgressDate() != null) {
                String sentToPendingTime = Util.calculateTimeDifference(feedbackDetailsData.getProgressDate(), feedbackDetailsData.getCreatedAt());
                binding.tvTimeforInprogress.setText(sentToPendingTime);
            }
        } else if (feedbackDetailsData.getFeedbackStatus().equals("3")) {
            if (feedbackDetailsData.getCreatedAt() != null && feedbackDetailsData.getProgressDate() != null && feedbackDetailsData.getCloseDate() != null) {
                String sentToPendingTime = Util.calculateTimeDifference(feedbackDetailsData.getProgressDate(), feedbackDetailsData.getCreatedAt());
                binding.tvTimeforInprogress.setText(sentToPendingTime);
                String pendingToCloseTime = Util.calculateTimeDifference(feedbackDetailsData.getCloseDate(),feedbackDetailsData.getProgressDate());
                binding.tvTimeforCloss.setText(pendingToCloseTime);
            }
        }

    }

    void getChatHistory() {
        Util.checkInternet(this, new Util.NetworkCheckCallback() {
            @Override
            public void onNetworkCheckComplete(boolean isAvailable) {
                if (isAvailable) {
                    apiServiceProvider.getFeedbackMessages(getIntent().getStringExtra(Constant.FEEDBACK_ID), LOGIN_DETAIL.getAppuserID(), String.valueOf(page), limit, new RetrofitListener<MessageHistory>() {
                        @Override
                        public void onResponseSuccess(MessageHistory sucessRespnse, String apiFlag) {
                            isLoading = false;
                            List<MessageHistoryData> newMessages = sucessRespnse.getData();
                            if (newMessages != null && !newMessages.isEmpty()) {
                                messageList.addAll(newMessages);
                                adapter.updateData(newMessages);
//                                setupRecyclerView();
                            } else {
//                                Toast.makeText(FeedbackDetailsActivity.this, "No more messages", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                            isLoading = false;
                        }
                    });
                } else {
                    hideLoader();
                }
            }
        });
    }
}
