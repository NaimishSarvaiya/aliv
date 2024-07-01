package com.iotsmartaliv.utils;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.iotsmartaliv.roomDB.AccessLogModel;
import com.iotsmartaliv.roomDB.DatabaseClient;

import static com.iotsmartaliv.services.DeviceLogSyncService.UPDATE_APPS_PACKAGE;

/**
 * This class is used as .
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 8/7/19 :July : 2019 on 19 : 03.
 */
public class SaveAccessLogTask extends AsyncTask<Void, Void, Void> {

    private Context context;
    private AccessLogModel accessLogModel;

    public SaveAccessLogTask(Context context, AccessLogModel accessLogModel) {
        this.context = context;
        this.accessLogModel = accessLogModel;

    }

    @Override
    protected Void doInBackground(Void... voids) {
        DatabaseClient.getInstance(context).getAppDatabase()
                .deviceDao()
                .insertAccessLog(accessLogModel);
        Log.d("SaveAccessLogTask", "doInBackground: " + accessLogModel.getDevice_SN() + ":Time:- " + accessLogModel.getEvent_time());
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Intent localIntent = new Intent(UPDATE_APPS_PACKAGE);
        context.sendBroadcast(localIntent);
        //    Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
    }
}
