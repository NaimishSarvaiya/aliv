package com.iotsmartaliv.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iotsmartaliv.R;
import com.iotsmartaliv.apiCalling.models.GroupData;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used as .
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 9/8/19 :August.
 */
public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.ViewHolder> {
    List<GroupData> data = new ArrayList<>();
    private RecyclerViewItemClickListener recyclerViewItemClickListener;

    public GroupListAdapter(List<GroupData> mDataseta, RecyclerViewItemClickListener listener) {
        data = mDataseta;
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
        viewHolder.mTextView.setText(data.get(i).getGroupName());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void addItem(List<GroupData> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void updateList(List<GroupData> temp) {
        data = temp;
        notifyDataSetChanged();
    }

    public interface RecyclerViewItemClickListener {
        void clickOnItem(GroupData data);
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
            recyclerViewItemClickListener.clickOnItem(data.get(this.getAdapterPosition()));

        }
    }
}
