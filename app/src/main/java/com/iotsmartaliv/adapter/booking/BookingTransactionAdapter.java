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
import com.iotsmartaliv.model.booking.TransactionModel;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class BookingTransactionAdapter extends RecyclerView.Adapter<BookingTransactionAdapter.BookingTransactionViewHolder> {

    Context context;
    ArrayList<TransactionModel> transactionList;

    public BookingTransactionAdapter(Context context, ArrayList<TransactionModel> transactionList) {
        this.context = context;
        this.transactionList = transactionList;
    }

    public void setContext(Context context) {
        this.context = context;
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
        holder.tvTitle.setText(transactionList.get(position).getBookingName());
        holder.tvDate.setText(transactionList.get(position).getBookingDate());
        holder.tvRate.setText(transactionList.get(position).getRate());

        if (transactionList.get(position).getRateIncrese() == 1){
            holder.tvRate.setTextColor(ContextCompat.getColor(context,R.color.green));
            holder.rateStatus.setImageResource(R.drawable.ic_rate_up);
        }else if (transactionList.get(position).getRateIncrese() == 2){
            holder.tvRate.setText("-"+transactionList.get(position).getRate());
            holder.tvRate.setTextColor(ContextCompat.getColor(context,R.color.red));
            holder.rateStatus.setImageResource(R.drawable.ic_rate_down);
        }else {
            holder.tvRate.setTextColor(ContextCompat.getColor(context,R.color.newNavyBuleBaseColor));
            holder.rateStatus.setImageResource(R.drawable.ic_flat);
        }

    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public class BookingTransactionViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDate, tvRate;
        ImageView rateStatus;


        public BookingTransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_room);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvRate = itemView.findViewById(R.id.tv_rate);
            rateStatus = itemView.findViewById(R.id.img_rateStatus);
        }
    }
}
