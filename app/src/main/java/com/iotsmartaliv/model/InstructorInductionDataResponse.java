package com.iotsmartaliv.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * This class is used as .
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 1/8/19 :August.
 */
public class InstructorInductionDataResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private List<InstructorInductionData> data = null;

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

    public List<InstructorInductionData> getData() {
        return data;
    }

    public void setData(List<InstructorInductionData> data) {
        this.data = data;
    }

}
