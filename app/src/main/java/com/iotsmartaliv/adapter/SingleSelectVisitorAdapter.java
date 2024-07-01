package com.iotsmartaliv.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iotsmartaliv.R;
import com.iotsmartaliv.model.VisitorData;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used as .
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 10/8/19 :August.
 */
public class SingleSelectVisitorAdapter extends RecyclerView.Adapter<SingleSelectVisitorAdapter.ViewHolder> {
    List<VisitorData> listData = new ArrayList<>();
    private RecyclerViewItemClickListener recyclerViewItemClickListener;

    public SingleSelectVisitorAdapter(List<VisitorData> mDataseta, RecyclerViewItemClickListener listener) {
        listData = mDataseta;
        this.recyclerViewItemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.community_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.mTextView.setText(listData.get(i).getUvisitorName());

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public void addItem(List<VisitorData> data) {
        this.listData = data;
        notifyDataSetChanged();
    }

    public void updateList(List<VisitorData> temp) {
        listData = temp;
        notifyDataSetChanged();
    }

    public interface RecyclerViewItemClickListener {
        void clickOnItem(VisitorData data);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.textView);
            mTextView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            recyclerViewItemClickListener.clickOnItem(listData.get(this.getAdapterPosition()));

        }
    }
}
