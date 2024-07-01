package com.iotsmartaliv.activity;

import android.content.Context;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ListView;

import com.iotsmartaliv.R;
import com.iotsmartaliv.adapter.TabAdapter;
import com.iotsmartaliv.model.CardModel;

import java.util.ArrayList;

/**
 * This activity class is used for payment setup.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2018-10-23
 */
public class PaymentSetupActivity extends AppCompatActivity {
    Context context;
    private FloatingActionButton fab;
    private ListView cardList;
    private ArrayList<CardModel> al = new ArrayList<>();
    private TabAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_setup);
//        adapter = new TabAdapter(getSupportFragmentManager());
//        adapter.addFragment(new PaymentFragment(), "Payment");
    }

}
