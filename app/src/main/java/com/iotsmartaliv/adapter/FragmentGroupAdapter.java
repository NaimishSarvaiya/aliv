package com.iotsmartaliv.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iotsmartaliv.R;
import com.iotsmartaliv.apiCalling.models.GroupData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * This class is used as .
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 30/7/19 :July : 2019 on 20 : 22
 */
public class FragmentGroupAdapter extends RecyclerView.Adapter<FragmentGroupAdapter.ViewHolder> {
    Context context;
    List<GroupData> data = new ArrayList<>();
    private GroupUpdateClick groupUpdateClick;

    public FragmentGroupAdapter(Context context, GroupUpdateClick groupUpdateClick) {
        this.context = context;
        this.groupUpdateClick = groupUpdateClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_group_visitor, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.tvGroup.setText(data.get(i).getGroupName());
        viewHolder.assignVisitorTv.setOnClickListener(v -> groupUpdateClick.groupAssignVisitorNotify(data.get(i)));
        viewHolder.viewVisitorTv.setOnClickListener(v -> groupUpdateClick.viewVisitorNotify(data.get(i)));
        viewHolder.edit_group.setOnClickListener(v -> groupUpdateClick.groupEditNotify(data.get(i)));
        viewHolder.deleteBtn.setOnClickListener(v -> groupUpdateClick.groupDeleteNotify(data.get(i)));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void addItem(List<GroupData> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public interface GroupUpdateClick {
        void groupEditNotify(GroupData groupData);

        void groupDeleteNotify(GroupData groupData);

        void groupAssignVisitorNotify(GroupData groupData);

        void viewVisitorNotify(GroupData groupData);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_group)
        TextView tvGroup;
        @BindView(R.id.delete_btn)
        ImageView deleteBtn;
        @BindView(R.id.edit_group)
        View edit_group;
        @BindView(R.id.assign_visitor_tv)
        TextView assignVisitorTv;
        @BindView(R.id.view_visitor_tv)
        TextView viewVisitorTv;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
