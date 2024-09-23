package com.iotsmartaliv.model.feedback;

import java.sql.Struct;

public class MessageStatusUpdateRequest {
    String feedback_ID;
    String message_from;

    public MessageStatusUpdateRequest(String feedback_ID, String message_from) {
        this.feedback_ID = feedback_ID;
        this.message_from = message_from;
    }

    public String getFeedback_ID() {
        return feedback_ID;
    }

    public void setFeedback_ID(String feedback_ID) {
        this.feedback_ID = feedback_ID;
    }

    public String getMessage_from() {
        return message_from;
    }

    public void setMessage_from(String message_from) {
        this.message_from = message_from;
    }
}
