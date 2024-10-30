package com.iotsmartaliv.model.booking;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ComFeature implements Serializable {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("module_name")
    @Expose
    private String moduleName;
    @SerializedName("module_ID")
    @Expose
    private String moduleID;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getModuleID() {
        return moduleID;
    }

    public void setModuleID(String moduleID) {
        this.moduleID = moduleID;
    }

}
