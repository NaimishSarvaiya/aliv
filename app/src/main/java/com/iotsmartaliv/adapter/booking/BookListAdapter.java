package com.iotsmartaliv.adapter.booking;

import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iotsmartaliv.R;
import com.iotsmartaliv.utils.Util;

public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.BookListViewHolder> {
    private Context context;

    public BookListAdapter(Context context) {
        this.context = context;
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
        Util.setBrightness(holder.imageView, 2.0f);
    }

    @Override
    public int getItemCount() {
        return 15;
    }

    public class BookListViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public BookListViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.hotel_image);

        }
    }

}
