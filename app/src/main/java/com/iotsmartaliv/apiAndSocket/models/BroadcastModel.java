package com.iotsmartaliv.apiAndSocket.models;

/**
 * This class is used as
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 28 Apr,2021
 */



import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.*;
public class BroadcastModel {
    @SerializedName("broadcast")
    @Expose
    private List<Broadcast> broadcast = null;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("msg")
    @Expose
    private String msg;
    public List<Broadcast> getBroadcast() {
        return broadcast;
    }
    public void setBroadcast(List<Broadcast> broadcast) {
        this.broadcast = broadcast;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
}
