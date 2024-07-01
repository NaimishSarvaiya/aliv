package com.iotsmartaliv.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * This class is used as .
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 1/8/19 :August.
 */
public class InstructorInductionData {
    @SerializedName("instructor_ID")
    @Expose
    private String instructorID;
    @SerializedName("visitor_ID")
    @Expose
    private String visitorID;
    @SerializedName("community_ID")
    @Expose
    private String communityID;
    @SerializedName("instructor_status")
    @Expose
    private String instructorStatus;
    @SerializedName("valid_date")
    @Expose
    private String validDate;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("iapproval_ID")
    @Expose
    private String iapprovalID;
    @SerializedName("program_start_date")
    @Expose
    private String programStartDate;
    @SerializedName("program_end_date")
    @Expose
    private String programEndDate;
    @SerializedName("program_start_time")
    @Expose
    private String programStartTime;
    @SerializedName("program_end_time")
    @Expose
    private String programEndTime;
    @SerializedName("activity")
    @Expose
    private String activity;
    @SerializedName("recurrence_days")
    @Expose
    private String recurrenceDays;
    @SerializedName("inducted_by")
    @Expose
    private String inductedBy;
    @SerializedName("induction_status")
    @Expose
    private String inductionStatus;
    @SerializedName("approved_date")
    @Expose
    private String approvedDate;
    @SerializedName("created_by")
    @Expose
    private String createdBy;
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
    private String visitorIdentificationCode;
    @SerializedName("visitor_license_plate")
    @Expose
    private String visitorLicensePlate;
    @SerializedName("instructor_company")
    @Expose
    private String instructorCompany;
    @SerializedName("added_by")
    @Expose
    private String addedBy;
    @SerializedName("deleted")
    @Expose
    private String deleted;

    public String getInstructorID() {
        return instructorID;
    }

    public void setInstructorID(String instructorID) {
        this.instructorID = instructorID;
    }

    public String getVisitorID() {
        return visitorID;
    }

    public void setVisitorID(String visitorID) {
        this.visitorID = visitorID;
    }

    public String getCommunityID() {
        return communityID;
    }

    public void setCommunityID(String communityID) {
        this.communityID = communityID;
    }

    public String getInstructorStatus() {
        return instructorStatus;
    }

    public void setInstructorStatus(String instructorStatus) {
        this.instructorStatus = instructorStatus;
    }

    public String getValidDate() {
        return validDate;
    }

    public void setValidDate(String validDate) {
        this.validDate = validDate;
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

    public String getIapprovalID() {
        return iapprovalID;
    }

    public void setIapprovalID(String iapprovalID) {
        this.iapprovalID = iapprovalID;
    }

    public String getProgramStartDate() {
        return programStartDate;
    }

    public void setProgramStartDate(String programStartDate) {
        this.programStartDate = programStartDate;
    }

    public String getProgramEndDate() {
        return programEndDate;
    }

    public void setProgramEndDate(String programEndDate) {
        this.programEndDate = programEndDate;
    }

    public String getProgramStartTime() {
        return programStartTime;
    }

    public void setProgramStartTime(String programStartTime) {
        this.programStartTime = programStartTime;
    }

    public String getProgramEndTime() {
        return programEndTime;
    }

    public void setProgramEndTime(String programEndTime) {
        this.programEndTime = programEndTime;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getRecurrenceDays() {
        return recurrenceDays;
    }

    public void setRecurrenceDays(String recurrenceDays) {
        this.recurrenceDays = recurrenceDays;
    }

    public String getInductedBy() {
        return inductedBy;
    }

    public void setInductedBy(String inductedBy) {
        this.inductedBy = inductedBy;
    }

    public String getInductionStatus() {
        return inductionStatus;
    }

    public void setInductionStatus(String inductionStatus) {
        this.inductionStatus = inductionStatus;
    }

    public String getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(String approvedDate) {
        this.approvedDate = approvedDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
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

    public String getVisitorIdentificationCode() {
        return visitorIdentificationCode;
    }

    public void setVisitorIdentificationCode(String visitorIdentificationCode) {
        this.visitorIdentificationCode = visitorIdentificationCode;
    }

    public String getVisitorLicensePlate() {
        return visitorLicensePlate;
    }

    public void setVisitorLicensePlate(String visitorLicensePlate) {
        this.visitorLicensePlate = visitorLicensePlate;
    }

    public String getInstructorCompany() {
        return instructorCompany;
    }

    public void setInstructorCompany(String instructorCompany) {
        this.instructorCompany = instructorCompany;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

}
