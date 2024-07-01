package com.iotsmartaliv.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.iotsmartaliv.R;
import com.iotsmartaliv.activity.AddCardActivity;
import com.iotsmartaliv.adapter.CardAdapter;
import com.iotsmartaliv.model.CardModel;

import java.util.ArrayList;

/**
 * This fragment class is used for my payment fragment.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2018-10-24
 */
public class PaymentFragment extends Fragment {
    Context context;
    private CardAdapter adapter;
    private ImageView fab;
    private ListView listView;
    private ArrayList<CardModel> al = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = null;
        view = inflater.inflate(R.layout.payment_fragment, container, false);
        fab = view.findViewById(R.id.fab);
        listView = view.findViewById(R.id.lv_card);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddCardActivity.class);
                startActivityForResult(intent, 101);
            }
        });
        CardModel cardModel = new CardModel();
        cardModel.setCrad_no("13525454");
        cardModel.setExpiry_date("12/22");
        al.add(cardModel);
        adapter = new CardAdapter(getActivity(), al);
        listView.setAdapter(adapter);
        return view;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 101:
                if (data != null) {
                    String cardNumber = data.getStringExtra("cardNo");
                    String expiryDate = data.getStringExtra("expire_date");
                    CardModel cardModel = new CardModel();
                    cardModel.setCrad_no(cardNumber);
                    cardModel.setExpiry_date(expiryDate);
                    al.add(cardModel);
                    adapter = new CardAdapter(getActivity(), al);
                    listView.setAdapter(adapter);
                }
                break;
        }
    }
}