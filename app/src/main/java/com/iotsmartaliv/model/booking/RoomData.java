package com.iotsmartaliv.model.booking;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RoomData implements Serializable {
    @SerializedName("room_ID")
    @Expose
    private String roomID;
    @SerializedName("room_name")
    @Expose
    private String roomName;
    @SerializedName("room_image")
    @Expose
    private String roomImage;
    @SerializedName("room_base_price")
    @Expose
    private String roomBasePrice;
    @SerializedName("room_type")
    @Expose
    private String roomType;
    @SerializedName("room_book_limit")
    @Expose
    private String roomBookLimit;
    @SerializedName("community_name")
    @Expose
    private String communityName;

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

    public String getRoomBasePrice() {
        return roomBasePrice;
    }

    public void setRoomBasePrice(String roomBasePrice) {
        this.roomBasePrice = roomBasePrice;
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

    public void setRoomBookLimit(String  roomBookLimit) {
        this.roomBookLimit = roomBookLimit;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

}
