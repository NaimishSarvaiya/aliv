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

import butterknife.BindView;
import butterknife.ButterKnife;

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
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        //todo Perfrom UI task
        if (position == 1) {
            holder.textViewTitle.setText("Software update");
            holder.buttonStatus.setText("Pending");
            holder.buttonStatus.setTextColor(context.getResources().getColor(R.color.colorBlack));
            holder.buttonStatus.setBackground(ContextCompat.getDrawable(context, R.drawable.btn_non_select));
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
        @BindView(R.id.textViewTitle)
        TextView textViewTitle;
        @BindView(R.id.textViewDate)
        TextView textViewDate;
        @BindView(R.id.textViewToken)
        TextView textViewToken;
        @BindView(R.id.buttonStatus)
        Button buttonStatus;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}