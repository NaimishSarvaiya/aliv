package com.iotsmartaliv.dmvphonedemotest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by foxking on 2018/6/11.
 */

public class MyBroadcastReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("MyBroadcastReceiver", "onReceive: ");
        Toast.makeText(context, intent.getStringExtra("hello"), Toast.LENGTH_SHORT).show();
    }
}