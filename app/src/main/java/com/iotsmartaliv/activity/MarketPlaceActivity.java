package com.iotsmartaliv.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.iotsmartaliv.R;
import com.iotsmartaliv.adapter.Category_adpat;
import com.iotsmartaliv.model.Category;

import java.util.ArrayList;

/**
 * This activity class is used for market place .
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2018-10-23
 */
public class MarketPlaceActivity extends AppCompatActivity implements View.OnClickListener {
    public String[] prgmNameList = {"Category 1", "Category 2", "Category 3", "Category 4"
            , "Category 5"};
    ListView listView;
    private ArrayList<Category> al;
    private ImageView floatingActionButton, imageViewBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_place);

        floatingActionButton = findViewById(R.id.fab);
        imageViewBack = findViewById(R.id.back_market_place);
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MarketPlaceActivity.this, AddProductActivity.class);
                startActivity(intent);
            }
        });
        listView = findViewById(R.id.lists);
        al = new ArrayList<>();
        for (String aPrgmNameList : prgmNameList) {
            Category category = new Category();
            category.setCategory(aPrgmNameList);
            al.add(category);
        }
        Category_adpat category_adpat = new Category_adpat(getApplicationContext(), al);
        listView.setAdapter(category_adpat);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(MarketPlaceActivity.this, "position: " + i, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MarketPlaceActivity.this, ProductListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onClick(View view) {

    }
}
