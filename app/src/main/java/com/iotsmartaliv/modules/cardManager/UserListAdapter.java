package com.iotsmartaliv.modules.cardManager;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iotsmartaliv.R;

import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {
    //private CardModel[] listdata;
    List<CardUserList> cardUserLists;
    // RecyclerView recyclerView;
    public UserListAdapter(List<CardUserList> cardUserLists) {
        this.cardUserLists = cardUserLists;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_user_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // final CardModel myListData = listdata[position];
        holder.userNameTv.setText(cardUserLists.get(position).getUserFullName());
        holder.email_tv.setText(cardUserLists.get(position).getUserEmail());
        holder.card_no__tv.setText(cardUserLists.get(position).getUidNumber());
    }


    @Override
    public int getItemCount() {
        return cardUserLists.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView userNameTv, email_tv, card_no__tv;

        public ViewHolder(View itemView) {
            super(itemView);
            this.userNameTv = itemView.findViewById(R.id.card_user_tv);
            this.email_tv = itemView.findViewById(R.id.email_tv);
            this.card_no__tv = itemView.findViewById(R.id.card_no__tv);
        }
    }
}  