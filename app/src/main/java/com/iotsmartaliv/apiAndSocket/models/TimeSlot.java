package com.iotsmartaliv.apiAndSocket.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * This class is used as .
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 21/8/19 :August : 2019 on 20 : 37.
 */
public class TimeSlot implements Serializable {

    @SerializedName("slots_ID")
    @Expose
    private String slotsID;

    @SerializedName("timeslot_ID")
    @Expose
    private String timeslot_ID;
    @SerializedName("room_ID")
    @Expose
    private String roomID;
    @SerializedName("start_time")
    @Expose
    private String startTime;
    @SerializedName("end_time")
    @Expose
    private String endTime;
    @SerializedName("deleted")
    @Expose
    private String deleted;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    private boolean isSelected;
    @SerializedName("bv_ID")
    @Expose
    private String bvID;
    @SerializedName("booking_ID")
    @Expose
    private String bookingID;
    @SerializedName("slot_ID")
    @Expose
    private String slotID;
    @SerializedName("purpose")
    @Expose
    private String purpose;
    @SerializedName("b_start_date")
    @Expose
    private String bStartDate;
    @SerializedName("b_end_date")
    @Expose
    private String bEndDate;
    @SerializedName("weekend_excluded")
    @Expose
    private String weekendExcluded;

    public TimeSlot(String startTime, String endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getTimeslot_ID() {
        return timeslot_ID;
    }

    public void setTimeslot_ID(String timeslot_ID) {
        this.timeslot_ID = timeslot_ID;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getSlotsID() {
        return slotsID;
    }

    public void setSlotsID(String slotsID) {
        this.slotsID = slotsID;
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
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

    public String getBvID() {
        return bvID;
    }

    public void setBvID(String bvID) {
        this.bvID = bvID;
    }

    public String getBookingID() {
        return bookingID;
    }

    public void setBookingID(String bookingID) {
        this.bookingID = bookingID;
    }


    public String getSlotID() {
        return slotID;
    }

    public void setSlotID(String slotID) {
        this.slotID = slotID;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getBStartDate() {
        return bStartDate;
    }

    public void setBStartDate(String bStartDate) {
        this.bStartDate = bStartDate;
    }

    public String getBEndDate() {
        return bEndDate;
    }

    public void setBEndDate(String bEndDate) {
        this.bEndDate = bEndDate;
    }

    public String getWeekendExcluded() {
        return weekendExcluded;
    }

    public void setWeekendExcluded(String weekendExcluded) {
        this.weekendExcluded = weekendExcluded;
    }

}