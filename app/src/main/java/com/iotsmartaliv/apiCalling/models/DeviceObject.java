package com.iotsmartaliv.apiCalling.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.intelligoo.sdk.LibDevModel;

import java.io.Serializable;

/**
 * This class is used as Model that is contains the all the device detail.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 3/5/19 :May : 2019 on 14 : 53.
 */

@Entity
public class DeviceObject implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @SerializedName("ud_ID")
    @Expose
    private String udID;
    @SerializedName("user_ID")
    @Expose
    private String userID;
    @SerializedName("device_ID")
    @Expose
    private String deviceID;
    @SerializedName("group_ids")
    @Expose
    private String groupIds;
    @SerializedName("access_key")
    @Expose
    private String accessKey;
    @SerializedName("permissions")
    @Expose
    private String permissions;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("device_sno")
    @Expose
    private String deviceSno;
    @SerializedName("device_name")
    @Expose
    private String deviceName;
    @SerializedName("device_MAC")
    @Expose
    private String deviceMAC;
    @SerializedName("device_key")
    @Expose
    private String deviceKey;
    @SerializedName("type_ID")
    @Expose
    private String typeID;
    @SerializedName("e_key")
    @Expose
    private String eKey;
    @SerializedName("function")
    @Expose
    private String function;
    @SerializedName("volp_account")
    @Expose
    private String volpAccount;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("iotyun_server")
    @Expose
    private String iotyunServer;
    @SerializedName("tcp_server")
    @Expose
    private String tcpServer;
    @SerializedName("remarks")
    @Expose
    private String remarks;
    @SerializedName("offline_password_version")
    @Expose
    private String offlinePasswordVersion;
    @SerializedName("deleted")
    @Expose
    private String deleted;
    @SerializedName("device_model")
    @Expose
    private String deviceModel;
    @SerializedName("device_type")
    @Expose
    private String deviceType;
    @SerializedName("dev_type")
    @Expose
    private String devType;
    @SerializedName("networking")
    @Expose
    private String networking;
    @SerializedName("cd_ID")
    @Expose
    private String cdID;
    @SerializedName("community_ID")
    @Expose
    private String communityID;
    @SerializedName("cdevice_name")
    @Expose
    private String cdeviceName;
    @SerializedName("assigned")
    @Expose
    private String assigned;

    @SerializedName("smartcity_ID")
    @Expose
    private String smartcityID;

    @SerializedName("rssi")
    @Expose
    private int rssi = 0;

    @SerializedName("access_endtime")
    @Expose
    private String accessEndtime;

    @SerializedName("access_starttime")
    @Expose
    private String accessStarttime;

    @SerializedName("isAccessTimeEnabled")
    @Expose
    private String isAccessTimeEnabled;
    //access_endtime,access_starttime,isAccessTimeEnabled

    public DeviceObject(String udID, String userID, String deviceID, String groupIds, String accessKey, String permissions, String createdAt, String updatedAt, String deviceSno, String deviceName, String deviceMAC, String deviceKey, String typeID, String eKey, String function, String volpAccount, String status, String iotyunServer, String tcpServer, String remarks, String offlinePasswordVersion, String deleted, String deviceModel, String deviceType, String devType, String networking, String cdID, String communityID, String cdeviceName, String assigned, String smartcityID, int rssi
            , String accessEndtime, String accessStarttime, String isAccessTimeEnabled) {
        this.udID = udID;
        this.userID = userID;
        this.deviceID = deviceID;
        this.groupIds = groupIds;
        this.accessKey = accessKey;
        this.permissions = permissions;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deviceSno = deviceSno;
        this.deviceName = deviceName;
        this.deviceMAC = deviceMAC;
        this.deviceKey = deviceKey;
        this.typeID = typeID;
        this.eKey = eKey;
        this.function = function;
        this.volpAccount = volpAccount;
        this.status = status;
        this.iotyunServer = iotyunServer;
        this.tcpServer = tcpServer;
        this.remarks = remarks;
        this.offlinePasswordVersion = offlinePasswordVersion;
        this.deleted = deleted;
        this.deviceModel = deviceModel;
        this.deviceType = deviceType;
        this.devType = devType;
        this.networking = networking;
        this.cdID = cdID;
        this.communityID = communityID;
        this.cdeviceName = cdeviceName;
        this.assigned = assigned;
        this.smartcityID = smartcityID;
        this.rssi = rssi;
        this.accessStarttime = accessStarttime;
        this.accessEndtime = accessEndtime;
        this.isAccessTimeEnabled = isAccessTimeEnabled;
    }

    public DeviceObject(LibDevModel libDev) {
        deviceSno = libDev.devSn;
        deviceMAC = libDev.devMac;
        devType = String.valueOf(libDev.devType);
        eKey = libDev.eKey;

        //  privilege = libDev.privilege;
        // openType = libDev.openType;
        //    verified = libDev.verified;
        //  startDate = libDev.startDate;
        //endDate = libDev.endDate;
        //useCount = libDev.useCount;
    }

    @SerializedName("relay_connected")
    @Expose
    private String relayConnected;

    public String getSmartcityID() {
        return smartcityID;
    }

    public void setSmartcityID(String smartcityID) {
        this.smartcityID = smartcityID;
    }

    public String getAccessEndtime() {
        return accessEndtime;
    }

    public void setAccessEndtime(String accessEndtime) {
        this.accessEndtime = accessEndtime;
    }

    public String getAccessStarttime() {
        return accessStarttime;
    }

    public void setAccessStarttime(String accessStarttime) {
        this.accessStarttime = accessStarttime;
    }

    public String getIsAccessTimeEnabled() {
        return isAccessTimeEnabled;
    }

    public void setIsAccessTimeEnabled(String isAccessTimeEnabled) {
        this.isAccessTimeEnabled = isAccessTimeEnabled;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUdID() {
        return udID;
    }

    public void setUdID(String udID) {
        this.udID = udID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getGroupIds() {
        return groupIds;
    }

    public void setGroupIds(String groupIds) {
        this.groupIds = groupIds;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getDeviceSno() {
        return deviceSno;
    }

    public String getDeviceSnoWithoutAlphabet() {
        //  String deviceSNNumber=deviceNo;
        //if (deviceNo.contains)
        String s1 = deviceSno;
        s1 = s1.replace("VF", "");
        Log.d("ALiv", "getDeviceSnoWithoutAlphabet: " + s1);
        s1 = s1.replace("V", "");
        System.out.println(s1);
        Log.d("ALiv", "getDeviceSnoWithoutAlphabet: " + s1);
        return s1;
    }

    public void setDeviceSno(String deviceSno) {
        this.deviceSno = deviceSno;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceMAC() {
        return deviceMAC;
    }

    public void setDeviceMAC(String deviceMAC) {
        this.deviceMAC = deviceMAC;
    }

    public String getDeviceKey() {
        return deviceKey;
    }

    public void setDeviceKey(String deviceKey) {
        this.deviceKey = deviceKey;
    }

    public String getTypeID() {
        return typeID;
    }

    public void setTypeID(String typeID) {
        this.typeID = typeID;
    }

    public String getEKey() {
        return eKey;
    }

    public void setEKey(String eKey) {
        this.eKey = eKey;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getVolpAccount() {
        return volpAccount;
    }

    public void setVolpAccount(String volpAccount) {
        this.volpAccount = volpAccount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIotyunServer() {
        return iotyunServer;
    }

    public void setIotyunServer(String iotyunServer) {
        this.iotyunServer = iotyunServer;
    }

    public String getTcpServer() {
        return tcpServer;
    }

    public void setTcpServer(String tcpServer) {
        this.tcpServer = tcpServer;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getOfflinePasswordVersion() {
        return offlinePasswordVersion;
    }

    public void setOfflinePasswordVersion(String offlinePasswordVersion) {
        this.offlinePasswordVersion = offlinePasswordVersion;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDevType() {
        return devType;
    }

    public void setDevType(String devType) {
        this.devType = devType;
    }

    public String getNetworking() {
        return networking;
    }

    public void setNetworking(String networking) {
        this.networking = networking;
    }

    public String getCdID() {
        return cdID;
    }

    public void setCdID(String cdID) {
        this.cdID = cdID;
    }

    public String getCommunityID() {
        return communityID;
    }

    public void setCommunityID(String communityID) {
        this.communityID = communityID;
    }

    public String getCdeviceName() {
        return cdeviceName;
    }

    public void setCdeviceName(String cdeviceName) {
        this.cdeviceName = cdeviceName;
    }

    public String getAssigned() {
        return assigned;
    }

    public void setAssigned(String assigned) {
        this.assigned = assigned;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    @SerializedName("user_device")
    @Expose
    private String userDevice;
    @SerializedName("use_as")
    @Expose
    private String useAs;

    public static LibDevModel getLibDev(DeviceObject dev) {
        LibDevModel device = new LibDevModel();
      /*  if (dev.getDeviceSno().contains("V")) {
            device.devSn = dev.getDeviceSno().replace("V", "");
        } else {
            device.devSn = dev.getDeviceSno();
        }*/
        String snStr = dev.getDeviceSno();
        snStr = snStr.replaceAll("[^\\d.]", "");
        device.devSn = snStr;
        device.devMac = dev.getDeviceMAC();
        device.devType = Integer.valueOf(dev.getDevType());
        device.eKey = dev.getEKey();

        device.privilege = 1;
        device.openType = 2;
//        device.useCount = Integer.parseInt(dev.getUseAs());
        device.verified = 3;
        device.cardno = "123";//卡号从服务器获取，此卡号为测试卡号
     /*   device.openType = dev.getOpenType();
        device.privilege = dev.getPrivilege();
        device.startDate = dev.getStartDate();
        device.useCount = dev.getUseCount();
        device.verified = dev.getVerified();
        device.cardno = "123";*/
        return device;
    }

    public String getRelayConnected() {
        return relayConnected;
    }

    public void setRelayConnected(String relayConnected) {
        this.relayConnected = relayConnected;
    }

    public String getUserDevice() {
        return userDevice;
    }

    public void setUserDevice(String userDevice) {
        this.userDevice = userDevice;
    }

    public String getUseAs() {
        return useAs;
    }

    public void setUseAs(String useAs) {
        this.useAs = useAs;
    }


    @SerializedName("sync_users")
    @Expose
    private String syncUsers;
    @SerializedName("admin_device")
    @Expose
    private Integer adminDevice;


    public Integer getAdminDevice() {
        return adminDevice;
    }

    public void setAdminDevice(Integer adminDevice) {
        this.adminDevice = adminDevice;
    }

    public String getSyncUsers() {
        return syncUsers;
    }

    public void setSyncUsers(String syncUsers) {
        this.syncUsers = syncUsers;
    }
}
