package com.iotsmartaliv.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.iotsmartaliv.R;
import com.iotsmartaliv.adapter.GuestListAdapter;
import com.iotsmartaliv.model.GuestListModel;

import java.util.ArrayList;

/**
 * This activity class is used for Guest .
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2019-01-16
 */
public class GuestActivity extends AppCompatActivity implements View.OnClickListener {
    private GuestListAdapter adapter;
    private ListView guestList;
    private ImageView addGuest;
    private ImageView imgBackGuestList;
    private ArrayList<GuestListModel> arrayListInboxMessages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest);
        initViews();
        initListeners();
    }

    /**
     * init listeners
     */
    private void initListeners() {
        addGuest.setOnClickListener(this);
        imgBackGuestList.setOnClickListener(this);
    }

    /**
     * initialize views.
     */
    private void initViews() {
        guestList = findViewById(R.id.guest_list);
        addGuest = findViewById(R.id.add_guest_image);
        imgBackGuestList = findViewById(R.id.img_back_guest_list);
        GuestListModel guestListModel1 = new GuestListModel();
        guestListModel1.setGuestName("Guest 1");
        guestListModel1.setDeviceName("Device name");
        arrayListInboxMessages.add(guestListModel1);
        GuestListModel guestListModel2 = new GuestListModel();
        guestListModel2.setGuestName("Guest 2");
        guestListModel2.setDeviceName("Device name");
        arrayListInboxMessages.add(guestListModel2);
        GuestListModel guestListModel3 = new GuestListModel();
        guestListModel3.setGuestName("Guest 3");
        guestListModel3.setDeviceName("Device name");
        arrayListInboxMessages.add(guestListModel3);
        adapter = new GuestListAdapter(arrayListInboxMessages, this);
        guestList.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_guest_image:
                Intent intentCreateGuest = new Intent(GuestActivity.this, CreateGuestActivity.class);
                startActivityForResult(intentCreateGuest, 101);
                break;
            case R.id.img_back_guest_list:
                finish();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 101:
                if (data != null) {
                    String guestName = data.getStringExtra("guest_name");
                    String deviceName = data.getStringExtra("device_name");
                    if (guestName.equalsIgnoreCase("") && deviceName.equalsIgnoreCase("")) {
                        GuestListModel guestListModel = new GuestListModel();
                        guestListModel.setGuestName("Guest");
                        guestListModel.setDeviceName("Device name");
                        arrayListInboxMessages.add(guestListModel);
                        adapter = new GuestListAdapter(arrayListInboxMessages, this);
                        guestList.setAdapter(adapter);
                    } else {
                        GuestListModel guestListModel = new GuestListModel();
                        guestListModel.setGuestName(guestName);
                        guestListModel.setDeviceName(deviceName);
                        arrayListInboxMessages.add(guestListModel);
                        adapter = new GuestListAdapter(arrayListInboxMessages, this);
                        guestList.setAdapter(adapter);
                    }
                }
                break;
        }
    }
}

