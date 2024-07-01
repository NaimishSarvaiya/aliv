package com.iotsmartaliv.adapter;

import android.app.Activity;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.iotsmartaliv.R;
import com.iotsmartaliv.activity.booking.BookedRoomsActivity;
import com.iotsmartaliv.model.BookingData;

import java.util.List;

/**
 * This class is used as select room Adapter
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 23/7/19 :July.
 */
public class BookingsAdapter extends RecyclerView.Adapter<BookingsAdapter.ViewHolder> {
    Activity context;
    private BookingCancelListener bookingCancelListener;
    private List<BookingData> bookingData;

    public BookingsAdapter(Activity context, List<BookingData> bookingData, BookingCancelListener bookingCancelListener) {
        this.context = context;
        this.bookingData = bookingData;
        this.bookingCancelListener = bookingCancelListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_bookings, viewGroup, false);
        return new ViewHolder(view);
    }

    public void removeCancelBooking(int pos) {
        bookingData.remove(pos);
        notifyItemRemoved(pos);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.tv_booking_no.setText("Booking No: " + bookingData.get(i).getBookingNumber());
        viewHolder.tv_start_date.setText("Start Date: " + bookingData.get(i).getBookingStartDate());
        viewHolder.tv_end_date.setText("End Date: " + bookingData.get(i).getBookingEndDate());
        viewHolder.cancel_booking_btn.setOnClickListener(v -> {
            bookingCancelListener.cancelBooking(bookingData.get(viewHolder.getAdapterPosition()), viewHolder.getAdapterPosition());
        });
        viewHolder.itemView.setOnClickListener(v -> {
            context.startActivityForResult(new Intent(context, BookedRoomsActivity.class).putExtra("booking_ID", bookingData.get(viewHolder.getAdapterPosition()).getBookingID()), 121);
        });
    }

    @Override
    public int getItemCount() {
        return bookingData.size();
    }

    public void setData(List<BookingData> data) {
        this.bookingData = data;
        notifyDataSetChanged();
    }

    public interface BookingCancelListener {
        void cancelBooking(BookingData bookingData, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_booking_no, tv_start_date, tv_end_date;
        Button cancel_booking_btn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_booking_no = itemView.findViewById(R.id.tv_booking_no);
            tv_start_date = itemView.findViewById(R.id.tv_start_date);
            tv_end_date = itemView.findViewById(R.id.tv_end_date);
            cancel_booking_btn = itemView.findViewById(R.id.cancel_booking_btn);
        }
    }


}
