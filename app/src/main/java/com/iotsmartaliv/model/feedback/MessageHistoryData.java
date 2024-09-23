package com.iotsmartaliv.model.feedback;

public class MessageHistoryData {
    private String message;
    private String message_document;
    private String read_status;
    private String created_at;
    private String message_from;

    public MessageHistoryData(String message, String message_document, String read_status, String created_at, String message_from) {
        this.message = message;
        this.message_document = message_document;
        this.read_status = read_status;
        this.created_at = created_at;
        this.message_from = message_from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage_document() {
        return message_document;
    }

    public void setMessage_document(String message_document) {
        this.message_document = message_document;
    }

    public String getRead_status() {
        return read_status;
    }

    public void setRead_status(String read_status) {
        this.read_status = read_status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getMessage_from() {
        return message_from;
    }

    public void setMessage_from(String message_from) {
        this.message_from = message_from;
    }
}