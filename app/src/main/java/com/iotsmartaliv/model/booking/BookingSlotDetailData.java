package com.iotsmartaliv.model.booking;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

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
    private List<String> roomImage;
    @SerializedName("room_name")
    @Expose
    private String roomName;
    @SerializedName("cancellation_policy")
    @Expose
    private String cancellationPolicy;
    @SerializedName("rescheduling_policy")
    @Expose
    private String reschedulingPolicy;
    @SerializedName("rescheduled")
    @Expose
    private String rescheduled;
    @SerializedName("is_refunded")
    @Expose
    private String isRefunded;
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
    @SerializedName("refunded_amount")
    @Expose
    private String refundedAmount;
    @SerializedName("cancellation_charge")
    @Expose
    private String cancellationCharge;
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
    @SerializedName("payment_type")
    @Expose
    private String paymentType;
    @SerializedName("reschedule_limit")
    @Expose
    private String rescheduleLimit;
    @SerializedName("community_name")
    @Expose
    private String communityName;
    @SerializedName("penalty_for")
    @Expose
    private String penaltyFor;
    @SerializedName("deposit_refunded")
    @Expose
    private String depositRefunded;
    @SerializedName("penalty_amount")
    @Expose
    private String penaltyAmount;

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

    public List<String> getRoomImage() {
        return roomImage;
    }

    public void setRoomImage(List<String> roomImage) {
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

    public String getRescheduled() {
        return rescheduled;
    }

    public void setRescheduled(String rescheduled) {
        this.rescheduled = rescheduled;
    }

    public String getIsRefunded() {
        return isRefunded;
    }

    public void setIsRefunded(String isRefunded) {
        this.isRefunded = isRefunded;
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

    public String getRefundedAmount() {
        return refundedAmount;
    }

    public void setRefundedAmount(String refundedAmount) {
        this.refundedAmount = refundedAmount;
    }

    public String getCancellationCharge() {
        return cancellationCharge;
    }

    public void setCancellationCharge(String cancellationCharge) {
        this.cancellationCharge = cancellationCharge;
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

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getRescheduleLimit() {
        return rescheduleLimit;
    }

    public void setRescheduleLimit(String rescheduleLimit) {
        this.rescheduleLimit = rescheduleLimit;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getPenaltyFor() {
        return penaltyFor;
    }

    public void setPenaltyFor(String penaltyFor) {
        this.penaltyFor = penaltyFor;
    }

    public String getDepositRefunded() {
        return depositRefunded;
    }

    public void setDepositRefunded(String depositRefunded) {
        this.depositRefunded = depositRefunded;
    }

    public String getPenaltyAmount() {
        return penaltyAmount;
    }

    public void setPenaltyAmount(String penaltyAmount) {
        this.penaltyAmount = penaltyAmount;
    }

    // Method to retrieve the 'fees' value from paymentType JSON string
    public int getFeesType() {
        try {
            JSONObject jsonObject = new JSONObject(paymentType);
            return jsonObject.getInt("fees");
        } catch (JSONException e) {
            e.printStackTrace();
            return -1; // Return -1 if parsing fails
        }
    }
    public int getDepositType() {
        try {
            JSONObject jsonObject = new JSONObject(paymentType);
            return jsonObject.getInt("deposit");
        } catch (JSONException e) {
            e.printStackTrace();
            return -1; // Return -1 if parsing fails
        }
    }
}
