package com.iotsmartaliv.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iotsmartaliv.R;
import com.iotsmartaliv.adapter.DocumentAdapter;
import com.iotsmartaliv.model.DocumentListModel;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used as
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 03 Apr,2021
 */
public class DocumentListFragment extends Fragment {

    RecyclerView recyclerView1;
    DocumentAdapter adapter1;

    List<DocumentListModel> documentListModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.document_list_layout, container, false);
        documentListModel = new ArrayList<>();
        recyclerView1 = view.findViewById(R.id.document_list_rv);
        recyclerView1.setLayoutManager(new LinearLayoutManager(getContext()));
        documentListModel.add(
                new DocumentListModel(
                        "",
                        R.drawable.forward_arrow
                )
        );




        return view;
    }
}