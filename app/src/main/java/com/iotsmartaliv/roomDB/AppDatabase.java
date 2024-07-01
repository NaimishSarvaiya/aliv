package com.iotsmartaliv.roomDB;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.iotsmartaliv.apiCalling.models.DeviceObject;

/**
 * This class is used as Room database class that is create table in Db.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 9/5/19 :May : 2019 on 15 : 12.
 */
@Database(entities = {DeviceObject.class, AccessLogModel.class}, version = 6, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DeviceDao deviceDao();


}
