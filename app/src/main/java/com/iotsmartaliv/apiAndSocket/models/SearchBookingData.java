package com.iotsmartaliv.apiAndSocket.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * This class is used as .
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 21/8/19 :August : 2019 on 20 : 36.
 */
public class SearchBookingData {
    boolean isSelectedForBooing;
    @SerializedName("room_details")
    @Expose
    private RoomDetails roomDetails;
    @SerializedName("time_slots")
    @Expose
    private List<TimeSlot> timeSlots = null;
    @SerializedName("remaining")
    @Expose
    private String remaining;
    private String purpuse;

    public boolean isSelectedForBooing() {
        return isSelectedForBooing;
    }

    public void setSelectedForBooing(boolean selectedForBooing) {
        isSelectedForBooing = selectedForBooing;
    }

    public String getPurpuse() {
        return purpuse;
    }

    public void setPurpuse(String purpuse) {
        this.purpuse = purpuse;
    }


    public RoomDetails getRoomDetails() {
        return roomDetails;
    }

    public void setRoomDetails(RoomDetails roomDetails) {
        this.roomDetails = roomDetails;
    }

    public List<TimeSlot> getTimeSlots() {
        return timeSlots;
    }

    public void setTimeSlots(List<TimeSlot> timeSlots) {
        this.timeSlots = timeSlots;
    }

    public String getRemaining() {
        return remaining;
    }

    public void setRemaining(String remaining) {
        this.remaining = remaining;
    }

}
