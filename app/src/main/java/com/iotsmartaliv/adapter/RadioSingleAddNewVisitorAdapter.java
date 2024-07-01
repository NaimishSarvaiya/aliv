package com.iotsmartaliv.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iotsmartaliv.R;
import com.iotsmartaliv.model.AddVisitor;

import java.util.ArrayList;

/**
 * This class is used as .
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 13/8/19 :August.
 */
public class RadioSingleAddNewVisitorAdapter extends RecyclerView.Adapter<RadioSingleAddNewVisitorAdapter.ViewHolder> {
    private ArrayList<AddVisitor> visitors;

    public RadioSingleAddNewVisitorAdapter(ArrayList<AddVisitor> visitors) {
        this.visitors = visitors;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.visitor_row_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.tv_name.setText(visitors.get(i).getVisitorName());
        viewHolder.tv_contact_name.setText((visitors.get(i).getCountryCode() + "-" + visitors.get(i).getContactNumber()));
    }

    @Override
    public int getItemCount() {
        return visitors.size();
    }

    public void addItem(AddVisitor addVisitor) {
        visitors.add(0, addVisitor);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        visitors.remove(position);
        notifyDataSetChanged();
    }

    public ArrayList<AddVisitor> getVisitors() {
        return visitors;
    }

    public void clearAll() {
        visitors.clear();
        notifyDataSetChanged();
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
            removeItem(getAdapterPosition());
        }
    }
}
