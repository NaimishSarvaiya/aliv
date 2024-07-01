package com.iotsmartaliv.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.iotsmartaliv.R;
import com.iotsmartaliv.model.Category;

import java.util.ArrayList;

/**
 * This adapter class is used for category .
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2018-10-23
 */
public class Category_adpat extends BaseAdapter {
    ArrayList<Category> al = new ArrayList<>();
    LayoutInflater layoutInflater;
    private Context context;

    public Category_adpat(Context context, ArrayList<Category> al) {
        this.context = context;
        this.al = al;
        layoutInflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return al.size();
    }

    @Override
    public Object getItem(int i) {
        return al.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.items, viewGroup, false);
            holder = new Holder(view);
            view.setTag(holder);

        } else {
            holder = (Holder) view.getTag();
        }
        Category category = (Category) getItem(i);
        holder.textView.setText(category.getCategory());
        return view;
    }

    private class Holder {
        public TextView textView;

        public Holder(View view) {
            textView = view.findViewById(R.id.itemone);

        }
    }

}
