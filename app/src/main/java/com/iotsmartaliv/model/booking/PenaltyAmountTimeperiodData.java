package com.iotsmartaliv.model.booking;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PenaltyAmountTimeperiodData implements Serializable {
    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("duration_type")
    @Expose
    private String durationType;
    @SerializedName("percentage")
    @Expose
    private String percentage;

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDurationType() {
        return durationType;
    }

    public void setDurationType(String durationType) {
        this.durationType = durationType;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }
}
