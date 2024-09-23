package com.iotsmartaliv.model.feedback;

public class AddFeedbackRequest {

    private String appuser_ID;
    private String comm_id;
    private String cat_id;
    private String feedback_title;
    private String feedback_description;
    private String feedback_doc_fullpath;

    public AddFeedbackRequest(String appuser_ID, String comm_id, String cat_id, String feedback_title, String feedback_description, String feedback_doc_fullpath) {
        this.appuser_ID = appuser_ID;
        this.comm_id = comm_id;
        this.cat_id = cat_id;
        this.feedback_title = feedback_title;
        this.feedback_description = feedback_description;
        this.feedback_doc_fullpath = feedback_doc_fullpath;
    }
    // Constructor

    public String getAppuser_id() {
        return appuser_ID;
    }

    public void setAppuser_id(String appuser_id) {
        this.appuser_ID = appuser_id;
    }

    public String getComm_id() {
        return comm_id;
    }

    public void setComm_id(String comm_id) {
        this.comm_id = comm_id;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getFeedback_title() {
        return feedback_title;
    }

    public void setFeedback_title(String feedback_title) {
        this.feedback_title = feedback_title;
    }

    public String getFeedback_description() {
        return feedback_description;
    }

    public void setFeedback_description(String feedback_description) {
        this.feedback_description = feedback_description;
    }

    public String getFeedback_doc_fullpath() {
        return feedback_doc_fullpath;
    }

    public void setFeedback_doc_fullpath(String feedback_doc_fullpath) {
        this.feedback_doc_fullpath = feedback_doc_fullpath;
    }
}
