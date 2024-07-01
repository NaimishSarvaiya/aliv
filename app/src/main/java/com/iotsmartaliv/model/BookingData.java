package com.iotsmartaliv.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BookingData {
    @SerializedName("booking_ID")
    @Expose
    private String bookingID;
    @SerializedName("appuser_ID")
    @Expose
    private String appuserID;
    @SerializedName("booking_start_date")
    @Expose
    private String bookingStartDate;
    @SerializedName("booking_end_date")
    @Expose
    private String bookingEndDate;
    @SerializedName("weekend_excluded")
    @Expose
    private String weekendExcluded;
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
    @SerializedName("booking_number")
    @Expose
    private String bookingNumber;

    public String getBookingID() {
        return bookingID;
    }

    public void setBookingID(String bookingID) {
        this.bookingID = bookingID;
    }

    public String getAppuserID() {
        return appuserID;
    }

    public void setAppuserID(String appuserID) {
        this.appuserID = appuserID;
    }

    public String getBookingStartDate() {
        return bookingStartDate;
    }

    public void setBookingStartDate(String bookingStartDate) {
        this.bookingStartDate = bookingStartDate;
    }

    public String getBookingEndDate() {
        return bookingEndDate;
    }

    public void setBookingEndDate(String bookingEndDate) {
        this.bookingEndDate = bookingEndDate;
    }

    public String getWeekendExcluded() {
        return weekendExcluded;
    }

    public void setWeekendExcluded(String weekendExcluded) {
        this.weekendExcluded = weekendExcluded;
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

    public String getBookingNumber() {
        return bookingNumber;
    }

    public void setBookingNumber(String bookingNumber) {
        this.bookingNumber = bookingNumber;
    }

}
