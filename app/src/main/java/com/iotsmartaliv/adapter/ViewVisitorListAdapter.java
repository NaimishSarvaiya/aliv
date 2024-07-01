package com.iotsmartaliv.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iotsmartaliv.R;
import com.iotsmartaliv.model.VisitorData;

import java.util.List;

public class ViewVisitorListAdapter extends RecyclerView.Adapter<ViewVisitorListAdapter.ViewHolder> {
    private List<VisitorData> visitors;
    private UnassignedClickListener unassignedClickListener;

    public ViewVisitorListAdapter(List<VisitorData> visitors) {
        this.visitors = visitors;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.visitor_row_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    public void updateList(List<VisitorData> temp) {
        visitors = temp;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.tv_name.setText(visitors.get(i).getUvisitorName());
        viewHolder.tv_contact_name.setText(visitors.get(i).getVisitorContact());
    }

    @Override
    public int getItemCount() {
        return visitors.size();
    }

    public void addItem(VisitorData addVisitor) {
        visitors.add(0, addVisitor);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        visitors.remove(position);
        notifyDataSetChanged();
    }

    public void setOnUnassignedListener(UnassignedClickListener unassignedClickListener) {
        this.unassignedClickListener = unassignedClickListener;
    }

    public interface UnassignedClickListener {
        void onUnassignGroup(VisitorData data, int position);
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
            unassignedClickListener.onUnassignGroup(visitors.get(getAdapterPosition()), getAdapterPosition());

        }
    }


}