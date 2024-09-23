package com.iotsmartaliv.fragments;

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


public class DocumentDetailFragment extends Fragment {

    private TextView txtDocumentHead;
    private Broadcast mBroadcast;
    private ViewPager viewPager;
    private BroadcastImageAdapter broadcastImageAdapter;
    private TabLayout tabLayout;
    private ApiServiceProvider apiServiceProvider;


    public DocumentDetailFragment(Broadcast mBroadcast){
        this.mBroadcast = mBroadcast;

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.document_detail_layout, container, false);

        initView(view);

        callApi();

        txtDocumentHead.setText(mBroadcast.getBroadcastTitle());

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

    private void initView(View view) {

        viewPager = view.findViewById(R.id.viewpager);
        tabLayout = view.findViewById(R.id.tab_layout);
        txtDocumentHead = view.findViewById(R.id.txt_document_head);

    }

    private void callApi() {

        if (mBroadcast.getReadStatus().equalsIgnoreCase("0")){

            apiServiceProvider = ApiServiceProvider.getInstance(getContext());
            apiServiceProvider.callUpdateBroadcastReadStatus(mBroadcast.getBuID(), new RetrofitListener<SuccessResponse>() {
                @Override
                public void onResponseSuccess(SuccessResponse sucessRespnse, String apiFlag) {

                    if (sucessRespnse.getStatus().equalsIgnoreCase("OK")) {

                        mBroadcast.setReadStatus("1");

                    }

                }

                @Override
                public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                    try {
                        Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                    }
                }
            });

        }

    }
}
