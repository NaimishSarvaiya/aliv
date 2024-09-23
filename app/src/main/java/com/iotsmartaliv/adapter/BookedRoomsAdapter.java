package com.iotsmartaliv.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.iotsmartaliv.R;
import com.iotsmartaliv.apiAndSocket.models.TimeSlot;
import com.iotsmartaliv.model.BookRoomData;

import java.util.List;

/**
 * This class is used as select room Adapter
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 23/7/19 :July.
 */
public class BookedRoomsAdapter extends RecyclerView.Adapter<BookedRoomsAdapter.ViewHolder> {
    Context context;
    RoomCancelListener roomCancelListener;
    private List<BookRoomData> bookingData;

    public BookedRoomsAdapter(Context context, List<BookRoomData> bookingData, RoomCancelListener roomCancelListener) {
        this.context = context;
        this.bookingData = bookingData;
        this.roomCancelListener = roomCancelListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_booking_rooms, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.tv_room_name.setText("Room Name: " + bookingData.get(i).getRoomName());
        Glide.with(context)
                .load(bookingData.get(i).getRoomImage())
                .placeholder(R.mipmap.ic_room)
                .fitCenter()
                .into(viewHolder.imageView);
        viewHolder.timeSlotTv.setText("");
        for (TimeSlot timeSlot : bookingData.get(i).getTimeSlots()) {
            if (viewHolder.timeSlotTv.length() > 0) {
                viewHolder.timeSlotTv.append(", \n");
            }
            viewHolder.timeSlotTv.append(timeSlot.getStartTime() + "-" + timeSlot.getEndTime());
        }
        viewHolder.cancel_room_btn.setOnClickListener(v -> {
            roomCancelListener.cancelRoom(bookingData.get(viewHolder.getAdapterPosition()), viewHolder.getAdapterPosition());
        });

    }

    @Override
    public int getItemCount() {
        return bookingData.size();
    }

    public void setData(List<BookRoomData> data) {
        this.bookingData = data;
        notifyDataSetChanged();
    }

    public void removeCancelBooking(int pos) {
        bookingData.remove(pos);
        notifyItemRemoved(pos);
    }

    public interface RoomCancelListener {
        void cancelRoom(BookRoomData roomData, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_room_name, timeSlotTv;
        ImageView imageView;
        Button cancel_room_btn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cancel_room_btn = itemView.findViewById(R.id.cancel_room_btn);
            tv_room_name = itemView.findViewById(R.id.tv_room_name);
            timeSlotTv = itemView.findViewById(R.id.timeSlotTv);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }


}
