package com.iotsmartaliv.model.booking;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TimeSlotModel {
    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("timeslots")
    @Expose
    private List<TimeSlotDataModel> timeslots;

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<TimeSlotDataModel> getTimeslots() {
        return timeslots;
    }

    public void setTimeslots(List<TimeSlotDataModel> timeslots) {
        this.timeslots = timeslots;
    }

}
