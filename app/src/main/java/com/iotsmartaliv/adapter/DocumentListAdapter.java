package com.iotsmartaliv.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iotsmartaliv.R;
import com.iotsmartaliv.activity.DocumentPdfActivity;
import com.iotsmartaliv.activity.ViewPager.BroadcastDetailActivity;
import com.iotsmartaliv.apiAndSocket.models.Broadcast;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to display document list row
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 03 Apr,2021
 */
public class DocumentListAdapter extends RecyclerView.Adapter<DocumentListAdapter.DocumentListViewHolder> {

    private Context mcx;
    private List<Broadcast> documentlListModel;


    public DocumentListAdapter(Context mcx, List<Broadcast> documentlListModel) {
        this.mcx = mcx;
        this.documentlListModel = documentlListModel;
    }


    @NonNull
    @Override
    public DocumentListAdapter.DocumentListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mcx);
        View view = inflater.inflate(R.layout.document_list_items, null);
        return new DocumentListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DocumentListAdapter.DocumentListViewHolder holder, int position) {

        Broadcast mDocument = documentlListModel.get(position);

        setView(holder,mDocument);

        holder.rlDocumentView.setOnClickListener(view -> {

            Intent intent = new Intent(mcx, BroadcastDetailActivity.class);

            intent.putExtra("BROADCAST_ITEM",mDocument);

            mcx.startActivity(intent);

            documentlListModel.get(position).setReadStatus("1");

        });
    }

    private void setView(DocumentListViewHolder holder, Broadcast mDocument) {

        holder.txtDocList.setText(mDocument.getBroadcastTitle());

        if (mDocument.getReadStatus().equalsIgnoreCase("0")) {

            holder.txtDocList.setTypeface(null, Typeface.BOLD);

        } else {

            holder.txtDocList.setTypeface(null, Typeface.NORMAL);

        }

    }

    @Override
    public int getItemCount() {
        return documentlListModel.size();
    }

    public class DocumentListViewHolder extends RecyclerView.ViewHolder {

        TextView txtDocList;
        ImageView imgDocList;
        RelativeLayout rlDocumentView;

        public DocumentListViewHolder(@NonNull View itemView) {
            super(itemView);

            txtDocList = itemView.findViewById(R.id.txt_doc_item);

            imgDocList = itemView.findViewById(R.id.img_forward_doc_item);

            rlDocumentView = itemView.findViewById(R.id.rl_document_list);

        }
    }
}
