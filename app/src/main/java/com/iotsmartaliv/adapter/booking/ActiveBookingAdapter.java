package com.iotsmartaliv.adapter.booking;

import static com.iotsmartaliv.constants.Constant.BOOKING_ID;
import static com.iotsmartaliv.utils.Util.convertDateFormatForBooking;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.iotsmartaliv.R;
import com.iotsmartaliv.activity.booking.BookingDetailsActivity;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.model.booking.ActiveBookingData;
import com.iotsmartaliv.model.booking.RoomData;
import com.iotsmartaliv.utils.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ActiveBookingAdapter extends RecyclerView.Adapter<ActiveBookingAdapter.ActiveBookingViewHolder> {
    private Context context;
    List<ActiveBookingData> bookingData = new ArrayList<>();
    String path;

    public ActiveBookingAdapter(Context context, List<ActiveBookingData> bookingData,String path) {
        this.context = context;
        this.bookingData = bookingData;
        this.path = path;
    }

    @NonNull
    @Override
    public ActiveBookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.active_booking_item, parent, false);
        return new ActiveBookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActiveBookingViewHolder holder, int position) {
        ActiveBookingData activeBookingData = bookingData.get(position);
        holder.tvBookingId.setText("#" + activeBookingData.getBookingID());
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, BookingDetailsActivity.class);
            intent.putExtra(BOOKING_ID,activeBookingData.getBookingID());
            intent.putExtra(Constant.PATH,path);
            context.startActivity(intent);
        });
        Util.setBrightness(holder.img_preview, 2.0f);
        if (activeBookingData.getRoomImage() != null) {
            Glide.with(context)
                    .load(activeBookingData.getRoomImage().get(0))
//                .override(800, 800) // Resize the image for preview
                    .placeholder(R.mipmap.ic_room) // Placeholder while loading
                    .into(holder.img_preview);
        }
        holder.tvCommunity.setText(activeBookingData.getRoomName());
        holder.tvStartDate.setText("FROM: " + convertDateFormatForBooking(activeBookingData.getStartDate()));
        holder.tvEndDate.setText("TO: " + convertDateFormatForBooking(activeBookingData.getEndDate()));
        SimpleDateFormat inputFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        String formattedEndTime;
        String formattedStartTime;
        try {
            formattedEndTime = outputFormat.format(inputFormat.parse(activeBookingData.getEndTime()));
            formattedStartTime = outputFormat.format(inputFormat.parse(activeBookingData.getStartTime()));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        // Bind the time slot data to UI elements

        holder.tvBookingSlot.setText(formattedStartTime + " - " + formattedEndTime);
    }

    @Override
    public int getItemCount() {
        return bookingData.size();
    }

    public void add(List<ActiveBookingData> data) {
        bookingData.clear();
        bookingData.addAll(data);
        notifyDataSetChanged();
    }

    public class ActiveBookingViewHolder extends RecyclerView.ViewHolder {
        ImageView img_preview;
        TextView tvBookingId, tvCommunity, tvStartDate, tvEndDate, tvBookingSlot;

        public ActiveBookingViewHolder(@NonNull View itemView) {
            super(itemView);
            img_preview = itemView.findViewById(R.id.img_preview);
            tvBookingId = itemView.findViewById(R.id.tv_bookingId);
            tvCommunity = itemView.findViewById(R.id.tv_community);
            tvStartDate = itemView.findViewById(R.id.tv_bookingFrom);
            tvEndDate = itemView.findViewById(R.id.tv_bookingTo);
            tvBookingSlot = itemView.findViewById(R.id.tv_bookingSlot);
        }
    }

    public void refreshItems( List<ActiveBookingData> newData) {
        bookingData.clear(); // Clear existing data
        bookingData.addAll(newData); // Add new data
        notifyDataSetChanged(); // Notify adapter of the changes
    }

    public void addItems(List<ActiveBookingData> newData) {
        bookingData.addAll(newData); // Append new data
        notifyDataSetChanged(); // Notify adapter of the changes
    }
}
