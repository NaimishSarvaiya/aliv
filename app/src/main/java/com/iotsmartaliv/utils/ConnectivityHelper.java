package com.iotsmartaliv.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

public class ConnectivityHelper{
    private static ConnectivityReceiver connectivityReceiver;

    public static void initialize(Context context) {
        if (connectivityReceiver == null) {
            connectivityReceiver = new ConnectivityReceiver();
            registerConnectivityReceiver(context);
        }
    }

    private static void registerConnectivityReceiver(Context context) {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        context.getApplicationContext().registerReceiver(connectivityReceiver, filter);
    }

    public static void release(Context context) {
        if (connectivityReceiver != null) {
            context.getApplicationContext().unregisterReceiver(connectivityReceiver);
            connectivityReceiver = null;
        }
    }

    public interface ConnectivityListener {
        void onConnectivityChanged(boolean isConnected, boolean hasInternet);
    }


    public static class ConnectivityReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (connectivityListener != null) {
                boolean isConnected = NetworkUtils.isNetworkConnected(context);
                boolean hasInternet = isConnected && NetworkUtils.hasInternetConnectivity(context);

                connectivityListener.onConnectivityChanged(isConnected, hasInternet);
            }
        }
    }

    private static ConnectivityListener connectivityListener;

    public static void setConnectivityListener(ConnectivityListener listener) {
        connectivityListener = listener;
    }
}
