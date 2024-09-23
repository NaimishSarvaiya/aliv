package com.iotsmartaliv.model.feedback;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FeedbackDetailsData {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("comm_ID")
    @Expose
    private String commID;
    @SerializedName("appuser_ID")
    @Expose
    private String appuserID;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("feedback_title")
    @Expose
    private String feedbackTitle;
    @SerializedName("feedback_description")
    @Expose
    private String feedbackDescription;
    @SerializedName("feedback_document")
    @Expose
    private String feedbackDocument;
    @SerializedName("feedback_status")
    @Expose
    private String feedbackStatus;
    @SerializedName("progress_date")
    @Expose
    private String progressDate;
    @SerializedName("close_date")
    @Expose
    private String closeDate;
    @SerializedName("deleted")
    @Expose
    private String deleted;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("cat_name")
    @Expose
    private String catName;
    @SerializedName("community_name")
    @Expose
    private String communityName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCommID() {
        return commID;
    }

    public void setCommID(String commID) {
        this.commID = commID;
    }

    public String getAppuserID() {
        return appuserID;
    }

    public void setAppuserID(String appuserID) {
        this.appuserID = appuserID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFeedbackTitle() {
        return feedbackTitle;
    }

    public void setFeedbackTitle(String feedbackTitle) {
        this.feedbackTitle = feedbackTitle;
    }

    public String getFeedbackDescription() {
        return feedbackDescription;
    }

    public void setFeedbackDescription(String feedbackDescription) {
        this.feedbackDescription = feedbackDescription;
    }

    public String getFeedbackDocument() {
        return feedbackDocument;
    }

    public void setFeedbackDocument(String feedbackDocument) {
        this.feedbackDocument = feedbackDocument;
    }

    public String getFeedbackStatus() {
        return feedbackStatus;
    }

    public void setFeedbackStatus(String feedbackStatus) {
        this.feedbackStatus = feedbackStatus;
    }

    public String getProgressDate() {
        return progressDate;
    }

    public void setProgressDate(String progressDate) {
        this.progressDate = progressDate;
    }

    public String getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(String  closeDate) {
        this.closeDate = closeDate;
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

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

}
