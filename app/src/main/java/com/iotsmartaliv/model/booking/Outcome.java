package com.iotsmartaliv.model.booking;

public class Outcome {
    private String network_status;
    private String reason;
    private String risk_level;
    private int risk_score;
    private String seller_message;
    private String type;

    // Getters and Setters
    public String getNetwork_status() {
        return network_status;
    }

    public void setNetwork_status(String network_status) {
        this.network_status = network_status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getRisk_level() {
        return risk_level;
    }

    public void setRisk_level(String risk_level) {
        this.risk_level = risk_level;
    }

    public int getRisk_score() {
        return risk_score;
    }

    public void setRisk_score(int risk_score) {
        this.risk_score = risk_score;
    }

    public String getSeller_message() {
        return seller_message;
    }

    public void setSeller_message(String seller_message) {
        this.seller_message = seller_message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
