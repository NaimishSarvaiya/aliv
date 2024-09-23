package com.iotsmartaliv.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.iotsmartaliv.R;
import com.iotsmartaliv.apiAndSocket.models.SearchBookingData;
import com.iotsmartaliv.apiAndSocket.models.TimeSlot;
import com.iotsmartaliv.dialog_box.SelectTimeSlotForRoomDialog;

import java.util.ArrayList;

/**
 * This class is used as select room Adapter
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 23/7/19 :July.
 */
public class SelectRoomAdapter extends RecyclerView.Adapter<SelectRoomAdapter.ViewHolder> {
    Context context;
    private ArrayList<SearchBookingData> search_rooms;

    public SelectRoomAdapter(Context context, ArrayList<SearchBookingData> search_rooms) {
        this.context = context;
        this.search_rooms = search_rooms;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_select_room, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.tv_roomNo.setText(search_rooms.get(i).getRoomDetails().getRoomName());
        viewHolder.slots_detail.setText(search_rooms.get(i).getRemaining());
        viewHolder.checkBox.setChecked(search_rooms.get(i).isSelectedForBooing());
        Glide.with(context)
                .load(search_rooms.get(i).getRoomDetails().getRoomImage())
                .placeholder(R.mipmap.ic_room)
                .fitCenter()
                .into(viewHolder.imageView);
        viewHolder.checkboxLay.setOnClickListener(v -> {
            if (viewHolder.checkBox.isChecked()) {
                for (TimeSlot timeSlot : search_rooms.get(i).getTimeSlots()) {
                    timeSlot.setSelected(false);
                }
                search_rooms.get(i).setPurpuse("");
                viewHolder.checkBox.setChecked(false);
                search_rooms.get(i).setSelectedForBooing(false);

            } else {
                new SelectTimeSlotForRoomDialog(context, search_rooms.get(i), new SelectItemListener() {
                    @Override
                    public void doneBtnSelected() {
                        viewHolder.checkBox.setChecked(true);
                        search_rooms.get(i).setSelectedForBooing(true);
                    }

                    @Override
                    public void cancelBtnClick() {

                    }
                }).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return search_rooms.size();
    }

    public interface SelectItemListener {
        void doneBtnSelected();

        void cancelBtnClick();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_roomNo, slots_detail;
        CheckBox checkBox;
        LinearLayout checkboxLay;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_roomNo = itemView.findViewById(R.id.tv_room_no);
            slots_detail = itemView.findViewById(R.id.slots_detail);
            checkBox = itemView.findViewById(R.id.checkbox);
            imageView = itemView.findViewById(R.id.imageView);
            checkboxLay = itemView.findViewById(R.id.checkboxLay);
        }
    }


}
