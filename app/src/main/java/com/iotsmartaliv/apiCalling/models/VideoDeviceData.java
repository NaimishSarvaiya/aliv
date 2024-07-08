package com.iotsmartaliv.apiCalling.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class VideoDeviceData implements Serializable {
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
    @SerializedName("ufacePersonTaskId")
    @Expose
    private Object ufacePersonTaskId;
    @SerializedName("ufaceFacialTaskId")
    @Expose
    private Object ufaceFacialTaskId;
    @SerializedName("access_key")
    @Expose
    private String accessKey;
    @SerializedName("permissions")
    @Expose
    private Object permissions;
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
    @SerializedName("sensor_address")
    @Expose
    private String sensorAddress;
    @SerializedName("device_ip")
    @Expose
    private Object deviceIp;
    @SerializedName("camera_rtsp_url")
    @Expose
    private Object cameraRtspUrl;
    @SerializedName("camera_hls_url")
    @Expose
    private Object cameraHlsUrl;
    @SerializedName("device_port")
    @Expose
    private Object devicePort;
    @SerializedName("device_placement")
    @Expose
    private String devicePlacement;
    @SerializedName("fa_ip")
    @Expose
    private Object faIp;
    @SerializedName("fa_pass")
    @Expose
    private Object faPass;
    @SerializedName("fa_distance")
    @Expose
    private String faDistance;
    @SerializedName("fa_threshold")
    @Expose
    private String faThreshold;
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
    private Object function;
    @SerializedName("volp_account")
    @Expose
    private Object volpAccount;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("iotyun_server")
    @Expose
    private Object iotyunServer;
    @SerializedName("tcp_server")
    @Expose
    private Object tcpServer;
    @SerializedName("remarks")
    @Expose
    private String remarks;
    @SerializedName("offline_password_version")
    @Expose
    private Object offlinePasswordVersion;
    @SerializedName("relay_connected")
    @Expose
    private Object relayConnected;
    @SerializedName("locked_status")
    @Expose
    private String lockedStatus;
    @SerializedName("wifiMsg")
    @Expose
    private Object wifiMsg;
    @SerializedName("sync_users")
    @Expose
    private String syncUsers;
    @SerializedName("smartcity_ID")
    @Expose
    private String smartcityID;
    @SerializedName("online_status")
    @Expose
    private String onlineStatus;
    @SerializedName("device_token")
    @Expose
    private Object deviceToken;
    @SerializedName("basic_setting")
    @Expose
    private Object basicSetting;
    @SerializedName("advanced_setting")
    @Expose
    private Object advancedSetting;
    @SerializedName("card_config_setting")
    @Expose
    private Object cardConfigSetting;
    @SerializedName("super_password")
    @Expose
    private Object superPassword;
    @SerializedName("door_count")
    @Expose
    private String doorCount;
    @SerializedName("sensor_param")
    @Expose
    private Object sensorParam;
    @SerializedName("device_set_as")
    @Expose
    private String deviceSetAs;
    @SerializedName("device_visibility")
    @Expose
    private String deviceVisibility;
    @SerializedName("power_status")
    @Expose
    private String powerStatus;
    @SerializedName("power_notificaition_time")
    @Expose
    private Object powerNotificaitionTime;
    @SerializedName("assigned_access_controller")
    @Expose
    private String assignedAccessController;
    @SerializedName("door_direction")
    @Expose
    private Object doorDirection;
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
    @SerializedName("smartcity_device")
    @Expose
    private String smartcityDevice;
    @SerializedName("user_device")
    @Expose
    private String userDevice;
    @SerializedName("temperature_device")
    @Expose
    private String temperatureDevice;
    @SerializedName("card_device")
    @Expose
    private String cardDevice;
    @SerializedName("card_type")
    @Expose
    private String cardType;
    @SerializedName("cd_ID")
    @Expose
    private String cdID;
    @SerializedName("community_ID")
    @Expose
    private String communityID;
    @SerializedName("dev_smar_build_ID")
    @Expose
    private Object devSmarBuildID;
    @SerializedName("cdevice_name")
    @Expose
    private String cdeviceName;
    @SerializedName("use_as")
    @Expose
    private String useAs;
    @SerializedName("qr_code_facial")
    @Expose
    private Object qrCodeFacial;
    @SerializedName("door_names")
    @Expose
    private Object doorNames;
    @SerializedName("automation_status")
    @Expose
    private Object automationStatus;
    @SerializedName("assigned")
    @Expose
    private String assigned;
    @SerializedName("relay_data")
    @Expose
    private List<IntercomRelayData> relayData;
    @SerializedName("isAccessTimeEnabled")
    @Expose
    private String isAccessTimeEnabled;
    @SerializedName("access_starttime")
    @Expose
    private String accessStarttime;
    @SerializedName("access_endtime")
    @Expose
    private String accessEndtime;

    public VideoDeviceData(String udID, String userID, String deviceID, String groupIds, Object ufacePersonTaskId, Object ufaceFacialTaskId, String accessKey, Object permissions, String createdAt, String updatedAt, String deviceSno, String deviceName, String deviceMAC, String sensorAddress, Object deviceIp, Object cameraRtspUrl, Object cameraHlsUrl, Object devicePort, String devicePlacement, Object faIp, Object faPass, String faDistance, String faThreshold, String deviceKey, String typeID, String eKey, Object function, Object volpAccount, String status, Object iotyunServer, Object tcpServer, String remarks, Object offlinePasswordVersion, Object relayConnected, String lockedStatus, Object wifiMsg, String syncUsers, String smartcityID, String onlineStatus, Object deviceToken, Object basicSetting, Object advancedSetting, Object cardConfigSetting, Object superPassword, String doorCount, Object sensorParam, String deviceSetAs, String deviceVisibility, String powerStatus, Object powerNotificaitionTime, String assignedAccessController, Object doorDirection, String deleted, String deviceModel, String deviceType, String devType, String networking, String smartcityDevice, String userDevice, String temperatureDevice, String cardDevice, String cardType, String cdID, String communityID, Object devSmarBuildID, String cdeviceName, String useAs, Object qrCodeFacial, Object doorNames, Object automationStatus, String assigned, List<IntercomRelayData> relayData, String isAccessTimeEnabled, String accessStarttime, String accessEndtime) {
        this.udID = udID;
        this.userID = userID;
        this.deviceID = deviceID;
        this.groupIds = groupIds;
        this.ufacePersonTaskId = ufacePersonTaskId;
        this.ufaceFacialTaskId = ufaceFacialTaskId;
        this.accessKey = accessKey;
        this.permissions = permissions;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deviceSno = deviceSno;
        this.deviceName = deviceName;
        this.deviceMAC = deviceMAC;
        this.sensorAddress = sensorAddress;
        this.deviceIp = deviceIp;
        this.cameraRtspUrl = cameraRtspUrl;
        this.cameraHlsUrl = cameraHlsUrl;
        this.devicePort = devicePort;
        this.devicePlacement = devicePlacement;
        this.faIp = faIp;
        this.faPass = faPass;
        this.faDistance = faDistance;
        this.faThreshold = faThreshold;
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
        this.relayConnected = relayConnected;
        this.lockedStatus = lockedStatus;
        this.wifiMsg = wifiMsg;
        this.syncUsers = syncUsers;
        this.smartcityID = smartcityID;
        this.onlineStatus = onlineStatus;
        this.deviceToken = deviceToken;
        this.basicSetting = basicSetting;
        this.advancedSetting = advancedSetting;
        this.cardConfigSetting = cardConfigSetting;
        this.superPassword = superPassword;
        this.doorCount = doorCount;
        this.sensorParam = sensorParam;
        this.deviceSetAs = deviceSetAs;
        this.deviceVisibility = deviceVisibility;
        this.powerStatus = powerStatus;
        this.powerNotificaitionTime = powerNotificaitionTime;
        this.assignedAccessController = assignedAccessController;
        this.doorDirection = doorDirection;
        this.deleted = deleted;
        this.deviceModel = deviceModel;
        this.deviceType = deviceType;
        this.devType = devType;
        this.networking = networking;
        this.smartcityDevice = smartcityDevice;
        this.userDevice = userDevice;
        this.temperatureDevice = temperatureDevice;
        this.cardDevice = cardDevice;
        this.cardType = cardType;
        this.cdID = cdID;
        this.communityID = communityID;
        this.devSmarBuildID = devSmarBuildID;
        this.cdeviceName = cdeviceName;
        this.useAs = useAs;
        this.qrCodeFacial = qrCodeFacial;
        this.doorNames = doorNames;
        this.automationStatus = automationStatus;
        this.assigned = assigned;
        this.relayData = relayData;
        this.isAccessTimeEnabled = isAccessTimeEnabled;
        this.accessStarttime = accessStarttime;
        this.accessEndtime = accessEndtime;
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

    public Object getUfacePersonTaskId() {
        return ufacePersonTaskId;
    }

    public void setUfacePersonTaskId(Object ufacePersonTaskId) {
        this.ufacePersonTaskId = ufacePersonTaskId;
    }

    public Object getUfaceFacialTaskId() {
        return ufaceFacialTaskId;
    }

    public void setUfaceFacialTaskId(Object ufaceFacialTaskId) {
        this.ufaceFacialTaskId = ufaceFacialTaskId;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public Object getPermissions() {
        return permissions;
    }

    public void setPermissions(Object permissions) {
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

    public String getSensorAddress() {
        return sensorAddress;
    }

    public void setSensorAddress(String sensorAddress) {
        this.sensorAddress = sensorAddress;
    }

    public Object getDeviceIp() {
        return deviceIp;
    }

    public void setDeviceIp(Object deviceIp) {
        this.deviceIp = deviceIp;
    }

    public Object getCameraRtspUrl() {
        return cameraRtspUrl;
    }

    public void setCameraRtspUrl(Object cameraRtspUrl) {
        this.cameraRtspUrl = cameraRtspUrl;
    }

    public Object getCameraHlsUrl() {
        return cameraHlsUrl;
    }

    public void setCameraHlsUrl(Object cameraHlsUrl) {
        this.cameraHlsUrl = cameraHlsUrl;
    }

    public Object getDevicePort() {
        return devicePort;
    }

    public void setDevicePort(Object devicePort) {
        this.devicePort = devicePort;
    }

    public String getDevicePlacement() {
        return devicePlacement;
    }

    public void setDevicePlacement(String devicePlacement) {
        this.devicePlacement = devicePlacement;
    }

    public Object getFaIp() {
        return faIp;
    }

    public void setFaIp(Object faIp) {
        this.faIp = faIp;
    }

    public Object getFaPass() {
        return faPass;
    }

    public void setFaPass(Object faPass) {
        this.faPass = faPass;
    }

    public String getFaDistance() {
        return faDistance;
    }

    public void setFaDistance(String faDistance) {
        this.faDistance = faDistance;
    }

    public String getFaThreshold() {
        return faThreshold;
    }

    public void setFaThreshold(String faThreshold) {
        this.faThreshold = faThreshold;
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

    public String geteKey() {
        return eKey;
    }

    public void seteKey(String eKey) {
        this.eKey = eKey;
    }

    public Object getFunction() {
        return function;
    }

    public void setFunction(Object function) {
        this.function = function;
    }

    public Object getVolpAccount() {
        return volpAccount;
    }

    public void setVolpAccount(Object volpAccount) {
        this.volpAccount = volpAccount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getIotyunServer() {
        return iotyunServer;
    }

    public void setIotyunServer(Object iotyunServer) {
        this.iotyunServer = iotyunServer;
    }

    public Object getTcpServer() {
        return tcpServer;
    }

    public void setTcpServer(Object tcpServer) {
        this.tcpServer = tcpServer;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Object getOfflinePasswordVersion() {
        return offlinePasswordVersion;
    }

    public void setOfflinePasswordVersion(Object offlinePasswordVersion) {
        this.offlinePasswordVersion = offlinePasswordVersion;
    }

    public Object getRelayConnected() {
        return relayConnected;
    }

    public void setRelayConnected(Object relayConnected) {
        this.relayConnected = relayConnected;
    }

    public String getLockedStatus() {
        return lockedStatus;
    }

    public void setLockedStatus(String lockedStatus) {
        this.lockedStatus = lockedStatus;
    }

    public Object getWifiMsg() {
        return wifiMsg;
    }

    public void setWifiMsg(Object wifiMsg) {
        this.wifiMsg = wifiMsg;
    }

    public String getSyncUsers() {
        return syncUsers;
    }

    public void setSyncUsers(String syncUsers) {
        this.syncUsers = syncUsers;
    }

    public String getSmartcityID() {
        return smartcityID;
    }

    public void setSmartcityID(String smartcityID) {
        this.smartcityID = smartcityID;
    }

    public String getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(String onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public Object getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(Object deviceToken) {
        this.deviceToken = deviceToken;
    }

    public Object getBasicSetting() {
        return basicSetting;
    }

    public void setBasicSetting(Object basicSetting) {
        this.basicSetting = basicSetting;
    }

    public Object getAdvancedSetting() {
        return advancedSetting;
    }

    public void setAdvancedSetting(Object advancedSetting) {
        this.advancedSetting = advancedSetting;
    }

    public Object getCardConfigSetting() {
        return cardConfigSetting;
    }

    public void setCardConfigSetting(Object cardConfigSetting) {
        this.cardConfigSetting = cardConfigSetting;
    }

    public Object getSuperPassword() {
        return superPassword;
    }

    public void setSuperPassword(Object superPassword) {
        this.superPassword = superPassword;
    }

    public String getDoorCount() {
        return doorCount;
    }

    public void setDoorCount(String doorCount) {
        this.doorCount = doorCount;
    }

    public Object getSensorParam() {
        return sensorParam;
    }

    public void setSensorParam(Object sensorParam) {
        this.sensorParam = sensorParam;
    }

    public String getDeviceSetAs() {
        return deviceSetAs;
    }

    public void setDeviceSetAs(String deviceSetAs) {
        this.deviceSetAs = deviceSetAs;
    }

    public String getDeviceVisibility() {
        return deviceVisibility;
    }

    public void setDeviceVisibility(String deviceVisibility) {
        this.deviceVisibility = deviceVisibility;
    }

    public String getPowerStatus() {
        return powerStatus;
    }

    public void setPowerStatus(String powerStatus) {
        this.powerStatus = powerStatus;
    }

    public Object getPowerNotificaitionTime() {
        return powerNotificaitionTime;
    }

    public void setPowerNotificaitionTime(Object powerNotificaitionTime) {
        this.powerNotificaitionTime = powerNotificaitionTime;
    }

    public String getAssignedAccessController() {
        return assignedAccessController;
    }

    public void setAssignedAccessController(String assignedAccessController) {
        this.assignedAccessController = assignedAccessController;
    }

    public Object getDoorDirection() {
        return doorDirection;
    }

    public void setDoorDirection(Object doorDirection) {
        this.doorDirection = doorDirection;
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

    public String getSmartcityDevice() {
        return smartcityDevice;
    }

    public void setSmartcityDevice(String smartcityDevice) {
        this.smartcityDevice = smartcityDevice;
    }

    public String getUserDevice() {
        return userDevice;
    }

    public void setUserDevice(String userDevice) {
        this.userDevice = userDevice;
    }

    public String getTemperatureDevice() {
        return temperatureDevice;
    }

    public void setTemperatureDevice(String temperatureDevice) {
        this.temperatureDevice = temperatureDevice;
    }

    public String getCardDevice() {
        return cardDevice;
    }

    public void setCardDevice(String cardDevice) {
        this.cardDevice = cardDevice;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
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

    public Object getDevSmarBuildID() {
        return devSmarBuildID;
    }

    public void setDevSmarBuildID(Object devSmarBuildID) {
        this.devSmarBuildID = devSmarBuildID;
    }

    public String getCdeviceName() {
        return cdeviceName;
    }

    public void setCdeviceName(String cdeviceName) {
        this.cdeviceName = cdeviceName;
    }

    public String getUseAs() {
        return useAs;
    }

    public void setUseAs(String useAs) {
        this.useAs = useAs;
    }

    public Object getQrCodeFacial() {
        return qrCodeFacial;
    }

    public void setQrCodeFacial(Object qrCodeFacial) {
        this.qrCodeFacial = qrCodeFacial;
    }

    public Object getDoorNames() {
        return doorNames;
    }

    public void setDoorNames(Object doorNames) {
        this.doorNames = doorNames;
    }

    public Object getAutomationStatus() {
        return automationStatus;
    }

    public void setAutomationStatus(Object automationStatus) {
        this.automationStatus = automationStatus;
    }

    public String getAssigned() {
        return assigned;
    }

    public void setAssigned(String assigned) {
        this.assigned = assigned;
    }

    public List<IntercomRelayData> getRelayData() {
        return relayData;
    }

    public void setRelayData(List<IntercomRelayData> relayData) {
        this.relayData = relayData;
    }

    public String getIsAccessTimeEnabled() {
        return isAccessTimeEnabled;
    }

    public void setIsAccessTimeEnabled(String isAccessTimeEnabled) {
        this.isAccessTimeEnabled = isAccessTimeEnabled;
    }

    public String getAccessStarttime() {
        return accessStarttime;
    }

    public void setAccessStarttime(String accessStarttime) {
        this.accessStarttime = accessStarttime;
    }

    public String getAccessEndtime() {
        return accessEndtime;
    }

    public void setAccessEndtime(String accessEndtime) {
        this.accessEndtime = accessEndtime;
    }

}
