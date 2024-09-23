package com.iotsmartaliv.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iotsmartaliv.R;
import com.iotsmartaliv.apiAndSocket.models.Country;

import java.util.List;

public class CountryCodeDialogAdapter extends RecyclerView.Adapter<CountryCodeDialogAdapter.CommunityViewHolder> {
    RecyclerViewItemClickListener recyclerViewItemClickListener;
    private List<Country> mDataset;

    public CountryCodeDialogAdapter(List<Country> mDataseta, RecyclerViewItemClickListener listener) {
        mDataset = mDataseta;
        this.recyclerViewItemClickListener = listener;
    }

    @NonNull
    @Override
    public CommunityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.community_item, parent, false);
        return new CommunityViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CommunityViewHolder fruitViewHolder, int i) {
        fruitViewHolder.mTextView.setText((mDataset.get(i).getName() + "(" + mDataset.get(i).getPhonecode() + ")"));


    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void updateList(List<Country> temp) {
        mDataset = temp;
        notifyDataSetChanged();
    }


    public interface RecyclerViewItemClickListener {
        void clickOnItem(Country data);
    }

    public class CommunityViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView mTextView;

        public CommunityViewHolder(View v) {
            super(v);
            mTextView = v.findViewById(R.id.textView);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            recyclerViewItemClickListener.clickOnItem(mDataset.get(this.getAdapterPosition()));

        }
    }
}


