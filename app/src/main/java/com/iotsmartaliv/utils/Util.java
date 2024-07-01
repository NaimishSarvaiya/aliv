package com.iotsmartaliv.utils;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.utils.faceenroll.ConnectionManager;

public class Util {

    public  static  MutableLiveData<Boolean> value = new MutableLiveData<Boolean>();
    public static void showToast(Context context, String message){
        Toast.makeText(context,message, Toast.LENGTH_LONG).show();
    }
    public interface NetworkCheckCallback {
        void onNetworkCheckComplete(boolean isAvailable);
    }
    public static void checkInternet(Context context, NetworkCheckCallback callback) {
        ConnectionManager.performApiCallWithNetworkCheckExtra(context, callback);
    }

//    public static Boolean checkInternet(Context context){
//        ConnectionManager.performApiCallWithNetworkCheck(context, false);
//        return ConnectionManager.isNetworkAvailable;
//    }

//    public static void check(Context context){
//        ConnectionManager.performApiCallWithNetworkCheck(context, false);
//        value.setValue(ConnectionManager.isNetworkAvailable);
//    }

    public static void firebaseEvent(String eventName,Context context, String url, String userName,String userId,int errorCode
//            , String parameters,String apiType
    ){
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.URL, url);
//        bundle.putString(Constant.APITYPE, apiType);
//        bundle.putString(Constant.PARAMETER, parameters);
        bundle.putString(Constant.USERNAME,userName );
        bundle.putString(Constant.USERID,userId );
        bundle.putInt(Constant.ERRORCODE,errorCode );
        mFirebaseAnalytics.logEvent(eventName, bundle);
    }
}
