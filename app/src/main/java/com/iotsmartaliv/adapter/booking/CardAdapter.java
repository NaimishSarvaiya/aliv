package com.iotsmartaliv.adapter.booking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iotsmartaliv.R;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.BookListViewHolder> {
    private Context context;

    public CardAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public BookListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.card_item, parent, false);
        return new BookListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookListViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class BookListViewHolder extends RecyclerView.ViewHolder {

        public BookListViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
