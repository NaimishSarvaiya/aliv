package com.iotsmartaliv.onboarding;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.fragment.app.Fragment;

import com.iotsmartaliv.R;


public class LastPageFragment extends Fragment {


    private TextView tvGPSState, tvBGModeState, tvBluetoothState;
    private OnBoardingActivity activity;
    private BroadcastReceiver locationStateReceiver, btStateReceiver;
    private ToggleButton bleSwitch, gpsSwitch, bgSwitch;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_onboarding_last_page, container, false);

        activity = (OnBoardingActivity) getActivity();
        bleSwitch = view.findViewById(R.id.ble_switch);
        gpsSwitch = view.findViewById(R.id.gps_switch);
        bgSwitch = view.findViewById(R.id.bg_switch);
        tvGPSState = view.findViewById(R.id.tvGPSState);
        tvBGModeState = view.findViewById(R.id.tvBGModeState);
        tvBluetoothState = view.findViewById(R.id.tvBluetoothState);
        if (activity.isBluetoothEnabled()) {
            bleSwitch.setChecked(true);
        } else bleSwitch.setChecked(false);
        if (activity.isGPSEnabled()) {
            gpsSwitch.setChecked(true);
        } else gpsSwitch.setChecked(false);
        if (activity.isBackgroundModeEnabled()) {
            bgSwitch.setChecked(true);
        } else bgSwitch.setChecked(false);
        setTextViewState(tvBluetoothState, activity.isBluetoothEnabled());
        setTextViewState(tvGPSState, activity.isGPSEnabled());
        setTextViewState(tvBGModeState, activity.isBackgroundModeEnabled());
        locationStateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                setTextViewState(tvGPSState, activity.isGPSEnabled());
            }
        };

        btStateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                setTextViewState(tvBluetoothState, activity.isBluetoothEnabled());
            }
        };

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (activity.isBluetoothEnabled()) {
            bleSwitch.setChecked(true);
        } else bleSwitch.setChecked(false);
        if (activity.isGPSEnabled()) {
            gpsSwitch.setChecked(true);
        } else gpsSwitch.setChecked(false);
        if (activity.isBackgroundModeEnabled()) {
            bgSwitch.setChecked(true);
        } else bgSwitch.setChecked(false);
        getActivity().registerReceiver(btStateReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
        getActivity().registerReceiver(locationStateReceiver, new IntentFilter("android.location.PROVIDERS_CHANGED"));
    }

    private void setTextViewState(TextView tv, boolean state) {
        if (state) {
            tv.setText(R.string.enabled);
            tv.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.onboarding_state_green), null, null, null);
            tv.setCompoundDrawablePadding((int) getResources().getDimension(R.dimen._12dp));
        } else {
            tv.setText(R.string.disabled);
            tv.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.onboarding_state_red), null, null, null);
            tv.setCompoundDrawablePadding((int) getResources().getDimension(R.dimen.eight));
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (locationStateReceiver != null)
            getActivity().unregisterReceiver(locationStateReceiver);
        if (btStateReceiver != null)
            getActivity().unregisterReceiver(btStateReceiver);
    }
}