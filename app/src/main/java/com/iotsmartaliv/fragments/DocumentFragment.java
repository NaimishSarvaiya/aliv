package com.iotsmartaliv.fragments;


import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.iotsmartaliv.R;
import com.iotsmartaliv.adapter.DocumentAdapter;
import com.iotsmartaliv.model.BroadcastDocumentFolder;

import java.util.ArrayList;


public class DocumentFragment extends Fragment {


    RecyclerView recyclerView1;
    DocumentAdapter adapter1;

    public ArrayList<BroadcastDocumentFolder> Document1 = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.document_layout, container, false);
        recyclerView1 = view.findViewById(R.id.document_rv);

        recyclerView1.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter1 = new DocumentAdapter(getContext(),Document1);
        recyclerView1.setAdapter(adapter1);

        return view;
    }

    public void updateList(ArrayList<BroadcastDocumentFolder> Massage){

        adapter1.setListUdpate(Massage);

    }
    @Override
    public void onResume() {
        super.onResume();
        adapter1.notifyDataSetChanged();
    }
}