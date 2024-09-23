package com.iotsmartaliv.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iotsmartaliv.R;
import com.iotsmartaliv.apiAndSocket.models.GroupData;

import java.util.List;

public class ViewGroupListAdapter extends RecyclerView.Adapter<ViewGroupListAdapter.ViewHolder> {
    private UnassignedClickListener unassignedClickListener;
    private List<GroupData> groupDataList;

    public ViewGroupListAdapter(List<GroupData> groupDataList) {
        this.groupDataList = groupDataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.group_row_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    public void updateList(List<GroupData> temp) {
        groupDataList = temp;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.tv_name.setText(groupDataList.get(i).getGroupName());
    }

    @Override
    public int getItemCount() {
        return groupDataList.size();
    }

    public void addItem(GroupData addVisitor) {
        groupDataList.add(0, addVisitor);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        groupDataList.remove(position);
        notifyDataSetChanged();
    }

    public void setOnUnassignedListener(UnassignedClickListener unassignedClickListener) {
        this.unassignedClickListener = unassignedClickListener;
    }

    public interface UnassignedClickListener {
        void onUnassignGroup(GroupData data, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_name, tv_contact_name;
        ImageView imageView_remove;

        public ViewHolder(View view) {
            super(view);
            tv_name = view.findViewById(R.id.tv_name);
            imageView_remove = view.findViewById(R.id.imageView_remove);
            tv_contact_name = view.findViewById(R.id.tv_contact_name);
            imageView_remove.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            unassignedClickListener.onUnassignGroup(groupDataList.get(getAdapterPosition()), getAdapterPosition());
        }
    }
}