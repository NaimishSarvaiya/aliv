package com.iotsmartaliv.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.iotsmartaliv.R;
import com.iotsmartaliv.model.MyCartModel;

import java.util.List;

/**
 * This adapter class is used for my cart.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2018-10-23
 */
public class MyCartAdapter extends BaseAdapter {
    private Context context;
    private List<MyCartModel> list;
    private LayoutInflater inflater;

    public MyCartAdapter(Context context, List<MyCartModel> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        Holder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.cart_items, viewGroup, false);
            holder = new Holder();
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        final MyCartModel myCartModel = (MyCartModel) getItem(i);
        holder.productname = view.findViewById(R.id.tv_productname);
        holder.productname.setText(myCartModel.getProductname());
        holder.remove = view.findViewById(R.id.tv_remove);
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.remove(i);
                MyCartAdapter.this.notifyDataSetChanged();
            }
        });
        return view;
    }

    private class Holder {
        public TextView remove;
        TextView productname;
    }
}
