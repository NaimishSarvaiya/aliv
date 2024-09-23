package com.iotsmartaliv.dialog_box;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.iotsmartaliv.R;
import com.iotsmartaliv.adapter.CountryCodeDialogAdapter;
import com.iotsmartaliv.apiAndSocket.models.Country;

import java.util.ArrayList;
import java.util.List;


public class CustomCountryCodeDialog extends Dialog implements View.OnClickListener {
    public Activity activity;
    public Dialog dialog;
    public Button cancel_btn;
    TextView title;
    EditText search_ed;
    RecyclerView recyclerView;
    CountryCodeDialogAdapter adapter;
    List<Country> countryList;
    private RecyclerView.LayoutManager mLayoutManager;

    public CustomCountryCodeDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public CustomCountryCodeDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public CustomCountryCodeDialog(Activity a, CountryCodeDialogAdapter adapter, List<Country> mDataset) {
        super(a);
        this.activity = a;
        this.adapter = adapter;
        this.countryList = mDataset;
        setupLayout();
    }


    private void setupLayout() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog_country_code_layout);
        Window window = getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        cancel_btn = findViewById(R.id.cancel_btn);
        title = findViewById(R.id.title);
        search_ed = findViewById(R.id.search_ed);
        recyclerView = findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
        cancel_btn.setOnClickListener(this);
        search_ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // filter your list from your input
                filter(s.toString());
                //you can use runnable postDelayed like 500 ms to delay search text
            }
        });

    }

    private void filter(String text) {
        ArrayList<Country> temp = new ArrayList<>();
        for (Country d : countryList) {
            if (d.getName().toLowerCase().contains(text.toLowerCase()) ||
                    d.getPhonecode().contains(text) ||
                    d.getIso().contains(text) ||
                    d.getIso3().contains(text) ||
                    d.getNicename().toLowerCase().contains(text.toLowerCase())) {
                temp.add(d);
            }
        }
        adapter.updateList(temp);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_btn:
                dismiss();
                //Do Something
                break;
            default:
                break;
        }
        dismiss();
    }
}