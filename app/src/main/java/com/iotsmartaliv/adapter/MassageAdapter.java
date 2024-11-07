package com.iotsmartaliv.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.iotsmartaliv.R;
import com.iotsmartaliv.activity.ViewPager.BroadcastDetailActivity;
import com.iotsmartaliv.apiAndSocket.models.Broadcast;
import java.util.ArrayList;

/**
 * This class is used to display massage row.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 31 Mar,2021
 */
public class MassageAdapter extends RecyclerView.Adapter<MassageAdapter.MassageViewHolder>{

    private Context mcx;
    private ArrayList<Broadcast> messageList = new ArrayList<>();

    public MassageAdapter(Context mcx, ArrayList<Broadcast> messageList) {
        this.mcx = mcx;
        this.messageList = messageList;
    }

    public void setListUdpate(ArrayList<Broadcast> messageList){
        this.messageList = messageList;
        notifyDataSetChanged();

    }


    @NonNull
    @Override
    public MassageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mcx);
        View view = inflater.inflate(R.layout.massage_items,null);
        return new MassageViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MassageViewHolder holder, int position) {

        Broadcast mEvent = messageList.get(position);

        setView(holder,mEvent);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mcx, BroadcastDetailActivity.class);
                
                intent.putExtra("BROADCAST_ITEM",mEvent);
                
                mcx.startActivity(intent);

            }
        });

    }

    private void setView(MassageViewHolder holder, Broadcast mEvent) {

        holder.txtHead.setText(mEvent.getBroadcastTitle());
        
        holder.txtDate.setText(mEvent.getBuCreatedAt());

        if (mEvent.getReadStatus().equalsIgnoreCase("0")){

            holder.txtHead.setTypeface(null, Typeface.BOLD);

            holder.txtDate.setTypeface(null, Typeface.BOLD);
            holder.llMain.setBackgroundColor(ContextCompat.getColor(mcx, R.color.unReadBroacastColor));
            
        }else {
            holder.txtHead.setTypeface(null, Typeface.NORMAL);
            holder.txtDate.setTypeface(null, Typeface.NORMAL);
            holder.llMain.setBackgroundColor(ContextCompat.getColor(mcx, R.color.white));

        }
    }

    @Override
    public int getItemCount()
    {
        return  messageList.size();
    }

    public class MassageViewHolder extends RecyclerView.ViewHolder{
        TextView txtHead,txtDate;
        LinearLayout llMain;
        public MassageViewHolder(@NonNull View itemView) {
            super(itemView);

            txtHead = itemView.findViewById(R.id.txt_message_head);
            txtDate = itemView.findViewById(R.id.txt_message_date);
            llMain = itemView.findViewById(R.id.ll_main);
        }
    }
}
