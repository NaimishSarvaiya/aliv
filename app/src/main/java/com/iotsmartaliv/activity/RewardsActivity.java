package com.iotsmartaliv.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.iotsmartaliv.R;
import com.iotsmartaliv.adapter.RewardsAdapter;
import com.iotsmartaliv.model.RewardsModels;

import java.util.ArrayList;

/**
 * This activity class is used for rewards.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2018-10-23
 */
public class RewardsActivity extends AppCompatActivity implements View.OnClickListener {
    private ListView imgOffers;
    private RewardsAdapter adapter;
    private ArrayList<RewardsModels> arrayListNotification = new ArrayList<>();
    private ImageView imgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards);
        initViews();
        initListeners();
        RewardsModels notificationModel1 = new RewardsModels();
        notificationModel1.setOffersImageUrl("http://125.99.173.44//daddy_pocket//uploads//profile_image//d7185683ca95019dc4e216e2b5fc47f11535362336_profile.png");
        arrayListNotification.add(notificationModel1);
        RewardsModels notificationModel2 = new RewardsModels();
        notificationModel2.setOffersImageUrl("http://125.99.173.44//daddy_pocket//uploads//profile_image//d7185683ca95019dc4e216e2b5fc47f11535362336_profile.png");
        arrayListNotification.add(notificationModel2);

        RewardsModels notificationModel3 = new RewardsModels();
        notificationModel3.setOffersImageUrl("http://125.99.173.44//daddy_pocket//uploads//profile_image//d7185683ca95019dc4e216e2b5fc47f11535362336_profile.png");
        arrayListNotification.add(notificationModel3);

        adapter = new RewardsAdapter(arrayListNotification, this);
        imgOffers.setAdapter(adapter);

        imgOffers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(RewardsActivity.this, OffersDetails.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Initialize views.
     */
    private void initViews() {
        imgOffers = findViewById(R.id.list_item_rewards);
        imgView = findViewById(R.id.img_back_rewards);
    }

    /**
     * Initialize listeners.
     */
    private void initListeners() {
        imgView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back_rewards:
                finish();
                break;
        }
    }
}

