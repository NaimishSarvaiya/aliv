package com.iotsmartaliv.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * This class is used as .
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 6/8/19 :August.
 */
public class VisitorData {
    boolean check = false;
    @SerializedName("vu_ID")
    @Expose
    private String vuID;
    @SerializedName("visitor_ID")
    @Expose
    private String visitorID;
    @SerializedName("user_ID")
    @Expose
    private String userID;
    @SerializedName("uvisitor_name")
    @Expose
    private String uvisitorName;
    @SerializedName("vgroup_ID")
    @Expose
    private String vgroupID;
    @SerializedName("deleted")
    @Expose
    private String deleted;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("visit_ID")
    @Expose
    private String visitID;
    @SerializedName("visitor_name")
    @Expose
    private String visitorName;
    @SerializedName("visitor_country_code")
    @Expose
    private String visitorCountryCode;
    @SerializedName("visitor_contact")
    @Expose
    private String visitorContact;
    @SerializedName("visitor_identification_code")
    @Expose
    private Object visitorIdentificationCode;
    @SerializedName("visitor_license_plate")
    @Expose
    private String visitorLicensePlate;
    @SerializedName("instructor_company")
    @Expose
    private Object instructorCompany;
    @SerializedName("added_by")
    @Expose
    private String addedBy;

    public String getVuID() {
        return vuID;
    }

    public void setVuID(String vuID) {
        this.vuID = vuID;
    }

    public String getVisitorID() {
        return visitorID;
    }

    public void setVisitorID(String visitorID) {
        this.visitorID = visitorID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUvisitorName() {
        return uvisitorName;
    }

    public void setUvisitorName(String uvisitorName) {
        this.uvisitorName = uvisitorName;
    }

    public String getVgroupID() {
        return vgroupID;
    }

    public void setVgroupID(String vgroupID) {
        this.vgroupID = vgroupID;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getVisitID() {
        return visitID;
    }

    public void setVisitID(String visitID) {
        this.visitID = visitID;
    }

    public String getVisitorName() {
        return visitorName;
    }

    public void setVisitorName(String visitorName) {
        this.visitorName = visitorName;
    }

    public String getVisitorCountryCode() {
        return visitorCountryCode;
    }

    public void setVisitorCountryCode(String visitorCountryCode) {
        this.visitorCountryCode = visitorCountryCode;
    }

    public String getVisitorContact() {
        return visitorContact;
    }

    public void setVisitorContact(String visitorContact) {
        this.visitorContact = visitorContact;
    }

    public Object getVisitorIdentificationCode() {
        return visitorIdentificationCode;
    }

    public void setVisitorIdentificationCode(Object visitorIdentificationCode) {
        this.visitorIdentificationCode = visitorIdentificationCode;
    }

    public String getVisitorLicensePlate() {
        return visitorLicensePlate;
    }

    public void setVisitorLicensePlate(String visitorLicensePlate) {
        this.visitorLicensePlate = visitorLicensePlate;
    }

    public Object getInstructorCompany() {
        return instructorCompany;
    }

    public void setInstructorCompany(Object instructorCompany) {
        this.instructorCompany = instructorCompany;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
