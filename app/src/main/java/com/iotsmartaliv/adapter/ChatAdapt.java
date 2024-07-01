package com.iotsmartaliv.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.iotsmartaliv.R;
import com.iotsmartaliv.model.Chat;

import java.util.List;

/**
 * This adapter class is used for setup chats.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2018-10-23
 */
public class ChatAdapt extends BaseAdapter {
    int size;
    LayoutInflater inflater;
    private Context context;
    private List<Chat> list;

    public ChatAdapt(Context context, List<Chat> list) {
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder viewHolder;
        if (view == null) {
            view = inflater.inflate(R.layout.msglist, viewGroup, false);
            viewHolder = new Holder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (Holder) view.getTag();
        }
        Chat chat = (Chat) getItem(i);
        viewHolder.snd.setText(chat.getSend());
        viewHolder.rec.setText(chat.getReceive());

        return view;
    }

    private class Holder {
        TextView snd, rec;

        public Holder(View view) {
            snd = view.findViewById(R.id.leftText);
            rec = view.findViewById(R.id.rightText);

        }
    }
}
