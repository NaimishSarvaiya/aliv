package com.iotsmartaliv.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.iotsmartaliv.R;
import com.iotsmartaliv.model.CardModel;

import java.util.List;

/**
 * This adapter class is used for setup cards .
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2018-10-23
 */
public class CardAdapter extends BaseAdapter {
    List<CardModel> cardList;
    LayoutInflater inflater;
    private Context context;

    public CardAdapter(Context context, List<CardModel> cardList) {
        this.context = context;
        this.cardList = cardList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return cardList.size();
    }

    @Override
    public Object getItem(int i) {
        return cardList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.card, viewGroup, false);
            holder = new Holder(view);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        CardModel cardModel = (CardModel) getItem(i);
        holder.card_no.setText(cardModel.getCrad_no());
        holder.expiry_date.setText(cardModel.getExpiry_date());
        return view;
    }

    private class Holder {
        TextView card_no, expiry_date;

        public Holder(View view) {
            card_no = view.findViewById(R.id.tv_mastercard);
            expiry_date = view.findViewById(R.id.tv_holdername);
        }
    }
}
