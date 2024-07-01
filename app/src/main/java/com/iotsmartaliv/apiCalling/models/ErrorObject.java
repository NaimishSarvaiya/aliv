package com.iotsmartaliv.apiCalling.models;

import java.io.Serializable;

/**
 * This class is used as Model that is contains the Error object value.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 3/5/19 :May : 2019 on 14 : 53.
 */

public class ErrorObject implements Serializable {
    private String message;
    private int status;
    private int code;
    private String developerMessage;
    private String field1;
    private String field2;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDeveloperMessage() {
        return developerMessage;
    }

    public void setDeveloperMessage(String developerMessage) {
        this.developerMessage = developerMessage;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public String getField2() {
        return field2;
    }

    public void setField2(String field2) {
        this.field2 = field2;
    }
}