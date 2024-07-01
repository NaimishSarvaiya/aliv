package com.iotsmartaliv.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class AutomationRoomsData implements Serializable {
    @SerializedName("automation_ID")
    @Expose
    private String automationID;
    @SerializedName("aroom_ID")
    @Expose
    private String aroomID;
    @SerializedName("lights")
    @Expose
    private String lights;
    @SerializedName("airconditioner")
    @Expose
    private String airconditioner;
    @SerializedName("deleted")
    @Expose
    private String deleted;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("room_ID")
    @Expose
    private String roomID;
    @SerializedName("room_name")
    @Expose
    private String roomName;
    @SerializedName("room_image")
    @Expose
    private String roomImage;
    @SerializedName("automation")
    @Expose
    private String automation;
    @SerializedName("booking")
    @Expose
    private String booking;
    @SerializedName("community_ID")
    @Expose
    private String communityID;
    @SerializedName("auto_device_ID")
    @Expose
    private String autoDeviceID;
    @SerializedName("device_ID")
    @Expose
    private String deviceID;
    @SerializedName("assigned")
    @Expose
    private String assigned;
    @SerializedName("status_1")
    @Expose
    private String status1;
    @SerializedName("status_2")
    @Expose
    private String status2;
    @SerializedName("status_automation")
    @Expose
    private String statusAutomation;
    @SerializedName("assignment_name")
    @Expose
    private String assignmentName;
    @SerializedName("schedule")
    @Expose
    private List<Schedule> schedule;
    @SerializedName("role_permission")
    @Expose
    private String rolePermission;
    @SerializedName("userType")
    @Expose
    private String userType;
    public String getAutomationID() {
        return automationID;
    }

    public void setAutomationID(String automationID) {
        this.automationID = automationID;
    }

    public String getAroomID() {
        return aroomID;
    }

    public void setAroomID(String aroomID) {
        this.aroomID = aroomID;
    }

    public String getLights() {
        return lights;
    }

    public void setLights(String lights) {
        this.lights = lights;
    }

    public String getAirconditioner() {
        return airconditioner;
    }

    public void setAirconditioner(String airconditioner) {
        this.airconditioner = airconditioner;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
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

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomImage() {
        return roomImage;
    }

    public void setRoomImage(String roomImage) {
        this.roomImage = roomImage;
    }

    public String getAutomation() {
        return automation;
    }

    public void setAutomation(String automation) {
        this.automation = automation;
    }

    public String getCommunityID() {
        return communityID;
    }

    public void setCommunityID(String communityID) {
        this.communityID = communityID;
    }

    public List<Schedule> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<Schedule> schedule) {
        this.schedule = schedule;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getBooking() {
        return booking;
    }

    public void setBooking(String booking) {
        this.booking = booking;
    }

    public String getAutoDeviceID() {
        return autoDeviceID;
    }

    public void setAutoDeviceID(String autoDeviceID) {
        this.autoDeviceID = autoDeviceID;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getAssigned() {
        return assigned;
    }

    public void setAssigned(String assigned) {
        this.assigned = assigned;
    }

    public String getStatus1() {
        return status1;
    }

    public void setStatus1(String status1) {
        this.status1 = status1;
    }

    public String getStatus2() {
        return status2;
    }

    public void setStatus2(String status2) {
        this.status2 = status2;
    }

    public String getStatusAutomation() {
        return statusAutomation;
    }

    public void setStatusAutomation(String statusAutomation) {
        this.statusAutomation = statusAutomation;
    }

    public String getAssignmentName() {
        return assignmentName;
    }

    public void setAssignmentName(String assignmentName) {
        this.assignmentName = assignmentName;
    }

    public String getRolePermission() {
        return rolePermission;
    }

    public void setRolePermission(String rolePermission) {
        this.rolePermission = rolePermission;
    }
}

