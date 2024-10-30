package com.iotsmartaliv.model.booking;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BookingSlotDetailData implements Serializable {
    @SerializedName("room_ID")
    @Expose
    private String roomID;
    @SerializedName("room_type")
    @Expose
    private String roomType;
    @SerializedName("room_book_limit")
    @Expose
    private String roomBookLimit;
    @SerializedName("room_image")
    @Expose
    private String roomImage;
    @SerializedName("room_name")
    @Expose
    private String roomName;
    @SerializedName("cancellation_policy")
    @Expose
    private String cancellationPolicy;
    @SerializedName("rescheduling_policy")
    @Expose
    private String reschedulingPolicy;
    @SerializedName("booking_ID")
    @Expose
    private String bookingID;
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("end_date")
    @Expose
    private String endDate;
    @SerializedName("start_time")
    @Expose
    private String startTime;
    @SerializedName("end_time")
    @Expose
    private String endTime;
    @SerializedName("booking_status")
    @Expose
    private String bookingStatus;
    @SerializedName("payment_ID")
    @Expose
    private String paymentID;
    @SerializedName("deposit_payment_ID")
    @Expose
    private String depositPaymentID;
    @SerializedName("deposit")
    @Expose
    private String deposit;
    @SerializedName("fees")
    @Expose
    private String fees;
    @SerializedName("deposit_status")
    @Expose
    private String depositStatus;
    @SerializedName("slot_ID")
    @Expose
    private String slotID;
    @SerializedName("community_name")
    @Expose
    private String communityName;

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getRoomBookLimit() {
        return roomBookLimit;
    }

    public void setRoomBookLimit(String roomBookLimit) {
        this.roomBookLimit = roomBookLimit;
    }

    public String getRoomImage() {
        return roomImage;
    }

    public void setRoomImage(String roomImage) {
        this.roomImage = roomImage;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getCancellationPolicy() {
        return cancellationPolicy;
    }

    public void setCancellationPolicy(String cancellationPolicy) {
        this.cancellationPolicy = cancellationPolicy;
    }

    public String getReschedulingPolicy() {
        return reschedulingPolicy;
    }

    public void setReschedulingPolicy(String reschedulingPolicy) {
        this.reschedulingPolicy = reschedulingPolicy;
    }

    public String getBookingID() {
        return bookingID;
    }

    public void setBookingID(String bookingID) {
        this.bookingID = bookingID;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
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

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public String getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(String paymentID) {
        this.paymentID = paymentID;
    }

    public String getDepositPaymentID() {
        return depositPaymentID;
    }

    public void setDepositPaymentID(String depositPaymentID) {
        this.depositPaymentID = depositPaymentID;
    }

    public String getDeposit() {
        return deposit;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }

    public String getFees() {
        return fees;
    }

    public void setFees(String fees) {
        this.fees = fees;
    }

    public String getDepositStatus() {
        return depositStatus;
    }

    public void setDepositStatus(String depositStatus) {
        this.depositStatus = depositStatus;
    }

    public String getSlotID() {
        return slotID;
    }

    public void setSlotID(String slotID) {
        this.slotID = slotID;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }
}
