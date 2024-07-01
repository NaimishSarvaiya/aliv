package com.iotsmartaliv.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iotsmartaliv.R;
import com.iotsmartaliv.adapter.MassageAdapter;
import com.iotsmartaliv.apiCalling.models.Broadcast;

import java.util.ArrayList;

public class MessageCommunityBroadcastFragment extends Fragment {

    RecyclerView recyclerView;
    MassageAdapter adapter;
    public ArrayList<Broadcast> Massage1 = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.message_layout, container, false);
        recyclerView = view.findViewById(R.id.massage_rv);
        recyclerView.setHasFixedSize(false);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new MassageAdapter(getContext(), Massage1);
        recyclerView.setAdapter(adapter);

        return view;
    }


    public void updateList(ArrayList<Broadcast> Massage) {

        adapter.setListUdpate(Massage);

    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}
