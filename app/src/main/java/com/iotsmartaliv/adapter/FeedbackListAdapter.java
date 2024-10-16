package com.iotsmartaliv.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iotsmartaliv.R;
import com.iotsmartaliv.activity.feedback.FeedbackDetailsActivity;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.model.feedback.FeedbackData;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * This class is used to display feedback row.
 */
public class FeedbackListAdapter extends RecyclerView.Adapter<FeedbackListAdapter.FeedbackViewHolder> {

    private Context mcx;
    private ArrayList<FeedbackData> feedbackList;
    String type;

    public FeedbackListAdapter(Context mcx, String type, ArrayList<FeedbackData> feedbackList) {
        this.mcx = mcx;
        this.type = type;
        this.feedbackList = (feedbackList != null) ? feedbackList : new ArrayList<>();
    }

    // Refresh the entire list (used for pull-to-refresh)
    public void refreshItems(ArrayList<FeedbackData> newFeedbackList) {
        feedbackList.clear(); // Clear existing data
        feedbackList.addAll(newFeedbackList); // Add new data
        notifyDataSetChanged(); // Notify adapter of the changes
    }

    // Add new items for pagination
    public void addItems(ArrayList<FeedbackData> newFeedbackList) {
        feedbackList.addAll(newFeedbackList); // Append new data
        notifyDataSetChanged(); // Notify adapter of the changes
    }

    @NonNull
    @Override
    public FeedbackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mcx);
        View view = inflater.inflate(R.layout.feedback_sent_items, parent, false);
        return new FeedbackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackViewHolder holder, int position) {
//        if (type.equals(Constant.HISTORY_FEED)) {
//            holder.img_chat.setVisibility(View.GONE);
//        } else {
//            holder.img_chat.setVisibility(View.VISIBLE);
//
//        }

        if (feedbackList.get(position).getUnread_message_count().equals("0")){
            holder.rlChat.setVisibility(View.GONE);
        }else {
            holder.rlChat.setVisibility(View.VISIBLE);
        }
        holder.tvUnReadChat.setText(feedbackList.get(position).getUnread_message_count());
        holder.tvFeedbackTitle.setText(feedbackList.get(position).feedback_title);
        holder.tvFeedbackDate.setText(feedbackList.get(position).feedback_date);
        holder.tvFeedbackCategoryName.setText(feedbackList.get(position).cat_name);
        holder.tvFeedbackId.setText("#" + feedbackList.get(position).feedback_id);

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(mcx, FeedbackDetailsActivity.class);
            intent.putExtra(Constant.FEEDBACK_ID,feedbackList.get(position).getFeedback_id());
            mcx.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return feedbackList.size();
    }

    public class FeedbackViewHolder extends RecyclerView.ViewHolder {
        TextView tvFeedbackId, tvFeedbackTitle, tvFeedbackDate, tvFeedbackCategoryName;
        TextView tvUnReadChat;
        RelativeLayout rlChat;

        public FeedbackViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUnReadChat = itemView.findViewById(R.id.tv_unReadChat);
            tvFeedbackDate = itemView.findViewById(R.id.txt_feedbackDate);
            tvFeedbackCategoryName = itemView.findViewById(R.id.txt_categoryName);
            tvFeedbackTitle = itemView.findViewById(R.id.txt_feedbackTitle);
            tvFeedbackId = itemView.findViewById(R.id.txt_feedbackId);
            rlChat = itemView.findViewById(R.id.rl_chat);
        }
    }
}
