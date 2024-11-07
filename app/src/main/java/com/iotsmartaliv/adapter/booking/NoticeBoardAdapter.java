package com.iotsmartaliv.adapter.booking;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.iotsmartaliv.R;
import com.iotsmartaliv.activity.booking.RoomBookingActivity;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.model.booking.RoomData;
import com.iotsmartaliv.utils.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class NoticeBoardAdapter extends RecyclerView.Adapter<NoticeBoardAdapter.BookListViewHolder> {
    private Context context;


    public NoticeBoardAdapter(Context context) {
        this.context = context;


    }

    @NonNull
    @Override
    public BookListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.notice_borad_item, parent, false);
        return new BookListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookListViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return 6;
    }

    public class BookListViewHolder extends RecyclerView.ViewHolder {
        ImageView imgRoom;

        public BookListViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }

}