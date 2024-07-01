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
import com.iotsmartaliv.model.VisitorData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * This class is used as .
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 31/7/19 :July
 */
public class FragmentVisitorAdapter extends RecyclerView.Adapter<FragmentVisitorAdapter.ViewHolder> {
    Context context;
    List<VisitorData> dataList = new ArrayList<>();
    VisitorUpdateClick visitorUpdateClick;

    public FragmentVisitorAdapter(Context context, VisitorUpdateClick visitorUpdateClick) {
        this.context = context;
        this.visitorUpdateClick = visitorUpdateClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_visitor, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.tvVisitor.setText(dataList.get(i).getUvisitorName());
        viewHolder.assignGroupTv.setOnClickListener(v -> visitorUpdateClick.assignGroupVisitorNotify(dataList.get(i)));
        viewHolder.editVisitor.setOnClickListener(v -> visitorUpdateClick.visitorEditNotify(dataList.get(i)));
        viewHolder.deleteBtn.setOnClickListener(v -> visitorUpdateClick.visitorDeleteNotify(dataList.get(i)));
        viewHolder.viewGroupTv.setOnClickListener(v -> visitorUpdateClick.viewGroupVisitorNotify(dataList.get(i)));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void addItem(List<VisitorData> data) {
        this.dataList = data;
        notifyDataSetChanged();
    }


    public interface VisitorUpdateClick {
        void visitorEditNotify(VisitorData visitorData);

        void visitorDeleteNotify(VisitorData visitorData);

        void assignGroupVisitorNotify(VisitorData visitorData);

        void viewGroupVisitorNotify(VisitorData visitorData);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_visitor)
        TextView tvVisitor;
        @BindView(R.id.delete_btn)
        ImageView deleteBtn;
        @BindView(R.id.assign_group_tv)
        TextView assignGroupTv;
        @BindView(R.id.view_group_tv)
        TextView viewGroupTv;
        @BindView(R.id.edit_visitor)
        TextView editVisitor;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
