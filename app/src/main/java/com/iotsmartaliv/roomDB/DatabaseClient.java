package com.iotsmartaliv.roomDB;

import androidx.room.Room;
import android.content.Context;

/**
 * This class is used as DatabaseClient that handle all DB instance.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 9/5/19 :May : 2019 on 16 : 41.
 */
public class DatabaseClient {
    private static DatabaseClient mInstance;
    private Context mCtx;
    //our app database object
    private AppDatabase appDatabase;

    private DatabaseClient(Context mCtx) {
        this.mCtx = mCtx;
        //creating the app database with Room database builder
        //MyToDos is the name of the database
        appDatabase = Room.databaseBuilder(mCtx, AppDatabase.class, "ALive_DB")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
    }

    public static synchronized DatabaseClient getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new DatabaseClient(mCtx);
        }
        return mInstance;
    }

    public AppDatabase getAppDatabase() {
        return appDatabase;
    }

}
