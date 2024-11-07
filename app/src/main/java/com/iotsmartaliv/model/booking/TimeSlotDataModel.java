package com.iotsmartaliv.model.booking;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TimeSlotDataModel {
    @SerializedName("slots_ID")
    @Expose
    private String slotsID;
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
    @SerializedName("is_booked")
    @Expose
    private Boolean isBooked;
    @SerializedName("booked_start_date")
    @Expose
    private String bookedStartDate;
    @SerializedName("booked_end_date")
    @Expose
    private String bookedEndDate;

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

    public Boolean getIsBooked() {
        return isBooked;
    }

    public void setIsBooked(Boolean isBooked) {
        this.isBooked = isBooked;
    }

    public String getBookedStartDate() {
        return bookedStartDate;
    }

    public void setBookedStartDate(String bookedStartDate) {
        this.bookedStartDate = bookedStartDate;
    }

    public String getBookedEndDate() {
        return bookedEndDate;
    }

    public void setBookedEndDate(String bookedEndDate) {
        this.bookedEndDate = bookedEndDate;
    }

}
