package com.iotsmartaliv.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.iotsmartaliv.R;
import com.iotsmartaliv.adapter.NotificationListAdapter;
import com.iotsmartaliv.model.NotificationModel;

import java.util.ArrayList;

/**
 * This fragment class is used for notification fragment.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2018-10-24
 */
public class NotificationFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {
    private NotificationListAdapter adapter;
    private ListView notificationListView;
    private ArrayList<NotificationModel> arrayListNotification = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = null;
        view = inflater.inflate(R.layout.notification_fragments, container, false);
        initViews(view);
        initListeners();
        NotificationModel notificationModel1 = new NotificationModel();
        notificationModel1.setNotificationString("Password change successful");
        notificationModel1.setNotificationTime(getResources().getString(R.string.dummy_date));
        arrayListNotification.add(notificationModel1);
        NotificationModel notificationModel2 = new NotificationModel();
        notificationModel2.setNotificationString("Services completed ");
        notificationModel2.setNotificationTime(getResources().getString(R.string.dummy_date));
        arrayListNotification.add(notificationModel2);

        NotificationModel notificationModel3 = new NotificationModel();
        notificationModel3.setNotificationString("New Device added");
        notificationModel3.setNotificationTime(getResources().getString(R.string.dummy_date));
        arrayListNotification.add(notificationModel3);

        adapter = new NotificationListAdapter(arrayListNotification, getActivity());
        notificationListView.setAdapter(adapter);

        notificationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            }
        });
        return view;
    }

    /**
     * Initializing listeners
     */
    private void initListeners() {

    }

    /**
     * Initializing views
     *
     * @param view
     */
    private void initViews(View view) {
        notificationListView = view.findViewById(R.id.notification_list);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onClick(View view) {

    }
}