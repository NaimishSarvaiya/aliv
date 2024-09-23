package com.iotsmartaliv.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iotsmartaliv.R;
import com.iotsmartaliv.apiAndSocket.models.ResArrayObjectData;

import java.util.List;

public class CommunityDialogAdapter extends RecyclerView.Adapter<CommunityDialogAdapter.CommunityViewHolder> {
    private RecyclerViewItemClickListener recyclerViewItemClickListener;
    private List<ResArrayObjectData> mDataset;

    public CommunityDialogAdapter(List<ResArrayObjectData> mDataseta, RecyclerViewItemClickListener listener) {
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
        fruitViewHolder.mTextView.setText(mDataset.get(i).getCommunityName());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void updateList(List<ResArrayObjectData> temp) {
        mDataset = temp;
        notifyDataSetChanged();
    }


    public interface RecyclerViewItemClickListener {
        void clickOnItem(ResArrayObjectData data);
    }

    public class CommunityViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mTextView;

        CommunityViewHolder(View v) {
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


