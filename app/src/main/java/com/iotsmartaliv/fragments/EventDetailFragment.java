package com.iotsmartaliv.fragments;

import static com.iotsmartaliv.constants.Constant.LOGIN_DETAIL;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.iotsmartaliv.R;
import com.iotsmartaliv.adapter.BroadcastImageAdapter;
import com.iotsmartaliv.apiAndSocket.listeners.RetrofitListener;
import com.iotsmartaliv.apiAndSocket.models.Broadcast;
import com.iotsmartaliv.apiAndSocket.models.ErrorObject;
import com.iotsmartaliv.apiAndSocket.models.SuccessResponse;
import com.iotsmartaliv.apiAndSocket.retrofit.ApiServiceProvider;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.utils.Util;


public class EventDetailFragment extends Fragment {

    private TextView txteventHead, txteventAddress, txteventDescription,
            txteventLocation, txteventOrganizer, txteventDate,txteventTime;
    private Broadcast mBroadcast;
    private ViewPager viewPager;
    private BroadcastImageAdapter broadcastImageAdapter;
    private TabLayout tabLayout;
    private ApiServiceProvider apiServiceProvider;


    public EventDetailFragment(Broadcast mBroadcast){
        this.mBroadcast = mBroadcast;

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.event_detail_layout, container, false);

        initView(view);

        callApi();

        setView();

        if (mBroadcast.getBroadcastAttach() != null){

            broadcastImageAdapter = new BroadcastImageAdapter(getContext(),mBroadcast.getBroadcastAttach(), true);
            viewPager.setAdapter(broadcastImageAdapter);
            tabLayout.setupWithViewPager(viewPager, true);

            if (mBroadcast.getBroadcastAttach().size() == 1){
                tabLayout.setVisibility(View.GONE);
            }else{
                tabLayout.setVisibility(View.VISIBLE);
            }

        }else {
            viewPager.setVisibility(View.GONE);
            tabLayout.setVisibility(View.GONE);
        }

