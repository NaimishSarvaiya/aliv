package com.iotsmartaliv.modules.cardManager;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iotsmartaliv.R;

import java.util.ArrayList;
import java.util.List;

public class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.ViewHolder> {
    //private CardModel[] listdata;
    List<CardUserList> cardUserLists = new ArrayList<>();

    // RecyclerView recyclerView;
    public CardListAdapter(List<CardUserList> cardUserLists) {
        this.cardUserLists = cardUserLists;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_card_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // final CardModel myListData = listdata[position];
        holder.userNameTv.setText(cardUserLists.get(position).getUserFullName());
        holder.cardNoTv.setText("Card id: " + cardUserLists.get(position).getUidNumber());
        holder.status.setText(cardUserLists.get(position).getOperation().equalsIgnoreCase("0") ? "Added" : "Removed");
        holder.status.setTextColor(cardUserLists.get(position).getOperation().equalsIgnoreCase("0") ? ContextCompat.getColor(holder.itemView.getContext(), R.color.green) : ContextCompat.getColor(holder.itemView.getContext(), R.color.red));
    }


    @Override
    public int getItemCount() {
        return cardUserLists.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView userNameTv, cardNoTv, status;

        public ViewHolder(View itemView) {
            super(itemView);
            this.userNameTv = itemView.findViewById(R.id.card_user_tv);
            this.cardNoTv = itemView.findViewById(R.id.card_number);
            this.status = itemView.findViewById(R.id.status_val);
        }
    }
}  