package com.iotsmartaliv.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iotsmartaliv.R;
import com.iotsmartaliv.adapter.DocumentListAdapter;
import com.iotsmartaliv.apiCalling.models.Broadcast;

import java.util.ArrayList;

public class DocumentListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView tvDocumentTitle;
    private ImageView imgBack;

    DocumentListAdapter adapter;

    public  ArrayList<Broadcast> documentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_list);

        initView();

        extractIntent();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new DocumentListAdapter(this, documentList);

        recyclerView.setAdapter(adapter);


    }

    private void initView() {

        recyclerView = findViewById(R.id.document_list_rv);

        tvDocumentTitle = findViewById(R.id.tv_document_title);

        imgBack= findViewById(R.id.img_back);

        imgBack.setOnClickListener(view -> onBackPressed());

    }

    private void extractIntent() {

        if (getIntent().getExtras() != null){

            documentList = (ArrayList<Broadcast>) getIntent().getExtras().getSerializable("DOCUMENT_LIST");

            tvDocumentTitle.setText(documentList.get(0).getBroadcastFolder().toString());

        }

    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }



}