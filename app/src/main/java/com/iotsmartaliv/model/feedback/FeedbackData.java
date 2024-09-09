package com.iotsmartaliv.model.feedback;

public class FeedbackData {
    public String feedback_id;
    public String cat_id;
    public String feedback_title;
    public String cat_name;
    public String feedback_status;
    public String feedback_date;

    public String getFeedback_id() {
        return feedback_id;
    }

    public void setFeedback_id(String feedback_id) {
        this.feedback_id = feedback_id;
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

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public String getFeedback_status() {
        return feedback_status;
    }

    public void setFeedback_status(String feedback_status) {
        this.feedback_status = feedback_status;
    }

    public String getFeedback_date() {
        return feedback_date;
    }

    public void setFeedback_date(String feedback_date) {
        this.feedback_date = feedback_date;
    }
}
