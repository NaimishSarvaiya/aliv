package com.iotsmartaliv.apiCalling.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * This class is used as model class when call the api for device list data.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 28/3/19 :March : 2019 on 20 : 03.
 */
public class SuccessDeviceListResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private List<DeviceObject> data = null;

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

    public List<DeviceObject> getData() {
        return data;
    }

    public void setData(List<DeviceObject> data) {
        this.data = data;
    }

}
