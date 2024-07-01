package com.iotsmartaliv.utils.faceenroll;

import android.content.Context;
import android.widget.Toast;

import com.iotsmartaliv.utils.Util;

public class ConnectionManager {
    public static Boolean isNetworkAvailable = false;

    public static void performApiCallWithNetworkCheck(Context context, Boolean internet) {
        ConnectionUtils.checkNetworkStatusAsync(context, new ConnectionUtils.OnNetworkStatusListener() {
            @Override
            public void onNetworkStatus(boolean isAvailable) {
                if (isAvailable) {
                    isNetworkAvailable = true; // Set to true when the network is available
                    // Network is available, execute the API call
                    // Call your API method or perform other actions here
                    // ...
                } else {
                    isNetworkAvailable = false;
                    // Network is not available, show a message to the user or handle accordingly
                    // For example, you could show a Snackbar or display a message in the UI
                    // Snackbar.make(view, "No internet connection available", Snackbar.LENGTH_SHORT).show();
                    Toast.makeText(context, "No internet connection available", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public static void performApiCallWithNetworkCheckExtra(Context context, Util.NetworkCheckCallback callback) {
        ConnectionUtils.checkNetworkStatusAsync(context, new ConnectionUtils.OnNetworkStatusListener() {
            @Override
            public void onNetworkStatus(boolean isAvailable) {
                if (isAvailable) {
                    if (callback != null) callback.onNetworkCheckComplete(true);
                } else {
                    if (callback != null) callback.onNetworkCheckComplete(false);
                    Toast.makeText(context, "No internet connection available", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}