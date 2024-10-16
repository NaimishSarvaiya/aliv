package com.iotsmartaliv.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iotsmartaliv.R;

import java.util.List;
import java.util.Map;

/**
 * This adapter class is used to display scanned device in the list.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2018-01-16
 */
public class ScanDevListAdapter extends RecyclerView.Adapter<ViewHolder> {
    private Context context;
    private List<Map.Entry<String, Integer>> list;
//    private Map<String, Integer> deviceMap;

    private ScanDevListAdapter.OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public ScanDevListAdapter(Context context, List<Map.Entry<String, Integer>> list) {
        this.context = context;
        this.list = list;
    }

//    public void addDevice(Map.Entry<String, Integer> entry) {
//        if (deviceMap != null ) {
//            deviceMap.put()
//            notifyDataSetChanged();
//            notifyItemChanged();
//        }
//    }

    /**
     * 设置item点击事件
     *
     * @param listener
     */
    public void setOnItemClickListener(ScanDevListAdapter.OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder;
        View view = LayoutInflater.from(context).inflate(R.layout.add_scanlist_item, parent, false);
        viewHolder = new ItemViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        Map.Entry<String, Integer> entry = list.get(position);
        String name = entry.getKey();
        int rssi = entry.getValue();
        itemViewHolder.tvLists[0].setText(name);
        itemViewHolder.tvLists[1].setText(String.valueOf(rssi));
        itemViewHolder.itemView.setTag(entry.getKey());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, String sn);
    }

    /**
     * ViewHolder
     */
    class ItemViewHolder extends ViewHolder implements View.OnClickListener {
        LinearLayout item_activity_layout;
        TextView[] tvLists = new TextView[2];

        ItemViewHolder(View itemView) {
            super(itemView);
//            ButterKnife.bind(this, itemView);
            item_activity_layout = itemView.findViewById(R.id.item_activity_layout);
            tvLists[0] = itemView.findViewById(R.id.tv_item_name);
            tvLists[1] = itemView.findViewById(R.id.tv_item_rssi);
            item_activity_layout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int i = view.getId();
            if (i == R.id.item_activity_layout) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, (String) view.getTag());
                }
            } else {
            }
        }
    }
}