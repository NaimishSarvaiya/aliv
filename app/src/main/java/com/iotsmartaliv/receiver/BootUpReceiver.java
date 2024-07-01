package com.iotsmartaliv.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.iotsmartaliv.services.ShakeOpenService;
import com.iotsmartaliv.utils.SharePreference;

import static com.iotsmartaliv.constants.Constant.SHAKE_ENABLE;

/**
 * This class is used as .
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2/11/19 :November.
 */
public class BootUpReceiver extends BroadcastReceiver {
    private static final String TAG = "BootUpReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
          if(SharePreference.getInstance(context).getBoolean(SHAKE_ENABLE)) {
              Intent serviceIntent = new Intent(context, ShakeOpenService.class);
              context.startService(serviceIntent);
              Log.d(TAG, "onReceive: " + "boot up completed");
          }

        }
        Log.d(TAG, "onReceive: " + "In boot up receiver");
        /*Intent serviceIntent = new Intent(context, ShakeOpenService.class);
        //serviceIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(serviceIntent);*/
    }
}
