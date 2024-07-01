package com.iotsmartaliv.apiCalling.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.iotsmartaliv.model.InstructorInductionData;

public class InstructorInfoResponse {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private InstructorInductionData data;

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

    public InstructorInductionData getData() {
        return data;
    }

    public void setData(InstructorInductionData data) {
        this.data = data;
    }
}