        return view;
    }

    private void setView() {

        String[] startDate = mBroadcast.getEventStartDate().toString().split(",");
        String[] endDate = mBroadcast.getEventEndDate().toString().split(",");

        txteventHead.setText(mBroadcast.getBroadcastTitle());

        txteventDescription.setText(mBroadcast.getBroadcastDetails());

        txteventLocation.setText(mBroadcast.getEventLocation().toString());

        txteventAddress.setText(mBroadcast.getEventFullAddress().toString());

        txteventOrganizer.setText(mBroadcast.getEventOrganizer().toString());

        txteventDate.setText(startDate[0]);

        txteventTime.setText(startDate[1]+"-"+endDate[1]);
    }

    private void initView(View view) {

        txteventHead = view.findViewById(R.id.txt_event_head);
        txteventDescription = view.findViewById(R.id.txt_event_detail);
        txteventLocation = view.findViewById(R.id.tv_location);
        txteventAddress = view.findViewById(R.id.tv_location_city);
        txteventOrganizer = view.findViewById(R.id.tv_organizer);
        txteventDate = view.findViewById(R.id.tv_event_date);
        txteventTime = view.findViewById(R.id.tv_event_time);
        viewPager = view.findViewById(R.id.viewpager);
        tabLayout = view.findViewById(R.id.tab_layout);
    }

    private void callApi() {

        if (mBroadcast.getReadStatus().equalsIgnoreCase("0")){

            apiServiceProvider = ApiServiceProvider.getInstance(getContext(),false);
            Util.checkInternet(requireActivity(), new Util.NetworkCheckCallback() {
                @Override
                public void onNetworkCheckComplete(boolean isAvailable) {
                    if (isAvailable) {
                        apiServiceProvider.callUpdateBroadcastReadStatus(mBroadcast.getBuID(), new RetrofitListener<SuccessResponse>() {
                            @Override
                            public void onResponseSuccess(SuccessResponse sucessRespnse, String apiFlag) {

                                if (sucessRespnse.getStatus().equalsIgnoreCase("OK")) {

                                    mBroadcast.setReadStatus("1");

                                }

                            }

                            @Override
                            public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                                Util.firebaseEvent(Constant.APIERROR, requireActivity(), Constant.UrlPath.SERVER_URL + apiFlag, LOGIN_DETAIL.getUsername(), LOGIN_DETAIL.getAppuserID(), errorObject.getStatus());

                                try {
                                    Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                                } catch (Exception e) {
                                    Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                }
            });

        }

    }
}
/*
package com.iotsmart.activity.ViewPager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.iotsmart.R;
import com.iotsmart.apiCalling.listeners.RetrofitListener;
import com.iotsmart.apiCalling.models.Broadcast;
import com.iotsmart.apiCalling.models.BroadcastModel;
import com.iotsmart.apiCalling.models.ErrorObject;
import com.iotsmart.apiCalling.retrofit.ApiServiceProvider;
import com.iotsmart.constants.Constant;
import com.iotsmart.fragments.DocumentFragment;
import com.iotsmart.fragments.EventFragment;
import com.iotsmart.fragments.MessageCommunityBroadcastFragment;

import java.util.ArrayList;
import java.util.List;

public class BroadcastCommunityActivity extends AppCompatActivity implements RetrofitListener<BroadcastModel> {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageView imgBack;
    ViewPagerAdapter adapter;
    private Boolean isActive = false;

    EventFragment eventFragment;
    MessageCommunityBroadcastFragment messageFragment;
    DocumentFragment documentFragment;

    ArrayList<Broadcast> mMassage = new ArrayList<>();
    ArrayList<Broadcast> mEvent = new ArrayList<>();
    ArrayList<Broadcast> mDocument = new ArrayList<>();

    private ApiServiceProvider apiServiceProvider;

    String[] tabTitle = {"Messages", "Events", "Documents"};
    int[] tabIcon = {R.drawable.massage, R.drawable.event, R.drawable.doc};
    public List<Broadcast> mBroadcastList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast_community);

        initView();

        setListeners();

        setupViewPagerTablayout();

        callBroadcastApi();

//        updateTabItem(tabLayout.getTabAt(0), true);
    }

    private void initView() {

        viewPager = findViewById(R.id.viewpager);

        tabLayout = findViewById(R.id.tablayout);

        imgBack = findViewById(R.id.img_back_open_door);

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
            public void onTabReselected(TabLayout.Tab tab) { }
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

        apiServiceProvider = ApiServiceProvider.getInstance(this);

        apiServiceProvider.callGetUserBroadcast(this);

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

    private void updateTabBadgeCount(TabLayout.Tab tab, ArrayList<Broadcast> broadcasts) {

        int badgeCount = 0;

        for (Broadcast broadcast : broadcasts) {

            if (broadcast.getReadStatus().equalsIgnoreCase("0"))
                badgeCount++;

        }

        TextView badge =tab.getCustomView().findViewById(R.id.tv_tab_batch_count);


        if (badgeCount > 0) {

            badge.setVisibility(View.VISIBLE);

            badge.setText("" + badgeCount);

        } else{

            badge.setVisibility(View.GONE);

        }
    }

    private void setBroadcastData() {

        mMassage.clear();
        mEvent.clear();
        mDocument.clear();

        for (int i = 0; i < mBroadcastList.size(); i++) {

            if (mBroadcastList.get(i).getBroadcastType().equals("0")) {

                mMassage.add(mBroadcastList.get(i));

            } else if (mBroadcastList.get(i).getBroadcastType().equals("1")) {

                mEvent.add(mBroadcastList.get(i));

            } else if (mBroadcastList.get(i).getBroadcastType().equals("2")) {

                mDocument.add(mBroadcastList.get(i));

            }

        }

        if (!isActive) {

            messageFragment.updateList(mMassage);

            eventFragment.updateList(mEvent);

            documentFragment.updateList(mDocument);

        }

        isActive = true;

        updateTabBadgeCount(tabLayout.getTabAt(0), mMassage);

        updateTabBadgeCount(tabLayout.getTabAt(1), mEvent);

        updateTabBadgeCount(tabLayout.getTabAt(2), mDocument);

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

                    setBroadcastData();

                    updateTabItem(tabLayout.getTabAt(0), true);

                } else {

                    Toast.makeText(this, sucessRespnse.getMsg(), Toast.LENGTH_LONG).show();

                }
                break;
        }
    }

    @Override
    public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isActive) {

            setBroadcastData();

        }

    }

}
*/
