package com.iotsmartaliv.roomDB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.iotsmartaliv.apiCalling.models.DeviceObject;

import java.util.List;

/**
 * This class is used as Dao class when we want perform action on DB.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 9/5/19 :May : 2019 on 15 : 08.
 */

@Dao
public interface DeviceDao {

    @Query("SELECT * FROM DeviceObject")
    List<DeviceObject> getAllDeviceList();

    @Query("SELECT * FROM AccessLogModel")
    List<AccessLogModel> getAllDeviceLog();

    @Insert
    void insert(List<DeviceObject> deviceObject);

    @Insert
    void insertAccessLog(AccessLogModel accessLogModel);

    @Delete
    void delete(DeviceObject deviceObject);

    @Delete
    void deleteAccessLog(AccessLogModel accessLogModel);

    @Query("DELETE FROM DeviceObject")
    void deleteAll();

    @Update
    void update(DeviceObject deviceObject);
}
