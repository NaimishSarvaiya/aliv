package com.iotsmartaliv.adapter.booking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iotsmartaliv.R;
import com.iotsmartaliv.model.booking.CustomerCardsResponse;
import com.iotsmartaliv.model.booking.PaymentMethodModel;
import com.iotsmartaliv.model.feedback.MessageHistoryData;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    private Context context;
    private List<PaymentMethodModel> cardList;
    private OnItemClickListener onItemClickListener;
    private OnDeleteClickListener onDeleteClickListener;

    public CardAdapter(Context context, List<PaymentMethodModel> cardList, OnItemClickListener itemClickListener, OnDeleteClickListener deleteClickListener) {
        this.context = context;
        this.cardList = cardList;
        this.onItemClickListener = itemClickListener;
        this.onDeleteClickListener = deleteClickListener;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.card_item, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        PaymentMethodModel card = cardList.get(position);

        // Set the last four digits of the card
        holder.cardNumber.setText("**** **** **** " + card.getLast4());

        // Set the expiry date
        holder.cardExpiry.setText("Expires: " + card.getExpMonth() + "/" + card.getExpYear());

        // Set the card brand image
        setCardBrandImage(card.getBrand(), holder.cardBrandImage);

        // Normal item click
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(position, card);
            }
        });

        // Delete icon click
        holder.deleteIcon.setOnClickListener(v -> {
            if (onDeleteClickListener != null) {
                onDeleteClickListener.onDeleteClick(position, card);
            }
        });

    }

    @Override
    public int getItemCount() {
        return cardList != null ? cardList.size() : 0;
    }

    public class CardViewHolder extends RecyclerView.ViewHolder {
        TextView cardNumber, cardExpiry;
        ImageView cardBrandImage, deleteIcon;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            cardNumber = itemView.findViewById(R.id.cardNumber);
            cardExpiry = itemView.findViewById(R.id.cardExpiry);
            cardBrandImage = itemView.findViewById(R.id.cardBrandImage);
            deleteIcon = itemView.findViewById(R.id.deleteIcon);
        }
    }

    // Method to set the correct card brand image
    public void setCardBrandImage(String brand, ImageView imageView) {
        int imageResId = R.drawable.no_card;
        switch (brand != null ? brand.toLowerCase() : "") {
            case "visa":
                imageResId = R.drawable.card_visa;
                break;
            case "mastercard":
                imageResId = R.drawable.card_master;
                break;
            case "amex":
                imageResId = R.drawable.card_mx;
                break;
            case "discover":
                imageResId = R.drawable.card_discover;
                break;
            case "jcb":
                imageResId = R.drawable.card_jcb;
                break;
            case "dinersclub":
                imageResId = R.drawable.card_diner_club;
                break;
            case "unionpay":
                imageResId = R.drawable.card_union_pay;
                break;
            default:
                imageResId = R.drawable.no_card;
                break;
        }
        imageView.setImageResource(imageResId);
    }

    // Interface for normal item click
    public interface OnItemClickListener {
        void onItemClick(int position, PaymentMethodModel card);
    }

    // Interface for delete click
    public interface OnDeleteClickListener {
        void onDeleteClick(int position,PaymentMethodModel card);
    }

    public void deleteItem(int position) {
        cardList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, cardList.size());
//        holder.itemView.setVisibility(View.GONE);
    }

    public void updateData(List<PaymentMethodModel> newCardList) {
        cardList.clear();
        cardList.addAll(newCardList);
        notifyDataSetChanged();
    }
}