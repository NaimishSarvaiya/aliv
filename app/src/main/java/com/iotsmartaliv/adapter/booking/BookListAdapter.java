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
import com.iotsmartaliv.model.booking.RoomModel;
import com.iotsmartaliv.model.feedback.FeedbackData;
import com.iotsmartaliv.utils.Util;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.BookListViewHolder> {
    private Context context;
    private ArrayList<RoomData> roomDataList;

    private String startDate="";
    private String endDate="";

    public BookListAdapter(Context context, ArrayList<RoomData> roomDataList) {
        this.context = context;
        this.roomDataList = roomDataList;

    }

    @NonNull
    @Override
    public BookListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.book_item, parent, false);
        return new BookListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookListViewHolder holder, int position) {
        RoomData roomData = roomDataList.get(position);

        Util.setBrightness(holder.imgRoom, 2.0f);
        if (roomData.getRoomName()!=null) {
            holder.tvRoomTitle.setText(roomData.getRoomName());
        }
        if (roomData.getCommunityName()!=null) {
            holder.tvCommunityName.setText(roomData.getCommunityName());
        }
        if (roomData.getRoomBasePrice()!=null) {
            holder.tvPrice.setText("Price : $" + roomData.getRoomBasePrice());
        }
        if (roomData.getRoomImage()!=null) {
            Glide.with(context)
                    .load(roomData.getRoomImage())
//                .override(800, 800) // Resize the image for preview
                    .placeholder(R.mipmap.ic_room) // Placeholder while loading
                    .into(holder.imgRoom);
        }
        holder.itemView.setOnClickListener(v -> {
            if (roomData.getRoomBookLimit()!=null){
                dayCount(Integer.parseInt(roomData.getRoomBookLimit()));
            }else {
                dayCount(10);
            }
            Intent intent = new Intent(context, RoomBookingActivity.class);
            intent.putExtra(Constant.ROOM_ID,roomData.getRoomID());
            intent.putExtra(Constant.ROOM_START_DATE,startDate);
            intent.putExtra(Constant.ROOM_END_DATE,endDate);
            intent.putExtra(Constant.ROOM_TITLE,roomData.getRoomName());
            intent.putExtra(Constant.ROOM_TYPE,roomData.getRoomType());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return roomDataList.size();
    }

    public class BookListViewHolder extends RecyclerView.ViewHolder {
        ImageView imgRoom;
        TextView tvRoomTitle,tvCommunityName,tvPrice;

        public BookListViewHolder(@NonNull View itemView) {
            super(itemView);
            imgRoom = itemView.findViewById(R.id.img_room);
            tvRoomTitle = itemView.findViewById(R.id.tv_roomTitle);
            tvCommunityName = itemView.findViewById(R.id.tv_communityName);
            tvPrice = itemView.findViewById(R.id.tv_price);

        }
    }

    public void refreshItems(ArrayList<RoomData> newRoomList) {
        roomDataList.clear(); // Clear existing data
        roomDataList.addAll(newRoomList); // Add new data
        notifyDataSetChanged(); // Notify adapter of the changes
    }

    public void addItems(ArrayList<RoomData> newRoomList) {
        roomDataList.addAll(newRoomList); // Append new data
        notifyDataSetChanged(); // Notify adapter of the changes
    }
    void dayCount(int count){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        // Get today's date
        Calendar calendar = Calendar.getInstance();
       startDate = dateFormat.format(calendar.getTime());

        // Calculate the end date by adding count - 1 days to today's date
        calendar.add(Calendar.DAY_OF_YEAR, count - 1);
       endDate = dateFormat.format(calendar.getTime());
        Log.e("count", String.valueOf(count));
        Log.e("startDate",startDate);
        Log.e("endDate",endDate);

    }

}
