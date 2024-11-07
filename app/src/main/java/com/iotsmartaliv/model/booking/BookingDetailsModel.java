package com.iotsmartaliv.model.booking;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class BookingDetailsModel implements Serializable {
    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private BookingDetailsData data;
    @SerializedName("comFeatures")
    @Expose
    private List<ComFeature> comFeatures;

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

    public BookingDetailsData getData() {
        return data;
    }

    public void setData(BookingDetailsData data) {
        this.data = data;
    }

    public List<ComFeature> getComFeatures() {
        return comFeatures;
    }

    public void setComFeatures(List<ComFeature> comFeatures) {
        this.comFeatures = comFeatures;
    }
}
