package com.iotsmartaliv.apiAndSocket.models;

/**
 * This class is used as
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 28 Apr,2021
 */

import java.io.Serializable;
import java.util.ArrayList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class Broadcast implements Serializable {
    @SerializedName("broadcast_ID")
    @Expose
    private String broadcastID;
    @SerializedName("smartcity_broadcastID")
    @Expose
    private Object smartcityBroadcastID;
    @SerializedName("broadcast_type")
    @Expose
    private String broadcastType;
    @SerializedName("broadcast_title")
    @Expose
    private String broadcastTitle;
    @SerializedName("broadcast_details")
    @Expose
    private String broadcastDetails;
    @SerializedName("broadcast_attach")
    @Expose
    private ArrayList<String> broadcastAttach = null;
    @SerializedName("broadcast_folder")
    @Expose
    private Object broadcastFolder;
    @SerializedName("event_location")
    @Expose
    private Object eventLocation;
    @SerializedName("event_full_address")
    @Expose
    private Object eventFullAddress;
    @SerializedName("event_organizer")
    @Expose
    private Object eventOrganizer;
    @SerializedName("event_start_date")
    @Expose
    private Object eventStartDate;
    @SerializedName("event_end_date")
    @Expose
    private Object eventEndDate;
    @SerializedName("devices")
    @Expose
    private Object devices;
    @SerializedName("play_interval")
    @Expose
    private String playInterval;
    @SerializedName("video_attach")
    @Expose
    private Object videoAttach;
    @SerializedName("remark")
    @Expose
    private String remark;
    @SerializedName("start_date")
    @Expose
    private Object startDate;
    @SerializedName("end_date")
    @Expose
    private Object endDate;
    @SerializedName("published")
    @Expose
    private String published;
    @SerializedName("last_published_at")
    @Expose
    private String lastPublishedAt;
    @SerializedName("community_ID")
    @Expose
    private String communityID;
    @SerializedName("deleted")
    @Expose
    private String deleted;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("bu_ID")
    @Expose
    private String buID;
    @SerializedName("appuser_ID")
    @Expose
    private String appuserID;
    @SerializedName("read_status")
    @Expose
    private String readStatus;
    @SerializedName("bu_created_at")
    @Expose
    private String buCreatedAt;
    @SerializedName("bu_updated_at")
    @Expose
    private String buUpdatedAt;
    public String getBroadcastID() {
        return broadcastID;
    }
    public void setBroadcastID(String broadcastID) {
        this.broadcastID = broadcastID;
    }
    public Object getSmartcityBroadcastID() {
        return smartcityBroadcastID;
    }
    public void setSmartcityBroadcastID(Object smartcityBroadcastID) {
        this.smartcityBroadcastID = smartcityBroadcastID;
    }
    public String getBroadcastType() {
        return broadcastType;
    }
    public void setBroadcastType(String broadcastType) {
        this.broadcastType = broadcastType;
    }
    public String getBroadcastTitle() {
        return broadcastTitle;
    }
    public void setBroadcastTitle(String broadcastTitle) {
        this.broadcastTitle = broadcastTitle;
    }
    public String getBroadcastDetails() {
        return broadcastDetails;
    }
    public void setBroadcastDetails(String broadcastDetails) {
        this.broadcastDetails = broadcastDetails;
    }
    public ArrayList<String> getBroadcastAttach() {
        return broadcastAttach;
    }
    public void setBroadcastAttach(ArrayList<String> broadcastAttach) {
        this.broadcastAttach = broadcastAttach;
    }
    public Object getBroadcastFolder() {
        return broadcastFolder;
    }
    public void setBroadcastFolder(Object broadcastFolder) {
        this.broadcastFolder = broadcastFolder;
    }
    public Object getEventLocation() {
        return eventLocation;
    }
    public void setEventLocation(Object eventLocation) {
        this.eventLocation = eventLocation;
    }
    public Object getEventFullAddress() {
        return eventFullAddress;
    }
    public void setEventFullAddress(Object eventFullAddress) {
        this.eventFullAddress = eventFullAddress;
    }
    public Object getEventOrganizer() {
        return eventOrganizer;
    }
    public void setEventOrganizer(Object eventOrganizer) {
        this.eventOrganizer = eventOrganizer;
    }
    public Object getEventStartDate() {
        return eventStartDate;
    }
    public void setEventStartDate(Object eventStartDate) {
        this.eventStartDate = eventStartDate;
    }
    public Object getEventEndDate() {
        return eventEndDate;
    }
    public void setEventEndDate(Object eventEndDate) {
        this.eventEndDate = eventEndDate;
    }
    public Object getDevices() {
        return devices;
    }
    public void setDevices(Object devices) {
        this.devices = devices;
    }
    public String getPlayInterval() {
        return playInterval;
    }
    public void setPlayInterval(String playInterval) {
        this.playInterval = playInterval;
    }
    public Object getVideoAttach() {
        return videoAttach;
    }
    public void setVideoAttach(Object videoAttach) {
        this.videoAttach = videoAttach;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public Object getStartDate() {
        return startDate;
    }
    public void setStartDate(Object startDate) {
        this.startDate = startDate;
    }
    public Object getEndDate() {
        return endDate;
    }
    public void setEndDate(Object endDate) {
        this.endDate = endDate;
    }
    public String getPublished() {
        return published;
    }
    public void setPublished(String published) {
        this.published = published;
    }
    public String getLastPublishedAt() {
        return lastPublishedAt;
    }
    public void setLastPublishedAt(String lastPublishedAt) {
        this.lastPublishedAt = lastPublishedAt;
    }
    public String getCommunityID() {
        return communityID;
    }
    public void setCommunityID(String communityID) {
        this.communityID = communityID;
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
    public String getBuID() {
        return buID;
    }
    public void setBuID(String buID) {
        this.buID = buID;
    }
    public String getAppuserID() {
        return appuserID;
    }
    public void setAppuserID(String appuserID) {
        this.appuserID = appuserID;
    }
    public String getReadStatus() {
        return readStatus;
    }
    public void setReadStatus(String readStatus) {
        this.readStatus = readStatus;
    }
    public String getBuCreatedAt() {
        return buCreatedAt;
    }
    public void setBuCreatedAt(String buCreatedAt) {
        this.buCreatedAt = buCreatedAt;
    }
    public String getBuUpdatedAt() {
        return buUpdatedAt;
    }
    public void setBuUpdatedAt(String buUpdatedAt) {
        this.buUpdatedAt = buUpdatedAt;
    }
}