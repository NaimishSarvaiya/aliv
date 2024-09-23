package com.iotsmartaliv.adapter.automation;

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

import com.bumptech.glide.Glide;
import com.iotsmartaliv.R;
import com.iotsmartaliv.activity.feedback.FilePreviewActivity;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.model.feedback.MessageHistoryData;
import com.iotsmartaliv.utils.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FeedbackHistoryChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_SENT = 1;
    private static final int VIEW_TYPE_RECEIVED = 2;

    private Context context;
    private List<MessageHistoryData> messageList;

    public FeedbackHistoryChatAdapter(Context context, List<MessageHistoryData> messageList) {
        this.context = context;
        this.messageList = messageList;
    }

    @Override
    public int getItemViewType(int position) {
        MessageHistoryData message = messageList.get(position);
        return message.getMessage_from().equals("1") ? VIEW_TYPE_SENT : VIEW_TYPE_RECEIVED;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SENT) {
            View view = LayoutInflater.from(context).inflate(R.layout.sent_message_item, parent, false);
            return new SentMessageViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.received_message_item, parent, false);
            return new ReceivedMessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == VIEW_TYPE_SENT) {
            ((SentMessageViewHolder) holder).bind(messageList.get(position));
        } else {
            ((ReceivedMessageViewHolder) holder).bind(messageList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public void updateData(List<MessageHistoryData> newMessages) {
        messageList.addAll(newMessages);
        notifyDataSetChanged();
    }

    public void addMessage(List<MessageHistoryData> newMessages) {
        messageList.addAll(0, newMessages);
        notifyItemRangeInserted(0, newMessages.size());
    }

    private class SentMessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageText, messageTime;
        RelativeLayout rlsentDoc;
        ImageView imgSent, imgRead;

        SentMessageViewHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.text_message_body);
            messageTime = itemView.findViewById(R.id.text_message_time);
            rlsentDoc = itemView.findViewById(R.id.rl_sentDocument);
            imgSent = itemView.findViewById(R.id.img_sentDoc);
            imgRead = itemView.findViewById(R.id.img_messageSeen);
        }

        void bind(MessageHistoryData message) {
            messageText.setText(Util.decodeChatString(message.getMessage()));
            messageTime.setText(formatTime(message.getCreated_at()));
            if (message.getMessage() != null && !message.getMessage().isEmpty()) {
                messageText.setText(message.getMessage());
                messageText.setVisibility(View.VISIBLE);
            } else {
                messageText.setVisibility(View.GONE);
            }

            // Show read status
            imgRead.setImageResource(message.getRead_status().equals("1") ? R.drawable.messasge_read : R.drawable.ic_message_sent);

            // Show document if available
            if (message.getMessage_document() != null && !message.getMessage_document().isEmpty()) {
                String fileUri = message.getMessage_document();
                rlsentDoc.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(fileUri)
                        .override(800, 800)
                        .placeholder(R.drawable.attached_file)
                        .into(imgSent);
            } else {
                rlsentDoc.setVisibility(View.GONE);
            }

            rlsentDoc.setOnClickListener(v -> filePreview(message));
        }
    }

    private class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageText, messageTime;
        RelativeLayout rlreceiveDoc;
        ImageView imgReceive;

        ReceivedMessageViewHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.text_message_body);
            messageTime = itemView.findViewById(R.id.text_message_time);
            rlreceiveDoc = itemView.findViewById(R.id.rl_reciveDocument);
            imgReceive = itemView.findViewById(R.id.img_recive);
        }

        void bind(MessageHistoryData message) {
            messageText.setText(Util.decodeChatString(message.getMessage()));
            messageTime.setText(formatTime(message.getCreated_at()));
            if (message.getMessage() != null && !message.getMessage().isEmpty()) {
                messageText.setText(message.getMessage());
                messageText.setVisibility(View.VISIBLE);
            } else {
                messageText.setVisibility(View.GONE);
            }

            // Show document if available
            if (message.getMessage_document() != null && !message.getMessage_document().isEmpty()) {
                String fileUri = message.getMessage_document();
                rlreceiveDoc.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(fileUri)
                        .override(800, 800)
                        .placeholder(R.drawable.attached_file)
                        .into(imgReceive);
            } else {
                rlreceiveDoc.setVisibility(View.GONE);
            }

            rlreceiveDoc.setOnClickListener(v -> filePreview(message));
        }
    }

    private void filePreview(MessageHistoryData message) {
        Intent intent = new Intent(context, FilePreviewActivity.class);
        intent.putExtra(Constant.PATH, Constant.FROM_FEEDBACK_DETAILS);
        intent.putExtra(Constant.FILE_URI, message.getMessage_document());
        context.startActivity(intent);
    }

    private String formatTime(String dateStr) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MMM-yy, hh:mm a", Locale.getDefault());
            Date date = inputFormat.parse(dateStr);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return dateStr;
        }
    }
}
