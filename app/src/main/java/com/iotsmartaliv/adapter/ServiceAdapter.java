package com.iotsmartaliv.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.iotsmartaliv.R;
import com.iotsmartaliv.activity.ServiceDetailsActivity;
import com.iotsmartaliv.databinding.ServiceListRowBinding;
//


/**
 * This adapter class is used for service0.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2018-10-23
 */
public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.MyViewHolder> {
    public Context context;

    public ServiceAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ServiceListRowBinding binding = ServiceListRowBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
//        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_list_row, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        //todo Perfrom UI task
        if (position == 1) {
            holder.binding.textViewTitle.setText("Software update");
            holder.binding.buttonStatus.setText("Pending");
            holder.binding.buttonStatus.setTextColor(context.getResources().getColor(R.color.colorBlack));
            holder.binding.buttonStatus.setBackground(ContextCompat.getDrawable(context, R.drawable.btn_non_select));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, ServiceDetailsActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
//        @BindView(R.id.textViewTitle)
//        TextView textViewTitle;
//        @BindView(R.id.textViewDate)
//        TextView textViewDate;
//        @BindView(R.id.textViewToken)
//        TextView textViewToken;
//        @BindView(R.id.buttonStatus)
//        Button buttonStatus;

        ServiceListRowBinding binding;

        MyViewHolder( ServiceListRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}