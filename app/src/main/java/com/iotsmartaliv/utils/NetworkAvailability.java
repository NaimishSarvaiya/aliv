package com.iotsmartaliv.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;

/**
 * TO check if Internet connection is available or not.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2018-07-23
 */
public class NetworkAvailability {
    private static NetworkAvailability mReference = null;
    private static Context context;

    private NetworkAvailability() {
    }

    public static NetworkAvailability getInstance(Context activity) {
        if (null == mReference)
            mReference = new NetworkAvailability();
        context = activity;
        return mReference;
    }

    public static boolean checkNetworkStatus() {
        try {
            boolean haveConnectedWifi = false;
            boolean haveConnectedMobile = false;
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo[] netInfo = cm.getAllNetworkInfo();
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                NetworkCapabilities nc = cm.getNetworkCapabilities(cm.getActiveNetwork());

            }
            for (NetworkInfo ni : netInfo) {
                if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                    if (ni.isConnected())
                        haveConnectedWifi = true;

                if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                    if (ni.isConnected())
                        haveConnectedMobile = true;
            }
            return haveConnectedWifi || haveConnectedMobile;
        } catch (Exception e) {
            return false;
        }
    }



    // AsyncTask to perform the internet-related task in the background
    private static class InternetTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                // Attempt to establish a connection to a reliable internet server (e.g., Google's DNS)
                // You can replace "8.8.8.8" with a different server IP address if needed
                return Runtime.getRuntime().exec("ping -c 1 8.8.8.8").waitFor() == 0;
            } catch (Exception e) {
                Log.e("NetworkAvailability", "Error checking internet connection", e);
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean isConnected) {
            if (isConnected) {
                // Internet is available, perform your internet-related task
                Toast.makeText(context, "Internet Task Executed", Toast.LENGTH_SHORT).show();
            } else {
                // No internet connection, show a message or take appropriate action
                Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static void checkAndPerformInternetTask() {
        if (checkNetworkStatus()) {
            // Internet is available, initiate the internet-related task
            new InternetTask().execute();
        } else {
            // No internet connection, show a message or take appropriate action
            Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }
}