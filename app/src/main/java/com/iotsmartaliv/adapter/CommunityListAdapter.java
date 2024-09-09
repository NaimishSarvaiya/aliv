package com.iotsmartaliv.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iotsmartaliv.R;
import com.iotsmartaliv.apiCalling.models.ResArrayObjectData;
import com.iotsmartaliv.databinding.DeviceListRowBinding;
import com.iotsmartaliv.fragments.community.CommunityListFragment;

import java.util.ArrayList;
import java.util.List;



/**
 * This class is used as adapter for community list adapter.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 26/3/19 :March : 2019 on 15 : 15.
 */
public class CommunityListAdapter extends RecyclerView.Adapter<CommunityListAdapter.MyViewHolder> {

    public Context context;
    private List<ResArrayObjectData> data = new ArrayList<>();
    private CommunityListFragment.OnFragmentInteractionListener mListener;

    public CommunityListAdapter(Context context, CommunityListFragment.OnFragmentInteractionListener mListener) {
        this.context = context;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DeviceListRowBinding binding = DeviceListRowBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
//        View itemView = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.device_list_row, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.binding.itemText.setText(data.get(position).getCommunityName());
        if (mListener == null){
                holder.binding.itemImg.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onFragmentInteractionSelectCategory(data.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void addItem(List<ResArrayObjectData> datas) {
        data.clear();
        data.addAll(datas);
        notifyDataSetChanged();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
//        @BindView(R.id.item_text)
//        TextView itemText;
//        @BindView(R.id.item_img)
//        ImageView itemImg;
//        @BindView(R.id.ll_device)
//        LinearLayout llDevice;

        DeviceListRowBinding binding;

        MyViewHolder( DeviceListRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
//            ButterKnife.bind(this, view);
        }
    }
}
