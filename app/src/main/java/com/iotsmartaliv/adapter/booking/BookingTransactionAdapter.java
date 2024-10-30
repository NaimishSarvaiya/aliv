package com.iotsmartaliv.adapter.booking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.iotsmartaliv.R;
import com.iotsmartaliv.model.booking.PaymentMethodModel;
import com.iotsmartaliv.model.booking.TransactionModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class BookingTransactionAdapter extends RecyclerView.Adapter<BookingTransactionAdapter.BookingTransactionViewHolder> {

    private Context context;
    private List<TransactionModel> transactionList;
    private OnItemClickListener onItemClickListener;

    public BookingTransactionAdapter(Context context, List<TransactionModel> transactionList, OnItemClickListener itemClickListener) {
        this.context = context;
        this.transactionList = transactionList;
        this.onItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public BookingTransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.booking_transaction_item, parent, false);
        return new BookingTransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingTransactionViewHolder holder, int position) {
        TransactionModel transaction = transactionList.get(position);
        Map<String, String> metadata = transaction.getMetadata();
        // Set room name and booking ID
        if (metadata != null) {
            String roomName = metadata.get("room_name");
            String bookingId = metadata.get("booking_id");

            if (roomName != null) {
                holder.tvTitle.setText(roomName);
            } else {
                holder.tvTitle.setText("N/A");
            }

            if (bookingId != null) {
                holder.tvBookingId.setText(bookingId);
            } else {
                holder.tvBookingId.setText("N/A");
            }
        }
//        holder.tvTitle.setText(transaction.getMetadata().getRoom_name());
//        holder.tvBookingId.setText("Booking ID: " + transaction.getMetadata().getBooking_id());

        // Set date
        holder.tvDate.setText(convertTimestampToDate(transaction.getCreated()));

        // Configure amount and refunded amount display
        if (transaction.isCaptured()) {
            holder.tvAmount.setTextColor(ContextCompat.getColor(context, R.color.red));
            holder.tvRefunded.setTextColor(ContextCompat.getColor(context, R.color.green));
            holder.tvAmount.setText("$" + (transaction.getAmount_captured() / 100.0));

            if (transaction.getAmount() != transaction.getAmount_captured() &&
                    transaction.getAmount() != transaction.getAmount_refunded()) {
                holder.tvRefunded.setVisibility(View.VISIBLE);
                holder.tvRefunded.setText("$" + (transaction.getAmount_refunded() / 100.0));
            } else if (transaction.getAmount() != transaction.getAmount_captured() ||
                    transaction.getAmount_refunded() != 0 && transaction.getAmount() != transaction.getAmount_refunded()) {
                holder.tvRefunded.setVisibility(View.VISIBLE);
                holder.tvAmount.setText("$" + (transaction.getAmount_captured() - transaction.getAmount_refunded()) / 100 );
                holder.tvRefunded.setText("$" + (transaction.getAmount_refunded() / 100.0) );
            } else {
                holder.tvRefunded.setVisibility(View.GONE);
            }

        } else if (transaction.isRefunded()) {
            holder.tvAmount.setTextColor(ContextCompat.getColor(context, R.color.green));
            holder.tvRefunded.setTextColor(ContextCompat.getColor(context, R.color.red));
            holder.tvAmount.setText("$" + (transaction.getAmount_refunded() / 100.0));

            if (transaction.getAmount() != transaction.getAmount_received() &&
                    transaction.getAmount() != transaction.getAmount_refunded()) {
                holder.tvRefunded.setVisibility(View.VISIBLE);
                holder.tvRefunded.setText("$" + (transaction.getAmount_received() / 100.0));
            } else {
                holder.tvRefunded.setVisibility(View.GONE);
            }

        } else {
            holder.tvAmount.setTextColor(ContextCompat.getColor(context, R.color.newNavyBuleBaseColor));
            holder.tvRefunded.setTextColor(ContextCompat.getColor(context, R.color.red));
            holder.tvAmount.setText("$" + (transaction.getAmount() / 100.0));

            if (transaction.getAmount() != transaction.getAmount_captured() && transaction.getAmount_captured() > 0) {
                holder.tvRefunded.setVisibility(View.VISIBLE);
                holder.tvRefunded.setText("$" + (transaction.getAmount_captured() / 100.0));
            } else {
                holder.tvRefunded.setVisibility(View.GONE);
            }
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(position, transaction);
            }
        });
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    // Helper method to convert timestamp to date
    private String convertTimestampToDate(long timestamp) {
        Date date = new Date(timestamp * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy 'at' h:mm:ss a", Locale.getDefault());
        return sdf.format(date);
    }

    public class BookingTransactionViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDate, tvAmount, tvRefunded, tvBookingId;

        public BookingTransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_room);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvAmount = itemView.findViewById(R.id.tv_rate);
            tvRefunded = itemView.findViewById(R.id.tv_refunded);
            tvBookingId = itemView.findViewById(R.id.tv_bookingID);
        }
    }

    public void addData(List<TransactionModel> transaction) {
        transactionList.clear();
        transactionList.addAll(transaction);
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(int position, TransactionModel transactionData);
    }

}
