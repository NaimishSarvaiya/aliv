package com.iotsmartaliv.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.iotsmartaliv.R;
import com.iotsmartaliv.adapter.MyCartAdapter;
import com.iotsmartaliv.model.MyCartModel;

import java.util.ArrayList;
import java.util.List;

/**
 * This activity class is used for My cart activity.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2018-10-23
 */
public class MyCartActivity extends AppCompatActivity {

    TextView remove;
    ImageView goback;
    String[] product_name = {"Product Name", "Product Name", "Product Name"};
    private MyCartAdapter adapter;
    private ListView listView;
    private List<MyCartModel> al;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);
        goback = findViewById(R.id.go_back);
        al = new ArrayList<>();
        listView = findViewById(R.id.list_cart);
        MyCartModel myCartModel = new MyCartModel();
        for (int i = 0; i < product_name.length; i++) {
            myCartModel.setProductname(product_name[i]);
            al.add(myCartModel);
        }

        adapter = new MyCartAdapter(MyCartActivity.this, al);
        listView.setAdapter(adapter);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
