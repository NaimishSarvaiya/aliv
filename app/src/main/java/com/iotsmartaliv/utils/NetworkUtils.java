package com.iotsmartaliv.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtils {

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }

    public static boolean hasInternetConnectivity(Context context) {
        try {
            // Try to connect to a well-known server (Google DNS)
            java.net.Socket socket = new java.net.Socket();
            java.net.SocketAddress socketAddress = new java.net.InetSocketAddress("8.8.8.8", 53);
            socket.connect(socketAddress, 1500); // Timeout of 1.5 seconds
            socket.close();
            return true; // If successful, internet connectivity is available
        } catch (java.io.IOException e) {
            return false;
        }
    }
}
