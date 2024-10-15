package com.iotsmartaliv.model.booking;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class BookingDetailsData implements Serializable {
    @SerializedName("room_ID")
    @Expose
    private String roomID;
    @SerializedName("community_name")
    @Expose
    private String communityName;
    @SerializedName("room_name")
    @Expose
    private String roomName;
    @SerializedName("room_image")
    @Expose
    private String roomImage;
    @SerializedName("room_type")
    @Expose
    private String roomType;
    @SerializedName("price_name")
    @Expose
    private String priceName;
    @SerializedName("term_conditions")
    @Expose
    private String termConditions;
    @SerializedName("cancellation_policy")
    @Expose
    private String cancellationPolicy;
    @SerializedName("penalty_amount")
    @Expose
    private String penaltyAmount;
    @SerializedName("penalty_amount_timeperiod")
    @Expose
    private List<PenaltyAmountTimeperiodData> penaltyAmountTimeperiod;
    @SerializedName("room_base_price")
    @Expose
    private Integer roomBasePrice;
    @SerializedName("advance_deposit")
    @Expose
    private Integer advanceDeposit;
    @SerializedName("room_book_limit")
    @Expose
    private String roomBookLimit;
    @SerializedName("rescheduling_policy")
    @Expose
    private String reschedulingPolicy;
    @SerializedName("no_of_days")
    @Expose
    private Integer noOfDays;
    @SerializedName("fees")
    @Expose
    private Integer fees;

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
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

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getPriceName() {
        return priceName;
    }

    public void setPriceName(String priceName) {
        this.priceName = priceName;
    }

    public String getTermConditions() {
        return termConditions;
    }

    public void setTermConditions(String termConditions) {
        this.termConditions = termConditions;
    }

    public String getCancellationPolicy() {
        return cancellationPolicy;
    }

    public void setCancellationPolicy(String cancellationPolicy) {
        this.cancellationPolicy = cancellationPolicy;
    }

    public String getPenaltyAmount() {
        return penaltyAmount;
    }

    public void setPenaltyAmount(String penaltyAmount) {
        this.penaltyAmount = penaltyAmount;
    }

    public List<PenaltyAmountTimeperiodData> getPenaltyAmountTimeperiod() {
        return penaltyAmountTimeperiod;
    }

    public void setPenaltyAmountTimeperiod(List<PenaltyAmountTimeperiodData> penaltyAmountTimeperiod) {
        this.penaltyAmountTimeperiod = penaltyAmountTimeperiod;
    }

    public Integer getRoomBasePrice() {
        return roomBasePrice;
    }

    public void setRoomBasePrice(Integer roomBasePrice) {
        this.roomBasePrice = roomBasePrice;
    }

    public Integer getAdvanceDeposit() {
        return advanceDeposit;
    }

    public void setAdvanceDeposit(Integer advanceDeposit) {
        this.advanceDeposit = advanceDeposit;
    }

    public String getRoomBookLimit() {
        return roomBookLimit;
    }

    public void setRoomBookLimit(String roomBookLimit) {
        this.roomBookLimit = roomBookLimit;
    }

    public String getReschedulingPolicy() {
        return reschedulingPolicy;
    }

    public void setReschedulingPolicy(String reschedulingPolicy) {
        this.reschedulingPolicy = reschedulingPolicy;
    }

    public Integer getNoOfDays() {
        return noOfDays;
    }

    public void setNoOfDays(Integer noOfDays) {
        this.noOfDays = noOfDays;
    }

    public Integer getFees() {
        return fees;
    }

    public void setFees(Integer fees) {
        this.fees = fees;
    }

}
