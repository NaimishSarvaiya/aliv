package com.iotsmartaliv.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iotsmartaliv.R;
import com.iotsmartaliv.adapter.EventAdapter;
import com.iotsmartaliv.apiAndSocket.models.Broadcast;

import java.util.ArrayList;


public class EventFragment extends Fragment {

    RecyclerView recyclerView;
    EventAdapter adapter;

    public ArrayList<Broadcast> Event1 = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.event_layout, container, false);
        recyclerView = view.findViewById(R.id.event_rv);
        recyclerView.setHasFixedSize(false);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new EventAdapter(getContext(),Event1);
        recyclerView.setAdapter(adapter);

        return view;
    }

    public void updateList(ArrayList<Broadcast> Massage){

        adapter.setListUdpate(Massage);

    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}