package com.iotsmartaliv.activity.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.iotsmartaliv.R;
import com.iotsmartaliv.apiAndSocket.models.Broadcast;
import com.iotsmartaliv.fragments.DocumentDetailFragment;
import com.iotsmartaliv.fragments.EventDetailFragment;
import com.iotsmartaliv.fragments.MessageDetailFragment;

/**
 * This activity class is used for loading fragment in broadcast details.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2020-04-06
 */


public class BroadcastDetailActivity extends AppCompatActivity {

    TextView tvHeader;
    ImageView imgback;
    Broadcast broadcastItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast_detail);

        tvHeader = findViewById(R.id.tv_header);

        extractIntent();

        imgback = findViewById(R.id.img_back_broadcast);

        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }


    private void extractIntent() {

        if (getIntent().getExtras() != null){

            broadcastItem = (Broadcast) getIntent().getExtras().getSerializable("BROADCAST_ITEM");          //intent.putExtra("BROADCAST_ITEM",massageList.get(position));

            switch (broadcastItem.getBroadcastType()) {

                case "0":
                    tvHeader.setText("Messages");
                    loadFragment(new MessageDetailFragment(broadcastItem));
                    break;

                case "1":
                    tvHeader.setText("Events");
                    loadFragment(new EventDetailFragment(broadcastItem));
                    break;

                case "2":
                    tvHeader.setText("Documents");
                    loadFragment(new DocumentDetailFragment(broadcastItem));
                    break;

            }



        }



    }

    /*
     * Method is used to load the fragment
     */
    public  void loadFragment(Fragment activeFragment) {
        FragmentTransaction fragment = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.broadcast_detail_container, activeFragment);

//        if (!(activeFragment instanceof MessageFragment)) { fragment.addToBackStack(null); }

        fragment.commit();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}