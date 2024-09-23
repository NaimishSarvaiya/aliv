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

import com.bumptech.glide.Glide;
import com.iotsmartaliv.R;
import com.iotsmartaliv.activity.feedback.FilePreviewActivity;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.model.feedback.MessageHistoryData;
import com.iotsmartaliv.utils.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FeedbackChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_SENT = 1;
    private static final int VIEW_TYPE_RECEIVED = 2;
    private static final int VIEW_TYPE_DATE_HEADER = 3;

    private Context context;
    private List<Object> itemList;

    public FeedbackChatAdapter(Context context, List<MessageHistoryData> messageList) {
        this.context = context;
        this.itemList = prepareItemList(messageList); // Prepare items with date headers
    }

    private List<Object> prepareItemList(List<MessageHistoryData> messageList) {
        List<Object> itemList = new ArrayList<>();
        List<Object> tempDayMessages = new ArrayList<>();
        String lastDate = "";

//        // Temporary list to hold messages for a specific day
//        List<MessageHistoryData> tempDayMessages = new ArrayList<>();

        for (MessageHistoryData message : messageList) {
            // Get the formatted date for comparison (e.g., "September 21, 2024")
            String currentDate = formatDate(message.getCreated_at());

            // If this is a new date, we need to process the previous date's messages
            if (!currentDate.equals(lastDate) && !lastDate.isEmpty()) {
                // Add all messages for the previous date
                itemList.addAll(tempDayMessages);
                // Now add the date header AFTER the messages
                itemList.add(lastDate);

                // Clear temp list for the next set of messages
                tempDayMessages.clear();
            }

            // Add the message to the temp list
            tempDayMessages.add(message);
            lastDate = currentDate;  // Update the lastDate to the current date
        }

        // Handle the last date group if there are remaining messages
        if (!tempDayMessages.isEmpty()) {
            itemList.addAll(tempDayMessages);
            if (!this.itemList.contains(lastDate)) {
                itemList.add(lastDate);
            }// Add the last date header
        }

        return itemList;
    }

    @Override
    public int getItemViewType(int position) {
        if (itemList.get(position) instanceof String) {
            return VIEW_TYPE_DATE_HEADER;
        } else {
            MessageHistoryData message = (MessageHistoryData) itemList.get(position);
            if (message.getMessage_from().equals("1")) {
                return VIEW_TYPE_SENT;
            } else {
                return VIEW_TYPE_RECEIVED;
            }
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SENT) {
            View view = LayoutInflater.from(context).inflate(R.layout.sent_message_item, parent, false);
            return new SentMessageViewHolder(view);
        } else if (viewType == VIEW_TYPE_RECEIVED) {
            View view = LayoutInflater.from(context).inflate(R.layout.received_message_item, parent, false);
            return new ReceivedMessageViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.date_header_item, parent, false);
            return new DateHeaderViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == VIEW_TYPE_SENT) {
            MessageHistoryData message = (MessageHistoryData) itemList.get(position);
            ((SentMessageViewHolder) holder).bind(message);
        } else if (holder.getItemViewType() == VIEW_TYPE_RECEIVED) {
            MessageHistoryData message = (MessageHistoryData) itemList.get(position);
            ((ReceivedMessageViewHolder) holder).bind(message);
        } else {
            String date = (String) itemList.get(position);
            ((DateHeaderViewHolder) holder).bind(date);
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void updateData(List<MessageHistoryData> newMessages) {
        itemList.addAll(prepareItemList(newMessages));
        notifyDataSetChanged();
    }


    public void addMessage(List<MessageHistoryData> newMessages) {
        List<Object> newItems = prepareItemList(newMessages);
        itemList.addAll(0, newItems);
        notifyItemRangeInserted(0, newItems.size());
    }

    private class SentMessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageText, messageTime;
        RelativeLayout rlsentDoc;
        ImageView imgSent,imgRead;

        SentMessageViewHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.text_message_body);
            messageTime = itemView.findViewById(R.id.text_message_time);
            rlsentDoc = itemView.findViewById(R.id.rl_sentDocument);
            imgSent = itemView.findViewById(R.id.img_sentDoc);
            imgRead = itemView.findViewById(R.id.img_messageSeen);
        }

        void bind(MessageHistoryData message) {
            messageTime.setText(formatTime(message.getCreated_at()));
            if (message.getMessage() != null && !message.getMessage().isEmpty()) {
                messageText.setText(Util.decodeChatString(message.getMessage()));
                messageText.setVisibility(View.VISIBLE);
            } else {
                messageText.setVisibility(View.GONE);
            }
            if (message.getRead_status().equals("1")){
                imgRead.setImageResource(R.drawable.messasge_read);
            }else {
                imgRead.setImageResource(R.drawable.ic_message_sent);
            }

            if (message.getMessage_document() != null && !message.getMessage_document().isEmpty()) {
                String fileUri = message.getMessage_document();
                rlsentDoc.setVisibility(View.VISIBLE);
                if (fileUri.endsWith(".pdf")) {
                    Glide.with(context)
                            .load(fileUri)
                            .override(800, 800) // Resize the image for preview
                            .placeholder(R.drawable.attached_file) // Placeholder while loading
                            .into(imgSent);
                } else {
                    Glide.with(context)
                            .load(fileUri)
                            .override(800, 800) // Resize the image for preview
                            .placeholder(R.drawable.attached_file) // Placeholder while loading
                            .into(imgSent);
                }
            } else {
                rlsentDoc.setVisibility(View.GONE);
            }
            rlsentDoc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    filePreview(message);
                }
            });
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

            if (message.getMessage_document() != null && !message.getMessage_document().isEmpty()) {
                String fileUri = message.getMessage_document();
                rlreceiveDoc.setVisibility(View.VISIBLE);
                if (fileUri.endsWith(".pdf")) {
                    Glide.with(context)
                            .load(fileUri)
                            .override(800, 800) // Resize the image for preview
                            .placeholder(R.drawable.attached_file) // Placeholder while loading
                            .into(imgReceive);
                } else {
                    Glide.with(context)
                            .load(fileUri)
                            .override(800, 800) // Resize the image for preview
                            .placeholder(R.drawable.attached_file) // Placeholder while loading
                            .into(imgReceive);
                }
            } else {
                rlreceiveDoc.setVisibility(View.GONE);
            }
            rlreceiveDoc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    filePreview(message);
                }
            });
        }
    }

    void filePreview(MessageHistoryData message) {
        Intent intent = new Intent(context, FilePreviewActivity.class);
        intent.putExtra(Constant.PATH, Constant.FROM_FEEDBACK_DETAILS);
        intent.putExtra(Constant.FILE_URI, message.getMessage_document());
        context.startActivity(intent);
    }

    private class DateHeaderViewHolder extends RecyclerView.ViewHolder {
        TextView dateHeader;

        DateHeaderViewHolder(View itemView) {
            super(itemView);
            dateHeader = itemView.findViewById(R.id.text_message_date);
        }

        void bind(String date) {
            dateHeader.setText(date);
        }
    }

    private String formatDate(String dateStr) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());
            Date date = inputFormat.parse(dateStr);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return dateStr;
        }
    }

    private String formatTime(String dateStr) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            Date date = inputFormat.parse(dateStr);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return dateStr;
        }
    }
}
