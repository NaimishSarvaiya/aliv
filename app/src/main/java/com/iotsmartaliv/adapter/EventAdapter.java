package com.iotsmartaliv.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.iotsmartaliv.R;
import com.iotsmartaliv.activity.ViewPager.BroadcastDetailActivity;
import com.iotsmartaliv.apiAndSocket.models.Broadcast;

import java.util.ArrayList;

/**
 * This class is used to display event row
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 03 Apr,2021
 */
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private Context mcx;
    private ArrayList<Broadcast> eventlList;

    public EventAdapter (Context mcx, ArrayList<Broadcast> eventlList){
        this.mcx = mcx;
        this.eventlList = eventlList;
    }

    public void setListUdpate(ArrayList<Broadcast> eventlList){
        this.eventlList = eventlList;
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public EventAdapter.EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mcx);
        View view = inflater.inflate(R.layout.event_items,null);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventAdapter.EventViewHolder holder, int position) {
        
        Broadcast mEvent = eventlList.get(position);

        setView(holder,mEvent);

        holder.itemView.setOnClickListener(view -> {

            Intent intent = new Intent(mcx, BroadcastDetailActivity.class);

            intent.putExtra("BROADCAST_ITEM",mEvent);

            mcx.startActivity(intent);

        });

    }

    private void setView(EventViewHolder holder, Broadcast mEvent) {

        holder.eventHead.setText(mEvent.getBroadcastTitle());

        holder.eventDate.setText(mEvent.getBuCreatedAt());

        holder.eventOrganizer.setText("Organizer :- "+(CharSequence) mEvent.getEventOrganizer());

        if (mEvent.getReadStatus().equalsIgnoreCase("0")){

            holder.eventHead.setTypeface(null, Typeface.BOLD);

            holder.eventDate.setTypeface(null, Typeface.BOLD);

            holder.eventOrganizer.setTypeface(null, Typeface.BOLD);
            holder.rlMain.setBackgroundColor(ContextCompat.getColor(mcx, R.color.unReadBroacastColor));


        }else {

            holder.eventHead.setTypeface(null, Typeface.BOLD);

            holder.eventDate.setTypeface(null, Typeface.NORMAL);

            holder.eventOrganizer.setTypeface(null, Typeface.NORMAL);
            holder.rlMain.setBackgroundColor(ContextCompat.getColor(mcx, R.color.white));

        }
    }


    @Override
    public int getItemCount() {
        return eventlList.size();
    }

    public class EventViewHolder extends RecyclerView.ViewHolder{
        TextView eventHead,eventDate,eventOrganizer;
        ImageView eventImage;
        RelativeLayout rlMain;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);

            eventHead = itemView.findViewById(R.id.txt_message_head_event);
            eventDate = itemView.findViewById(R.id.txt_message_date_event);
            eventOrganizer = itemView.findViewById(R.id.txt_organizer_event);
            eventImage = itemView.findViewById(R.id.img_eventforward);
            rlMain = itemView.findViewById(R.id.rl_main);
        }
    }

}
