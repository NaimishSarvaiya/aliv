package com.iotsmartaliv.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iotsmartaliv.R;
import com.iotsmartaliv.activity.DocumentListActivity;
import com.iotsmartaliv.apiCalling.retrofit.ApiServiceProvider;
import com.iotsmartaliv.model.BroadcastDocumentFolder;

import java.util.ArrayList;

/**
 * This class is used to display document row.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 03 Apr,2021
 */
public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.DocumentViewHolder> {

    private Context mcx;
    private ArrayList<BroadcastDocumentFolder> documentlList;
    private ApiServiceProvider apiServiceProvider;

    public DocumentAdapter(Context mcx, ArrayList<BroadcastDocumentFolder> documentlList) {
        this.mcx = mcx;
        this.documentlList = documentlList;
    }

    public void setListUdpate(ArrayList<BroadcastDocumentFolder> documentlList) {
        this.documentlList = documentlList;
        notifyDataSetChanged();

    }


    @NonNull
    @Override
    public DocumentAdapter.DocumentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mcx);
        View view = inflater.inflate(R.layout.document_items, null);
        return new DocumentViewHolder(view);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull DocumentAdapter.DocumentViewHolder holder, int position) {

        BroadcastDocumentFolder mDocument = documentlList.get(position);

        setView(holder, mDocument);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mcx, DocumentListActivity.class);

                intent.putExtra("DOCUMENT_LIST", documentlList.get(position).getBroadcasts());

                mcx.startActivity(intent);

            }
        });


    }


    private void setView(DocumentViewHolder holder, BroadcastDocumentFolder mDocument) {

        /*holder.txtDoc.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        Drawable img = mcx.getResources().getDrawable(
                                R.drawable.document_folder_icon);
                        img.setBounds(0, 0, 70, 70);
                        holder.txtDoc.setCompoundDrawables(img, null, null, null);
                        holder.txtDoc.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });*/

        holder.txtDoc.setText("\uD83D\uDCC2  " + mDocument.getDocumentFolderTitle());

        if (mDocument.getReadStatus().equalsIgnoreCase("0")) {

            holder.txtDoc.setTypeface(null, Typeface.BOLD);

        } else {

            holder.txtDoc.setTypeface(null, Typeface.NORMAL);

        }

    }

    @Override
    public int getItemCount() {
        return documentlList.size();
    }

    public class DocumentViewHolder extends RecyclerView.ViewHolder {

        TextView txtDoc;
        ImageView imgDoc;

        public DocumentViewHolder(@NonNull View itemView) {
            super(itemView);

            txtDoc = itemView.findViewById(R.id.txt_doc);
            imgDoc = itemView.findViewById(R.id.img_forward_doc);
        }
    }


//        if (!(activeFragment instanceof MessageFragment)) { fragment.addToBackStack(null); }


}



