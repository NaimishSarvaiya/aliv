package com.iotsmartaliv.utils.faceenroll;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConnectionUtils {
    public static void checkNetworkStatusAsync(Context context, OnNetworkStatusListener listener) {
        new CheckNetworkStatusTask(context, listener).execute();
    }

    public interface OnNetworkStatusListener {
        void onNetworkStatus(boolean isAvailable);
    }

    private static class CheckNetworkStatusTask extends AsyncTask<Void, Void, Boolean> {
        private final Context context;
        private final OnNetworkStatusListener listener;

        public CheckNetworkStatusTask(Context context, OnNetworkStatusListener listener) {
            this.context = context;
            this.listener = listener;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

                if (cm != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        Network network = cm.getActiveNetwork();
                        if (network != null) {
                            NetworkCapabilities nc = cm.getNetworkCapabilities(network);
                            return nc != null && (nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                                    nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) &&
                                    isInternetConnectionAvailable();
                        }
                    } else {
                        // For devices below Android 6.0
                        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                        return activeNetwork != null && (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI ||
                                activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) &&
                                isInternetConnectionAvailable();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (listener != null) {
                listener.onNetworkStatus(result);
            }
        }
    }

    private static boolean isInternetConnectionAvailable() {
        try {
            HttpURLConnection urlConnection = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
            urlConnection.setRequestProperty("User-Agent", "Test");
            urlConnection.setRequestProperty("Connection", "close");
            urlConnection.setConnectTimeout(1500);
            urlConnection.connect();
            return (urlConnection.getResponseCode() == 200 || urlConnection.getResponseCode() == 204);
        } catch (IOException e) {
            return false;
        }
    }
}
