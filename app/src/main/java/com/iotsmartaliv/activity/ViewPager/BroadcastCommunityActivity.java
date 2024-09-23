package com.iotsmartaliv.activity.ViewPager;

import static com.iotsmartaliv.constants.Constant.LOGIN_DETAIL;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.iotsmartaliv.R;
import com.iotsmartaliv.apiAndSocket.listeners.RetrofitListener;
import com.iotsmartaliv.apiAndSocket.models.Broadcast;
import com.iotsmartaliv.apiAndSocket.models.BroadcastModel;
import com.iotsmartaliv.apiAndSocket.models.ErrorObject;
import com.iotsmartaliv.apiAndSocket.retrofit.ApiServiceProvider;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.fragments.DocumentFragment;
import com.iotsmartaliv.fragments.EventFragment;
import com.iotsmartaliv.fragments.MessageCommunityBroadcastFragment;
import com.iotsmartaliv.model.BroadcastDocumentFolder;
import com.iotsmartaliv.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BroadcastCommunityActivity extends AppCompatActivity implements RetrofitListener<BroadcastModel> {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageView imgBack;
    private RelativeLayout rlHeader;
    ViewPagerAdapter adapter;

    EventFragment eventFragment;
    MessageCommunityBroadcastFragment messageFragment;
    DocumentFragment documentFragment;

    ArrayList<Broadcast> mMassage = new ArrayList<>();
    ArrayList<Broadcast> mEvent = new ArrayList<>();
    ArrayList<Broadcast> mDocument = new ArrayList<>();
    ArrayList<BroadcastDocumentFolder> mDocumentFolderlist = new ArrayList<>();

    String[] tabTitle = {"Announcement", "Events", "Documents"};
    int[] tabIcon = {R.drawable.ic_announcement, R.drawable.event, R.drawable.doc};
    public List<Broadcast> mBroadcastList;

    ApiServiceProvider apiServiceProvider;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast_community);

        initView();

        apiServiceProvider = ApiServiceProvider.getInstance(this);

        setupViewPagerTablayout();

        setListeners();

        updateTabItem(tabLayout.getTabAt(0), true);

    }

    private void initView() {

        viewPager = findViewById(R.id.viewpager);

        tabLayout = findViewById(R.id.tablayout);

        imgBack = findViewById(R.id.img_back_open_door);

        rlHeader = findViewById(R.id.header_layout);

    }


    private void setListeners() {

        imgBack.setOnClickListener(view -> onBackPressed());

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                updateTabItem(tab, true);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

                updateTabItem(tab, false);

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

    }


    private void setupViewPagerTablayout() {
        viewPager.setOffscreenPageLimit(3);

        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        messageFragment = new MessageCommunityBroadcastFragment();
        eventFragment = new EventFragment();
        documentFragment = new DocumentFragment();

        adapter.addFragment(messageFragment, "Messages");
        adapter.addFragment(eventFragment, "Events");
        adapter.addFragment(documentFragment, "Documents");

        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);

        deselectAllTab();

        setupTabIcons();

    }


    private void setupTabIcons() {

        try {

            for (int i = 0; i < tabTitle.length; i++) {

                tabLayout.getTabAt(i).setCustomView(prepareTabView(i));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private View prepareTabView(int pos) {

        int[] unreadCount = {mMassage.size(), mEvent.size(), mDocument.size()};

        View view = getLayoutInflater().inflate(R.layout.custom_broadcast_tab_item, null);
        ImageView imgIcon = view.findViewById(R.id.img_tab_icon);
        TextView tvTitle = view.findViewById(R.id.tv_tab_title);
        TextView tvCount = view.findViewById(R.id.tv_tab_batch_count);

        tvTitle.setText(tabTitle[pos]);

        imgIcon.setImageResource(tabIcon[pos]);

        if (unreadCount[pos] > 0) {
            tvCount.setVisibility(View.VISIBLE);
            tvCount.setText("" + unreadCount[pos]);
        } else
            tvCount.setVisibility(View.GONE);

        return view;
    }


    private void callBroadcastApi() {

        String appUserId  = LOGIN_DETAIL.getAppuserID();

        Util.checkInternet(this, new Util.NetworkCheckCallback() {
            @Override
            public void onNetworkCheckComplete(boolean isAvailable) {
                if (isAvailable){
                    apiServiceProvider.callGetUserBroadcast(BroadcastCommunityActivity.this, appUserId);
                }

            }
        });

    }


    private void updateTabItem(TabLayout.Tab tab, boolean selected) {

        int color;

        if (selected)
            color = ContextCompat.getColor(getApplicationContext(), R.color.Orange);
        else
            color = ContextCompat.getColor(getApplicationContext(), R.color.black);

        TextView title = (TextView) tab.getCustomView().findViewById(R.id.tv_tab_title);

        ImageView icon = (ImageView) tab.getCustomView().findViewById(R.id.img_tab_icon);

        title.setTextColor(color);

        DrawableCompat.setTint(icon.getDrawable(), color);

    }

    private void updateTabBadgeCount(TabLayout.Tab tab, ArrayList<Broadcast> broadcasts, ArrayList<BroadcastDocumentFolder> mDocumentFolderlist) {

        int badgeCount = 0;

        if (broadcasts != null) {

            for (Broadcast broadcast : broadcasts) {

                if (broadcast.getReadStatus().equalsIgnoreCase("0"))
                    badgeCount++;

            }

        } else {

            for (BroadcastDocumentFolder documentFolder : mDocumentFolderlist) {

                if (documentFolder.getReadStatus().equalsIgnoreCase("0"))
                    badgeCount++;

            }


        }


        TextView badge = tab.getCustomView().findViewById(R.id.tv_tab_batch_count);


        if (badgeCount > 0) {

            badge.setVisibility(View.VISIBLE);

            badge.setText("" + badgeCount);

        } else {

            badge.setVisibility(View.GONE);

        }
    }

    private void setBroadcastData() {

        mMassage.clear();
        mEvent.clear();
        mDocument.clear();
        mDocumentFolderlist.clear();


        for (int i = 0; i < mBroadcastList.size(); i++) {

            switch (mBroadcastList.get(i).getBroadcastType()) {

                case "0":
                    mMassage.add(mBroadcastList.get(i));
                    break;

                case "1":
                    mEvent.add(mBroadcastList.get(i));
                    break;

                case "2":
                    mDocument.add(mBroadcastList.get(i));
                    break;

            }

        }

        createDocumentFolder();

        messageFragment.updateList(mMassage);

        eventFragment.updateList(mEvent);

        documentFragment.updateList(mDocumentFolderlist);


        updateTabBadgeCount(tabLayout.getTabAt(0), mMassage, null);

        updateTabBadgeCount(tabLayout.getTabAt(1), mEvent, null);

        updateTabBadgeCount(tabLayout.getTabAt(2), null, mDocumentFolderlist);

    }

    private void createDocumentFolder() {

        for (int i = 0; i < mDocument.size(); i++) {

            boolean folderAdded = true;

            ArrayList<Broadcast> mDocumentdd = new ArrayList<>();

            String folderName = mDocument.get(i).getBroadcastFolder().toString().trim();

            for (int j = 0; j < mDocument.size(); j++) {

                if (folderName.equalsIgnoreCase(mDocument.get(j).getBroadcastFolder().toString().trim())) {

                    mDocumentdd.add(mDocument.get(j));

                }

            }

            for (int j = 0; j < mDocumentFolderlist.size(); j++) {

                if (mDocumentFolderlist.get(j).getBroadcasts().get(0).getBroadcastFolder().toString().trim().equalsIgnoreCase(folderName)) {

                    folderAdded = false;

                    break;
                }

            }

            if (folderAdded) {

                boolean readStatus = true;

                BroadcastDocumentFolder documentFolder = new BroadcastDocumentFolder();

                documentFolder.setBroadcasts(mDocumentdd);

                documentFolder.setDocumentFolderTitle(folderName);

                for (Broadcast broadcast : mDocumentdd) {

                    if (broadcast.getReadStatus().equalsIgnoreCase("0")) {

                        readStatus = false;

                        break;
                    }
                }

                if (readStatus)
                    documentFolder.setReadStatus("1");
                else
                    documentFolder.setReadStatus("0");

                mDocumentFolderlist.add(documentFolder);

            }

        }
    }

    private void deselectAllTab() {

        int color = ContextCompat.getColor(getApplicationContext(), R.color.black);

        for (int icon : tabIcon) {

            DrawableCompat.setTint(getResources().getDrawable(icon), color);

        }

    }

    @Override
    public void onResponseSuccess(BroadcastModel sucessRespnse, String apiFlag) {
        switch (apiFlag) {
            case Constant.UrlPath.Broadcast_API:
                if (sucessRespnse.getStatus().equalsIgnoreCase("OK")) {

                    mBroadcastList = sucessRespnse.getBroadcast();

                    if (getIntent().getExtras() != null) {

                        redirectToDetailBraodcast();

                    } else {

                        setBroadcastData();

                    }

                } else {

                    Toast.makeText(this, sucessRespnse.getMsg(), Toast.LENGTH_LONG).show();

                }
                break;
        }
    }

    @Override
    public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {

        switch (apiFlag) {
            case Constant.UrlPath.Broadcast_API:
                Util.firebaseEvent(Constant.APIERROR, BroadcastCommunityActivity.this,Constant.UrlPath.SERVER_URL+apiFlag, LOGIN_DETAIL.getUsername(),LOGIN_DETAIL.getAppuserID(),errorObject.getStatus());

                try {
                    Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
                }
                break;
        }

    }

    private void redirectToDetailBraodcast() {

        String mAppUserID ;
        String mBroadcastID;

        if (getIntent().getExtras().getString("APP_USER_ID") !=null && getIntent().getExtras().getString("BROADCAST_ID") !=null){
            mAppUserID = getIntent().getExtras().getString("APP_USER_ID");
            mBroadcastID = getIntent().getExtras().getString("BROADCAST_ID");
        }else {
            String data = getIntent().getExtras().get("data").toString();
            // Convert the string to a JSONObject
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(data);
                mAppUserID = jsonObject.getString("appuser_ID");
                mBroadcastID = jsonObject.getString("broadcast_ID");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            // Retrieve the values using the keys
        }

        for (Broadcast broadcast : mBroadcastList) {

            if (broadcast.getBroadcastID().equalsIgnoreCase(mBroadcastID) && broadcast.getAppuserID().equalsIgnoreCase(mAppUserID)) {

                Intent intent = new Intent(BroadcastCommunityActivity.this, BroadcastDetailActivity.class);

                intent.putExtra("BROADCAST_ITEM", broadcast);

                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                startActivity(intent);

                setIntent(new Intent());

            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getIntent().getExtras() != null) {

            rlHeader.setVisibility(View.GONE);
            tabLayout.setVisibility(View.GONE);
        } else {
            rlHeader.setVisibility(View.VISIBLE);
            tabLayout.setVisibility(View.VISIBLE);

        }
        callBroadcastApi();

        setupBroadcastReceiver();

    }
    private void setupBroadcastReceiver() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Call the method to refresh the activity content
                callBroadcastApi();
            }
        };

        // Registering the BroadcastReceiver
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("com.iotsmartaliv.UPDATE_BROADCAST_ACTIVITY"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister BroadcastReceiver when the activity is destroyed
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }
}