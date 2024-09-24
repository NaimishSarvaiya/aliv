package com.iotsmartaliv.adapter.booking;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iotsmartaliv.R;
import com.iotsmartaliv.activity.booking.BookingDetailsActivity;
import com.iotsmartaliv.utils.Util;

public class ActiveBookingAdapter extends RecyclerView.Adapter<ActiveBookingAdapter.ActiveBookingViewHolder> {
    private Context context;

    public ActiveBookingAdapter(Context context) {
        this.context = context;
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

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, BookingDetailsActivity.class);
            context.startActivity(intent);
        });
        Util.setBrightness(holder.img_preview, 2.0f);

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class ActiveBookingViewHolder extends RecyclerView.ViewHolder{
        ImageView img_preview;

        public ActiveBookingViewHolder(@NonNull View itemView) {
            super(itemView);
            img_preview = itemView.findViewById(R.id.img_preview);
        }
    }
}
